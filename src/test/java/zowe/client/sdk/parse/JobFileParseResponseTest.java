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
import zowe.client.sdk.zosjobs.input.JobFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Class containing unit tests for JobFileParseResponse.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class JobFileParseResponseTest {

    @Test
    public void tstJobFileParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.JOB_FILE).setJsonObject(null);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstJobFileParseJsonStopResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.JOB_FILE);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.JOB_FILE);
        assertSame(parser, parser2);
    }

    @Test
    public void tstJobFileParseJsonStopResponseSingletonWithDataSuccess() {
        final JsonParse parser =
                JsonParseFactory.buildParser(ParseType.JOB_FILE).setJsonObject(new JSONObject());
        final JsonParse parser2 =
                JsonParseFactory.buildParser(ParseType.JOB_FILE).setJsonObject(new JSONObject());
        assertSame(parser, parser2);
    }

    @Test
    public void tstJobFileParseJsonStopResponseResetDataFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.JOB_FILE).setJsonObject(new JSONObject());
            JsonParseFactory.buildParser(ParseType.JOB_FILE).parseResponse();
            JsonParseFactory.buildParser(ParseType.JOB_FILE).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
        try {
            JsonParseFactory.buildParser(ParseType.JOB_FILE).setJsonObject(new JSONObject()).parseResponse();
            JsonParseFactory.buildParser(ParseType.JOB_FILE).parseResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ParseConstants.REQUIRED_ACTION_MSG, msg);
    }

    @Test
    public void tstJobFileParseJsonStopResponseSuccess() {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("jobname", "ver");
        jsonMap.put("jobid", "dev");
        final JSONObject json = new JSONObject(jsonMap);

        final JobFile response = (JobFile) JsonParseFactory.buildParser(ParseType.JOB_FILE)
                .setJsonObject(json).parseResponse();
        assertEquals("ver", response.getJobName().orElse("n\\a"));
        assertEquals("dev", response.getJobId().orElse("n\\a"));
        assertEquals(0, response.getId().orElse(-1L));
    }

}
