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
 * Interface for various common GetJobs APIs
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class CommonJobParams {

    /**
     * job id for a job
     */
    private final Optional<String> jobId;

    /**
     * job name for a job
     */
    private final Optional<String> jobName;

    /**
     * Flag to indicate whether to include step data
     * <p>
     * Step data is an optional parameter that indicates whether the service returns information about each step in
     * the job that completed, such as the step name, step number, and completion code.
     */
    private final Boolean stepData;

    /**
     * CommonJobParams constructor, no step data information included.
     *
     * @param jobId   job id value
     * @param jobName job name value
     * @author Frank Giordano
     */
    public CommonJobParams(String jobId, String jobName) {
        this.jobId = Optional.ofNullable(jobId);
        this.jobName = Optional.ofNullable(jobName);
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
    public CommonJobParams(String jobId, String jobName, Boolean stepData) {
        this.jobId = Optional.ofNullable(jobId);
        this.jobName = Optional.ofNullable(jobName);
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
     * @return true or false
     * @author Frank Giordano
     */
    public Boolean isStepData() {
        return stepData;
    }

    @Override
    public String toString() {
        return "CommonJobParams{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                '}';
    }

}
