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
import zowe.client.sdk.zosjobs.response.Job;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class containing unit tests for JobParseResponse.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobParseResponseTest {

    @Test
    public void tstJobParseJsonStopResponseNullFail() {
        String msg = "";
        try {
            JsonParseFactory.buildParser(ParseType.JOB).parseResponse((Object) null);
        } catch (NullPointerException e) {
            msg = e.getMessage();
        }
        assertEquals("data is null", msg);
    }

    @Test
    public void tstJobParseJsonStopResponseSingletonSuccess() {
        final JsonParse parser = JsonParseFactory.buildParser(ParseType.JOB);
        final JsonParse parser2 = JsonParseFactory.buildParser(ParseType.JOB);
        assertSame(parser, parser2);
    }

    @Test
    public void tstJobParseJsonStopResponseSuccess() {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("jobname", "ver");
        jsonMap.put("jobid", "dev");
        final JSONObject json = new JSONObject(jsonMap);

        final Job response = (Job) JsonParseFactory.buildParser(ParseType.JOB).parseResponse(json);
        assertEquals("ver", response.getJobName().orElse("n\\a"));
        assertEquals("dev", response.getJobId().orElse("n\\a"));
    }

}
