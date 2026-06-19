/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import zowe.client.sdk.zosmfworkflow.model.WorkflowStepDefinition;
import zowe.client.sdk.zosmfworkflow.model.WorkflowVariableDefinition;

import java.util.Collections;
import java.util.List;

/**
 * API response for retrieve workflow definition.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-retrieve-workflow-definition">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowGetDefinitionResponse {

    /**
     * Default name for the workflow.
     */
    private final String workflowDefaultName;

    /**
     * Description of the workflow.
     */
    private final String workflowDescription;

    /**
     * Workflow ID. A short, arbitrary value that identifies the workflow.
     */
    private final String workflowID;

    /**
     * Version of the workflow definition file.
     */
    private final String workflowVersion;

    /**
     * Name of the vendor that provided the workflow definition file.
     */
    private final String vendor;

    /**
     * A 128-bit hash value that z/OSMF generates to uniquely identify the workflow definition file.
     */
    private final String workflowDefinitionFileMD5Value;

    /**
     * Indicates the callable scope for the workflow.
     */
    private final String isCallable;

    /**
     * Indicates whether the automated steps can be run in parallel.
     */
    private final Boolean containsParallelSteps;

    /**
     * Indicates the singleton scope for the workflow.
     */
    private final String scope;

    /**
     * Name of the UNIX directory that is used for automatically saving job spool files from the workflow.
     */
    private final String jobsOutputDirectory;

    /**
     * Category of the workflow, which is general, configuration, or provisioning.
     */
    private final String category;

    /**
     * Identifier of the product or component that is being configured through the workflow.
     */
    private final String productID;

    /**
     * Name of the product or component that is being configured through the workflow.
     */
    private final String productName;

    /**
     * Version and release of the product or component that is configured through the workflow.
     */
    private final String productVersion;

    /**
     * Global variable group for the workflow.
     */
    private final String globalVariableGroup;

    /**
     * Indicates whether the simplified format is used for references to instance variables.
     */
    private final Boolean isInstanceVariableWithoutPrefix;

    /**
     * Array of step-definition objects, returned only when the steps attribute is requested.
     */
    private final List<WorkflowStepDefinition> steps;

    /**
     * Array of variable-definition objects, returned only when the variables attribute is requested.
     */
    private final List<WorkflowVariableDefinition> variables;

    /**
     * WorkflowGetResponse Jackson constructor.
     *
     * @param workflowDefaultName             default name for the workflow
     * @param workflowDescription             description of the workflow
     * @param workflowID                      workflow id
     * @param workflowVersion                 version of the workflow definition file
     * @param vendor                          vendor name
     * @param workflowDefinitionFileMD5Value  hash value identifying the workflow definition file
     * @param isCallable                      callable scope for the workflow
     * @param containsParallelSteps           indicates whether automated steps can be run in parallel
     * @param scope                           singleton scope for the workflow
     * @param jobsOutputDirectory             directory used for saving job spool files
     * @param category                        category of the workflow
     * @param productID                       identifier of the product being configured
     * @param productName                     name of the product being configured
     * @param productVersion                  version of the product being configured
     * @param globalVariableGroup             global variable group for the workflow
     * @param isInstanceVariableWithoutPrefix indicates whether the simplified instance variable format is used
     * @param steps                           array of step-definition objects
     * @param variables                       array of variable-definition objects
     */
    @JsonCreator
    public WorkflowGetDefinitionResponse(
            @JsonProperty("workflowDefaultName") final String workflowDefaultName,
            @JsonProperty("workflowDescription") final String workflowDescription,
            @JsonProperty("workflowID") final String workflowID,
            @JsonProperty("workflowVersion") final String workflowVersion,
            @JsonProperty("vendor") final String vendor,
            @JsonProperty("workflowDefinitionFileMD5Value") final String workflowDefinitionFileMD5Value,
            @JsonProperty("isCallable") final String isCallable,
            @JsonProperty("containsParallelSteps") final Boolean containsParallelSteps,
            @JsonProperty("scope") final String scope,
            @JsonProperty("jobsOutputDirectory") final String jobsOutputDirectory,
            @JsonProperty("category") final String category,
            @JsonProperty("productID") final String productID,
            @JsonProperty("productName") final String productName,
            @JsonProperty("productVersion") final String productVersion,
            @JsonProperty("globalVariableGroup") final String globalVariableGroup,
            @JsonProperty("isInstanceVariableWithoutPrefix") final Boolean isInstanceVariableWithoutPrefix,
            @JsonProperty("steps") final List<WorkflowStepDefinition> steps,
            @JsonProperty("variables") final List<WorkflowVariableDefinition> variables) {
        this.workflowDefaultName = orEmpty(workflowDefaultName);
        this.workflowDescription = orEmpty(workflowDescription);
        this.workflowID = orEmpty(workflowID);
        this.workflowVersion = orEmpty(workflowVersion);
        this.vendor = orEmpty(vendor);
        this.workflowDefinitionFileMD5Value = orEmpty(workflowDefinitionFileMD5Value);
        this.isCallable = orEmpty(isCallable);
        this.containsParallelSteps = containsParallelSteps;
        this.scope = orEmpty(scope);
        this.jobsOutputDirectory = orEmpty(jobsOutputDirectory);
        this.category = orEmpty(category);
        this.productID = orEmpty(productID);
        this.productName = orEmpty(productName);
        this.productVersion = orEmpty(productVersion);
        this.globalVariableGroup = orEmpty(globalVariableGroup);
        this.isInstanceVariableWithoutPrefix = isInstanceVariableWithoutPrefix;
        this.steps = orEmpty(steps);
        this.variables = orEmpty(variables);
    }

    /* Null-handling helpers */

    private static String orEmpty(final String value) {
        return value == null ? "" : value;
    }

    private static <T> List<T> orEmpty(final List<T> value) {
        return value == null ? Collections.emptyList() : value;
    }

    /**
     * Retrieve workflowDefaultName value.
     *
     * @return workflowDefaultName value
     */
    public String getWorkflowDefaultName() {
        return workflowDefaultName;
    }

    /**
     * Retrieve workflowDescription value.
     *
     * @return workflowDescription value
     */
    public String getWorkflowDescription() {
        return workflowDescription;
    }

    /**
     * Retrieve workflowID value.
     *
     * @return workflowID value
     */
    public String getWorkflowID() {
        return workflowID;
    }

    /**
     * Retrieve workflowVersion value.
     *
     * @return workflowVersion value
     */
    public String getWorkflowVersion() {
        return workflowVersion;
    }

    /**
     * Retrieve vendor value.
     *
     * @return vendor value
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Retrieve workflowDefinitionFileMD5Value value.
     *
     * @return workflowDefinitionFileMD5Value value
     */
    public String getWorkflowDefinitionFileMD5Value() {
        return workflowDefinitionFileMD5Value;
    }

    /**
     * Retrieve isCallable value.
     *
     * @return isCallable value
     */
    public String getIsCallable() {
        return isCallable;
    }

    /**
     * Retrieve containsParallelSteps value.
     *
     * @return containsParallelSteps value
     */
    public Boolean getContainsParallelSteps() {
        return containsParallelSteps;
    }

    /**
     * Retrieve scope value.
     *
     * @return scope value
     */
    public String getScope() {
        return scope;
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
     * Retrieve category value.
     *
     * @return category value
     */
    public String getCategory() {
        return category;
    }

    /**
     * Retrieve productID value.
     *
     * @return productID value
     */
    public String getProductID() {
        return productID;
    }

    /**
     * Retrieve productName value.
     *
     * @return productName value
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Retrieve productVersion value.
     *
     * @return productVersion value
     */
    public String getProductVersion() {
        return productVersion;
    }

    /**
     * Retrieve globalVariableGroup value.
     *
     * @return globalVariableGroup value
     */
    public String getGlobalVariableGroup() {
        return globalVariableGroup;
    }

    /**
     * Retrieve isInstanceVariableWithoutPrefix value.
     *
     * @return isInstanceVariableWithoutPrefix value
     */
    public Boolean getIsInstanceVariableWithoutPrefix() {
        return isInstanceVariableWithoutPrefix;
    }

    /**
     * Retrieve steps value.
     *
     * @return steps value
     */
    public List<WorkflowStepDefinition> getSteps() {
        return steps;
    }

    /**
     * Retrieve variables value.
     *
     * @return variables value
     */
    public List<WorkflowVariableDefinition> getVariables() {
        return variables;
    }

    /**
     * Return string value representing WorkflowGetResponse object.
     *
     * @return string representation of WorkflowGetResponse
     */
    @Override
    public String toString() {
        return "WorkflowGetResponse{" +
                "workflowDefaultName='" + workflowDefaultName + '\'' +
                ", workflowDescription='" + workflowDescription + '\'' +
                ", workflowID='" + workflowID + '\'' +
                ", workflowVersion='" + workflowVersion + '\'' +
                ", vendor='" + vendor + '\'' +
                ", workflowDefinitionFileMD5Value='" + workflowDefinitionFileMD5Value + '\'' +
                ", isCallable='" + isCallable + '\'' +
                ", containsParallelSteps=" + containsParallelSteps +
                ", scope='" + scope + '\'' +
                ", jobsOutputDirectory='" + jobsOutputDirectory + '\'' +
                ", category='" + category + '\'' +
                ", productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", productVersion='" + productVersion + '\'' +
                ", globalVariableGroup='" + globalVariableGroup + '\'' +
                ", isInstanceVariableWithoutPrefix=" + isInstanceVariableWithoutPrefix +
                ", steps=" + steps +
                ", variables=" + variables +
                '}';
    }

}
