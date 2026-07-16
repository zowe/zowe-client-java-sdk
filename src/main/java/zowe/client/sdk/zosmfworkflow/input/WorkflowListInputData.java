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

import zowe.client.sdk.zosmfworkflow.types.CategoryType;
import zowe.client.sdk.zosmfworkflow.types.StatusNameType;

import java.util.Optional;

/**
 * Parameters for the z/OSMF list workflows API input data.
 * <p>
 * All parameters are optional filters; when none are specified, all workflows are returned.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-list-workflows-system-sysplex">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowListInputData {

    /**
     * Workflow name. A regular expression can be specified to match the desired workflow names.
     */
    private final String workflowName;

    /**
     * Category of the workflow, which is either general or configuration.
     */
    private final CategoryType category;

    /**
     * Nickname of the system on which the workflow is to be performed.
     */
    private final String system;

    /**
     * Workflow status.
     */
    private final StatusNameType statusName;

    /**
     * Workflow owner (a valid z/OS user ID).
     */
    private final String owner;

    /**
     * Name of the vendor that provided the workflow definition file.
     */
    private final String vendor;

    /**
     * WorkflowListInputData constructor.
     *
     * @param builder builder instance
     * @author Ashish Kumar Dash
     */
    private WorkflowListInputData(final Builder builder) {
        this.workflowName = builder.workflowName;
        this.category = builder.category;
        this.system = builder.system;
        this.statusName = builder.statusName;
        this.owner = builder.owner;
        this.vendor = builder.vendor;
    }

    /**
     * Retrieve workflowName value.
     *
     * @return workflowName value
     */
    public Optional<String> getWorkflowName() {
        return Optional.ofNullable(workflowName);
    }

    /**
     * Retrieve category value.
     *
     * @return category value
     */
    public Optional<CategoryType> getCategory() {
        return Optional.ofNullable(category);
    }

    /**
     * Retrieve system value.
     *
     * @return system value
     */
    public Optional<String> getSystem() {
        return Optional.ofNullable(system);
    }

    /**
     * Retrieve statusName value.
     *
     * @return statusName value
     */
    public Optional<StatusNameType> getStatusName() {
        return Optional.ofNullable(statusName);
    }

    /**
     * Retrieve owner value.
     *
     * @return owner value
     */
    public Optional<String> getOwner() {
        return Optional.ofNullable(owner);
    }

    /**
     * Retrieve vendor value.
     *
     * @return vendor value
     */
    public Optional<String> getVendor() {
        return Optional.ofNullable(vendor);
    }

    /**
     * Create a new builder for the workflow list input data.
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Return a string value representing a WorkflowListInputData object.
     *
     * @return string representation of WorkflowListInputData
     */
    @Override
    public String toString() {
        return "WorkflowListInputData{" +
                "workflowName='" + workflowName + '\'' +
                ", category=" + category +
                ", system='" + system + '\'' +
                ", statusName=" + statusName +
                ", owner='" + owner + '\'' +
                ", vendor='" + vendor + '\'' +
                '}';
    }

    /**
     * Builder for workflow list input data.
     */
    public static final class Builder {

        private String workflowName;
        private CategoryType category;
        private String system;
        private StatusNameType statusName;
        private String owner;
        private String vendor;

        private Builder() {
        }

        /**
         * Set the workflow name value. A regular expression can be specified to match desired workflow names.
         *
         * @param workflowName workflow name value
         * @return this builder instance
         */
        public Builder workflowName(final String workflowName) {
            this.workflowName = workflowName;
            return this;
        }

        /**
         * Set the category value.
         *
         * @param category category value
         * @return this builder instance
         */
        public Builder category(final CategoryType category) {
            this.category = category;
            return this;
        }

        /**
         * Set the system value.
         *
         * @param system nickname of the system on which the workflow is to be performed
         * @return this builder instance
         */
        public Builder system(final String system) {
            this.system = system;
            return this;
        }

        /**
         * Set the status name value.
         *
         * @param statusName status name value
         * @return this builder instance
         */
        public Builder statusName(final StatusNameType statusName) {
            this.statusName = statusName;
            return this;
        }

        /**
         * Set the owner value.
         *
         * @param owner workflow owner, a valid z/OS user ID
         * @return this builder instance
         */
        public Builder owner(final String owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Set the vendor value.
         *
         * @param vendor name of the vendor that provided the workflow definition file
         * @return this builder instance
         */
        public Builder vendor(final String vendor) {
            this.vendor = vendor;
            return this;
        }

        /**
         * Build WorkflowListInputData instance.
         *
         * @return WorkflowListInputData instance
         */
        public WorkflowListInputData build() {
            return new WorkflowListInputData(this);
        }

    }

}
