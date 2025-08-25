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
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.zosmfinfo.response.ZosmfSystemsResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Class containing unit tests for SystemsParseResponse.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class SystemsParseResponseTest {

    @Test
    public void tstSystemsParseResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.ZOSMF_SYSTEMS).parseResponse((Object) null);
        } catch (NullPointerException e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstSystemsParseResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.ZOSMF_SYSTEMS);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.ZOSMF_SYSTEMS);
        assertSame(parser, parser2);
    }

    @Test
    public void tstSystemsParseResponseSuccess() throws Exception {
        final String data = "{\"items\":[{\"systemNickName\":\"test\",\"zosVR\":\"2.5\"}],\"numRows\":1}";
        final JSONObject json = new JSONObject((JSONObject) new JSONParser().parse(data));
        final ZosmfSystemsResponse response = (ZosmfSystemsResponse) JsonParseFactory
                .buildParser(ParseType.ZOSMF_SYSTEMS).parseResponse(json);
        assertEquals(Long.parseLong("1"), response.getNumRows().orElse(-1L));
        assertEquals("test", response.getDefinedSystems().get()[0].getSystemNickName().get());
    }

}
