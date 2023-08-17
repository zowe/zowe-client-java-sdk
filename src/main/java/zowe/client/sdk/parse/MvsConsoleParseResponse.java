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
 * @version 2.0
 */
public final class MvsConsoleParseResponse implements JsonParseResponse {

    /**
     * Represents one singleton instance
     */
    private static JsonParseResponse INSTANCE;

    /**
     * JSON data value to be parsed
     */
    private JSONObject data;

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private MvsConsoleParseResponse() {
    }

    /**
     * Get singleton instance
     *
     * @return MvsConsoleParseResponse object
     * @author Frank Giordano
     */
    public synchronized static JsonParseResponse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MvsConsoleParseResponse();
        }
        return INSTANCE;
    }

    /**
     * Transform data into ZosmfIssueResponse object
     *
     * @return ZosmfIssueResponse object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.REQUIRED_ACTION_MSG);
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
        data = null;
        return zosmfIssueResponse;
    }

    /**
     * Set the data to be parsed
     *
     * @param data json data to parse
     * @return JsonParseResponse this object
     * @author Frank Giordano
     */
    @Override
    public JsonParseResponse setJsonObject(final JSONObject data) {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.DATA_NULL_MSG);
        this.data = data;
        return this;
    }

}
