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

import org.json.simple.JSONObject;
import zowe.client.sdk.zosjobs.response.JobStepData;

/**
 * Class to transform JSON into JobStepData object
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ParseJobStepDataJson implements IParseJson<JobStepData> {

    /**
     * Transform JSON into JobStepData object
     *
     * @param jsonObject JSON object
     * @return JobStepData object
     * @author Frank Giordano
     */
    @Override
    public JobStepData parse(JSONObject jsonObject) {
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
