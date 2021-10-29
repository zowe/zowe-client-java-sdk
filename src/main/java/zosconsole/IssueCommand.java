/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosconsole;

import core.ZOSConnection;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import rest.Response;
import rest.ZoweRequest;
import rest.ZoweRequestFactory;
import rest.ZoweRequestType;
import utility.Util;
import utility.UtilConsole;
import utility.UtilRest;
import zosconsole.input.IssueParams;
import zosconsole.zosmf.ZosmfIssueParams;
import zosconsole.zosmf.ZosmfIssueResponse;

import java.util.HashMap;

/**
 * Issue MVS Console commands by using a system console
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueCommand {

    private static final Logger LOG = LogManager.getLogger(IssueCommand.class);

    private final ZOSConnection connection;

    /**
     * IssueCommand constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public IssueCommand(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
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
    public ZosmfIssueResponse issueCommon(String consoleName, ZosmfIssueParams commandParams) throws Exception {
        Util.checkNullParameter(consoleName == null, "consoleName is null");
        Util.checkIllegalParameter(consoleName.isEmpty(), "consoleName not specified");
        Util.checkNullParameter(commandParams == null, "commandParams is null");
        Util.checkIllegalParameter(commandParams.getCmd().isEmpty(), "command not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ConsoleConstants.RESOURCE + "/" + consoleName;

        LOG.debug(url);

        var jsonMap = new HashMap<String, String>();
        jsonMap.put("cmd", commandParams.getCmd().get());
        var jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(jsonRequestBody);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, jsonRequestBody.toString(),
                ZoweRequestType.VerbType.PUT_JSON);
        Response response = request.executeRequest();
        if (response.isEmpty())
            return new ZosmfIssueResponse();

        UtilRest.checkHttpErrors(response);
        LOG.debug("Response result {}", response.getResponsePhrase());

        ZosmfIssueResponse zosmfIssueResponse = new ZosmfIssueResponse();
        JSONObject result = (JSONObject) response.getResponsePhrase()
                .orElseThrow(() -> new Exception("response phrase missing"));
        zosmfIssueResponse.setCmdResponseKey((String) result.get("cmd-response-key"));
        zosmfIssueResponse.setCmdResponseUrl((String) result.get("cmd-response-url"));
        zosmfIssueResponse.setCmdResponseUri((String) result.get("cmd-response-uri"));
        zosmfIssueResponse.setCmdResponse((String) result.get("cmd-response"));
        zosmfIssueResponse.setSolKeyDetected((String) result.get("sol-key-detected"));

        return zosmfIssueResponse;
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
        ZosmfIssueResponse resp = issueCommon(ConsoleConstants.RES_DEF_CN, commandParams);
        resp.setCmdResponse(StringEscapeUtils.escapeJava(resp.getCmdResponse().orElse("")));
        return resp;
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
    public ConsoleResponse issue(IssueParams params) throws Exception {
        Util.checkNullParameter(params == null, "params is null");

        String consoleName = params.getConsoleName().orElse(ConsoleConstants.RES_DEF_CN);
        ZosmfIssueParams commandParams = buildZosmfConsoleApiParameters(params);
        ConsoleResponse response = new ConsoleResponse();

        ZosmfIssueResponse resp = issueCommon(consoleName, commandParams);
        UtilConsole.populate(resp, response, params.getProcessResponses().orElse(true));

        return response;
    }

    /**
     * Simple issue console command method. Does not accept parameters, so all defaults on the z/OSMF API are taken.
     *
     * @param theCommand string command to issue
     * @return command response on resolve, see ConsoleResponse object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ConsoleResponse issueSimple(String theCommand) throws Exception {
        IssueParams params = new IssueParams();
        params.setCommand(theCommand);
        return issue(params);
    }

    /**
     * Build ZosmfIssueParams object from provided parameters
     *
     * @param params parameters for issue command, see IssueParams object
     * @return request body parameters, see ZosmfIssueParams object
     * @author Frank Giordano
     */
    private ZosmfIssueParams buildZosmfConsoleApiParameters(IssueParams params) {
        Util.checkNullParameter(params == null, "params is null");
        Util.checkIllegalParameter(params.getCommand().isEmpty(), "command not specified");

        ZosmfIssueParams zosmfParams = new ZosmfIssueParams();
        zosmfParams.setCmd(params.getCommand().get());

        params.getSolicitedKeyword().ifPresent(zosmfParams::setSolKey);
        params.getSysplexSystem().ifPresent(zosmfParams::setSystem);

        return zosmfParams;
    }

}
