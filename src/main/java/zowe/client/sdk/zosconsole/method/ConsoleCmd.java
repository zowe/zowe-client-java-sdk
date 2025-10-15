/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.method;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Issue a MVS console command
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ConsoleCmd {

    private static final String CMD = "cmd";
    private static final String SOL_KEY = "sol-key";
    private static final String SYSTEM = "system";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * ConsoleCmd constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ConsoleCmd(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative ConsoleCmd constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    ConsoleCmd(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Issue an MVS console command on default console name (Defcn) done synchronously - meaning solicited
     * (direct command responses) are gathered immediately after the command is issued.
     * However, after (according to the z/OSMF REST API documentation), approximately 3 seconds, the response
     * will be returned.
     *
     * @param command string value that represents command to issue
     * @return IssueCommandResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleCmdResponse issueCommand(final String command) throws ZosmfRequestException {
        return issueCommandCommon(ConsoleConstants.RES_DEF_CN, new ConsoleCmdInputData(command));
    }

    /**
     * Issue an MVS console command on a given console name done synchronously - meaning solicited
     * (direct command responses) are gathered immediately after the command is issued.
     * However, after (according to the z/OSMF REST API documentation), approximately 3 seconds, the response
     * will be returned.
     *
     * @param command     string value representing console command to issue
     * @param consoleName name of the console that is used to issue the command
     * @return IssueCommandResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleCmdResponse issueCommand(final String command, final String consoleName) throws ZosmfRequestException {
        return issueCommandCommon(consoleName, new ConsoleCmdInputData(command));
    }

    /**
     * Issue an MVS console command on a given console name driven by IssueConsoleInputData settings done synchronously
     *
     * @param consoleName      name of the console that is used to issue the command
     * @param consoleInputData synchronous console issue parameters, see IssueConsoleInputData object
     * @return IssueCommandResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleCmdResponse issueCommandCommon(final String consoleName, final ConsoleCmdInputData consoleInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(consoleName, "consoleName");
        ValidateUtils.checkNullParameter(consoleInputData == null, "consoleInputData is null");

        final String url = connection.getZosmfUrl() + ConsoleConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(consoleName);

        final Map<String, String> issueMap = getIssueMap(consoleInputData);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(issueMap).toString());

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no issue console response phrase")).toString();

        final JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException(e.getMessage());
        }

        ConsoleCmdResponse response = objectMapper.convertValue(jsonNode, ConsoleCmdResponse.class);
        if (consoleInputData.isProcessResponse()) {
            String responseStr = response.getCmdResponse().orElse("");
            responseStr = responseStr.replace('\r', '\n');
            if (!responseStr.isBlank() && responseStr.charAt(responseStr.length() - 1) != '\n') {
                responseStr = responseStr + "\n";
            }
            response.setCmdResponse(responseStr);
        }
        return response;
    }

    /**
     * Generate a map of values for JSON body payload for request
     *
     * @param consoleInputData IssueConsoleInputData object
     * @return Map for JSON body
     * @author Shabaz Kowthalam
     */
    private Map<String, String> getIssueMap(ConsoleCmdInputData consoleInputData) {
        final Map<String, String> issueMap = new HashMap<>();
        issueMap.put(CMD, consoleInputData.getCmd());
        consoleInputData.getSolKey().ifPresent(solKey -> issueMap.put(SOL_KEY, solKey));
        consoleInputData.getSystem().ifPresent(sys -> issueMap.put(SYSTEM, sys));
        return issueMap;
    }

}
