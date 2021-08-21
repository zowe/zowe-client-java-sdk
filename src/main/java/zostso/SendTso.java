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
import zostso.input.SendTsoParms;
import zostso.zosmf.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SendTso {

    private static final Logger LOG = LogManager.getLogger(SendTso.class);

    private final ZOSConnection connection;

    public SendTso(ZOSConnection connection) {
        this.connection = connection;
    }

    public SendResponse sendDataToTSOCollect(String servletKey, String data) throws Exception {
        Util.checkNullParameter(servletKey == null, "servletKey is null");
        Util.checkNullParameter(data == null, "data is null");
        Util.checkStateParameter(servletKey.isEmpty(), "servletKey not specified");
        Util.checkStateParameter(data.isEmpty(), "data not specified");

        ZosmfTsoResponse putResponse = sendDataToTSOCommon(new SendTsoParms(servletKey, data));

        CollectedResponses responses = getAllResponses(putResponse);
        return SendTso.createResponse(responses);
    }

    public ZosmfTsoResponse sendDataToTSOCommon(SendTsoParms commandParms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(commandParms == null, "sendTsoParms is null");
        Util.checkStateParameter(commandParms.getData().isEmpty(), "sendTsoParms data not specified");
        Util.checkStateParameter(commandParms.getSevletKey().isEmpty(), "sendTsoParms sevletKey not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + commandParms.getSevletKey() + TsoConstants.RES_DONT_READ_REPLY;
        LOG.debug("SendTso::sendDataToTSOCommon - url {}", url);

        TsoResponseMessage tsoResponseMessage = new TsoResponseMessage(Optional.of("0100"),
                Optional.ofNullable(commandParms.getData()));
        String jobObjBody = getTsoResponseSendMessage(tsoResponseMessage);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, jobObjBody,
                ZoweRequestType.VerbType.PUT_JSON);
        Response response = request.executeHttpRequest();
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

    private String getTsoResponseSendMessage(TsoResponseMessage tsoResponseMessage) throws Exception {
        String message = "{\"TSO RESPONSE\":{\"VERSION\":\"" + tsoResponseMessage.getVersion().orElseThrow(Exception::new)
                + "\",\"DATA\":\"" + tsoResponseMessage.getData().orElseThrow(Exception::new) + "\"}}";
        LOG.debug("SendTo::getTsoResponseSendMessage - message {}", message);
        return message;
    }

    private CollectedResponses getAllResponses(ZosmfTsoResponse tso) throws Exception {
        boolean done = false;
        StringBuilder messages = new StringBuilder();
        List<ZosmfTsoResponse> tsos = new ArrayList<>();
        tsos.add(tso);
        while (!done) {
            if (tso.getTsoData().isPresent()) {
                for (TsoMessages tsoDatum : tso.getTsoData().get()) {
                    if (tsoDatum.getTsoMessage().isPresent()) {
                        TsoMessage tsoMsg = tsoDatum.getTsoMessage().orElseThrow(Exception::new);
                        String data = tsoMsg.getData().orElseThrow(Exception::new);
                        messages.append(data + "\n");
                    } else if (tsoDatum.getTsoPrompt().isPresent()) {
                        if (messages.length() > 0) {
                            done = true;
                        }
                        // TSO PROMPT reached without getting any data, retrying
                    }
                }
            }
            if (!done) {
                tso = getDataFromTSO(tso.getServletKey().orElseThrow(Exception::new));
                tsos.add(tso);
            }
        }
        return new CollectedResponses(tsos, messages.toString());
    }

    private ZosmfTsoResponse getDataFromTSO(String servletKey) throws Exception {
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + servletKey;
        LOG.debug("SendTso::getDataFromTSO - url {}", url);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, "",
                ZoweRequestType.VerbType.PUT_JSON);
        Response response = request.executeHttpRequest();
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

    private static SendResponse createResponse(CollectedResponses responses) {
        return new SendResponse(true, responses.getTsos().get(), responses.getMessages().get());
    }

}
