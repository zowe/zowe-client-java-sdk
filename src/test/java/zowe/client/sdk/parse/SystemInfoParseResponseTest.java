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
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for SystemInfoParseResponse.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class SystemInfoParseResponseTest {

    @Test
    public void tstZosmfInfoParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.ZOSMF_INFO).parseResponse((Object) null);
        } catch (NullPointerException e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstZosmfInfoParseJsonStopResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.ZOSMF_INFO);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.ZOSMF_INFO);
        assertSame(parser, parser2);
    }

    @Test
    public void tstZosmfInfoParseJsonStopResponseSuccess() {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("api_version", "ver");
        jsonMap.put("zosmf_port", "dev");
        final JSONObject json = new JSONObject(jsonMap);

        final ZosmfInfoResponse response = (ZosmfInfoResponse)
                JsonParseFactory.buildParser(ParseType.ZOSMF_INFO).parseResponse(json);
        assertEquals("ver", response.getApiVersion().orElse("n\\a"));
        assertEquals("dev", response.getZosmfPort().orElse("n\\a"));
        assertEquals("n\\a", response.getZosVersion().orElse("n\\a"));
    }

}
