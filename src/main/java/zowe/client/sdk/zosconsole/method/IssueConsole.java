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

import org.apache.commons.text.StringEscapeUtils;
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
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.input.IssueParams;
import zowe.client.sdk.zosconsole.input.ZosmfIssueParams;
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
     * Build ZosmfIssueParams object from provided parameters
     *
     * @param params parameters for issue command, see IssueParams object
     * @return request body parameters, see ZosmfIssueParams object
     * @author Frank Giordano
     */
    private ZosmfIssueParams buildZosmfConsoleApiParameters(IssueParams params) {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final ZosmfIssueParams zosmfParams = new ZosmfIssueParams();
        zosmfParams.setCmd(params.getCommand()
                .orElseThrow(() -> new IllegalStateException("issue console command not specified")));

        params.getSolicitedKeyword().ifPresent(zosmfParams::setSolKey);
        params.getSysplexSystem().ifPresent(zosmfParams::setSystem);

        return zosmfParams;
    }

    /**
     * Issue an MVS console command done synchronously - meaning solicited (direct command responses) are gathered
     * immediately after the command is issued. However, after (according to the z/OSMF REST API documentation)
     * approximately 3 seconds the response will be returned.
     *
     * @param params console issue parameters, see IssueParams object
     * @return command response on resolve, see ConsoleResponse object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ConsoleResponse issueCommand(IssueParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String consoleName = params.getConsoleName().orElse(ConsoleConstants.RES_DEF_CN);
        final ZosmfIssueParams zosmfIssueParams = buildZosmfConsoleApiParameters(params);
        final ZosmfIssueResponse zosmfIssueResponse = issueCommonCommand(consoleName, zosmfIssueParams);
        return new ConsoleResponseService(zosmfIssueResponse).setConsoleResponse(params.getProcessResponses().orElse(true));
    }

    /**
     * Issue an MVS console command, returns "raw" z/OSMF response
     *
     * @param consoleName   string name of the mvs console that is used to issue the command
     * @param commandParams synchronous console issue parameters, see ZosmfIssueParams object
     * @return command response on resolve, see ZosmfIssueResponse object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ZosmfIssueResponse issueCommonCommand(String consoleName, ZosmfIssueParams commandParams) throws Exception {
        ValidateUtils.checkNullParameter(consoleName == null, "consoleName is null");
        ValidateUtils.checkIllegalParameter(consoleName.isBlank(), "consoleName not specified");
        ValidateUtils.checkNullParameter(commandParams == null, "commandParams is null");
        ValidateUtils.checkIllegalParameter(commandParams.getCmd().isEmpty(), "command not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ConsoleConstants.RESOURCE + "/" + consoleName;

        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("cmd", commandParams.getCmd().get());

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        final String jsonStr = RestUtils.getResponse(request).getResponsePhrase()
                .orElseThrow(() -> new Exception("no issue console response phase")).toString();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonStr);
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(jsonObject, ParseType.MVS_CONSOLE);
        return (ZosmfIssueResponse) parser.parseResponse();
    }

    /**
     * Issue an MVS console command in default console, returns "raw" z/OSMF response
     *
     * @param commandParams synchronous console issue parameters, see ZosmfIssueParams object
     * @return command response on resolve, see ZosmfIssueResponse object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ZosmfIssueResponse issueDefConsoleCommon(ZosmfIssueParams commandParams) throws Exception {
        final ZosmfIssueResponse resp = issueCommonCommand(ConsoleConstants.RES_DEF_CN, commandParams);
        resp.setCmdResponse(StringEscapeUtils.escapeJava(resp.getCmdResponse().orElse("")));
        return resp;
    }

    /**
     * Issue console command method. Does not accept parameters, so all defaults on the z/OSMF API are taken.
     *
     * @param theCommand string command to issue
     * @return command response on resolve, see ConsoleResponse object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ConsoleResponse issueCommand(String theCommand) throws Exception {
        final IssueParams params = new IssueParams();
        params.setCommand(theCommand);
        return issueCommand(params);
    }

}
