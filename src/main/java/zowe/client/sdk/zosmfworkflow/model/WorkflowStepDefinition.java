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

import java.util.Collections;
import java.util.List;

/**
 * Workflow step-definition information returned for a workflow definition.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-retrieve-workflow-definition">z/OSMF REST API</a>
 *
 * @author Shantanu Danej
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowStepDefinition {

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
     * Names of the steps that must be completed before this step can be performed.
     */
    private final List<String> prereqStep;

    /**
     * Indicates whether the step is optional.
     */
    private final Boolean optional;

    /**
     * For a parent step, the array of nested step-definition objects. For a leaf step, this property is null.
     */
    private final List<WorkflowStepDefinition> steps;

    /**
     * Array of variable-specification-info objects referenced by the step.
     */
    private final List<WorkflowStepVariableSpecification> variableSpecifications;

    /**
     * WorkflowStepDefinition Jackson constructor.
     *
     * @param name                   step name
     * @param title                  step title
     * @param description            step description
     * @param prereqStep             names of prerequisite steps
     * @param optional               indicates whether the step is optional
     * @param steps                  nested step-definition objects
     * @param variableSpecifications variable-specification-info objects referenced by the step
     */
    @JsonCreator
    public WorkflowStepDefinition(
            @JsonProperty("name") final String name,
            @JsonProperty("title") final String title,
            @JsonProperty("description") final String description,
            @JsonProperty("prereqStep") final List<String> prereqStep,
            @JsonProperty("optional") final Boolean optional,
            @JsonProperty("steps") final List<WorkflowStepDefinition> steps,
            @JsonProperty("variable-specifications") final List<WorkflowStepVariableSpecification> variableSpecifications) {
        this.name = orEmpty(name);
        this.title = orEmpty(title);
        this.description = orEmpty(description);
        this.prereqStep = orEmpty(prereqStep);
        this.optional = optional;
        this.steps = orEmpty(steps);
        this.variableSpecifications = orEmpty(variableSpecifications);
    }

    /* Null-handling helpers */

    private static String orEmpty(final String value) {
        return value == null ? "" : value;
    }

    private static <T> List<T> orEmpty(final List<T> value) {
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
     * Retrieve prereqStep value.
     *
     * @return prereqStep value
     */
    public List<String> getPrereqStep() {
        return prereqStep;
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
     * Retrieve steps value.
     *
     * @return steps value
     */
    public List<WorkflowStepDefinition> getSteps() {
        return steps;
    }

    /**
     * Retrieve variableSpecifications value.
     *
     * @return variableSpecifications value
     */
    public List<WorkflowStepVariableSpecification> getVariableSpecifications() {
        return variableSpecifications;
    }

    /**
     * Return a string value representing a WorkflowStepDefinition object.
     *
     * @return string representation of WorkflowStepDefinition
     */
    @Override
    public String toString() {
        return "WorkflowStepDefinition{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prereqStep=" + prereqStep +
                ", optional=" + optional +
                ", steps=" + steps +
                ", variableSpecifications=" + variableSpecifications +
                '}';
    }

}
