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
 * <p><a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-create-workflow">z/OSMF REST API</a>
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

    public String getWorkflowName() {
        return workflowName;
    }

    public String getWorkflowDefinitionFile() {
        return workflowDefinitionFile;
    }

    public String getWorkflowDefinitionFileSystem() {
        return workflowDefinitionFileSystem;
    }

    public String getVariableInputFile() {
        return variableInputFile;
    }

    public List<WorkflowVariable> getVariables() {
        return variables;
    }

    public String getResolveGlobalConflictByUsing() {
        return resolveGlobalConflictByUsing;
    }

    public String getSystem() {
        return system;
    }

    public String getOwner() {
        return owner;
    }

    public String getWorkflowArchiveSAFID() {
        return workflowArchiveSAFID;
    }

    public String getComments() {
        return comments;
    }

    public Boolean getAssignToOwner() {
        return assignToOwner;
    }

    public String getAccessType() {
        return accessType;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public List<String> getJobStatement() {
        return jobStatement;
    }

    public Boolean getDeleteCompletedJobs() {
        return deleteCompletedJobs;
    }

    public String getJobsOutputDirectory() {
        return jobsOutputDirectory;
    }

    public Boolean getAutoDeleteOnCompletion() {
        return autoDeleteOnCompletion;
    }

    public String getTargetSystemuid() {
        return targetSystemuid;
    }

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

        public Builder workflowName(final String workflowName) {
            this.workflowName = workflowName;
            return this;
        }

        public Builder workflowDefinitionFile(final String workflowDefinitionFile) {
            this.workflowDefinitionFile = workflowDefinitionFile;
            return this;
        }

        public Builder workflowDefinitionFileSystem(final String workflowDefinitionFileSystem) {
            this.workflowDefinitionFileSystem = workflowDefinitionFileSystem;
            return this;
        }

        public Builder variableInputFile(final String variableInputFile) {
            this.variableInputFile = variableInputFile;
            return this;
        }

        public Builder variables(final List<WorkflowVariable> variables) {
            this.variables = variables;
            return this;
        }

        public Builder resolveGlobalConflictByUsing(final String resolveGlobalConflictByUsing) {
            this.resolveGlobalConflictByUsing = resolveGlobalConflictByUsing;
            return this;
        }

        public Builder system(final String system) {
            this.system = system;
            return this;
        }

        public Builder owner(final String owner) {
            this.owner = owner;
            return this;
        }

        public Builder workflowArchiveSAFID(final String workflowArchiveSAFID) {
            this.workflowArchiveSAFID = workflowArchiveSAFID;
            return this;
        }

        public Builder comments(final String comments) {
            this.comments = comments;
            return this;
        }

        public Builder assignToOwner(final Boolean assignToOwner) {
            this.assignToOwner = assignToOwner;
            return this;
        }

        public Builder accessType(final String accessType) {
            this.accessType = accessType;
            return this;
        }

        public Builder accountInfo(final String accountInfo) {
            this.accountInfo = accountInfo;
            return this;
        }

        public Builder jobStatement(final List<String> jobStatement) {
            this.jobStatement = jobStatement;
            return this;
        }

        public Builder deleteCompletedJobs(final Boolean deleteCompletedJobs) {
            this.deleteCompletedJobs = deleteCompletedJobs;
            return this;
        }

        public Builder jobsOutputDirectory(final String jobsOutputDirectory) {
            this.jobsOutputDirectory = jobsOutputDirectory;
            return this;
        }

        public Builder autoDeleteOnCompletion(final Boolean autoDeleteOnCompletion) {
            this.autoDeleteOnCompletion = autoDeleteOnCompletion;
            return this;
        }

        public Builder targetSystemuid(final String targetSystemuid) {
            this.targetSystemuid = targetSystemuid;
            return this;
        }

        public Builder targetSystempwd(final String targetSystempwd) {
            this.targetSystempwd = targetSystempwd;
            return this;
        }

        public WorkflowCreateInputData build() {
            return new WorkflowCreateInputData(this);
        }

    }

}
