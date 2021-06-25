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
import zosconsole.zosmf.IssueParms;
import zosconsole.zosmf.ZosmfIssueParms;
import zosconsole.zosmf.ZosmfIssueResponse;
import core.ZOSConnection;
import org.apache.http.client.methods.HttpPut;
import org.json.simple.JSONObject;
import rest.IZoweRequest;
import rest.JsonRequest;
import utility.Util;

public class IssueCommand {

    private static final Logger LOG = LogManager.getLogger(IssueCommand.class);

    public static ZosmfIssueResponse issueCommon(ZOSConnection connection, String consoleName,
                                                 ZosmfIssueParms commandParms) throws Exception {
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

        IZoweRequest request = new JsonRequest(connection, new HttpPut(url), reqBody.toString());
        JSONObject result = request.httpPut();

        if (result == null) {
            throw new Exception("No results for console command " + reqBody);
        }
        LOG.debug(result);

        ZosmfIssueResponse response = new ZosmfIssueResponse();
        response.setCmdResponseKey((String) result.get("cmd-response-key"));
        response.setCmdResponseUrl((String) result.get("cmd-response-url"));
        response.setCmdResponseUri((String) result.get("cmd-response-uri"));
        response.setCmdResponse((String) result.get("cmd-response"));
        response.setSolKeyDetected((String) result.get("sol-key-detected"));

        return response;
    }

    /**
     * Issue an MVS console command in default console, returns "raw" z/OSMF response
     */
    public static ZosmfIssueResponse issueDefConsoleCommon(ZOSConnection connection, ZosmfIssueParms commandParms)
            throws Exception {
        ZosmfIssueResponse resp = IssueCommand.issueCommon(connection, ConsoleConstants.RES_DEF_CN, commandParms);
        resp.setCmdResponse(StringEscapeUtils.escapeJava(resp.getCmdResponse().get()));
        return resp;
    }

    public static ConsoleResponse issue(ZOSConnection connection, IssueParms parms) throws Exception {
        Util.checkNullParameter(parms == null, "parms is null");

        String consoleName = parms.getConsoleName().isPresent() ?
                parms.getConsoleName().get() : ConsoleConstants.RES_DEF_CN;
        ZosmfIssueParms commandParms = IssueCommand.buildZosmfConsoleApiParameters(parms);
        ConsoleResponse response = new ConsoleResponse();

        ZosmfIssueResponse resp = IssueCommand.issueCommon(connection, consoleName, commandParms);
        response = ConsoleResponseService.populate(resp, response, parms.getProcessResponses().isPresent() ?
                parms.getProcessResponses().get() : true);

        return response;
    }

    public static ConsoleResponse issueSimple(ZOSConnection connection, String theCommand) throws Exception {
        IssueParms parms = new IssueParms();
        parms.setCommand(theCommand);
        return IssueCommand.issue(connection, parms);
    }

    public static ZosmfIssueParms buildZosmfConsoleApiParameters(IssueParms parms) throws Exception {
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
