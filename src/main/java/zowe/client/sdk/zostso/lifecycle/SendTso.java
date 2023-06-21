/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.TsoUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.SendTsoParams;
import zowe.client.sdk.zostso.message.TsoMessage;
import zowe.client.sdk.zostso.message.TsoMessages;
import zowe.client.sdk.zostso.message.TsoResponseMessage;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.CollectedResponses;
import zowe.client.sdk.zostso.response.SendResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle sending data to TSO
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class SendTso {

    private static final Logger LOG = LoggerFactory.getLogger(SendTso.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * SendTso constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public SendTso(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative SendTso constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public SendTso(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Create Response
     *
     * @param responses responses from CollectedResponses object
     * @return SendResponse, see SendResponse
     * @author Frank Giordano
     */
    private static SendResponse createResponse(CollectedResponses responses) throws Exception {
        return new SendResponse(true, responses.getTsos(),
                responses.getMessages().orElseThrow(() -> new Exception("no responses messages exist")));
    }

    /**
     * Collects responses from address space until it reaches prompt
     *
     * @param tso object from first Tso response from witch responses are needed, see ZosmfTsoResponse
     * @return CollectedResponses response object, see CollectedResponses
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    public CollectedResponses getAllResponses(ZosmfTsoResponse tso) throws Exception {
        boolean done = false;
        final StringBuilder messages = new StringBuilder();
        final List<ZosmfTsoResponse> tsos = new ArrayList<>();
        tsos.add(tso);
        while (!done) {
            if (!tso.getTsoData().isEmpty()) {
                for (TsoMessages tsoDatum : tso.getTsoData()) {
                    if (tsoDatum.getTsoMessage().isPresent()) {
                        final TsoMessage tsoMsg = tsoDatum.getTsoMessage().get();
                        tsoMsg.getData().ifPresent(data -> {
                            messages.append(data);
                            messages.append("\n");
                        });
                    } else if (tsoDatum.getTsoPrompt().isPresent()) {
                        if (messages.toString().contains("IKJ56602I COMMAND SYSTEM RESTARTING DUE TO ERROR")) {
                            final String IKJ56602I = "IKJ56602I COMMAND SYSTEM RESTARTING DUE TO ERROR";
                            final String msg = messages.toString();
                            final int startIndex = msg.indexOf("IKJ56602I");
                            messages.delete(startIndex, startIndex + IKJ56602I.length() + "\nREADY".length());
                        } else if (messages.length() > 0 && messages.toString().contains("READY")) {
                            done = true;
                        }
                        // TSO PROMPT reached without getting any data, retrying
                    }
                }
            }
            if (!done) {
                tso = getDataFromTSO(tso.getServletKey().orElseThrow((() -> new Exception("servlet key missing"))));
                tsos.add(tso);
            }
        }
        return new CollectedResponses(tsos, messages.toString());
    }

    /**
     * Retrieve tso http request response
     *
     * @param servletKey key of tso address space
     * @return z/OSMF tso response, see ZosmfTsoResponse
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    private ZosmfTsoResponse getDataFromTSO(String servletKey) throws Exception {
        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + servletKey;
        LOG.debug("SendTso::getDataFromTSO - url {}", url);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody("");

        return TsoUtils.getZosmfTsoResponse(RestUtils.getResponse(request));
    }

    /**
     * Generate TSO Response message in json format
     *
     * @param tsoResponseMessage tso response message, see tsoResponseMessage
     * @return json representation of TSO RESPONSE
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    private String getTsoResponseSendMessage(TsoResponseMessage tsoResponseMessage) throws Exception {
        final String message = "{\"TSO RESPONSE\":{\"VERSION\":\"" +
                tsoResponseMessage.getVersion().orElseThrow((() -> new Exception("response version missing")))
                + "\",\"DATA\":\"" +
                tsoResponseMessage.getData().orElseThrow((() -> new Exception("response data missing"))) + "\"}}";
        LOG.debug("SendTo::getTsoResponseSendMessage - message {}", message);
        return message;
    }

    /**
     * API method to send data to already started TSO address space, but will read TSO data until a PROMPT is reached.
     *
     * @param command    to send to the TSO address space.
     * @param servletKey returned from a successful start
     * @return response object, see ISendResponse
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    public SendResponse sendDataToTSOCollect(String servletKey, String command) throws Exception {
        ValidateUtils.checkNullParameter(servletKey == null, "servletKey is null");
        ValidateUtils.checkNullParameter(command == null, "command is null");
        ValidateUtils.checkIllegalParameter(servletKey.isBlank(), "servletKey not specified");
        ValidateUtils.checkIllegalParameter(command.isBlank(), "command not specified");
        final ZosmfTsoResponse putResponse = sendDataToTSOCommon(new SendTsoParams(servletKey, command));
        final CollectedResponses responses = getAllResponses(putResponse);
        return createResponse(responses);
    }

    /**
     * API method to send data to already started TSO address space
     *
     * @param commandParams object with required parameters, see SendTsoParams object
     * @return response object, see ZosmfTsoResponse
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    public ZosmfTsoResponse sendDataToTSOCommon(SendTsoParams commandParams) throws Exception {
        ValidateUtils.checkNullParameter(commandParams == null, "commandParams is null");
        ValidateUtils.checkIllegalParameter(commandParams.getData().isBlank(), "commandParams data not specified");
        ValidateUtils.checkIllegalParameter(commandParams.getServletKey().isBlank(), "commandParams servletKey not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + commandParams.getServletKey() + TsoConstants.RES_DONT_READ_REPLY;
        LOG.debug("SendTso::sendDataToTSOCommon - url {}", url);

        final TsoResponseMessage tsoResponseMessage = new TsoResponseMessage("0100", commandParams.getData());
        final String jobObjBody = getTsoResponseSendMessage(tsoResponseMessage);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(jobObjBody);

        return TsoUtils.getZosmfTsoResponse(RestUtils.getResponse(request));
    }

}
