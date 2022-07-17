package zowe.client.sdk.json;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosjobs.response.JobStepData;

public class JobStepDataJson implements IJson<JobStepData> {

    @Override
    public JobStepData parseJsonObject(JSONObject jsonObject) {
        return new JobStepData.Builder()
                .smfid((String) jsonObject.get("smfid"))
                .completion((String) jsonObject.get("completion"))
                .stepNumber((Long) jsonObject.get("step-number"))
                .programName((String) jsonObject.get("program-name"))
                .active((boolean) jsonObject.get("active"))
                .stepName((String) jsonObject.get("step-name"))
                .procStepName((String) jsonObject.get("proc-step-name")).build();
    }

}
