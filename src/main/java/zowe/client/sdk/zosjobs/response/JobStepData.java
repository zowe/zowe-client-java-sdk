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

/**
 * Step info on a job interface
 *
 * @author Frank Giordano
 * @version 1.0
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
    private final Optional<Boolean> active;

    /**
     * Job relevant step
     */
    private final Optional<Long> stepNumber;

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

    private JobStepData(JobStepData.Builder builder) {
        this.smfid = Optional.ofNullable(builder.smfid);
        this.completion = Optional.ofNullable(builder.completion);
        this.active = Optional.ofNullable(builder.active);
        this.stepNumber = Optional.ofNullable(builder.stepNumber);
        this.procStepName = Optional.ofNullable(builder.procStepName);
        this.stepName = Optional.ofNullable(builder.stepName);
        this.programName = Optional.ofNullable(builder.programName);
    }

    public Optional<String> getSmfid() {
        return smfid;
    }

    public Optional<String> getCompletion() {
        return completion;
    }

    public Optional<Boolean> getActive() {
        return active;
    }

    public Optional<Long> getStepNumber() {
        return stepNumber;
    }

    public Optional<String> getProcStepName() {
        return procStepName;
    }

    public Optional<String> getStepName() {
        return stepName;
    }

    public Optional<String> getProgramName() {
        return programName;
    }

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

    public static class Builder {

        private String smfid;
        private String completion;
        private boolean active;
        private long stepNumber;
        private String procStepName;
        private String stepName;
        private String programName;

        public JobStepData.Builder smfid(String smfid) {
            this.smfid = smfid;
            return this;
        }

        public JobStepData.Builder completion(String active) {
            this.completion = completion;
            return this;
        }

        public JobStepData.Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public JobStepData.Builder stepNumber(long stepNumber) {
            this.stepNumber = stepNumber;
            return this;
        }

        public JobStepData.Builder procStepName(String procStepName) {
            this.procStepName = procStepName;
            return this;
        }

        public JobStepData.Builder stepName(String stepName) {
            this.stepName = stepName;
            return this;
        }

        public JobStepData.Builder programName(String programName) {
            this.programName = programName;
            return this;
        }

        public JobStepData build() {
            return new JobStepData(this);
        }

    }


}
