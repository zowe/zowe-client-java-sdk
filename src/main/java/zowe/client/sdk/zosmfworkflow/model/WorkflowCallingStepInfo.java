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

import java.util.List;

/**
 * Calling step-info type returned for a workflow properties request. A calling step calls another
 * workflow for execution.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Shantanu Danej
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowCallingStepInfo extends WorkflowStepInfo {

    /**
     * Key of the called workflow instance.
     */
    private final String calledInstanceKey;

    /**
     * Scope of the called workflow instance.
     */
    private final String calledInstanceScope;

    /**
     * URI path of the called workflow instance.
     */
    private final String calledInstanceURI;

    /**
     * Workflow ID of a workflow definition file used to locate an existing workflow instance.
     */
    private final String calledWorkflowID;

    /**
     * Workflow version of a workflow definition file used to locate an existing workflow instance.
     */
    private final String calledWorkflowVersion;

    /**
     * The 128-bit hash value of a workflow definition file used to locate an existing workflow instance.
     */
    private final String calledWorkflowMD5;

    /**
     * Describes the workflow to be called, from the point of view of the calling workflow.
     */
    private final String calledWorkflowDescription;

    /**
     * Name of the workflow definition file used to create a new workflow if an existing instance is not found.
     */
    private final String calledWorkflowDefinitionFile;

    /**
     * WorkflowCallingStepInfo Jackson constructor.
     *
     * @param name                         step name
     * @param title                        step title
     * @param description                  step description
     * @param state                        state of the step
     * @param stepNumber                   step number
     * @param optional                     indicates whether the step is optional
     * @param autoEnable                   indicates whether the step can be performed automatically
     * @param prereqStep                   names of prerequisite steps
     * @param userDefined                  indicates whether the step was added manually
     * @param runAsUser                    user ID under which the step is performed
     * @param runAsUserDynamic             indicates whether the runAsUser ID can change
     * @param isRestStep                   indicates whether this step is a REST API step
     * @param owner                        user ID of the step owner
     * @param assignees                    step assignees
     * @param skills                       skills required to perform the step
     * @param weight                       relative difficulty of the step
     * @param hasCalledWorkflow            indicates whether this step calls another workflow
     * @param isConditionStep              indicates whether this step is a conditional step
     * @param steps                        nested step-info objects
     * @param calledInstanceKey            key of the called workflow instance
     * @param calledInstanceScope          scope of the called workflow instance
     * @param calledInstanceURI            URI path of the called workflow instance
     * @param calledWorkflowID             workflow ID used to locate an existing workflow instance
     * @param calledWorkflowVersion        workflow version used to locate an existing workflow instance
     * @param calledWorkflowMD5            hash value used to locate an existing workflow instance
     * @param calledWorkflowDescription    description of the workflow to be called
     * @param calledWorkflowDefinitionFile name of the workflow definition file for the called workflow
     */
    @JsonCreator
    public WorkflowCallingStepInfo(
            @JsonProperty("name") final String name,
            @JsonProperty("title") final String title,
            @JsonProperty("description") final String description,
            @JsonProperty("state") final String state,
            @JsonProperty("stepNumber") final String stepNumber,
            @JsonProperty("optional") final Boolean optional,
            @JsonProperty("autoEnable") final Boolean autoEnable,
            @JsonProperty("prereqStep") final List<String> prereqStep,
            @JsonProperty("userDefined") final Boolean userDefined,
            @JsonProperty("runAsUser") final String runAsUser,
            @JsonProperty("runAsUserDynamic") final Boolean runAsUserDynamic,
            @JsonProperty("isRestStep") final Boolean isRestStep,
            @JsonProperty("owner") final String owner,
            @JsonProperty("assignees") final String assignees,
            @JsonProperty("skills") final String skills,
            @JsonProperty("weight") final String weight,
            @JsonProperty("hasCalledWorkflow") final Boolean hasCalledWorkflow,
            @JsonProperty("isConditionStep") final Boolean isConditionStep,
            @JsonProperty("steps") final List<WorkflowStepInfo> steps,
            @JsonProperty("calledInstanceKey") final String calledInstanceKey,
            @JsonProperty("calledInstanceScope") final String calledInstanceScope,
            @JsonProperty("calledInstanceURI") final String calledInstanceURI,
            @JsonProperty("calledWorkflowID") final String calledWorkflowID,
            @JsonProperty("calledWorkflowVersion") final String calledWorkflowVersion,
            @JsonProperty("calledWorkflowMD5") final String calledWorkflowMD5,
            @JsonProperty("calledWorkflowDescription") final String calledWorkflowDescription,
            @JsonProperty("calledWorkflowDefinitionFile") final String calledWorkflowDefinitionFile) {
        super(name, title, description, state, stepNumber, optional, autoEnable, prereqStep, userDefined,
                runAsUser, runAsUserDynamic, isRestStep, owner, assignees, skills, weight, hasCalledWorkflow,
                isConditionStep, steps);
        this.calledInstanceKey = orEmpty(calledInstanceKey);
        this.calledInstanceScope = orEmpty(calledInstanceScope);
        this.calledInstanceURI = orEmpty(calledInstanceURI);
        this.calledWorkflowID = orEmpty(calledWorkflowID);
        this.calledWorkflowVersion = orEmpty(calledWorkflowVersion);
        this.calledWorkflowMD5 = orEmpty(calledWorkflowMD5);
        this.calledWorkflowDescription = orEmpty(calledWorkflowDescription);
        this.calledWorkflowDefinitionFile = orEmpty(calledWorkflowDefinitionFile);
    }

    /**
     * Retrieve calledInstanceKey value.
     *
     * @return calledInstanceKey value
     */
    public String getCalledInstanceKey() {
        return calledInstanceKey;
    }

    /**
     * Retrieve calledInstanceScope value.
     *
     * @return calledInstanceScope value
     */
    public String getCalledInstanceScope() {
        return calledInstanceScope;
    }

    /**
     * Retrieve calledInstanceURI value.
     *
     * @return calledInstanceURI value
     */
    public String getCalledInstanceURI() {
        return calledInstanceURI;
    }

    /**
     * Retrieve calledWorkflowID value.
     *
     * @return calledWorkflowID value
     */
    public String getCalledWorkflowID() {
        return calledWorkflowID;
    }

    /**
     * Retrieve calledWorkflowVersion value.
     *
     * @return calledWorkflowVersion value
     */
    public String getCalledWorkflowVersion() {
        return calledWorkflowVersion;
    }

    /**
     * Retrieve calledWorkflowMD5 value.
     *
     * @return calledWorkflowMD5 value
     */
    public String getCalledWorkflowMD5() {
        return calledWorkflowMD5;
    }

    /**
     * Retrieve calledWorkflowDescription value.
     *
     * @return calledWorkflowDescription value
     */
    public String getCalledWorkflowDescription() {
        return calledWorkflowDescription;
    }

    /**
     * Retrieve calledWorkflowDefinitionFile value.
     *
     * @return calledWorkflowDefinitionFile value
     */
    public String getCalledWorkflowDefinitionFile() {
        return calledWorkflowDefinitionFile;
    }

    /**
     * Return a string value representing a WorkflowCallingStepInfo object.
     *
     * @return string representation of WorkflowCallingStepInfo
     */
    @Override
    public String toString() {
        return "WorkflowCallingStepInfo{" +
                "name='" + getName() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", stepNumber='" + getStepNumber() + '\'' +
                ", state='" + getState() + '\'' +
                ", calledWorkflowID='" + calledWorkflowID + '\'' +
                ", calledInstanceURI='" + calledInstanceURI + '\'' +
                ", calledWorkflowDefinitionFile='" + calledWorkflowDefinitionFile + '\'' +
                '}';
    }

}
