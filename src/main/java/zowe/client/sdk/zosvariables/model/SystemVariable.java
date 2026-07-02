/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * z/OSMF system variable name, value, and optional description.
 *
 * @author Muhammad Imran
 * @version 7.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "value", "description"})
public final class SystemVariable {

    /**
     * Variable name (required).
     */
    private final String name;

    /**
     * Variable value (required).
     */
    private final String value;

    /**
     * Variable description (optional).
     */
    private final String description;

    /**
     * SystemVariable constructor.
     *
     * @param name        variable name (required)
     * @param value       variable value (required)
     * @param description variable description (optional)
     * @author Muhammad Imran
     */
    @JsonCreator
    public SystemVariable(
            @JsonProperty("name") final String name,
            @JsonProperty("value") final String value,
            @JsonProperty("description") final String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    /**
     * SystemVariable constructor without a description.
     *
     * @param name  variable name (required)
     * @param value variable value (required)
     * @author Muhammad Imran
     */
    public SystemVariable(final String name, final String value) {
        this(name, value, null);
    }

    /**
     * Retrieve name specified.
     *
     * @return name value
     * @author Muhammad Imran
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve value specified.
     *
     * @return value value
     * @author Muhammad Imran
     */
    public String getValue() {
        return value;
    }

    /**
     * Retrieve description specified.
     *
     * @return description value
     * @author Muhammad Imran
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return string value representing SystemVariable object.
     *
     * @return string representation of SystemVariable
     * @author Muhammad Imran
     */
    @Override
    public String toString() {
        return "SystemVariable{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    private static String orEmpty(final String s) {
        return s == null ? "" : s;
    }

}
