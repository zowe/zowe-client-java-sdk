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
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ConsoleUtils;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Issue a MVS console command.
 * <p>
 * This operation issues a command, based on the properties that are specified in the request body.
 * On successful completion, HTTP status code 200 is returned. A JSON object typically contains the
 * command response.
 * <p>
 * When a command is issued synchronously, the console API attempts to get the solicited messages
 * immediately after the command is issued. If there are no messages available within a certain time
 * interval, approximately 3 seconds when your system workload is not high, the API returns
 * "cmd-response": "" in the response body.
 * <p>
 * A value for cmd-response of the empty string, "", usually means that there is no command response.
 * However, it is also possible that the command response arrived after 3 seconds. If that is the case,
 * you can use the cmd-response-url field in the response body to retrieve the command response.
 * You might do this several times to ensure that all messages that are related to the command
 * are retrieved. This is prebuilt for you via the ConsoleGet class in this package.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class ConsoleCmd {

    private static final String CMD = "cmd";
    private static final String SOL_KEY = "sol-key";
    private static final String SYSTEM = "system";
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * ConsoleCmd constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ConsoleCmd(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
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
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
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
     * Issue an MVS console command on a given console name driven by ConsoleCmdInputData settings done synchronously.
     *
     * @param consoleName      name of the console that is used to issue the command
     * @param consoleInputData synchronous console issue parameters, see ConsoleCmdInputData object
     * @return IssueCommandResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleCmdResponse issueCommandCommon(final String consoleName, final ConsoleCmdInputData consoleInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(consoleName, "consoleName");
        ValidateUtils.checkNullParameter(consoleInputData, "consoleInputData");

        final String url = connection.getZosmfUrl() +
                ConsoleConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(consoleName);

        final Map<String, String> issueMap = getIssueMap(consoleInputData);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(issueMap).toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no issue console response phrase"))
                .toString();

        final String context = "issueCommandCommon";
        ConsoleCmdResponse response = JsonUtils.parseResponse(responsePhrase, ConsoleCmdResponse.class, context);
        if (consoleInputData.isProcessResponse()) {
            response = response.withCmdResponse(ConsoleUtils.processCmdResponse(response.getCmdResponse()));
        }
        return response;
    }

    /**
     * Generate a map of values for JSON body payload for request.
     *
     * @param consoleInputData ConsoleCmdInputData object
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
