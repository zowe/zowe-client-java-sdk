/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.input;

/**
 * Source type for z/OSMF system variables API.
 */
public enum SystemVariableSource {

    /**
     * z/OSMF variable source.
     */
    VARIABLE("variable"),

    /**
     * z/OS system symbol source.
     */
    SYMBOL("symbol");

    private final String value;

    /**
     * SystemVariableSource constructor.
     *
     * @param value query parameter value
     */
    SystemVariableSource(final String value) {
        this.value = value;
    }

    /**
     * Retrieve source query parameter value.
     *
     * @return source query parameter value
     */
    public String getValue() {
        return value;
    }

}