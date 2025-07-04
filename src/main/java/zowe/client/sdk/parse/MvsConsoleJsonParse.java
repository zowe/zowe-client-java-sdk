/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parse;

import org.json.simple.JSONObject;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.response.ZosmfIssueResponse;

/**
 * Parse json response from MVS console request
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class MvsConsoleJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final MvsConsoleJsonParse instance = new MvsConsoleJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private MvsConsoleJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return MvsConsoleJsonParse object
     * @author Frank Giordano
     */
    public static MvsConsoleJsonParse getInstance() {
        return MvsConsoleJsonParse.Holder.instance;
    }

    /**
     * Transform data into ZosmfIssueResponse object
     *
     * @param args json data to parse
     * @return ZosmfIssueResponse object
     * @author Frank Giordano
     */
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        final ZosmfIssueResponse zosmfIssueResponse = new ZosmfIssueResponse();
        zosmfIssueResponse.setCmdResponseKey(
                data.get("cmd-response-key") != null ? (String) data.get("cmd-response-key") : null);
        zosmfIssueResponse.setCmdResponseUrl(
                data.get("cmd-response-url") != null ? (String) data.get("cmd-response-url") : null);
        zosmfIssueResponse.setCmdResponseUri(
                data.get("cmd-response-uri") != null ? (String) data.get("cmd-response-uri") : null);
        zosmfIssueResponse.setCmdResponse(
                data.get("cmd-response") != null ? (String) data.get("cmd-response") : null);
        zosmfIssueResponse.setSolKeyDetected(
                data.get("sol-key-detected") != null ? (String) data.get("sol-key-detected") : null);
        return zosmfIssueResponse;
    }

}
