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
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;

/**
 * Parse json response from Tso Stop request
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TsoStopParseResponse extends JsonParseResponse {

    /**
     * TsoStopParseResponse constructor
     *
     * @param data json object
     * @author Frank Giordano
     */
    public TsoStopParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Retrieve parsed json Tso Stop Response
     *
     * @return populated console response, see ZosmfTsoResponse object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        return new ZosmfTsoResponse.Builder()
                .ver(data.get("ver") != null ? (String) data.get("ver") : null)
                .servletKey(data.get("servletKey") != null ? (String) data.get("servletKey") : null)
                .reused(data.get("reused") != null ? (Boolean) data.get("reused") : null)
                .timeout(data.get("timeout") != null ? (Boolean) data.get("timeout") : null)
                .build();
    }

}
