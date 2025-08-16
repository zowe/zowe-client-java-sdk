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
import zowe.client.sdk.zosfiles.uss.response.UnixFileDocument;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for UnixFileParseResponse.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class UnixFileParseResponseTest {

    @Test
    public void tstUnixFileParseResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.UNIX_FILE).parseResponse((Object) null);
        } catch (NullPointerException e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstUnixFileParseResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.UNIX_FILE);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.UNIX_FILE);
        assertSame(parser, parser2);
    }

    @Test
    public void tstUnixFileParseResponseSuccess() {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "ver");
        jsonMap.put("mode", "mode");
        final JSONObject json = new JSONObject(jsonMap);

        final UnixFileDocument response = (UnixFileDocument) JsonParseFactory.buildParser(ParseType.UNIX_FILE).parseResponse(json);
        assertEquals("ver", response.getName().orElse("n\\a"));
        assertEquals("mode", response.getMode().orElse("n\\a"));
    }

}
