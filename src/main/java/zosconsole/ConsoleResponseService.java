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

import zosconsole.zosmf.ZosmfIssueResponse;

public class ConsoleResponseService {

    public static ConsoleResponse populate(ZosmfIssueResponse zosmfResponse, ConsoleResponse response, boolean processResponses) {
        response.setZosmfResponse(zosmfResponse);
        response.setSuccess(true);

        if (zosmfResponse.getSolKeyDetected().isPresent())
            response.setKeywordDetected(true);

        // Append the command response string to the console response.
        if (zosmfResponse.getCmdResponse().isPresent() && zosmfResponse.getCmdResponse().get().length() > 0
                && processResponses) {
            // the IBM responses sometimes have \r and \r\n, we will process them our here and return them with just \n.
            response.setCommandResponse(zosmfResponse.getCmdResponse().get().replace('\r', '\n'));
            // If there are messages append a line-break to ensure that additional messages collected are displayed properly.
            if (response.getCommandResponse().get().length() > 0
                    && (response.getCommandResponse().get().indexOf("\n")
                    != response.getCommandResponse().get().length() - 1)) {
                response.setCommandResponse(response.getCommandResponse() + "\n");
            }
        }

        // If the response key is present, set the last response key value in the response.
        if (zosmfResponse.getCmdResponseKey().isPresent()) {
            response.setLastResponseKey(zosmfResponse.getCmdResponseKey().get());
        }

        // Collect the response url.
        if (zosmfResponse.getCmdResponseUrl().isPresent()) {
            response.setCmdResponseUrl(zosmfResponse.getCmdResponseUrl().get());
        }

        return response;
    }

}
