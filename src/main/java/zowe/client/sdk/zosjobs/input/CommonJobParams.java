/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.input;

import zowe.client.sdk.utility.ValidateUtils;

import java.util.Optional;

/**
 * Interface for various common GetJobs APIs
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class CommonJobParams {

    /**
     * Job id for a job
     */
    private final Optional<String> jobId;

    /**
     * Job name for a job
     */
    private final Optional<String> jobName;

    /**
     * Flag to indicate whether to include step data
     * <p>
     * Step data is an optional parameter that indicates whether the service returns information about each step in
     * the job that completed, such as the step name, step number, and completion code.
     */
    private final boolean stepData;

    /**
     * CommonJobParams constructor, no step data information included.
     *
     * @param jobId   job id value
     * @param jobName job name value
     * @author Frank Giordano
     */
    public CommonJobParams(final String jobId, final String jobName) {
        validateParameters(jobId, jobName);
        this.jobId = Optional.of(jobId);
        this.jobName = Optional.of(jobName);
        this.stepData = false;
    }

    /**
     * CommonJobParams constructor with step data flag
     *
     * @param jobId    job id value
     * @param jobName  job name value
     * @param stepData determines whether step data is included in rest call
     * @author Frank Giordano
     */
    public CommonJobParams(final String jobId, final String jobName, final boolean stepData) {
        validateParameters(jobId, jobName);
        this.jobId = Optional.of(jobId);
        this.jobName = Optional.of(jobName);
        this.stepData = stepData;
    }

    /**
     * Retrieve jobId specified
     *
     * @return jobId value
     * @author Frank Giordano
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve jobName specified
     *
     * @return jobName value
     * @author Frank Giordano
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Determines whether step data is included in rest call
     *
     * @return boolean true or false
     * @author Frank Giordano
     */
    public boolean isStepData() {
        return stepData;
    }

    /**
     * Helper method to validate constructor inputs
     *
     * @param jobId   job id value
     * @param jobName job name value
     * @author Frank Giordano
     */
    private void validateParameters(final String jobId, final String jobName) {
        ValidateUtils.checkNullParameter(jobId == null, "job id is null");
        ValidateUtils.checkIllegalParameter(jobId.isBlank(), "job id not specified");
        ValidateUtils.checkNullParameter(jobName == null, "job name is null");
        ValidateUtils.checkIllegalParameter(jobName.isBlank(), "job name not specified");
    }

    @Override
    public String toString() {
        return "CommonJobParams{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                '}';
    }

}
