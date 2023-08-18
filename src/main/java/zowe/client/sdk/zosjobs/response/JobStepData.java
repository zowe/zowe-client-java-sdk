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
 * @version 2.0
 */
public class JobStepData {

    /**
     * SMFID
     */
    private final Optional<String> smfid;

    /**
     * Completion
     */
    private final Optional<String> completion;

    /**
     * Active
     */
    private final boolean active;

    /**
     * Job relevant step
     */
    private final OptionalLong stepNumber;

    /**
     * Job relevant proc
     */
    private final Optional<String> procStepName;

    /**
     * Step for which job dd exists
     */
    private final Optional<String> stepName;

    /**
     * Program EXEC=
     */
    private final Optional<String> programName;

    /**
     * JobStepData constructor
     *
     * @param builder JobStepData.Builder object
     * @author Frank Giordano
     */
    private JobStepData(final JobStepData.Builder builder) {
        this.smfid = Optional.ofNullable(builder.smfid);
        this.completion = Optional.ofNullable(builder.completion);
        this.active = builder.active;
        if (builder.stepNumber == null) {
            this.stepNumber = OptionalLong.empty();
        } else {
            this.stepNumber = OptionalLong.of(builder.stepNumber);
        }
        this.procStepName = Optional.ofNullable(builder.procStepName);
        this.stepName = Optional.ofNullable(builder.stepName);
        this.programName = Optional.ofNullable(builder.programName);
    }

    public boolean isActive() {
        return active;
    }

    public Optional<String> getCompletion() {
        return completion;
    }

    public Optional<String> getProcStepName() {
        return procStepName;
    }

    public Optional<String> getProgramName() {
        return programName;
    }

    public Optional<String> getSmfid() {
        return smfid;
    }

    public Optional<String> getStepName() {
        return stepName;
    }

    public OptionalLong getStepNumber() {
        return stepNumber;
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

        private String smfid;
        private String completion;
        private boolean active;
        private Long stepNumber;
        private String procStepName;
        private String stepName;
        private String programName;

        public JobStepData build() {
            return new JobStepData(this);
        }

        public JobStepData.Builder active(final boolean active) {
            this.active = active;
            return this;
        }

        public JobStepData.Builder completion(final String completion) {
            this.completion = completion;
            return this;
        }

        public JobStepData.Builder procStepName(final String procStepName) {
            this.procStepName = procStepName;
            return this;
        }

        public JobStepData.Builder programName(final String programName) {
            this.programName = programName;
            return this;
        }

        public JobStepData.Builder smfid(final String smfid) {
            this.smfid = smfid;
            return this;
        }

        public JobStepData.Builder stepName(final String stepName) {
            this.stepName = stepName;
            return this;
        }

        public JobStepData.Builder stepNumber(final Long stepNumber) {
            this.stepNumber = stepNumber;
            return this;
        }

    }


}
