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
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonParserUtil;
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
 * @version 4.0
 */
public class IssueConsole {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * IssueCommand constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public IssueConsole(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative IssueCommand constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    IssueConsole(final ZosConnection connection, final ZosmfRequest request) {
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
     * @return ConsoleResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleResponse issueCommand(final String command) throws ZosmfRequestException {
        return issueCommandCommon(ConsoleConstants.RES_DEF_CN, new IssueConsoleParams(command));
    }

    /**
     * Issue an MVS console command on a given console name done synchronously - meaning solicited
     * (direct command responses) are gathered immediately after the command is issued.
     * However, after (according to the z/OSMF REST API documentation), approximately 3 seconds, the response
     * will be returned.
     *
     * @param command     string value representing console command to issue
     * @param consoleName name of the console that is used to issue the command
     * @return ConsoleResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleResponse issueCommand(final String command, final String consoleName) throws ZosmfRequestException {
        return issueCommandCommon(consoleName, new IssueConsoleParams(command));
    }

    /**
     * Issue an MVS console command on a given console name driven by IssueConsoleParams settings done synchronously
     *
     * @param consoleName name of the console that is used to issue the command
     * @param params      synchronous console issue parameters, see ZosmfIssueParams object
     * @return command response on resolve, see ZosmfIssueResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleResponse issueCommandCommon(final String consoleName, final IssueConsoleParams params)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(consoleName == null, "consoleName is null");
        ValidateUtils.checkIllegalParameter(consoleName.isBlank(), "consoleName not specified");

        final String url = connection.getZosmfUrl() + ConsoleConstants.RESOURCE + "/" + EncodeUtils.encodeURIComponent(consoleName);

        final Map<String, String> issueMap = new HashMap<>();
        issueMap.put("cmd", params.getCmd());
        params.getSolKey().ifPresent(solKey -> issueMap.put("sol-key", solKey));
        params.getSystem().ifPresent(sys -> issueMap.put("system", sys));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(issueMap).toString());

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no issue console response phrase")).toString();
        final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
        return ConsoleResponseService.getInstance()
                .buildConsoleResponse((ZosmfIssueResponse) JsonParseFactory.buildParser(ParseType.MVS_CONSOLE)
                        .parseResponse(jsonObject), params.isProcessResponse());
    }

}
