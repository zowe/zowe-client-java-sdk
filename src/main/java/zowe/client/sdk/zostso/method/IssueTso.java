/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.method;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StartTsoInputData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Issue tso command via z/OSMF restful api
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class IssueTso {

    private final List<String> msgLst = new ArrayList<>();
    private final List<String> promptLst = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ZosConnection connection;
    private final String accountNumber;
    private ZosmfRequest request;
    private StartTsoInputData startTsoData;

    /**
     * IssueTso constructor
     *
     * @param connection    ZosConnection object
     * @param accountNumber account number for tso processing
     * @author Frank Giordano
     */
    public IssueTso(final ZosConnection connection, final String accountNumber) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkIllegalParameter(accountNumber, "accountNumber");
        this.connection = connection;
        this.accountNumber = accountNumber;
    }

    /**
     * Issue TSO command API call to process the given command via z/OSMF restful api
     *
     * @param command tso command string
     * @return list of all tso returned messages
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<String> issueCommand(final String command) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(command, "command");
        return this.issueCommand(command, null);
    }

    /**
     * Issue TSO command API call to process the given command via z/OSMF restful with given custom
     * parameters for the start TSO session call
     *
     * @param command      tso command string
     * @param startTsoData start TSO request inputs parameters
     * @return list of all tso returned messages
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<String> issueCommand(final String command, final StartTsoInputData startTsoData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(command, "command");
        this.startTsoData = startTsoData;
        final String sessionKey = this.startTso();
        return this.processTsoCommand(sessionKey, command);
    }

    /**
     * Process the given TSO command string request
     *
     * @param sessionKey servletKey id retrieve from start TSO request
     * @param command    tso command string
     * @return list of all tso returned messages
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private List<String> processTsoCommand(final String sessionKey, final String command) throws ZosmfRequestException {
        this.msgLst.clear();
        this.promptLst.clear();

        String responseStr = this.sendTsoCommand(sessionKey, command);
        JsonNode tsoData = this.getJsonNode(responseStr, TsoConstants.SEND_TSO_COMMAND_FAIL_MSG).get("tsoData");
        this.processTsoData(tsoData);

        while (promptLst.isEmpty()) {
            responseStr = this.sendTso(sessionKey);
            tsoData = this.getJsonNode(responseStr, TsoConstants.SEND_TSO_FAIL_MSG).get("tsoData");
            this.processTsoData(tsoData);
        }

        this.stopTso(sessionKey);
        return msgLst;
    }

    /**
     * Make the first TSO request to start the TSO session and retrieve its session id (servletKey).
     *
     * @return string value representing the session id (servletKey)
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private String startTso() throws ZosmfRequestException {
        if (startTsoData == null) {
            startTsoData = new StartTsoInputData();
        }

        final String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO +
                "?" + "acct" + "=" + EncodeUtils.encodeURIComponent(startTsoData.getAccount().orElse(this.accountNumber)) +
                "&" + "proc" + "=" + startTsoData.getLogonProcedure().orElse(TsoConstants.DEFAULT_PROC) +
                "&" + "chset" + "=" + startTsoData.getCharacterSet().orElse(TsoConstants.DEFAULT_CHSET) +
                "&" + "cpage" + "=" + startTsoData.getCodePage().orElse(TsoConstants.DEFAULT_CPAGE) +
                "&" + "rows" + "=" + startTsoData.getRows().orElse(TsoConstants.DEFAULT_ROWS) +
                "&" + "cols" + "=" + startTsoData.getColumns().orElse(TsoConstants.DEFAULT_COLS) +
                "&" + "rsize" + "=" + startTsoData.getRegionSize().orElse(TsoConstants.DEFAULT_RSIZE);

        if (request == null || !(request instanceof PostJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody("");

        final String responseStr = this.executeRequest(request, TsoConstants.START_TSO_FAIL_MSG);
        final JsonNode rootNode = this.getJsonNode(responseStr, TsoConstants.START_TSO_FAIL_MSG);

        final String servletKey = rootNode.get("servletKey").asText();
        if ("null".equalsIgnoreCase(servletKey)) {
            throw new ZosmfRequestException(TsoConstants.START_TSO_FAIL_MSG + " Response: " + responseStr);
        }

        return servletKey;
    }

    /**
     * Make the second request to send TSO the command to perform via z/OSMF
     *
     * @param sessionKey servletKey id retrieve from start TSO request
     * @param command    tso command
     * @return response string representing the returned request payload
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private String sendTsoCommand(final String sessionKey, final String command) throws ZosmfRequestException {
        final String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + sessionKey + TsoConstants.RES_DONT_READ_REPLY;
        final String body = "{\"TSO RESPONSE\":{\"VERSION\":\"0100\",\"DATA\":\"" + command + "\"}}";

        if (request == null || !(request instanceof PutJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        return this.executeRequest(request, TsoConstants.SEND_TSO_COMMAND_FAIL_MSG);
    }

    /**
     * Send a request to z/OSMF TSO for additional TSO message data
     *
     * @param sessionKey servletKey id retrieve from start TSO request
     * @return response string representing the returned request payload
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private String sendTso(final String sessionKey) throws ZosmfRequestException {
        final String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + sessionKey;

        if (request == null || !(request instanceof PutJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody("");

        return this.executeRequest(request, TsoConstants.SEND_TSO_FAIL_MSG);
    }

    /**
     * Stop the TSO session by session id (servletKey)
     *
     * @param sessionKey servletKey id retrieve from start TSO request
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private void stopTso(final String sessionKey) throws ZosmfRequestException {
        final String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + sessionKey;

        if (request == null || !(request instanceof DeleteJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }
        request.setUrl(url);

        this.executeRequest(request, TsoConstants.STOP_TSO_COMMAND_FAIL_MSG);
    }

    /**
     * Transform the JSON response payload for its tso message types
     *
     * @param tsoData JsonNode object
     * @author Frank Giordano
     */
    private void processTsoData(final JsonNode tsoData) {
        tsoData.forEach(tsoDataItem -> {
            try {
                if (tsoDataItem.get(TsoConstants.TSO_MESSAGE) != null) {
                    this.msgLst.add(tsoDataItem.get("TSO MESSAGE").get("DATA").asText());
                }
                if (tsoDataItem.get(TsoConstants.TSO_PROMPT) != null) {
                    this.promptLst.add(tsoDataItem.get("TSO PROMPT").get("HIDDEN").asText());
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Perform the http request to z/OSMF
     *
     * @param request request object
     * @param msg     error message
     * @return response string representing a JSON returned payload
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private String executeRequest(final ZosmfRequest request, final String msg) throws ZosmfRequestException {
        final Response response = request.executeRequest();
        final String responseStr = response.getResponsePhrase().orElse("").toString();
        if ("".equals(responseStr)) {
            throw new ZosmfRequestException(msg + " Response: " + responseStr);
        }

        AtomicInteger statusCode = new AtomicInteger();
        response.getStatusCode().ifPresent(statusCode::set);

        if (!(statusCode.get() >= 100 && statusCode.get() <= 299)) {
            throw new ZosmfRequestException(msg + " Response: " + responseStr);
        }

        return responseStr;
    }

    /**
     * Transform a response string representing a JSON returned payload from a tso call into a JsonNode to
     * be used for parsing the response.
     *
     * @param responseStr response string
     * @param msg         error message
     * @return JsonNode object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private JsonNode getJsonNode(final String responseStr, final String msg) throws ZosmfRequestException {
        final JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(responseStr);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException(msg + " Response: " + e.getMessage());
        }
        return rootNode;
    }

}
