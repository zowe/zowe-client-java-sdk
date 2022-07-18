/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parsejson;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosconsole.zosmf.ZosmfIssueResponse;

/**
 * Class to transform JSON into ZosmfIssueResponse object
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ParseIssueCommandJson implements IParseJson<ZosmfIssueResponse> {

    /**
     * Transform JSON into ZosmfIssueResponse object
     *
     * @param jsonObject JSON object
     * @return ZosmfIssueResponse object
     * @author Frank Giordano
     */
    @Override
    public ZosmfIssueResponse parse(JSONObject jsonObject) {
        ZosmfIssueResponse zosmfIssueResponse = new ZosmfIssueResponse();
        zosmfIssueResponse.setCmdResponseKey((String) jsonObject.get("cmd-response-key"));
        zosmfIssueResponse.setCmdResponseUrl((String) jsonObject.get("cmd-response-url"));
        zosmfIssueResponse.setCmdResponseUri((String) jsonObject.get("cmd-response-uri"));
        zosmfIssueResponse.setCmdResponse((String) jsonObject.get("cmd-response"));
        zosmfIssueResponse.setSolKeyDetected((String) jsonObject.get("sol-key-detected"));
        return zosmfIssueResponse;
    }

}
