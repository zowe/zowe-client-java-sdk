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
import org.junit.Test;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class containing unit tests for TsoStopParseResponse.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TsoStopParseResponseTest {

    @Test
    public void tstParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(null, ParseType.TSO_STOP);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("json data is null", msg);
    }

    @Test
    public void tstParseJsonStopResponseSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("ver", "ver");
        jsonMap.put("servletKey", "servletKey");
        jsonMap.put("reused", true);
        jsonMap.put("timeout", true);
        final JSONObject json = new JSONObject(jsonMap);

        final ZosmfTsoResponse response =
                (ZosmfTsoResponse) JsonParseResponseFactory.buildParser(json, ParseType.TSO_STOP).parseResponse();
        assertEquals("ver", response.getVer().get());
        assertEquals("servletKey", response.getServletKey().get());
        assertTrue(response.getReused().get());
        assertTrue(response.getTimeout().get());
    }

}
