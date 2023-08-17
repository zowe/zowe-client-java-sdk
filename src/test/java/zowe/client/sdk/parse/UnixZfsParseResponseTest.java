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
import zowe.client.sdk.zosfiles.uss.response.UnixZfs;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for UnixZfsParseResponse.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UnixZfsParseResponseTest {

    @Test
    public void tstUnixZfsParseJsonStopResponseDataNullFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).setJsonObject(null);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstUnixZfsParseJsonStopResponseModeNullFail() {
        String msg = "";
        try {
            final UnixZfsParseResponse parser = (UnixZfsParseResponse)
                    JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).setJsonObject(new JSONObject());
            parser.setModeStr(null);
            parser.parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("modeStr is null", msg);
    }

    @Test
    public void tstUnixZfsParseJsonStopResponseSingletonSuccess() throws Exception {
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS);
        final JsonParseResponse parser2 = JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS);
        assertSame(parser, parser2);
    }

    @Test
    public void tstUnixZfsParseJsonStopResponseSingletonWithDataSuccess() throws Exception {
        final JsonParseResponse parser =
                JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).setJsonObject(new JSONObject());
        final JsonParseResponse parser2 =
                JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).setJsonObject(new JSONObject());
        assertSame(parser, parser2);
    }

    @Test
    public void tstUnixZfsParseJsonStopResponseResetDataModeFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).setJsonObject(new JSONObject());
            JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).parseResponse();
            JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MODE_STR_MSG, msg);
        try {
            final UnixZfsParseResponse parser = (UnixZfsParseResponse)
                    JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).setJsonObject(new JSONObject());
            parser.setModeStr("sd");
            parser.parseResponse();
            parser.parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
    }

    @Test
    public void tstUnixZfsParseJsonStopResponseSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "ver");
        jsonMap.put("mode", "mode");
        final JSONObject json = new JSONObject(jsonMap);

        final UnixZfsParseResponse parser = (UnixZfsParseResponse)
                JsonParseResponseFactory.buildParser(ParseType.UNIX_ZFS).setJsonObject(json);
        parser.setModeStr("mode");
        final UnixZfs response = parser.parseResponse();
        assertEquals("ver", response.getName().orElse("n\\a"));
        assertEquals("mode", response.getMode().orElse("n\\a"));
    }

}
