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
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for TsoParseResponse.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TsoParseResponseTest {

    @Test
    public void tstTsoParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).setJsonObject(null);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstTsoParseJsonStopResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.TSO_CONSOLE);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.TSO_CONSOLE);
        assertSame(parser, parser2);
    }

    @Test
    public void tstTsoParseJsonStopResponseSingletonWithDataSuccess() {
        final JsonParse parser =
                JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).setJsonObject(new JSONObject());
        final JsonParse parser2 =
                JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).setJsonObject(new JSONObject());
        assertSame(parser, parser2);
    }

    @Test
    public void tstTsoParseJsonStopResponseResetDataFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).setJsonObject(new JSONObject());
            JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).parseResponse();
            JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
        try {
            JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).setJsonObject(new JSONObject()).parseResponse();
            JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
    }

    @Test
    public void tstTsoParseJsonStopResponseSuccess() {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("ver", "ver");
        jsonMap.put("servletKey", "key");
        final JSONObject json = new JSONObject(jsonMap);

        final ZosmfTsoResponse response = (ZosmfTsoResponse) JsonParseFactory.buildParser(ParseType.TSO_CONSOLE)
                .setJsonObject(json).parseResponse();
        assertEquals("ver", response.getVer().orElse("n\\a"));
        assertEquals("key", response.getServletKey().orElse("n\\a"));
    }

}
