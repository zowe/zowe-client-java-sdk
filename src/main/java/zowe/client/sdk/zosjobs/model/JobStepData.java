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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Step data information. Provides information about each step in the job,
 * such as the step name, step number, and completion code.
 *
 * @author Frank Giordano
 * @version 6.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobStepData {

    /**
     * Value is set to true if the step is running. Otherwise, the value is false.
     */
    private final boolean active;

    /**
     * The SMF ID of the system where the step is running.
     */
    private final String smfid;

    /**
     * Step number.
     */
    private final Long stepNumber;

    /**
     * The z/OS user ID associated with the job.
     */
    private final String owner;

    /**
     * Name of the program to be run by the job step. This value is retrieved
     * from the EXEC statement. Program EXEC=
     */
    private final String programName;

    /**
     * Step for which a job dd exists
     */
    private final String stepName;

    /**
     * Path to the program in the z/OS UNIX System Services (z/OS UNIX) file system
     * that is run by the job step. Not returned if the step is active.
     */
    private final String pathName;

    /**
     * Name of the procedure to be run by the job step.
     * This value is retrieved from the EXEC statement.
     */
    private final String procStepName;

    /**
     * Step completion code. One of the following values:
     * ABENDUnnnn
     * Step ended with the user abend code nnnn.
     * ABEND Sxxx
     * Step ended with the system abend code xxx.
     * CANCELED
     * Step was canceled.
     * CC nnnn
     * Step ended with the completion code nnnn.
     * FLUSHED
     * Step was not processed.
     */
    private final String completion;

    /**
     * JobStepData constructor for Jackson JSON parsing.
     *
     * @param active       boolean value
     * @param smfid        SMFID value
     * @param stepNumber   long step number
     * @param owner        job owner
     * @param programName  program name
     * @param stepName     step name
     * @param pathName     path name
     * @param procStepName procedure step name
     * @param completion   completion
     * @author Frank Giordano
     */
    @JsonCreator
    public JobStepData(
            @JsonProperty("active") final boolean active,
            @JsonProperty("smfid") final String smfid,
            @JsonProperty("step-number") final Long stepNumber,
            @JsonProperty("owner") final String owner,
            @JsonProperty("program-name") final String programName,
            @JsonProperty("step-name") final String stepName,
            @JsonProperty("path-name") final String pathName,
            @JsonProperty("proc-step-name") final String procStepName,
            @JsonProperty("completion") final String completion) {
        this.active = active;
        this.smfid = smfid == null ? "" : smfid;
        this.stepNumber = stepNumber == null ? 0L : stepNumber;
        this.owner = owner == null ? "" : owner;
        this.programName = programName == null ? "" : programName;
        this.stepName = stepName == null ? "" : stepName;
        this.pathName = pathName == null ? "" : pathName;
        this.procStepName = procStepName == null ? "" : procStepName;
        this.completion = completion == null ? "" : completion;
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
     * Retrieve smfid optional string
     *
     * @return optional string
     */
    public String getSmfid() {
        return smfid;
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
     * Retrieve owner optional string.
     *
     * @return string value
     */
    public String getOwner() {
        return owner;
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
     * Retrieve stepName optional string
     *
     * @return optional string
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * Retrieve pathName optional string.
     *
     * @return string value
     */
    public String getPathName() {
        return pathName;
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
     * Retrieve completion optional string
     *
     * @return optional string
     */
    public String getCompletion() {
        return completion;
    }

    /**
     * Return string value representing JobStepData object
     *
     * @return string representation of JobStepData
     */
    @Override
    public String toString() {
        return "JobStepData{" +
                "active=" + active +
                ", smfid='" + smfid + '\'' +
                ", stepNumber=" + stepNumber +
                ", owner='" + owner + '\'' +
                ", programName='" + programName + '\'' +
                ", stepName='" + stepName + '\'' +
                ", pathName='" + pathName + '\'' +
                ", procStepName='" + procStepName + '\'' +
                ", completion='" + completion + '\'' +
                '}';
    }

}
