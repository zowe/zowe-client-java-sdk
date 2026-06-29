/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Workflow automation-info information returned for a workflow properties request.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowAutomationStatus {

    /**
     * User ID of the user who initiated the automation processing.
     */
    private final String startUser;

    /**
     * Time that automation processing started, expressed as the number of milliseconds since
     * midnight on January 1, 1970 UTC.
     */
    private final Long startedTime;

    /**
     * Time that automation processing stopped, expressed as the number of milliseconds since
     * midnight on January 1, 1970 UTC. Null when automation is still in progress.
     */
    private final Long stoppedTime;

    /**
     * Name of the step being processed automatically or the step that caused automation to stop.
     */
    private final String currentStepName;

    /**
     * The step number. Null when automation is stopped and the workflow status is complete.
     */
    private final String currentStepNumber;

    /**
     * Step title. Null when automation is stopped and the workflow status is complete.
     */
    private final String currentStepTitle;

    /**
     * Message identifier for the accompanying message. Null when automation is still in progress.
     */
    private final String messageID;

    /**
     * Message text that describes the reason that automation is stopped. Null when automation is still in progress.
     */
    private final String messageText;

    /**
     * WorkflowAutomationStatus Jackson constructor.
     *
     * @param startUser         user ID that initiated automation processing
     * @param startedTime       time automation processing started
     * @param stoppedTime       time automation processing stopped
     * @param currentStepName   name of the current step
     * @param currentStepNumber number of the current step
     * @param currentStepTitle  title of the current step
     * @param messageID         message identifier
     * @param messageText       message text
     */
    @JsonCreator
    public WorkflowAutomationStatus(
            @JsonProperty("startUser") final String startUser,
            @JsonProperty("startedTime") final Long startedTime,
            @JsonProperty("stoppedTime") final Long stoppedTime,
            @JsonProperty("currentStepName") final String currentStepName,
            @JsonProperty("currentStepNumber") final String currentStepNumber,
            @JsonProperty("currentStepTitle") final String currentStepTitle,
            @JsonProperty("messageID") final String messageID,
            @JsonProperty("messageText") final String messageText) {
        this.startUser = orEmpty(startUser);
        this.startedTime = startedTime;
        this.stoppedTime = stoppedTime;
        this.currentStepName = orEmpty(currentStepName);
        this.currentStepNumber = orEmpty(currentStepNumber);
        this.currentStepTitle = orEmpty(currentStepTitle);
        this.messageID = orEmpty(messageID);
        this.messageText = orEmpty(messageText);
    }

    /* Null-handling helpers */

    private static String orEmpty(final String value) {
        return value == null ? "" : value;
    }

    /**
     * Retrieve startUser value.
     *
     * @return startUser value
     */
    public String getStartUser() {
        return startUser;
    }

    /**
     * Retrieve startedTime value.
     *
     * @return startedTime value
     */
    public Long getStartedTime() {
        return startedTime;
    }

    /**
     * Retrieve stoppedTime value.
     *
     * @return stoppedTime value
     */
    public Long getStoppedTime() {
        return stoppedTime;
    }

    /**
     * Retrieve the currentStepName value.
     *
     * @return currentStepName value
     */
    public String getCurrentStepName() {
        return currentStepName;
    }

    /**
     * Retrieve the currentStepNumber value.
     *
     * @return currentStepNumber value
     */
    public String getCurrentStepNumber() {
        return currentStepNumber;
    }

    /**
     * Retrieve the currentStepTitle value.
     *
     * @return currentStepTitle value
     */
    public String getCurrentStepTitle() {
        return currentStepTitle;
    }

    /**
     * Retrieve messageID value.
     *
     * @return messageID value
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Retrieve messageText value.
     *
     * @return messageText value
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Return a string value representing a WorkflowAutomationStatus object.
     *
     * @return string representation of WorkflowAutomationStatus
     */
    @Override
    public String toString() {
        return "WorkflowAutomationStatus{" +
                "startUser='" + startUser + '\'' +
                ", startedTime=" + startedTime +
                ", stoppedTime=" + stoppedTime +
                ", currentStepName='" + currentStepName + '\'' +
                ", currentStepNumber='" + currentStepNumber + '\'' +
                ", currentStepTitle='" + currentStepTitle + '\'' +
                ", messageID='" + messageID + '\'' +
                ", messageText='" + messageText + '\'' +
                '}';
    }

}
