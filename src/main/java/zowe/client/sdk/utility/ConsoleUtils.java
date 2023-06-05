/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosconsole.response.ConsoleResponse;
import zowe.client.sdk.zosconsole.response.ZosmfIssueResponse;

/**
 * Utility Class contains helper methods for console response commands and response processing
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class ConsoleUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ConsoleUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Transform JSON into ZosmfIssueResponse object
     *
     * @param jsonObject JSON object
     * @return ZosmfIssueResponse object
     * @author Frank Giordano
     */
    public static ZosmfIssueResponse parseJsonIssueCmdResponse(JSONObject jsonObject) {
        ZosmfIssueResponse zosmfIssueResponse = new ZosmfIssueResponse();
        zosmfIssueResponse.setCmdResponseKey((String) jsonObject.get("cmd-response-key"));
        zosmfIssueResponse.setCmdResponseUrl((String) jsonObject.get("cmd-response-url"));
        zosmfIssueResponse.setCmdResponseUri((String) jsonObject.get("cmd-response-uri"));
        zosmfIssueResponse.setCmdResponse((String) jsonObject.get("cmd-response"));
        zosmfIssueResponse.setSolKeyDetected((String) jsonObject.get("sol-key-detected"));
        return zosmfIssueResponse;
    }

    /**
     * Populate the console response with the details returned from the z/OSMF console API.
     * Method takes two parameters: response from z/OSMF command and response to be populated.
     * Method adds response to a collection of z/OSMF responses, mark response as "succeeded"
     * (response.success = true) and populate other fields of response with values from z/OSMF response.
     *
     * @param zosmfResponse    zosmf console response, see ZosmfIssueResponse object
     * @param response         console response to be populated, see ConsoleResponse object
     * @param processResponses boolean if set to true, append command response string to the console API response
     * @author Frank Giordano
     */
    public static void populate(ZosmfIssueResponse zosmfResponse, ConsoleResponse response, boolean processResponses) {
        ValidateUtils.checkNullParameter(zosmfResponse == null, "zosmfResponse is null");
        ValidateUtils.checkNullParameter(response == null, "response is null");
        response.setZosmfResponse(zosmfResponse);
        response.setSuccess(true);

        if (zosmfResponse.getSolKeyDetected().isPresent()) {
            response.setKeywordDetected(true);
        }

        // Append the command response string to the console response.
        if (zosmfResponse.getCmdResponse().isPresent() && zosmfResponse.getCmdResponse().get().length() > 0
                && processResponses) {
            // the IBM responses sometimes have \r and \r\n, we will process them here and return them with just \n.
            final String responseValue = zosmfResponse.getCmdResponse().get().replace('\r', '\n');
            response.setCommandResponse(responseValue);
            // If there are messages append a line-break to ensure that additional messages collected are displayed properly.
            if (responseValue.length() > 0 && (responseValue.indexOf("\n") != responseValue.length() - 1)) {
                response.setCommandResponse(responseValue + "\n");
            }
        }

        // If the response key is present, set the last response key value in the response.
        zosmfResponse.getCmdResponseKey().ifPresent(response::setLastResponseKey);

        // Collect the response url.
        zosmfResponse.getCmdResponseUrl().ifPresent(response::setCmdResponseUrl);
    }

}

