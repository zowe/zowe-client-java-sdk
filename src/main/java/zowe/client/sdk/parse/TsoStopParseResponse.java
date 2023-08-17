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
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;

/**
 * Parse json response from Tso Stop request
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class TsoStopParseResponse implements JsonParseResponse {

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
    private TsoStopParseResponse() {
    }

    /**
     * Get singleton instance
     *
     * @return TsoStopParseResponse object
     * @author Frank Giordano
     */
    public synchronized static JsonParseResponse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TsoStopParseResponse();
        }
        return INSTANCE;
    }

    /**
     * Retrieve parsed json Tso Stop Response
     *
     * @return populated console response, see ZosmfTsoResponse object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.REQUIRED_ACTION_MSG);
        final ZosmfTsoResponse zosmfTsoResponse = new ZosmfTsoResponse.Builder()
                .ver(data.get("ver") != null ? (String) data.get("ver") : null)
                .servletKey(data.get("servletKey") != null ? (String) data.get("servletKey") : null)
                .reused(data.get("reused") != null && (boolean) data.get("reused"))
                .timeout(data.get("timeout") != null && (boolean) data.get("timeout"))
                .build();
        data = null;
        return zosmfTsoResponse;
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
