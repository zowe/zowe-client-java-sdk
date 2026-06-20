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
 * Workflow variable-reference information referenced by a workflow step.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowVariableReference {

    /**
     * Name of the variable.
     */
    private final String name;

    /**
     * Variable scope, which is either instance or global.
     */
    private final String scope;

    /**
     * WorkflowVariableReference Jackson constructor.
     *
     * @param name  variable name
     * @param scope variable scope
     */
    @JsonCreator
    public WorkflowVariableReference(
            @JsonProperty("name") final String name,
            @JsonProperty("scope") final String scope) {
        this.name = orEmpty(name);
        this.scope = orEmpty(scope);
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
     * Return string value representing WorkflowVariableReference object.
     *
     * @return string representation of WorkflowVariableReference
     */
    @Override
    public String toString() {
        return "WorkflowVariableReference{" +
                "name='" + name + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }

}
