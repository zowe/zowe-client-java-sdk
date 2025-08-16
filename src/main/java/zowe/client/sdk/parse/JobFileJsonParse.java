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
import zowe.client.sdk.zosjobs.response.JobFile;

/**
 * Parse JSON response for a job file
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class JobFileJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final JobFileJsonParse instance = new JobFileJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private JobFileJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return JobFileJsonParse object
     * @author Frank Giordano
     */
    public static JobFileJsonParse getInstance() {
        return JobFileJsonParse.Holder.instance;
    }

    /**
     * Transform data into JobFile object
     *
     * @param args json data to parse
     * @return JobFile object
     * @author Frank Giordano
     */
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
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
                .recordsUrl(data.get("records-url") != null ? (String) data.get("records-url") : null)
                .lrecl(data.get("lrecl") != null ? (Long) data.get("lrecl") : 0)
                .subSystem(data.get("subsystem") != null ? (String) data.get("subsystem") : null)
                .stepName(data.get("stepname") != null ? (String) data.get("stepname") : null)
                .procStep(data.get("procstep") != null ? (String) data.get("procstep") : null)
                .build();
    }

}
