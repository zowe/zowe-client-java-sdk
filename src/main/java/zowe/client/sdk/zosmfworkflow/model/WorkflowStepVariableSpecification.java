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
 * Workflow step variable-specification information returned for a step definition.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-retrieve-workflow-definition">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowStepVariableSpecification {

    /**
     * Name of the variable.
     */
    private final String name;

    /**
     * Variable scope, which is either instance or global.
     */
    private final String scope;

    /**
     * Indicates whether the variable is required.
     */
    private final Boolean required;

    /**
     * WorkflowStepVariableSpecification Jackson constructor.
     *
     * @param name     variable name
     * @param scope    variable scope
     * @param required indicates whether the variable is required
     */
    @JsonCreator
    public WorkflowStepVariableSpecification(
            @JsonProperty("name") final String name,
            @JsonProperty("scope") final String scope,
            @JsonProperty("required") final Boolean required) {
        this.name = orEmpty(name);
        this.scope = orEmpty(scope);
        this.required = required;
    }

    /* Null-handling helpers */

    private static String orEmpty(final String value) {
        return value == null ? "" : value;
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
     * Retrieve scope value.
     *
     * @return scope value
     */
    public String getScope() {
        return scope;
    }

    /**
     * Retrieve the required value.
     *
     * @return required value
     */
    public Boolean getRequired() {
        return required;
    }

    /**
     * Return a string value representing a WorkflowStepVariableSpecification object.
     *
     * @return string representation of WorkflowStepVariableSpecification
     */
    @Override
    public String toString() {
        return "WorkflowStepVariableSpecification{" +
                "name='" + name + '\'' +
                ", scope='" + scope + '\'' +
                ", required=" + required +
                '}';
    }

}
