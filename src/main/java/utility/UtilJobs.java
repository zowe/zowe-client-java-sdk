/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

import org.json.simple.JSONObject;
import zosjobs.input.ModifyJobParams;
import zosjobs.response.Job;

/**
 * Utility Class for GetJobs related static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class UtilJobs {

    /**
     * Formulate and return a Job document/object based on incoming Json object.
     *
     * @param json JSONObject object
     * @return job document
     * @author Frank Giordano
     */
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
                .retCode((String) json.get("retcode"))
                .jobCorrelator((String) json.get("job-correlator"))
                .phaseName((String) json.get("phase-name"))
                .build();
    }

    /**
     * Check the validity of a ModifyJobParams object
     *
     * @param params ModifyJobParams object
     * @author Frank Giordano
     */
    public static void checkForModifyJobParamsExceptions(ModifyJobParams params) {
        Util.checkNullParameter(params == null, "params is null");
        Util.checkStateParameter(params.getJobId().isEmpty(), "job id not specified");
        Util.checkStateParameter(params.getJobId().get().isEmpty(), "job id not specified");
        Util.checkStateParameter(params.getJobName().isEmpty(), "job name not specified");
        Util.checkStateParameter(params.getJobName().get().isEmpty(), "job name not specified");
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
        String errorMsg = exception.getMessage();
        if (errorMsg.contains("400"))
            throw new Exception(errorMsg + " JobId " + params.getJobId().get() + " may not exist.");
        throw new Exception(errorMsg);
    }

}
