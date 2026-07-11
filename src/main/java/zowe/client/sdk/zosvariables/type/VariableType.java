/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.type;

/**
 * Supported variable source types.
 *
 * @author Adithe Das
 * @version 7.0
 */
public enum VariableType {

    /**
     * z/OS variable.
     */
    VARIABLE("variable"),

    /**
     * z/OSMF symbol.
     */
    SYMBOL("symbol");

    /**
     * REST API value.
     */
    private final String value;

    VariableType(final String value) {
        this.value = value;
    }

    /**
     * Retrieve REST value.
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

}
