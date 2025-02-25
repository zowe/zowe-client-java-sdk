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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.SendTsoParams;
import zowe.client.sdk.zostso.message.TsoMessage;
import zowe.client.sdk.zostso.message.TsoMessages;
import zowe.client.sdk.zostso.message.TsoResponseMessage;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.CollectedResponses;
import zowe.client.sdk.zostso.response.SendResponse;
import zowe.client.sdk.zostso.service.TsoResponseService;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle sending data to TSO
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class SendTso {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * SendTso constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public SendTso(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative SendTso constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public SendTso(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
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
    private static SendResponse createResponse(final CollectedResponses responses) {
        return new SendResponse(true, responses.getTsos(), responses.getMessages()
                .orElseThrow(() -> new IllegalStateException("no responses tso messages exist")));
    }

    /**
     * Collects responses from address space until it reaches prompt
     *
     * @param tso object from first Tso response from witch responses are needed, see ZosmfTsoResponse
     * @return CollectedResponses response object, see CollectedResponses
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public CollectedResponses getAllResponses(ZosmfTsoResponse tso) throws ZosmfRequestException {
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
                tso = getDataFromTso(tso.getServletKey().orElseThrow((
                        () -> new IllegalStateException("servlet key missing"))));
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
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private ZosmfTsoResponse getDataFromTso(final String servletKey) throws ZosmfRequestException {
        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + servletKey;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        connection.getCookie().ifPresentOrElse(c -> request.setCookie(c), () -> request.setCookie(null));
        request.setBody("");

        return new TsoResponseService(request.executeRequest()).getZosmfTsoResponse();
    }

    /**
     * Generate TSO Response message in json format
     *
     * @param tsoResponseMessage tso response message, see tsoResponseMessage
     * @return json representation of TSO RESPONSE
     * @author Frank Giordano
     */
    private String getTsoResponseSendMessage(final TsoResponseMessage tsoResponseMessage) {
        return "{\"TSO RESPONSE\":{\"VERSION\":\"" + tsoResponseMessage.getVersion().orElse("")
                + "\",\"DATA\":\"" + tsoResponseMessage.getData().orElse("") + "\"}}";
    }

    /**
     * API method to send data to already started TSO address space,
     * but will read TSO data until a PROMPT is reached.
     *
     * @param command    to send to the TSO address space.
     * @param servletKey returned from a successful start
     * @return SendResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public SendResponse sendDataToTsoCollect(final String servletKey, final String command) throws ZosmfRequestException {
        final ZosmfTsoResponse putResponse = sendDataToTsoCommon(new SendTsoParams(servletKey, command));
        final CollectedResponses responses = getAllResponses(putResponse);
        return createResponse(responses);
    }

    /**
     * API method to send data to already started TSO address space
     *
     * @param commandParams object with required parameters, see SendTsoParams object
     * @return ZosmfTsoResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosmfTsoResponse sendDataToTsoCommon(final SendTsoParams commandParams) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(commandParams == null, "commandParams is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + commandParams.getServletKey() + TsoConstants.RES_DONT_READ_REPLY;

        final TsoResponseMessage tsoResponseMessage = new TsoResponseMessage("0100", commandParams.getData());
        final String jobObjBody = getTsoResponseSendMessage(tsoResponseMessage);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        connection.getCookie().ifPresentOrElse(c -> request.setCookie(c), () -> request.setCookie(null));
        request.setBody(jobObjBody);

        return new TsoResponseService(request.executeRequest()).getZosmfTsoResponse();
    }

}
