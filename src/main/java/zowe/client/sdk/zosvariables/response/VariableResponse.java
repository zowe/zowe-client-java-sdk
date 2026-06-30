/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API response for a z/OS system variable.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-system-variables"> z/OSMF REST API</a>
 *
 * @author Adithe Das
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class VariableResponse {
    /**
     * Variable or symbol name.
     */
    private final String name;

    /**
     * Variable or symbol value.
     */
    private final String value;

    /**
     * Variable description.
     */
    private final String description;

    /**
     * VariableResponse Jackson constructor.
     *
     * @param name variable name
     * @param value variable value
     * @param description variable description
     */
    @JsonCreator
    public VariableResponse(@JsonProperty("name") final String name, @JsonProperty("value") final String value, @JsonProperty("description") final String description) {
        this.name = name == null ? "" : name;
        this.value = value == null ? "" : value;
        this.description = description == null ? "" : description;
    }

    /**
     * Retrieve variable name.
     *
     * @return variable name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve variable value.
     *
     * @return variable value
     */
    public String getValue() {
        return value;
    }

    /**
     * Retrieve variable description.
     *
     * @return variable description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return string value representing VariableResponse object.
     *
     * @return string representation of VariableResponse
     */
    @Override
    public String toString() {
        return "VariableResponse{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' + '}';
    }

}
