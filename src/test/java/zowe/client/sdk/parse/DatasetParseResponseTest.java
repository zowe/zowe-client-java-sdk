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
import zowe.client.sdk.zosfiles.dsn.response.Dataset;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for DatasetParseResponse.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class DatasetParseResponseTest {

    @Test
    public void tstDatasetParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(ParseType.DATASET).setJsonObject(null);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstDatasetParseJsonStopResponseSingletonSuccess() throws Exception {
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(ParseType.DATASET);
        final JsonParseResponse parser2 = JsonParseResponseFactory.buildParser(ParseType.DATASET);
        assertSame(parser, parser2);
    }

    @Test
    public void tstDatasetParseJsonStopResponseSingletonWithDataSuccess() throws Exception {
        final JsonParseResponse parser =
                JsonParseResponseFactory.buildParser(ParseType.DATASET).setJsonObject(new JSONObject());
        final JsonParseResponse parser2 =
                JsonParseResponseFactory.buildParser(ParseType.DATASET).setJsonObject(new JSONObject());
        assertSame(parser, parser2);
    }

    @Test
    public void tstDatasetParseJsonStopResponseResetDataFail() {
        String msg = "";
        try {
            JsonParseResponseFactory.buildParser(ParseType.DATASET).setJsonObject(new JSONObject());
            JsonParseResponseFactory.buildParser(ParseType.DATASET).parseResponse();
            JsonParseResponseFactory.buildParser(ParseType.DATASET).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
        try {
            JsonParseResponseFactory.buildParser(ParseType.DATASET).setJsonObject(new JSONObject()).parseResponse();
            JsonParseResponseFactory.buildParser(ParseType.DATASET).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
    }

    @Test
    public void tstDatasetParseJsonStopResponseSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("dsname", "ver");
        jsonMap.put("dev", "dev");
        final JSONObject json = new JSONObject(jsonMap);

        final Dataset response = (Dataset) JsonParseResponseFactory.buildParser(ParseType.DATASET)
                .setJsonObject(json).parseResponse();
        assertEquals("ver", response.getDsname().orElse("n\\a"));
        assertEquals("dev", response.getDev().orElse("n\\a"));
    }

}