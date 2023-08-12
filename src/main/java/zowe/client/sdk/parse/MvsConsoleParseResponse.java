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
import zowe.client.sdk.zosconsole.response.ZosmfIssueResponse;

/**
 * Parse json response from MVS console request
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class MvsConsoleParseResponse extends JsonParseResponse {

    /**
     * MvsConsoleParseResponse constructor
     *
     * @param data json data value to be parsed
     * @author Frank Giordano
     */
    public MvsConsoleParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Transform data into ZosmfIssueResponse object
     *
     * @return ZosmfIssueResponse object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        ZosmfIssueResponse zosmfIssueResponse = new ZosmfIssueResponse();
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
