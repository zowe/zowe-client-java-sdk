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
import java.util.Collections;
import java.util.List;

/**
 * API response for get system variables.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-system-variables"> z/OSMF REST API</a>
 *
 * @author Adithe Das
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class VariableGetResponse {

    /**
     * List of system variables.
     */
    private final List<VariableResponse> systemVariableList;

    /**
     * List of system symbols.
     */
    private final List<VariableResponse> systemSymbolList;

    /**
     * VariableGetResponse Jackson constructor.
     *
     * @param systemVariableList list of system variables
     * @param systemSymbolList list of system symbols
     */
    @JsonCreator
    public VariableGetResponse(
            @JsonProperty ("system-variable-list")final List<VariableResponse> systemVariableList,
            @JsonProperty("system-symbol-list") final List<VariableResponse> systemSymbolList) {
        this.systemVariableList = systemVariableList == null ? Collections.emptyList(): systemVariableList;
        this.systemSymbolList = systemSymbolList == null ? Collections.emptyList(): systemSymbolList;
    }

    /**
     * Retrieve system variable list.
     *
     * @return system variable list
     */
    public List<VariableResponse> getSystemVariableList() {
        return systemVariableList;
    }

    /**
     * Retrieve system symbol list.
     *
     * @return system symbol list
     */
    public List<VariableResponse> getSystemSymbolList() {
        return systemSymbolList;
    }

    /**
     * Return string value representing VariableGetResponse object.
     *
     * @return string representation of VariableGetResponse
     */
    @Override
    public String toString() {
        return "VariableGetResponse{" +
                "systemVariableList=" + systemVariableList +
                ", systemSymbolList=" + systemSymbolList + '}';
    }

}
