/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.response;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Step info on a job interface
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class JobStepData {

    /**
     * SMFID
     */
    private final String smfid;

    /**
     * Completion
     */
    private final String completion;

    /**
     * Active
     */
    private final boolean active;

    /**
     * Job relevant step
     */
    private final Long stepNumber;

    /**
     * Job relevant proc
     */
    private final String procStepName;

    /**
     * Step for which a job dd exists
     */
    private final String stepName;

    /**
     * Program EXEC=
     */
    private final String programName;

    /**
     * JobStepData constructor
     *
     * @param builder JobStepData.Builder object
     * @author Frank Giordano
     */
    private JobStepData(final JobStepData.Builder builder) {
        this.smfid = builder.smfid;
        this.completion = builder.completion;
        this.active = builder.active;
        this.stepNumber = builder.stepNumber;
        this.procStepName = builder.procStepName;
        this.stepName = builder.stepName;
        this.programName = builder.programName;
    }

    /**
     * Retrieve active boolean value
     *
     * @return boolean value
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Retrieve completion optional string
     *
     * @return optional string
     */
    public Optional<String> getCompletion() {
        return Optional.ofNullable(completion);
    }

    /**
     * Retrieve procStepName optional string
     *
     * @return optional string
     */
    public Optional<String> getProcStepName() {
        return Optional.ofNullable(procStepName);
    }

    /**
     * Retrieve programName optional string
     *
     * @return optional string
     */
    public Optional<String> getProgramName() {
        return Optional.ofNullable(programName);
    }

    /**
     * Retrieve smfid optional string
     *
     * @return optional string
     */
    public Optional<String> getSmfid() {
        return Optional.ofNullable(smfid);
    }

    /**
     * Retrieve stepName optional string
     *
     * @return optional string
     */
    public Optional<String> getStepName() {
        return Optional.ofNullable(stepName);
    }

    /**
     * Retrieve stepNumber optional string
     *
     * @return optional long
     */
    public OptionalLong getStepNumber() {
        return (stepNumber == null) ? OptionalLong.empty() : OptionalLong.of(stepNumber);
    }

    /**
     * Return string value representing JobStepData object
     *
     * @return string representation of JobStepData
     */
    @Override
    public String toString() {
        return "JobStepData{" +
                "smfid=" + smfid +
                ", completion=" + completion +
                ", active=" + active +
                ", stepNumber=" + stepNumber +
                ", procStepName=" + procStepName +
                ", stepName=" + stepName +
                ", programName=" + programName +
                '}';
    }

    /**
     * Builder class for JobStepData
     */
    public static class Builder {

        /**
         * SMFID
         */
        private String smfid;

        /**
         * Completion
         */
        private String completion;

        /**
         * Active
         */
        private boolean active;

        /**
         * Job relevant step
         */
        private Long stepNumber;

        /**
         * Job relevant proc
         */
        private String procStepName;

        /**
         * Step for which a job dd exists
         */
        private String stepName;

        /**
         * Program EXEC=
         */
        private String programName;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set an active boolean value
         *
         * @param active boolean true or false value
         * @return Builder this object
         */
        public Builder active(final boolean active) {
            this.active = active;
            return this;
        }

        /**
         * Set completion string value
         *
         * @param completion string value
         * @return Builder this object
         */
        public Builder completion(final String completion) {
            this.completion = completion;
            return this;
        }

        /**
         * Set procStepName string value
         *
         * @param procStepName string value
         * @return Builder this object
         */
        public Builder procStepName(final String procStepName) {
            this.procStepName = procStepName;
            return this;
        }

        /**
         * Set programName string value
         *
         * @param programName string value
         * @return Builder this object
         */
        public Builder programName(final String programName) {
            this.programName = programName;
            return this;
        }

        /**
         * Set smfid string value
         *
         * @param smfid string value
         * @return Builder this object
         */
        public Builder smfid(final String smfid) {
            this.smfid = smfid;
            return this;
        }

        /**
         * Set stepName string value
         *
         * @param stepName string value
         * @return Builder this object
         */
        public Builder stepName(final String stepName) {
            this.stepName = stepName;
            return this;
        }

        /**
         * Set stepNumber long value
         *
         * @param stepNumber long value
         * @return Builder this object
         */
        public Builder stepNumber(final Long stepNumber) {
            this.stepNumber = stepNumber;
            return this;
        }

        /**
         * Return JobStepData object based on Builder this object
         *
         * @return JobStepData this object
         */
        public JobStepData build() {
            return new JobStepData(this);
        }

    }

}
