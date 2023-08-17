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
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.input.JobFile;

/**
 * Parse json response for job file
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class JobFileParseResponse implements JsonParseResponse {

    /**
     * Represents one singleton instance
     */
    private static JsonParseResponse INSTANCE;

    /**
     * JSON data value to be parsed
     */
    private JSONObject data;

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private JobFileParseResponse() {
    }

    /**
     * Get singleton instance
     *
     * @return JobFileParseResponse object
     * @author Frank Giordano
     */
    public synchronized static JsonParseResponse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JobFileParseResponse();
        }
        return INSTANCE;
    }

    /**
     * Transform data into JobFile object
     *
     * @return JobFile object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.REQUIRED_ACTION_MSG);
        final JobFile jobFile = new JobFile.Builder()
                .jobId(data.get("jobid") != null ? (String) data.get("jobid") : null)
                .jobName(data.get("jobname") != null ? (String) data.get("jobname") : null)
                .recfm(data.get("recfm") != null ? (String) data.get("recfm") : null)
                .byteCount(data.get("byteCount") != null ? (Long) data.get("byteCount") : null)
                .recordCount(data.get("recordCount") != null ? (Long) data.get("recordCount") : null)
                .jobCorrelator(data.get("job-correlator") != null ? (String) data.get("job-correlator") : null)
                .classs(data.get("class") != null ? (String) data.get("class") : null)
                .id(data.get("id") != null ? (Long) data.get("id") : 0)
                .ddName(data.get("ddname") != null ? (String) data.get("ddname") : null)
                .recordsUrl(data.get("records-url\"") != null ? (String) data.get("records-url") : null)
                .lrecl(data.get("lrecl") != null ? (Long) data.get("lrecl") : 0)
                .subSystem(data.get("subsystem") != null ? (String) data.get("subsystem") : null)
                .stepName(data.get("stepname") != null ? (String) data.get("stepname") : null)
                .procStep(data.get("procstep") != null ? (String) data.get("procstep") : null)
                .build();
        data = null;
        return jobFile;
    }

    /**
     * Set the data to be parsed
     *
     * @param data json data to parse
     * @return JsonParseResponse this object
     * @author Frank Giordano
     */
    @Override
    public JsonParseResponse setJsonObject(final JSONObject data) {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.DATA_NULL_MSG);
        this.data = data;
        return this;
    }

}
