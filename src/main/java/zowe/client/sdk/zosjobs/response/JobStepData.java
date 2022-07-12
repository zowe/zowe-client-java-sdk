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
     * Step for which job dd exists
     */
    private String stepName;

    /**
     * Program EXEC=
     */
    private String programName;

    // TODO

    public void setSmfid(String smfid) {
        this.smfid = smfid;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setProcStepName(String procStepName) {
        this.procStepName = procStepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public void setStepNumber(Long stepNumber) {
        this.stepNumber = stepNumber;
    }

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
