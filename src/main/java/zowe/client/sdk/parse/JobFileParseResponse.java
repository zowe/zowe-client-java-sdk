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
import zowe.client.sdk.zosjobs.input.JobFile;

/**
 * Parse json response for job file
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class JobFileParseResponse extends JsonParseResponse {

    /**
     * JobFileParseResponse constructor
     *
     * @param data json object
     * @author Frank Giordano
     */
    public JobFileParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Transform json into JobFile object
     *
     * @return JobFile object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        return new JobFile.Builder()
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
    }

}
