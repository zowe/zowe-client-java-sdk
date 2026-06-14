/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflow.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import zowe.client.sdk.zosworkflow.model.WorkflowVariable;

import java.util.List;

/**
 * Parameters for the z/OSMF create workflow API input data.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-create-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowCreateInputData {

    /**
     * Descriptive name for the workflow.
     */
    private final String workflowName;

    /**
     * Location of the workflow definition file.
     */
    private final String workflowDefinitionFile;

    /**
     * Nickname of the system on which the workflow definition file resides.
     */
    private final String workflowDefinitionFileSystem;

    /**
     * Optional workflow variable input file.
     */
    private final String variableInputFile;

    /**
     * Workflow variables supplied on the request.
     */
    private final List<WorkflowVariable> variables;

    /**
     * Resolve conflict behavior for global variables.
     */
    private final String resolveGlobalConflictByUsing;

    /**
     * Nickname of the system where the workflow is created.
     */
    private final String system;

    /**
     * User ID of the workflow owner.
     */
    private final String owner;

    /**
     * SAF group or workflow owner user ID for archived workflow access.
     */
    private final String workflowArchiveSAFID;

    /**
     * Workflow creation comments.
     */
    private final String comments;

    /**
     * Indicator that workflow steps are assigned to the owner on create.
     */
    private final Boolean assignToOwner;

    /**
     * Workflow access type.
     */
    private final String accessType;

    /**
     * Account information for job-submitting workflows.
     */
    private final String accountInfo;

    /**
     * JOB statement for job-submitting workflows.
     */
    private final List<String> jobStatement;

    /**
     * Indicator that completed jobs are deleted from JES spool.
     */
    private final Boolean deleteCompletedJobs;

    /**
     * Directory used to save job spool files.
     */
    private final String jobsOutputDirectory;

    /**
     * Indicator that the workflow is automatically deleted on completion.
     */
    private final Boolean autoDeleteOnCompletion;

    /**
     * User ID used for remote system basic authentication.
     */
    private final String targetSystemuid;

    /**
     * Password used for remote system basic authentication.
     */
    private final String targetSystempwd;

    /**
     * WorkflowCreateInputData constructor.
     *
     * @param builder builder instance
     */
    private WorkflowCreateInputData(final Builder builder) {
        this.workflowName = builder.workflowName;
        this.workflowDefinitionFile = builder.workflowDefinitionFile;
        this.workflowDefinitionFileSystem = builder.workflowDefinitionFileSystem;
        this.variableInputFile = builder.variableInputFile;
        this.variables = builder.variables;
        this.resolveGlobalConflictByUsing = builder.resolveGlobalConflictByUsing;
        this.system = builder.system;
        this.owner = builder.owner;
        this.workflowArchiveSAFID = builder.workflowArchiveSAFID;
        this.comments = builder.comments;
        this.assignToOwner = builder.assignToOwner;
        this.accessType = builder.accessType;
        this.accountInfo = builder.accountInfo;
        this.jobStatement = builder.jobStatement;
        this.deleteCompletedJobs = builder.deleteCompletedJobs;
        this.jobsOutputDirectory = builder.jobsOutputDirectory;
        this.autoDeleteOnCompletion = builder.autoDeleteOnCompletion;
        this.targetSystemuid = builder.targetSystemuid;
        this.targetSystempwd = builder.targetSystempwd;
    }

    /**
     * Retrieve workflowName value.
     *
     * @return workflowName value
     */
    public String getWorkflowName() {
        return workflowName;
    }

    /**
     * Retrieve workflowDefinitionFile value.
     *
     * @return workflowDefinitionFile value
     */
    public String getWorkflowDefinitionFile() {
        return workflowDefinitionFile;
    }

    /**
     * Retrieve workflowDefinitionFileSystem value.
     *
     * @return workflowDefinitionFileSystem value
     */
    public String getWorkflowDefinitionFileSystem() {
        return workflowDefinitionFileSystem;
    }

    /**
     * Retrieve variableInputFile value.
     *
     * @return variableInputFile value
     */
    public String getVariableInputFile() {
        return variableInputFile;
    }

    /**
     * Retrieve variables value.
     *
     * @return variables value
     */
    public List<WorkflowVariable> getVariables() {
        return variables;
    }

    /**
     * Retrieve resolveGlobalConflictByUsing value.
     *
     * @return resolveGlobalConflictByUsing value
     */
    public String getResolveGlobalConflictByUsing() {
        return resolveGlobalConflictByUsing;
    }

    /**
     * Retrieve system value.
     *
     * @return system value
     */
    public String getSystem() {
        return system;
    }

    /**
     * Retrieve owner value.
     *
     * @return owner value
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Retrieve workflowArchiveSAFID value.
     *
     * @return workflowArchiveSAFID value
     */
    public String getWorkflowArchiveSAFID() {
        return workflowArchiveSAFID;
    }

    /**
     * Retrieve comments value.
     *
     * @return comments value
     */
    public String getComments() {
        return comments;
    }

    /**
     * Retrieve assignToOwner value.
     *
     * @return assignToOwner value
     */
    public Boolean getAssignToOwner() {
        return assignToOwner;
    }

    /**
     * Retrieve accessType value.
     *
     * @return accessType value
     */
    public String getAccessType() {
        return accessType;
    }

    /**
     * Retrieve accountInfo value.
     *
     * @return accountInfo value
     */
    public String getAccountInfo() {
        return accountInfo;
    }

    /**
     * Retrieve jobStatement value.
     *
     * @return jobStatement value
     */
    public List<String> getJobStatement() {
        return jobStatement;
    }

    /**
     * Retrieve deleteCompletedJobs value.
     *
     * @return deleteCompletedJobs value
     */
    public Boolean getDeleteCompletedJobs() {
        return deleteCompletedJobs;
    }

    /**
     * Retrieve jobsOutputDirectory value.
     *
     * @return jobsOutputDirectory value
     */
    public String getJobsOutputDirectory() {
        return jobsOutputDirectory;
    }

    /**
     * Retrieve autoDeleteOnCompletion value.
     *
     * @return autoDeleteOnCompletion value
     */
    public Boolean getAutoDeleteOnCompletion() {
        return autoDeleteOnCompletion;
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
     * Create a new builder for workflow create input data.
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Return string value representing WorkflowCreateInputData object.
     *
     * @return string representation of WorkflowCreateInputData
     */
    @Override
    public String toString() {
        return "WorkflowCreateInputData{" +
                "workflowName='" + workflowName + '\'' +
                ", workflowDefinitionFile='" + workflowDefinitionFile + '\'' +
                ", workflowDefinitionFileSystem='" + workflowDefinitionFileSystem + '\'' +
                ", variableInputFile='" + variableInputFile + '\'' +
                ", variables=" + variables +
                ", resolveGlobalConflictByUsing='" + resolveGlobalConflictByUsing + '\'' +
                ", system='" + system + '\'' +
                ", owner='" + owner + '\'' +
                ", workflowArchiveSAFID='" + workflowArchiveSAFID + '\'' +
                ", comments='" + comments + '\'' +
                ", assignToOwner=" + assignToOwner +
                ", accessType='" + accessType + '\'' +
                ", accountInfo='" + accountInfo + '\'' +
                ", jobStatement=" + jobStatement +
                ", deleteCompletedJobs=" + deleteCompletedJobs +
                ", jobsOutputDirectory='" + jobsOutputDirectory + '\'' +
                ", autoDeleteOnCompletion=" + autoDeleteOnCompletion +
                ", targetSystemuid='" + targetSystemuid + '\'' +
                ", targetSystempwd='" + targetSystempwd + '\'' +
                '}';
    }

    /**
     * Builder for workflow create input data.
     */
    public static final class Builder {

        private String workflowName;
        private String workflowDefinitionFile;
        private String workflowDefinitionFileSystem;
        private String variableInputFile;
        private List<WorkflowVariable> variables;
        private String resolveGlobalConflictByUsing;
        private String system;
        private String owner;
        private String workflowArchiveSAFID;
        private String comments;
        private Boolean assignToOwner;
        private String accessType;
        private String accountInfo;
        private List<String> jobStatement;
        private Boolean deleteCompletedJobs;
        private String jobsOutputDirectory;
        private Boolean autoDeleteOnCompletion;
        private String targetSystemuid;
        private String targetSystempwd;

        private Builder() {
        }

        /**
         * Set the workflow name.
         *
         * @param workflowName workflow name value
         * @return this builder instance
         */
        public Builder workflowName(final String workflowName) {
            this.workflowName = workflowName;
            return this;
        }

        /**
         * Set the workflow definition file.
         *
         * @param workflowDefinitionFile workflow definition file value
         * @return this builder instance
         */
        public Builder workflowDefinitionFile(final String workflowDefinitionFile) {
            this.workflowDefinitionFile = workflowDefinitionFile;
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
         * Set the variable input file.
         *
         * @param variableInputFile variable input file value
         * @return this builder instance
         */
        public Builder variableInputFile(final String variableInputFile) {
            this.variableInputFile = variableInputFile;
            return this;
        }

        /**
         * Set the workflow variables.
         *
         * @param variables workflow variables value
         * @return this builder instance
         */
        public Builder variables(final List<WorkflowVariable> variables) {
            this.variables = variables;
            return this;
        }

        /**
         * Set the resolve global conflict by using.
         *
         * @param resolveGlobalConflictByUsing resolve global conflict by using value
         * @return this builder instance
         */
        public Builder resolveGlobalConflictByUsing(final String resolveGlobalConflictByUsing) {
            this.resolveGlobalConflictByUsing = resolveGlobalConflictByUsing;
            return this;
        }

        /**
         * Set the system.
         *
         * @param system system value
         * @return this builder instance
         */
        public Builder system(final String system) {
            this.system = system;
            return this;
        }

        /**
         * Set the owner.
         *
         * @param owner owner value
         * @return this builder instance
         */
        public Builder owner(final String owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Set the workflow archive SAF ID.
         *
         * @param workflowArchiveSAFID workflow archive SAF ID value
         * @return this builder instance
         */
        public Builder workflowArchiveSAFID(final String workflowArchiveSAFID) {
            this.workflowArchiveSAFID = workflowArchiveSAFID;
            return this;
        }

        /**
         * Set the comments.
         *
         * @param comments comments value
         * @return this builder instance
         */
        public Builder comments(final String comments) {
            this.comments = comments;
            return this;
        }

        /**
         * Set assign to owner.
         *
         * @param assignToOwner assign to owner value
         * @return this builder instance
         */
        public Builder assignToOwner(final Boolean assignToOwner) {
            this.assignToOwner = assignToOwner;
            return this;
        }

        /**
         * Set the access type.
         *
         * @param accessType access type value
         * @return this builder instance
         */
        public Builder accessType(final String accessType) {
            this.accessType = accessType;
            return this;
        }

        /**
         * Set the account info.
         *
         * @param accountInfo account info value
         * @return this builder instance
         */
        public Builder accountInfo(final String accountInfo) {
            this.accountInfo = accountInfo;
            return this;
        }

        /**
         * Set the job statement.
         *
         * @param jobStatement job statement value
         * @return this builder instance
         */
        public Builder jobStatement(final List<String> jobStatement) {
            this.jobStatement = jobStatement;
            return this;
        }

        /**
         * Set delete completed jobs.
         *
         * @param deleteCompletedJobs delete completed jobs value
         * @return this builder instance
         */
        public Builder deleteCompletedJobs(final Boolean deleteCompletedJobs) {
            this.deleteCompletedJobs = deleteCompletedJobs;
            return this;
        }

        /**
         * Set the jobs output directory.
         *
         * @param jobsOutputDirectory jobs output directory value
         * @return this builder instance
         */
        public Builder jobsOutputDirectory(final String jobsOutputDirectory) {
            this.jobsOutputDirectory = jobsOutputDirectory;
            return this;
        }

        /**
         * Set auto delete on completion.
         *
         * @param autoDeleteOnCompletion auto delete on completion value
         * @return this builder instance
         */
        public Builder autoDeleteOnCompletion(final Boolean autoDeleteOnCompletion) {
            this.autoDeleteOnCompletion = autoDeleteOnCompletion;
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
         * Build WorkflowCreateInputData instance.
         *
         * @return WorkflowCreateInputData instance
         */
        public WorkflowCreateInputData build() {
            return new WorkflowCreateInputData(this);
        }

    }

}
