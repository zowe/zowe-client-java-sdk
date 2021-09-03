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
import zosjobs.response.Job;

/**
 * Utility Class for Rest related static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class UtilJobs {

    /**
     * Formulate and return a Job object based on incoming Json object.
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
                .retcode((String) json.get("retcode"))
                .jobCorrelator((String) json.get("job-correlator"))
                .phaseName((String) json.get("phase-name"))
                .build();
    }

}
