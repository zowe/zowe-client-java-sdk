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
import com.fasterxml.jackson.annotation.JsonProperty;
import zowe.client.sdk.zosvariables.model.SystemVariable;

/**
 * Response object for z/OSMF get system variables processing.
 */
public final class SystemVariablesResponse {

    private final SystemVariable[] systemVariableList;
    private final SystemVariable[] systemSymbolList;

    /**
     * Jackson constructor for SystemVariablesResponse.
     *
     * @param systemVariableList system variable list
     * @param systemSymbolList   system symbol list
     */
    @JsonCreator
    public SystemVariablesResponse(
            @JsonProperty("system-variable-list") final SystemVariable[] systemVariableList,
            @JsonProperty("system-symbol-list") final SystemVariable[] systemSymbolList
    ) {
        this.systemVariableList = systemVariableList == null ? new SystemVariable[0] : systemVariableList;
        this.systemSymbolList = systemSymbolList == null ? new SystemVariable[0] : systemSymbolList;
    }

    /**
     * Retrieve system variable list.
     *
     * @return system variable list
     */
    public SystemVariable[] getSystemVariableList() {
        return systemVariableList;
    }

    /**
     * Retrieve system symbol list.
     *
     * @return system symbol list
     */
    public SystemVariable[] getSystemSymbolList() {
        return systemSymbolList;
    }

}