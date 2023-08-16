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
import zowe.client.sdk.zosconsole.response.ZosmfIssueResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for MvsConsoleParseResponse.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class MvsConsoleParseResponseTest {

    @Test
    public void tstMvsConsoleParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE).setJsonObject(null);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstMvsConsoleParseJsonStopResponseSingletonSuccess() throws Exception {
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE);
        final JsonParseResponse parser2 = JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE);
        assertSame(parser, parser2);
    }

    @Test
    public void tstMvsConsoleParseJsonStopResponseSingletonWithDataSuccess() throws Exception {
        final JsonParseResponse parser =
                JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE).setJsonObject(new JSONObject());
        final JsonParseResponse parser2 =
                JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE).setJsonObject(new JSONObject());
        assertSame(parser, parser2);
    }

    @Test
    public void tstMvsConsoleParseJsonStopResponseResetDataFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE).setJsonObject(new JSONObject());
            JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE).parseResponse();
            JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
        try {
            JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE).setJsonObject(new JSONObject()).parseResponse();
            JsonParseResponseFactory.buildParser(ParseType.MVS_CONSOLE).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
    }

    @Test
    public void tstMvsConsoleParseJsonStopResponseSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "ver");
        final JSONObject json = new JSONObject(jsonMap);

        final ZosmfIssueResponse response = (ZosmfIssueResponse) JsonParseResponseFactory
                .buildParser(ParseType.MVS_CONSOLE).setJsonObject(json).parseResponse();
        assertEquals("ver", response.getCmdResponse().orElse("n\\a"));
    }

}
