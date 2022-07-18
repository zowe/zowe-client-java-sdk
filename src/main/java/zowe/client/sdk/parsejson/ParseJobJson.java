/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parsejson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.zosjobs.response.Job;
import zowe.client.sdk.zosjobs.response.JobStepData;

/**
 * Class to transform JSON into Job object
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ParseJobJson implements IParseJson<Job> {

    /**
     * Transform JSON into Job object
     *
     * @param jsonObject JSON object
     * @return Job object
     * @author Frank Giordano
     */
    @Override
    public Job parse(JSONObject jsonObject) {
        Util.checkNullParameter(jsonObject == null, "json is null");

        Job.Builder job = new Job.Builder();
        job.jobId((String) jsonObject.get("jobid"))
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
                .phaseName((String) jsonObject.get("phase-name"));

        // check for "step-data" used by getStatusCommon if flag is set to true
        JSONArray stepData = (JSONArray) jsonObject.get("step-data");
        if (stepData != null) {
            int size = stepData.size();
            JobStepData[] jobStepDataArray = new JobStepData[size];
            for (int i = 0; i < size; i++) {
                IParseJson<JobStepData> jobStepDataJson = new ParseJobStepDataJson();
                jobStepDataArray[i] = jobStepDataJson.parse((JSONObject) stepData.get(i));
            }
            return job.stepData(jobStepDataArray).build();
        }

        return job.build();
    }

}
