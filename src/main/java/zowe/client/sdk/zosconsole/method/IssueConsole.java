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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseResponse;
import zowe.client.sdk.parse.JsonParseResponseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.input.IssueConsoleParams;
import zowe.client.sdk.zosconsole.response.ConsoleResponse;
import zowe.client.sdk.zosconsole.response.ZosmfIssueResponse;
import zowe.client.sdk.zosconsole.service.ConsoleResponseService;

import java.util.HashMap;
import java.util.Map;

/**
 * Issue MVS Console commands by using a system console
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class IssueConsole {

    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * IssueCommand constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public IssueConsole(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative IssueCommand constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public IssueConsole(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Issue an MVS console command on default console name (Defcn) done synchronously - meaning solicited
     * (direct command responses) are gathered immediately after the command is issued.
     * However, after (according to the z/OSMF REST API documentation) approximately 3 seconds the response
     * will be returned.
     *
     * @param command string value that represents command to issue
     * @return ConsoleResponse object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ConsoleResponse issueCommand(String command) throws Exception {
        return issueCommandCommon(ConsoleConstants.RES_DEF_CN, new IssueConsoleParams(command));
    }

    /**
     * Issue an MVS console command on given console name done synchronously - meaning solicited
     * (direct command responses) are gathered immediately after the command is issued.
     * However, after (according to the z/OSMF REST API documentation) approximately 3 seconds the response
     * will be returned.
     *
     * @param command     string value representing console command to issue
     * @param consoleName name of the console that is used to issue the command
     * @return ConsoleResponse object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ConsoleResponse issueCommand(String command, String consoleName) throws Exception {
        return issueCommandCommon(consoleName, new IssueConsoleParams(command));
    }

    /**
     * Issue an MVS console command on given console name driven by IssueConsoleParams settings done synchronously
     *
     * @param params synchronous console issue parameters, see ZosmfIssueParams object
     * @return command response on resolve, see ZosmfIssueResponse object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ConsoleResponse issueCommandCommon(String consoleName, IssueConsoleParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(consoleName == null, "consoleName is null");
        ValidateUtils.checkIllegalParameter(consoleName.isBlank(), "consoleName not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ConsoleConstants.RESOURCE + "/" + EncodeUtils.encodeURIComponent(consoleName);

        final Map<String, String> issueMap = new HashMap<>();
        issueMap.put("cmd", params.getCmd().orElseThrow(
                () -> new IllegalStateException("issue console params command not specified")));
        params.getSolKey().ifPresent(solKey -> issueMap.put("sol-key", solKey));
        params.getSystem().ifPresent(sys -> issueMap.put("system", sys));

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(issueMap).toString());

        final String jsonStr = RestUtils.getResponse(request).getResponsePhrase()
                .orElseThrow(() -> new Exception("no issue console response phrase")).toString();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonStr);
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(jsonObject, ParseType.MVS_CONSOLE);
        ConsoleResponseService consoleResponseService = new ConsoleResponseService((ZosmfIssueResponse) parser.parseResponse());
        return consoleResponseService.setConsoleResponse(params.isProcessResponse());
    }

}
