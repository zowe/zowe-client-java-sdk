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
public final class TsoStopJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final TsoStopJsonParse instance = new TsoStopJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private TsoStopJsonParse() {
    }

    /**
     * Get singleton instance
     *
     * @return TsoStopJsonParse object
     * @author Frank Giordano
     */
    public static TsoStopJsonParse getInstance() {
        return TsoStopJsonParse.Holder.instance;
    }

    /**
     * Transform retrieved Tso Stop response into ZosmfTsoResponse
     *
     * @param args json data to parse
     * @return populated console response, see ZosmfTsoResponse object
     * @author Frank Giordano
     */
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        return new ZosmfTsoResponse.Builder()
                .ver(data.get("ver") != null ? (String) data.get("ver") : null)
                .servletKey(data.get("servletKey") != null ? (String) data.get("servletKey") : null)
                .reused(data.get("reused") != null && (boolean) data.get("reused"))
                .timeout(data.get("timeout") != null && (boolean) data.get("timeout"))
                .build();
    }

}
