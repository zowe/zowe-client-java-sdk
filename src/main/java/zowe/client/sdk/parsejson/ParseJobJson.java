package zowe.client.sdk.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.zosjobs.response.Job;
import zowe.client.sdk.zosjobs.response.JobStepData;

public class JobJson implements IJson<Job> {

    @Override
    public Job parseJsonObject(JSONObject jsonObject) {
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
                IJson<JobStepData> jobStepDataJson = new JobStepDataJson();
                jobStepDataArray[i] = jobStepDataJson.parseJsonObject((JSONObject) stepData.get(i));
            }
            return job.stepData(jobStepDataArray).build();
        }

        return job.build();
    }

}
