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
 * @version 5.0
 */
public class MvsConsoleParseResponseTest {

    @Test
    public void tstMvsConsoleParseResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.MVS_CONSOLE).parseResponse((Object) null);
        } catch (NullPointerException e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstMvsConsoleParseResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.MVS_CONSOLE);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.MVS_CONSOLE);
        assertSame(parser, parser2);
    }

    @Test
    public void tstMvsConsoleParseResponseSuccess() {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "ver");
        final JSONObject json = new JSONObject(jsonMap);

        final ZosmfIssueResponse response = (ZosmfIssueResponse) JsonParseFactory
                .buildParser(ParseType.MVS_CONSOLE).parseResponse(json);
        assertEquals("ver", response.getCmdResponse().orElse("n\\a"));
    }

}
