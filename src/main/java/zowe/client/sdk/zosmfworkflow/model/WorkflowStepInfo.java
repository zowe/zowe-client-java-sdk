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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Collections;
import java.util.List;

/**
 * Base step-info type returned for a workflow properties request.
 * <p>
 * The concrete step type is deduced from the JSON fields that are present, as a workflow step is
 * either a template step, a REST step, or a calling step.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Shantanu Danej
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WorkflowTemplateStepInfo.class),
        @JsonSubTypes.Type(value = WorkflowRestStepInfo.class),
        @JsonSubTypes.Type(value = WorkflowCallingStepInfo.class)
})
public abstract class WorkflowStepInfo {

    /**
     * Name of the step.
     */
    private final String name;

    /**
     * Step title.
     */
    private final String title;

    /**
     * Step description.
     */
    private final String description;

    /**
     * State of the step.
     */
    private final String state;

    /**
     * The step number, indicating the sequence in which steps are to be performed.
     */
    private final String stepNumber;

    /**
     * Indicates whether the step is optional.
     */
    private final Boolean optional;

    /**
     * Indicates whether the step can be performed automatically when all prerequisite steps are completed.
     */
    private final Boolean autoEnable;

    /**
     * Names of the steps that must be completed before this step can be performed.
     */
    private final List<String> prereqStep;

    /**
     * Indicates whether the step was added manually to the workflow.
     */
    private final Boolean userDefined;

    /**
     * The user ID under which the step is to be performed.
     */
    private final String runAsUser;

    /**
     * Indicates whether the runAsUser ID value can change during processing of the workflow.
     */
    private final Boolean runAsUserDynamic;

    /**
     * Indicates whether this step is a REST API step.
     */
    private final Boolean isRestStep;

    /**
     * User ID of the step owner.
     */
    private final String owner;

    /**
     * Step assignees. One or more user IDs that are assigned to the step.
     */
    private final String assignees;

    /**
     * The type of skills that are required to perform the step.
     */
    private final String skills;

    /**
     * The relative difficulty of the step compared to other steps within this workflow.
     */
    private final String weight;

    /**
     * Indicates whether this step calls another workflow.
     */
    private final Boolean hasCalledWorkflow;

    /**
     * Indicates whether this step is a conditional step.
     */
    private final Boolean isConditionStep;

    /**
     * For a parent step, the nested array of step-info objects. For a leaf step, this property is null.
     */
    private final List<WorkflowStepInfo> steps;

    /**
     * WorkflowStepInfo constructor.
     *
     * @param name              step name
     * @param title             step title
     * @param description       step description
     * @param state             state of the step
     * @param stepNumber        step number
     * @param optional          indicates whether the step is optional
     * @param autoEnable        indicates whether the step can be performed automatically
     * @param prereqStep        names of prerequisite steps
     * @param userDefined       indicates whether the step was added manually
     * @param runAsUser         user ID under which the step is performed
     * @param runAsUserDynamic  indicates whether the runAsUser ID can change
     * @param isRestStep        indicates whether this step is a REST API step
     * @param owner             user ID of the step owner
     * @param assignees         step assignees
     * @param skills            skills required to perform the step
     * @param weight            relative difficulty of the step
     * @param hasCalledWorkflow indicates whether this step calls another workflow
     * @param isConditionStep   indicates whether this step is a conditional step
     * @param steps             nested step-info objects
     */
    protected WorkflowStepInfo(
            final String name,
            final String title,
            final String description,
            final String state,
            final String stepNumber,
            final Boolean optional,
            final Boolean autoEnable,
            final List<String> prereqStep,
            final Boolean userDefined,
            final String runAsUser,
            final Boolean runAsUserDynamic,
            final Boolean isRestStep,
            final String owner,
            final String assignees,
            final String skills,
            final String weight,
            final Boolean hasCalledWorkflow,
            final Boolean isConditionStep,
            final List<WorkflowStepInfo> steps) {
        this.name = orEmpty(name);
        this.title = orEmpty(title);
        this.description = orEmpty(description);
        this.state = orEmpty(state);
        this.stepNumber = orEmpty(stepNumber);
        this.optional = optional;
        this.autoEnable = autoEnable;
        this.prereqStep = orEmpty(prereqStep);
        this.userDefined = userDefined;
        this.runAsUser = orEmpty(runAsUser);
        this.runAsUserDynamic = runAsUserDynamic;
        this.isRestStep = isRestStep;
        this.owner = orEmpty(owner);
        this.assignees = orEmpty(assignees);
        this.skills = orEmpty(skills);
        this.weight = orEmpty(weight);
        this.hasCalledWorkflow = hasCalledWorkflow;
        this.isConditionStep = isConditionStep;
        this.steps = orEmpty(steps);
    }

    /* Null-handling helpers */

    /**
     * Return the supplied value, or an empty string when null.
     *
     * @param value string value
     * @return non-null string value
     */
    protected static String orEmpty(final String value) {
        return value == null ? "" : value;
    }

    /**
     * Return the supplied list, or an empty list when null.
     *
     * @param value list value
     * @param <T>   list element type
     * @return non-null list value
     */
    protected static <T> List<T> orEmpty(final List<T> value) {
        return value == null ? Collections.emptyList() : value;
    }

    /**
     * Retrieve name value.
     *
     * @return name value
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve title value.
     *
     * @return title value
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieve description value.
     *
     * @return description value
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieve state value.
     *
     * @return state value
     */
    public String getState() {
        return state;
    }

    /**
     * Retrieve stepNumber value.
     *
     * @return stepNumber value
     */
    public String getStepNumber() {
        return stepNumber;
    }

    /**
     * Retrieve optional value.
     *
     * @return optional value
     */
    public Boolean getOptional() {
        return optional;
    }

    /**
     * Retrieve autoEnable value.
     *
     * @return autoEnable value
     */
    public Boolean getAutoEnable() {
        return autoEnable;
    }

    /**
     * Retrieve prereqStep value.
     *
     * @return prereqStep value
     */
    public List<String> getPrereqStep() {
        return prereqStep;
    }

    /**
     * Retrieve userDefined value.
     *
     * @return userDefined value
     */
    public Boolean getUserDefined() {
        return userDefined;
    }

    /**
     * Retrieve runAsUser value.
     *
     * @return runAsUser value
     */
    public String getRunAsUser() {
        return runAsUser;
    }

    /**
     * Retrieve runAsUserDynamic value.
     *
     * @return runAsUserDynamic value
     */
    public Boolean getRunAsUserDynamic() {
        return runAsUserDynamic;
    }

    /**
     * Retrieve isRestStep value.
     *
     * @return isRestStep value
     */
    public Boolean getIsRestStep() {
        return isRestStep;
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
     * Retrieve assignees value.
     *
     * @return assignees value
     */
    public String getAssignees() {
        return assignees;
    }

    /**
     * Retrieve skills value.
     *
     * @return skills value
     */
    public String getSkills() {
        return skills;
    }

    /**
     * Retrieve weight value.
     *
     * @return weight value
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Retrieve hasCalledWorkflow value.
     *
     * @return hasCalledWorkflow value
     */
    public Boolean getHasCalledWorkflow() {
        return hasCalledWorkflow;
    }

    /**
     * Retrieve isConditionStep value.
     *
     * @return isConditionStep value
     */
    public Boolean getIsConditionStep() {
        return isConditionStep;
    }

    /**
     * Retrieve steps value.
     *
     * @return steps value
     */
    public List<WorkflowStepInfo> getSteps() {
        return steps;
    }

}
