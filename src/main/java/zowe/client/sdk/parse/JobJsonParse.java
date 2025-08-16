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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.response.JobDocument;
import zowe.client.sdk.zosjobs.response.StepDataDocument;

/**
 * Extract Job from JSON response
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class JobJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final JobJsonParse instance = new JobJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private JobJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return JobJsonParse object
     * @author Frank Giordano
     */
    public static JobJsonParse getInstance() {
        return JobJsonParse.Holder.instance;
    }

    /**
     * Transform data json into Job object
     *
     * @param args json data to parse
     * @return Job object
     * @author Frank Giordano
     */
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        final JobDocument.Builder job = new JobDocument.Builder()
                .jobId(data.get("jobid") != null ? (String) data.get("jobid") : null)
                .jobName(data.get("jobname") != null ? (String) data.get("jobname") : null)
                .subSystem(data.get("subsystem") != null ? (String) data.get("subsystem") : null)
                .owner(data.get("owner") != null ? (String) data.get("owner") : null)
                .type(data.get("type") != null ? (String) data.get("type") : null)
                .status(data.get("status") != null ? (String) data.get("status") : null)
                .url(data.get("url") != null ? (String) data.get("url") : null)
                .classs(data.get("class") != null ? (String) data.get("class") : null)
                .filesUrl(data.get("files-url") != null ? (String) data.get("files-url") : null)
                .retCode(data.get("retcode") != null ? (String) data.get("retcode") : null)
                .jobCorrelator(data.get("job-correlator") != null ? (String) data.get("job-correlator") : null)
                .phase(data.get("phase") != null ? (Long) data.get("phase") : null)
                .phaseName(data.get("phase-name") != null ? (String) data.get("phase-name") : null);

        // check for "step-data" used by getStatusCommon if a flag is set to true
        final JSONArray stepData = data.get("step-data") != null ? (JSONArray) data.get("step-data") : null;
        if (stepData != null) {
            final int size = stepData.size();
            final StepDataDocument[] jobStepDataArray = new StepDataDocument[size];
            for (int i = 0; i < size; i++) {
                jobStepDataArray[i] = parseStepDataResponse((JSONObject) stepData.get(i));
            }
            return job.stepData(jobStepDataArray).build();
        }
        return job.build();
    }

    /**
     * Transform JobStepData json into JobStepData object
     *
     * @return JobStepData object
     * @author Frank Giordano
     */
    private StepDataDocument parseStepDataResponse(final JSONObject data) {
        return new StepDataDocument.Builder()
                .smfid(data.get("smfid") != null ? (String) data.get("smfid") : null)
                .completion(data.get("completion") != null ? (String) data.get("completion") : null)
                .stepNumber(data.get("step-number") != null ? (Long) data.get("step-number") : null)
                .programName(data.get("program-name") != null ? (String) data.get("program-name") : null)
                .active(data.get("active") != null && (boolean) data.get("active"))
                .stepName(data.get("step-name") != null ? (String) data.get("step-name") : null)
                .procStepName(data.get("proc-step-name") != null ? (String) data.get("proc-step-name") : null)
                .build();
    }

}
