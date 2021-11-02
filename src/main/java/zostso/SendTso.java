/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import rest.ZoweRequest;
import rest.ZoweRequestFactory;
import rest.ZoweRequestType;
import utility.Util;
import utility.UtilRest;
import utility.UtilTso;
import zostso.input.SendTsoParams;
import zostso.zosmf.TsoMessages;
import zostso.zosmf.TsoResponseMessage;
import zostso.zosmf.ZosmfTsoResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle sending data to TSO
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SendTso {

    private static final Logger LOG = LogManager.getLogger(SendTso.class);

    private final ZOSConnection connection;

    /**
     * SendTso constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public SendTso(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
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
        Util.checkNullParameter(servletKey == null, "servletKey is null");
        Util.checkNullParameter(command == null, "command is null");
        Util.checkIllegalParameter(servletKey.isEmpty(), "servletKey not specified");
        Util.checkIllegalParameter(command.isEmpty(), "command not specified");

        ZosmfTsoResponse putResponse = sendDataToTSOCommon(new SendTsoParams(servletKey, command));

        CollectedResponses responses = getAllResponses(putResponse);
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
        Util.checkNullParameter(commandParams == null, "commandParams is null");
        Util.checkIllegalParameter(commandParams.getData().isEmpty(), "commandParams data not specified");
        Util.checkIllegalParameter(commandParams.getServletKey().isEmpty(), "commandParams servletKey not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + commandParams.getServletKey() + TsoConstants.RES_DONT_READ_REPLY;
        LOG.debug("SendTso::sendDataToTSOCommon - url {}", url);

        TsoResponseMessage tsoResponseMessage = new TsoResponseMessage("0100", commandParams.getData());
        String jobObjBody = getTsoResponseSendMessage(tsoResponseMessage);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, jobObjBody,
                ZoweRequestType.VerbType.PUT_JSON);
        Response response = request.executeRequest();
        if (response.isEmpty())
            return new ZosmfTsoResponse.Builder().build();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            throw new Exception("No results from executing tso command after getting TSO address space. " + errorMsg);
        }

        return UtilTso.getZosmfTsoResponse(response);
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
        String message = "{\"TSO RESPONSE\":{\"VERSION\":\"" +
                tsoResponseMessage.getVersion().orElseThrow((() -> new Exception("response version missing")))
                + "\",\"DATA\":\"" +
                tsoResponseMessage.getData().orElseThrow((() -> new Exception("response data missing"))) + "\"}}";
        LOG.debug("SendTo::getTsoResponseSendMessage - message {}", message);
        return message;
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
        StringBuilder messages = new StringBuilder();
        List<ZosmfTsoResponse> tsos = new ArrayList<>();
        tsos.add(tso);
        while (!done) {
            if (!tso.getTsoData().isEmpty()) {
                for (TsoMessages tsoDatum : tso.getTsoData()) {
                    if (tsoDatum.getTsoMessage().isPresent()) {
                        var tsoMsg = tsoDatum.getTsoMessage().get();
                        tsoMsg.getData().ifPresent(data -> {
                            messages.append(data);
                            messages.append("\n");
                        });
                    } else if (tsoDatum.getTsoPrompt().isPresent()) {
                        if (messages.toString().contains("IKJ56602I COMMAND SYSTEM RESTARTING DUE TO ERROR")) {
                            String IKJ56602I = "IKJ56602I COMMAND SYSTEM RESTARTING DUE TO ERROR";
                            String msg = messages.toString();
                            int startIndex = msg.indexOf("IKJ56602I");
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
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + servletKey;
        LOG.debug("SendTso::getDataFromTSO - url {}", url);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, "",
                ZoweRequestType.VerbType.PUT_JSON);
        Response response = request.executeRequest();
        if (response.isEmpty())
            return new ZosmfTsoResponse.Builder().build();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            throw new Exception("Follow up TSO Messages from TSO command cannot be retrieved. " + errorMsg);
        }

        return UtilTso.getZosmfTsoResponse(response);
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

}
