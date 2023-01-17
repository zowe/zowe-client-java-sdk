/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.zosjobs.input.ModifyJobParams;
import zowe.client.sdk.zosjobs.response.Job;
import zowe.client.sdk.zosjobs.response.JobStepData;

/**
 * Utility Class for GetJobs related static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public final class JobUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private JobUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check the validity of a ModifyJobParams object
     *
     * @param params ModifyJobParams object
     * @author Frank Giordano
     */
    public static void checkModifyJobParameters(ModifyJobParams params) {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getJobId().isEmpty(), "job id not specified");
        ValidateUtils.checkIllegalParameter(params.getJobId().get().isEmpty(), "job id not specified");
        ValidateUtils.checkIllegalParameter(params.getJobName().isEmpty(), "job name not specified");
        ValidateUtils.checkIllegalParameter(params.getJobName().get().isEmpty(), "job name not specified");
    }

    /**
     * Transform JSON into Job object
     *
     * @param jsonObject JSON object
     * @return Job object
     * @author Frank Giordano
     */
    public static Job parseJsonJobResponse(JSONObject jsonObject) {
        ValidateUtils.checkNullParameter(jsonObject == null, "json is null");
        final Job.Builder job = new Job.Builder()
                .jobId((String) jsonObject.get("jobid"))
                .jobName((String) jsonObject.get("jobname"))
                .subSystem((String) jsonObject.get("subsystem"))
                .owner((String) jsonObject.get("owner"))
                .type((String) jsonObject.get("type"))
                .status((String) jsonObject.get("status"))
                .url((String) jsonObject.get("url"))
                .classs((String) jsonObject.get("class"))
                .filesUrl((String) jsonObject.get("files-url"))
                .retCode((String) jsonObject.get("retcode"))
                .jobCorrelator((String) jsonObject.get("job-correlator"))
                .phase((Long) jsonObject.get("phase"))
                .phaseName((String) jsonObject.get("phase-name"));

        // check for "step-data" used by getStatusCommon if flag is set to true
        final JSONArray stepData = (JSONArray) jsonObject.get("step-data");
        if (stepData != null) {
            final int size = stepData.size();
            final JobStepData[] jobStepDataArray = new JobStepData[size];
            for (int i = 0; i < size; i++) {
                jobStepDataArray[i] = parseJsonJobStepDataResponse((JSONObject) stepData.get(i));
            }
            return job.stepData(jobStepDataArray).build();
        }

        return job.build();
    }

    /**
     * Transform JSON into JobStepData object
     *
     * @param jsonObject JSON object
     * @return JobStepData object
     * @author Frank Giordano
     */
    private static JobStepData parseJsonJobStepDataResponse(JSONObject jsonObject) {
        return new JobStepData.Builder()
                .smfid((String) jsonObject.get("smfid"))
                .completion((String) jsonObject.get("completion"))
                .stepNumber((Long) jsonObject.get("step-number"))
                .programName((String) jsonObject.get("program-name"))
                .active((boolean) jsonObject.get("active"))
                .stepName((String) jsonObject.get("step-name"))
                .procStepName((String) jsonObject.get("proc-step-name")).build();
    }

    /**
     * Formulate an exception to throw base on http error code
     *
     * @param params    ModifyJobParams object
     * @param exception incoming exception to inspect and use
     * @throws Exception error base on http error code
     * @author Frank Giordano
     */
    public static void throwHttpException(ModifyJobParams params, Exception exception) throws Exception {
        JobUtils.checkModifyJobParameters(params);
        final String errorMsg = exception.getMessage();
        if (errorMsg.contains("400")) {
            throw new Exception(errorMsg + " JobId " + params.getJobId().orElse("n/a") + " may not exist.");
        }
        throw new Exception(errorMsg);
    }

}
