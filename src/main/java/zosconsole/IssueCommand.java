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

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import rest.ZoweRequest;
import rest.ZoweRequestFactory;
import rest.ZoweRequestType;
import utility.UtilRest;
import zosconsole.zosmf.IssueParms;
import zosconsole.zosmf.ZosmfIssueParms;
import zosconsole.zosmf.ZosmfIssueResponse;
import core.ZOSConnection;
import org.json.simple.JSONObject;
import utility.Util;

/**
 * Issue MVS Console commands by using a system console
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueCommand {

    private static final Logger LOG = LogManager.getLogger(IssueCommand.class);

    private ZOSConnection connection;

    /**
     * IssueCommand constructor
     *
     * @param connection ZOSConnection object
     * @author Frank Giordano
     */
    public IssueCommand(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Issue an MVS console command, returns "raw" z/OSMF response
     *
     * @param consoleName name of the EMCS console that is used to issue the command
     * @param commandParms synchronous console issue parameters, see ZosmfIssueParms
     * @return ZosmfIssueResponse command response on resolve, see ZosmfIssueResponse
     * @author Frank Giordano
     * @throws Exception processing error
     */
    public ZosmfIssueResponse issueCommon(String consoleName, ZosmfIssueParms commandParms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(consoleName == null, "consoleName is null");
        Util.checkNullParameter(commandParms == null, "commandParms is null");
        Util.checkStateParameter(consoleName.isEmpty(), "consoleName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() +
                ConsoleConstants.RESOURCE + "/" + consoleName;

        LOG.debug(url);

        JSONObject reqBody = new JSONObject();
        reqBody.put("cmd", commandParms.getCmd().get());
        LOG.debug(reqBody);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, reqBody.toString(),
                ZoweRequestType.VerbType.PUT_JSON);
        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return new ZosmfIssueResponse();

        UtilRest.checkHttpErrors(response);
        LOG.debug("Response result {}", response.getResponsePhrase());

        ZosmfIssueResponse zosmfIssueResponse = new ZosmfIssueResponse();
        JSONObject result = (JSONObject) response.getResponsePhrase().orElseThrow(Exception::new);
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
     * @param commandParms synchronous console issue parameters, @see ZosmfIssueParms
     * @return ZosmfIssueResponse command response on resolve, @see ZosmfIssueResponse
     * @author Frank Giordano
     * @throws Exception processing error
     */
    public ZosmfIssueResponse issueDefConsoleCommon(ZosmfIssueParms commandParms) throws Exception {
        ZosmfIssueResponse resp = issueCommon(ConsoleConstants.RES_DEF_CN, commandParms);
        resp.setCmdResponse(StringEscapeUtils.escapeJava(resp.getCmdResponse().get()));
        return resp;
    }

    /**
     * Issue an MVS console command done synchronously - meaning solicited (direct command responses) are gathered
     * immediately after the command is issued. However, after (according to the z/OSMF REST API documentation)
     * approximately 3 seconds the response will be returned.
     *
     * @param parms console issue parameters, @see IssueParms
     * @return ConsoleResponse command response on resolve, @see ConsoleResponse
     * @author Frank Giordano
     * @throws Exception processing error
     */
    public ConsoleResponse issue(IssueParms parms) throws Exception {
        Util.checkNullParameter(parms == null, "parms is null");

        String consoleName = parms.getConsoleName().isPresent() ?
                parms.getConsoleName().get() : ConsoleConstants.RES_DEF_CN;
        ZosmfIssueParms commandParms = buildZosmfConsoleApiParameters(parms);
        ConsoleResponse response = new ConsoleResponse();

        ZosmfIssueResponse resp = issueCommon(consoleName, commandParms);
        response = ConsoleResponseService.populate(resp, response, parms.getProcessResponses().isPresent() ?
                parms.getProcessResponses().get() : true);

        return response;
    }

    /**
     * Simple issue console command method. Does not accept parameters, so all defaults on the z/OSMF API are taken.
     *
     * @param theCommand string command to issue
     * @return ConsoleResponse command response on resolve, @see ConsoleResponse
     * @author Frank Giordano
     * @throws Exception processing error
     */
    public ConsoleResponse issueSimple(String theCommand) throws Exception {
        IssueParms parms = new IssueParms();
        parms.setCommand(theCommand);
        return issue(parms);
    }

    /**
     * Build IZosmfIssueParms object from provided parameters
     * 
     * @param parms IssueParms parameters for issue command
     * @return ZosmfIssueParms request body, @see ZosmfIssueParms
     * @author Frank Giordano
     * @throws Exception processing error
     */
    public ZosmfIssueParms buildZosmfConsoleApiParameters(IssueParms parms) throws Exception {
        Util.checkNullParameter(parms == null, "parms is null");

        ZosmfIssueParms zosmfParms = new ZosmfIssueParms();
        if (parms.getCommand().isPresent()) {
            zosmfParms.setCmd(parms.getCommand().get());
        } else {
            throw new Exception("command not specified");
        }

        if (parms.getSolicitedKeyword().isPresent()) {
            zosmfParms.setSolKey(parms.getSolicitedKeyword().get());
        }

        if (parms.getSysplexSystem().isPresent()) {
            zosmfParms.setSystem(parms.getSysplexSystem().get());
        }

        return zosmfParms;
    }

}
