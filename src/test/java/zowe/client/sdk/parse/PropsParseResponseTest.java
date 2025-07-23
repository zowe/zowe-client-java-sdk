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
 * @version 4.0
 */
public class PropsParseResponseTest {

    @Test
    public void tstPropsParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.PROPS).parseResponse((Object) null);
        } catch (NullPointerException e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstPropsParseJsonStopResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.PROPS);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.PROPS);
        assertSame(parser, parser2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void tstPropsParseJsonStopResponseSuccess() {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "ver");
        final JSONObject json = new JSONObject(jsonMap);

        final Map<String, String> response = (Map<String, String>) JsonParseFactory
                .buildParser(ParseType.PROPS).parseResponse(json);
        assertEquals("ver", response.get("cmd-response"));
    }

}
