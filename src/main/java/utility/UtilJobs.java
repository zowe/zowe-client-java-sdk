package utility;

import org.json.simple.JSONObject;
import zosjobs.response.Job;

public class UtilJobs {

    public static Job createJobObjFromJson(JSONObject json) {
        return new Job.Builder().jobId((String) json.get("jobid"))
                                .jobName((String) json.get("jobname"))
                                .subSystem((String) json.get("subsystem"))
                                .owner((String) json.get("owner"))
                                .type((String) json.get("type"))
                                .status((String) json.get("status"))
                                .url((String) json.get("url"))
                                .classs((String) json.get("class"))
                                .filesUrl((String) json.get("files-url"))
                                .retCode((String) json.get("retCode"))
                                .jobCorrelator((String) json.get("job-correlator"))
                                .phaseName((String) json.get("phase-name"))
                                .build();
    }

}
