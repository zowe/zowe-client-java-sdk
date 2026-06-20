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
 * Workflow variable-info information returned for a workflow properties request.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowVariableInfo {

    /**
     * Name of the variable.
     */
    private final String name;

    /**
     * Variable scope, which is either instance or global.
     */
    private final String scope;

    /**
     * Type of variable, which is one of boolean, string, number, date, time, or array.
     */
    private final String type;

    /**
     * Variable value.
     */
    private final String value;

    /**
     * Public or private.
     */
    private final String visibility;

    /**
     * WorkflowVariableInfo Jackson constructor.
     *
     * @param name       variable name
     * @param scope      variable scope
     * @param type       type of variable
     * @param value      variable value
     * @param visibility public or private
     */
    @JsonCreator
    public WorkflowVariableInfo(
            @JsonProperty("name") final String name,
            @JsonProperty("scope") final String scope,
            @JsonProperty("type") final String type,
            @JsonProperty("value") final String value,
            @JsonProperty("visibility") final String visibility) {
        this.name = orEmpty(name);
        this.scope = orEmpty(scope);
        this.type = orEmpty(type);
        this.value = orEmpty(value);
        this.visibility = orEmpty(visibility);
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
     * Retrieve type value.
     *
     * @return type value
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieve value value.
     *
     * @return value value
     */
    public String getValue() {
        return value;
    }

    /**
     * Retrieve visibility value.
     *
     * @return visibility value
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Return string value representing WorkflowVariableInfo object.
     *
     * @return string representation of WorkflowVariableInfo
     */
    @Override
    public String toString() {
        return "WorkflowVariableInfo{" +
                "name='" + name + '\'' +
                ", scope='" + scope + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", visibility='" + visibility + '\'' +
                '}';
    }

}
