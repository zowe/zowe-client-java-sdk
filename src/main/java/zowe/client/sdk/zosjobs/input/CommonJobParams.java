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
import zowe.client.sdk.zosjobs.JobsConstants;

import java.util.Optional;

/**
 * Interface for various common GetJobs APIs
 *
 * @author Frank Giordano
 * @version 3.0
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
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve jobName specified
     *
     * @return jobName value
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Determines whether step data is included in rest call
     *
     * @return boolean true or false
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
        ValidateUtils.checkNullParameter(jobId == null, JobsConstants.JOB_ID_NULL_MSG);
        ValidateUtils.checkIllegalParameter(jobId.isBlank(), JobsConstants.JOB_ID_ILLEGAL_MSG);
        ValidateUtils.checkNullParameter(jobName == null, JobsConstants.JOB_NAME_NULL_MSG);
        ValidateUtils.checkIllegalParameter(jobName.isBlank(), JobsConstants.JOB_NAME_ILLEGAL_MSG);
    }

    /**
     * Return string value representing CommonJobParams object
     *
     * @return string representation of CommonJobParams
     */
    @Override
    public String toString() {
        return "CommonJobParams{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                '}';
    }

}
