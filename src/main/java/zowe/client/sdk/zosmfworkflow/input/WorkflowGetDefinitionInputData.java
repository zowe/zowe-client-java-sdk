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

import zowe.client.sdk.utility.ValidateUtils;

import java.util.Optional;

/**
 * Parameters for the z/OSMF retrieve workflow definition API input data.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-retrieve-workflow-definition">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowGetDefinitionInputData {

    /**
     * Location of the workflow definition file, which is either a UNIX path name (including the file name)
     * or a fully qualified z/OS data set name. This parameter is required.
     */
    private final String definitionFilePath;

    /**
     * Nickname of the system on which the specified workflow definition file and any related files reside.
     */
    private final String workflowDefinitionFileSystem;

    /**
     * Indicator that requests an array of step-definition objects in the response.
     */
    private final boolean returnSteps;

    /**
     * Indicator that requests an array of variable-definition objects in the response.
     */
    private final boolean returnVariables;

    /**
     * WorkflowGetInputData constructor.
     *
     * @param builder builder instance
     */
    private WorkflowGetDefinitionInputData(final Builder builder) {
        ValidateUtils.checkIllegalParameter(builder.definitionFilePath, "definitionFilePath");
        this.definitionFilePath = builder.definitionFilePath;
        this.workflowDefinitionFileSystem = builder.workflowDefinitionFileSystem;
        this.returnSteps = builder.returnSteps;
        this.returnVariables = builder.returnVariables;
    }

    /**
     * Retrieve definitionFilePath specified.
     *
     * @return definitionFilePath value
     */
    public Optional<String> getDefinitionFilePath() {
        if (definitionFilePath == null || definitionFilePath.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(definitionFilePath);
    }

    /**
     * Retrieve workflowDefinitionFileSystem specified.
     *
     * @return workflowDefinitionFileSystem value
     */
    public Optional<String> getWorkflowDefinitionFileSystem() {
        if (workflowDefinitionFileSystem == null || workflowDefinitionFileSystem.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(workflowDefinitionFileSystem);
    }

    /**
     * Retrieve returnSteps value.
     *
     * @return returnSteps value
     */
    public boolean isReturnSteps() {
        return returnSteps;
    }

    /**
     * Retrieve returnVariables value.
     *
     * @return returnVariables value
     */
    public boolean isReturnVariables() {
        return returnVariables;
    }

    /**
     * Create a new builder for workflow get input data.
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Return string value representing WorkflowGetInputData object.
     *
     * @return string representation of WorkflowGetInputData
     */
    @Override
    public String toString() {
        return "WorkflowGetInputData{" +
                "definitionFilePath='" + definitionFilePath + '\'' +
                ", workflowDefinitionFileSystem='" + workflowDefinitionFileSystem + '\'' +
                ", returnSteps=" + returnSteps +
                ", returnVariables=" + returnVariables +
                '}';
    }

    /**
     * Builder for workflow get input data.
     */
    public static final class Builder {

        private String definitionFilePath;
        private String workflowDefinitionFileSystem;
        private boolean returnSteps;
        private boolean returnVariables;

        private Builder() {
        }

        /**
         * Set the workflow definition file path.
         *
         * @param definitionFilePath workflow definition file path value
         * @return this builder instance
         */
        public Builder definitionFilePath(final String definitionFilePath) {
            this.definitionFilePath = definitionFilePath;
            return this;
        }

        /**
         * Set the workflow definition file system.
         *
         * @param workflowDefinitionFileSystem workflow definition file system value
         * @return this builder instance
         */
        public Builder workflowDefinitionFileSystem(final String workflowDefinitionFileSystem) {
            this.workflowDefinitionFileSystem = workflowDefinitionFileSystem;
            return this;
        }

        /**
         * Set whether the response includes the workflow step definitions.
         *
         * @param returnSteps return steps value
         * @return this builder instance
         */
        public Builder returnSteps(final boolean returnSteps) {
            this.returnSteps = returnSteps;
            return this;
        }

        /**
         * Set whether the response includes the workflow variable definitions.
         *
         * @param returnVariables return variables value
         * @return this builder instance
         */
        public Builder returnVariables(final boolean returnVariables) {
            this.returnVariables = returnVariables;
            return this;
        }

        /**
         * Build WorkflowGetInputData instance.
         *
         * @return WorkflowGetInputData instance
         */
        public WorkflowGetDefinitionInputData build() {
            return new WorkflowGetDefinitionInputData(this);
        }

    }

}
