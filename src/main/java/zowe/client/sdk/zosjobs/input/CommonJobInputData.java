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

import java.util.Optional;

/**
 * Parameters for various common Job API input data
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class CommonJobInputData {

    /**
     * Job id for a job
     */
    private final String jobId;

    /**
     * Job name for a job
     */
    private final String jobName;

    /**
     * Flag to indicate whether to include step data
     * <p>
     * Step data is an optional parameter that indicates whether the service returns information about each step in
     * the job that completed, such as the step name, step number, and completion code.
     */
    private final boolean stepData;

    /**
     * CommonJobInputData constructor, no step data information included.
     *
     * @param jobId   job id value
     * @param jobName job name value
     * @author Frank Giordano
     */
    public CommonJobInputData(final String jobId, final String jobName) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.stepData = false;
    }

    /**
     * CommonJobInputData constructor with a step data flag
     *
     * @param jobId    job id value
     * @param jobName  job name value
     * @param stepData determines whether step data is included in the rest call
     * @author Frank Giordano
     */
    public CommonJobInputData(final String jobId, final String jobName, final boolean stepData) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.stepData = stepData;
    }

    /**
     * Retrieve jobId specified
     *
     * @return jobId value
     */
    public Optional<String> getJobId() {
        return Optional.ofNullable(jobId);
    }

    /**
     * Retrieve jobName specified
     *
     * @return jobName value
     */
    public Optional<String> getJobName() {
        return Optional.ofNullable(jobName);
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
     * Return string value representing CommonJobInputData object
     *
     * @return string representation of CommonJobInputData
     */
    @Override
    public String toString() {
        return "CommonJobInputData{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                '}';
    }

}
