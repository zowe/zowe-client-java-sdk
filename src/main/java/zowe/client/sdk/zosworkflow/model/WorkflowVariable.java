/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Workflow variable name/value pair.
 * <p><a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-create-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowVariable {

    /**
     * Variable name.
     */
    private final String name;

    /**
     * Variable value.
     */
    private final String value;

    /**
     * WorkflowVariable constructor.
     *
     * @param name  variable name
     * @param value variable value
     */
    public WorkflowVariable(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Retrieve name specified.
     *
     * @return name value
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve value specified.
     *
     * @return value value
     */
    public String getValue() {
        return value;
    }

    /**
     * Return string value representing WorkflowVariable object.
     *
     * @return string representation of WorkflowVariable
     */
    @Override
    public String toString() {
        return "WorkflowVariable{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
