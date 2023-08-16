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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for PropsParseResponse.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class PropsParseResponseTest {

    @Test
    public void tstPropsParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(ParseType.PROPS).setJsonObject(null);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstPropsParseJsonStopResponseSingletonSuccess() throws Exception {
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(ParseType.PROPS);
        final JsonParseResponse parser2 = JsonParseResponseFactory.buildParser(ParseType.PROPS);
        assertSame(parser, parser2);
    }

    @Test
    public void tstPropsParseJsonStopResponseSingletonWithDataSuccess() throws Exception {
        final JsonParseResponse parser =
                JsonParseResponseFactory.buildParser(ParseType.PROPS).setJsonObject(new JSONObject());
        final JsonParseResponse parser2 =
                JsonParseResponseFactory.buildParser(ParseType.PROPS).setJsonObject(new JSONObject());
        assertSame(parser, parser2);
    }

    @Test
    public void tstPropsParseJsonStopResponseResetDataFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(ParseType.PROPS).setJsonObject(new JSONObject());
            JsonParseResponseFactory.buildParser(ParseType.PROPS).parseResponse();
            JsonParseResponseFactory.buildParser(ParseType.PROPS).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
        try {
            JsonParseResponseFactory.buildParser(ParseType.PROPS).setJsonObject(new JSONObject()).parseResponse();
            JsonParseResponseFactory.buildParser(ParseType.PROPS).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
    }

    @Test
    public void tstPropsParseJsonStopResponseSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "ver");
        final JSONObject json = new JSONObject(jsonMap);

        final Map<String, String> response = (Map<String, String>) JsonParseResponseFactory
                .buildParser(ParseType.PROPS).setJsonObject(json).parseResponse();
        assertEquals("ver", response.get("cmd-response"));
    }


}
