/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.service;

import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.response.ConsoleResponse;
import zowe.client.sdk.zosconsole.response.ZosmfIssueResponse;

/**
 * ConsoleResponseService singleton class service.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class ConsoleResponseService {

    private static ConsoleResponseService INSTANCE;

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private ConsoleResponseService() {
    }

    /**
     * Get singleton instance
     *
     * @return ConsoleResponseService object
     * @author Frank Giordano
     */
    public synchronized static ConsoleResponseService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConsoleResponseService();
        }
        return INSTANCE;
    }

    /**
     * Populate the console response with the details returned from the z/OSMF console API.
     * Method adds response to a collection of z/OSMF responses, mark response as "succeeded"
     * (response.success = true) and populate other fields of response with values from z/OSMF response.
     *
     * @param processResponses boolean if set to true, append command response string to the console API response
     * @return response         console response to be populated, see ConsoleResponse object
     * @author Frank Giordano
     */
    public ConsoleResponse buildConsoleResponse(ZosmfIssueResponse zosmfResponse, boolean processResponses) {
        ValidateUtils.checkNullParameter(zosmfResponse == null, "zosmfResponse is null");
        ConsoleResponse consoleResponse = new ConsoleResponse();
        consoleResponse.setZosmfResponse(zosmfResponse);
        consoleResponse.setSuccess(true);

        if (zosmfResponse.getSolKeyDetected().isPresent()) {
            consoleResponse.setKeywordDetected(true);
        }

        // Append the command response string to the console response.
        if (zosmfResponse.getCmdResponse().isPresent()) {
            String responseStr = zosmfResponse.getCmdResponse().get();
            consoleResponse.setCommandResponse(responseStr);
            if (processResponses) {
                // the IBM responses sometimes have \r and \r\n, we will process them here and return them with just \n.
                responseStr = responseStr.replace('\r', '\n');
                consoleResponse.setCommandResponse(responseStr);
                // Append a line-break to ensure that additional messages collected are displayed properly.
                if (responseStr.charAt(responseStr.length() - 1) != '\n') {
                    consoleResponse.setCommandResponse(responseStr + "\n");
                }
            }

        }

        // If the response key is present, set the last response key value in the response.
        zosmfResponse.getCmdResponseKey().ifPresent(consoleResponse::setLastResponseKey);

        // Collect the response url.
        zosmfResponse.getCmdResponseUrl().ifPresent(consoleResponse::setCmdResponseUrl);
        return consoleResponse;
    }

}

