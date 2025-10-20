/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.model;

import com.fasterxml.jackson.annotation.*;

/**
 * Step info on a job interface
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobStepData {

    /**
     * SMFID
     */
    @JsonSetter(value = "smfid", nulls = Nulls.AS_EMPTY)
    private final String smfid;

    /**
     * Completion
     */
    @JsonSetter(value = "completion", nulls = Nulls.AS_EMPTY)
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
    @JsonSetter(value = "procStepName", nulls = Nulls.AS_EMPTY)
    private final String procStepName;

    /**
     * Step for which a job dd exists
     */
    @JsonSetter(value = "stepName", nulls = Nulls.AS_EMPTY)
    private final String stepName;

    /**
     * Program EXEC=
     */
    @JsonSetter(value = "programName", nulls = Nulls.AS_EMPTY)
    private final String programName;

    /**
     * JobStepData constructor
     *
     * @param smfid        SMFID value
     * @param completion   completion string
     * @param active       boolean value
     * @param stepNumber   long step number
     * @param procStepName procedure step name
     * @param stepName     step name
     * @param programName  program name
     * @author Frank Giordano
     */
    @JsonCreator
    public JobStepData(
            @JsonProperty("smfid") final String smfid,
            @JsonProperty("completion") final String completion,
            @JsonProperty("active") final boolean active,
            @JsonProperty("step-number") final Long stepNumber,
            @JsonProperty("proc-step-name") final String procStepName,
            @JsonProperty("step-name") final String stepName,
            @JsonProperty("program-name") final String programName) {
        this.smfid = smfid;
        this.completion = completion;
        this.active = active;
        this.stepNumber = stepNumber == null ? 0L : stepNumber;
        this.procStepName = procStepName;
        this.stepName = stepName;
        this.programName = programName;
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
    public String getCompletion() {
        return completion;
    }

    /**
     * Retrieve procStepName optional string
     *
     * @return optional string
     */
    public String getProcStepName() {
        return procStepName;
    }

    /**
     * Retrieve programName optional string
     *
     * @return optional string
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Retrieve smfid optional string
     *
     * @return optional string
     */
    public String getSmfid() {
        return smfid;
    }

    /**
     * Retrieve stepName optional string
     *
     * @return optional string
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * Retrieve stepNumber optional long
     *
     * @return optional long
     */
    public Long getStepNumber() {
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
                "smfid='" + smfid + '\'' +
                ", completion='" + completion + '\'' +
                ", active=" + active +
                ", stepNumber=" + stepNumber +
                ", procStepName='" + procStepName + '\'' +
                ", stepName='" + stepName + '\'' +
                ", programName='" + programName + '\'' +
                '}';
    }

}
