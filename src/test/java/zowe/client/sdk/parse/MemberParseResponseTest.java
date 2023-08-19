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
import zowe.client.sdk.zosfiles.dsn.response.Member;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for emberParseResponse.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class MemberParseResponseTest {

    @Test
    public void tstMemberParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.MEMBER).setJsonObject(null);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstMemberParseJsonStopResponseSingletonSuccess() throws Exception {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.MEMBER);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.MEMBER);
        assertSame(parser, parser2);
    }

    @Test
    public void tstMemberParseJsonStopResponseSingletonWithDataSuccess() throws Exception {
        final JsonParse parser =
                JsonParseFactory.buildParser(ParseType.MEMBER).setJsonObject(new JSONObject());
        final JsonParse parser2 =
                JsonParseFactory.buildParser(ParseType.MEMBER).setJsonObject(new JSONObject());
        assertSame(parser, parser2);
    }

    @Test
    public void tstMemberParseJsonStopResponseResetDataFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.MEMBER).setJsonObject(new JSONObject());
            JsonParseFactory.buildParser(ParseType.MEMBER).parseResponse();
            JsonParseFactory.buildParser(ParseType.MEMBER).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
        try {
            JsonParseFactory.buildParser(ParseType.MEMBER).setJsonObject(new JSONObject()).parseResponse();
            JsonParseFactory.buildParser(ParseType.MEMBER).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
    }

    @Test
    public void tstMemberParseJsonStopResponseSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("member", "ver");
        jsonMap.put("vers", 0L);
        final JSONObject json = new JSONObject(jsonMap);

        final Member response = (Member) JsonParseFactory.buildParser(ParseType.MEMBER)
                .setJsonObject(json).parseResponse();
        assertEquals("ver", response.getMember().orElse("n\\a"));
        assertEquals(Long.parseLong("0"), response.getVers().orElse(-1L));
    }

}
