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

import org.json.JSONObject;
import zosjobs.response.Job;

import java.util.function.Supplier;
import java.util.stream.Stream;

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
     * @return A Job document
     * @author Frank Giordano
     */
    public static Job createJobObjFromJson(JSONObject json) {
        Supplier<Stream<String>> keys = Util.getStreamSupplier(json);
        return new Job.Builder()
                .jobId(keys.get().filter("jobid"::equals).findFirst().isPresent() ? (String) json.get("jobid") : null)
                .jobName(keys.get().filter("jobname"::equals).findFirst().isPresent() ? (String) json.get("jobname") : null)
                .subSystem(keys.get().filter("subsystem"::equals).findFirst().isPresent() ? (String) json.get("subsystem") : null)
                .owner(keys.get().filter("owner"::equals).findFirst().isPresent() ? (String) json.get("owner") : null)
                .type(keys.get().filter("type"::equals).findFirst().isPresent() ? (String) json.get("type") : null)
                .status(keys.get().filter("status"::equals).findFirst().isPresent() ? (String) json.get("status") : null)
                .url(keys.get().filter("url"::equals).findFirst().isPresent() ? (String) json.get("url") : null)
                .classs(keys.get().filter("class"::equals).findFirst().isPresent() ? (String) json.get("class") : null)
                .filesUrl(keys.get().filter("files-url"::equals).findFirst().isPresent() ? (String) json.get("files-url") : null)
                .retCode(keys.get().filter("retCode"::equals).findFirst().isPresent() ? (String) json.get("retCode") : null)
                .jobCorrelator(keys.get().filter("job-correlator"::equals).findFirst().isPresent() ? (String) json.get("job-correlator") : null)
                .phaseName(keys.get().filter("phase-name"::equals).findFirst().isPresent() ? (String) json.get("phase-name") : null)
                .build();
    }

}
