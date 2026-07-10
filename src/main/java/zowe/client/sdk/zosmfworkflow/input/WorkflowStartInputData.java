/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.types.ConflictStartType;

/**
 * Parameters for the z/OSMF start workflow API input data.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-start-workflow">z/OSMF REST API</a>
 *
 * @author Shantanu Danej
 * @version 7.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowStartInputData {

    /**
     * Workflow key identifying the workflow to start.
     */
    @JsonIgnore
    private final String workflowKey;

    /**
     * Determines how to resolve variable conflicts.
     */
    private final String resolveConflictByUsing;

    /**
     * Name of the step from which to begin automation.
     */
    private final String stepName;

    /**
     * Indicates whether subsequent automated steps should execute.
     */
    private final Boolean performSubsequent;

    /**
     * URL to receive workflow completion notification.
     */
    private final String notificationUrl;

    /**
     * User ID for remote system basic authentication.
     */
    private final String targetSystemuid;

    /**
     * Password for remote system basic authentication.
     */
    private final String targetSystempwd;

    /**
     * WorkflowStartInputData constructor.
     *
     * @param builder builder instance
     * @author Eshaan Gupta
     */
    private WorkflowStartInputData(final Builder builder) {
        ValidateUtils.checkIllegalParameter(builder.workflowKey, "workflowKey");
        this.workflowKey = builder.workflowKey;
        this.resolveConflictByUsing =
                builder.resolveConflictByUsing != null ? builder.resolveConflictByUsing.getValue() : null;
        this.stepName = builder.stepName;
        this.performSubsequent = builder.performSubsequent;
        this.notificationUrl = builder.notificationUrl;
        this.targetSystemuid = builder.targetSystemuid;
        this.targetSystempwd = builder.targetSystempwd;
    }

    /**
     * Retrieve workflowKey value.
     *
     * @return workflowKey value
     */
    public String getWorkflowKey() {
        return workflowKey;
    }

    /**
     * Retrieve resolveConflictByUsing value.
     *
     * @return resolveConflictByUsing value
     */
    public String getResolveConflictByUsing() {
        return resolveConflictByUsing;
    }

    /**
     * Retrieve stepName value.
     *
     * @return stepName value
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * Retrieve performSubsequent value.
     *
     * @return performSubsequent value
     */
    public Boolean getPerformSubsequent() {
        return performSubsequent;
    }

    /**
     * Retrieve notificationUrl value.
     *
     * @return notificationUrl value
     */
    public String getNotificationUrl() {
        return notificationUrl;
    }

    /**
     * Retrieve targetSystemuid value.
     *
     * @return targetSystemuid value
     */
    public String getTargetSystemuid() {
        return targetSystemuid;
    }

    /**
     * Retrieve targetSystempwd value.
     *
     * @return targetSystempwd value
     */
    public String getTargetSystempwd() {
        return targetSystempwd;
    }

    /**
     * Return a string value representing a WorkflowStartInputData object.
     *
     * @return string representation of WorkflowStartInputData
     */
    @Override
    public String toString() {
        return "WorkflowStartInputData{" +
                "workflowKey='" + workflowKey + '\'' +
                ", resolveConflictByUsing='" + resolveConflictByUsing + '\'' +
                ", stepName='" + stepName + '\'' +
                ", performSubsequent=" + performSubsequent +
                ", notificationUrl='" + notificationUrl + '\'' +
                ", targetSystemuid='" + targetSystemuid + '\'' +
                ", targetSystempwd='" + targetSystempwd + '\'' +
                '}';
    }

    /**
     * Builder for workflow start input data.
     */
    public static final class Builder {

        private final String workflowKey;
        private ConflictStartType resolveConflictByUsing;
        private String stepName;
        private Boolean performSubsequent;
        private String notificationUrl;
        private String targetSystemuid;
        private String targetSystempwd;

        /**
         * Builder constructor.
         *
         * @param workflowKey workflow key identifying the workflow to start
         */
        public Builder(final String workflowKey) {
            this.workflowKey = workflowKey;
        }

        /**
         * Set the conflict resolution strategy.
         *
         * @param type conflict resolution type
         * @return this builder instance
         */
        public Builder resolveConflictByUsing(final ConflictStartType type) {
            this.resolveConflictByUsing = type;
            return this;
        }

        /**
         * Set the step name.
         *
         * @param stepName step name value
         * @return this builder instance
         */
        public Builder stepName(final String stepName) {
            this.stepName = stepName;
            return this;
        }

        /**
         * Set perform subsequent value.
         *
         * @param performSubsequent perform subsequent value
         * @return this builder instance
         */
        public Builder performSubsequent(final Boolean performSubsequent) {
            this.performSubsequent = performSubsequent;
            return this;
        }

        /**
         * Set the notification URL.
         *
         * @param notificationUrl notification URL value
         * @return this builder instance
         */
        public Builder notificationUrl(final String notificationUrl) {
            this.notificationUrl = notificationUrl;
            return this;
        }

        /**
         * Set the target system UID.
         *
         * @param targetSystemuid target system UID value
         * @return this builder instance
         */
        public Builder targetSystemuid(final String targetSystemuid) {
            this.targetSystemuid = targetSystemuid;
            return this;
        }

        /**
         * Set the target system password.
         *
         * @param targetSystempwd target system password value
         * @return this builder instance
         */
        public Builder targetSystempwd(final String targetSystempwd) {
            this.targetSystempwd = targetSystempwd;
            return this;
        }

        /**
         * Build WorkflowStartInputData instance.
         *
         * @return WorkflowStartInputData instance
         */
        public WorkflowStartInputData build() {
            return new WorkflowStartInputData(this);
        }

    }

}
