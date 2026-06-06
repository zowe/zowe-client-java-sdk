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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * z/OSMF system variable or system symbol model.
 */
public final class SystemVariable {

    private final String name;
    private final String value;
    private final String description;

    /**
     * Jackson constructor for SystemVariable.
     *
     * @param name        variable or symbol name
     * @param value       variable or symbol value
     * @param description variable or symbol description
     */
    @JsonCreator
    public SystemVariable(
            @JsonProperty("name") final String name,
            @JsonProperty("value") final String value,
            @JsonProperty("description") @JsonAlias("Description") final String description
    ) {
        this.name = name == null ? "" : name;
        this.value = value == null ? "" : value;
        this.description = description == null ? "" : description;
    }

    /**
     * Retrieve variable or symbol name.
     *
     * @return variable or symbol name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve variable or symbol value.
     *
     * @return variable or symbol value
     */
    public String getValue() {
        return value;
    }

    /**
     * Retrieve variable or symbol description.
     *
     * @return variable or symbol description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return string value representing SystemVariable object.
     *
     * @return string representation of SystemVariable
     */
    @Override
    public String toString() {
        return "SystemVariable{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}