/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs.input;

import java.util.Optional;

public class CommonJobParms {

    private final Optional<String> jobId;
    private final Optional<String> jobName;

    public CommonJobParms(String jobId, String jobName) {
        this.jobId = Optional.ofNullable(jobId);
        this.jobName = Optional.ofNullable(jobName);
    }

    public Optional<String> getJobId() {
        return jobId;
    }

    public Optional<String> getJobName() {
        return jobName;
    }

    @Override
    public String toString() {
        return "CommonJobParms{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                '}';
    }

}
