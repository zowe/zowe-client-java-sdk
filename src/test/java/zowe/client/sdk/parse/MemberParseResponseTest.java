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
import zowe.client.sdk.zosfiles.dsn.response.MemberDocument;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for emberParseResponse.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class MemberParseResponseTest {

    @Test
    public void tstMemberParseResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.MEMBER).parseResponse((Object) null);
        } catch (NullPointerException e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstMemberParseResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.MEMBER);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.MEMBER);
        assertSame(parser, parser2);
    }

    @Test
    public void tstMemberParseResponseSuccess() {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("member", "ver");
        jsonMap.put("vers", 0L);
        final JSONObject json = new JSONObject(jsonMap);

        final MemberDocument response = (MemberDocument) JsonParseFactory.buildParser(ParseType.MEMBER).parseResponse(json);
        assertEquals("ver", response.getMember().orElse("n\\a"));
        assertEquals(0, response.getVers().orElse(-1L));
    }

}
