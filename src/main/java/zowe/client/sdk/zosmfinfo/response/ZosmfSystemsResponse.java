/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import zowe.client.sdk.zosmfinfo.model.DefinedSystem;

import java.util.Arrays;

/**
 * API response for list systems defined to z/OSMF.
 *
 * @author Frank Giordano
 * @version 6.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ZosmfSystemsResponse {

    /**
     * Total items returned.
     */
    private final Long numRows;

    /**
     * Properties of each defined system.
     */
    private final DefinedSystem[] definedSystems;

    /**
     * Jackson constructor for ZosmfSystemsResponse
     *
     * @param numRows        total number of rows returned
     * @param definedSystems array of defined systems
     */
    @JsonCreator
    public ZosmfSystemsResponse(
            @JsonProperty("numRows") Long numRows,
            @JsonProperty("items") DefinedSystem[] definedSystems
    ) {
        this.numRows = (numRows == null) ? 0L : numRows;
        this.definedSystems = definedSystems;
    }

    /**
     * Retrieve definedSystems specified
     *
     * @return definedSystems value
     */
    public DefinedSystem[] getDefinedSystems() {
        return definedSystems;
    }

    /**
     * Retrieve numRows specified
     *
     * @return numRows value
     */
    public Long getNumRows() {
        return numRows;
    }

    /**
     * Return string value representing ZosmfSystemsResponse object
     *
     * @return string representation of ZosmfSystemsResponse
     */
    @Override
    public String toString() {
        return "ZosmfSystemsResponse{" +
                "numRows=" + numRows +
                ", definedSystems=" + Arrays.toString(definedSystems) +
                '}';
    }

}
