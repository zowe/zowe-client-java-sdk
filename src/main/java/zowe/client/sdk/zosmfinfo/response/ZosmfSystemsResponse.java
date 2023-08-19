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

import java.util.Optional;
import java.util.OptionalLong;

/**
 * API response for list systems defined to z/OSMF.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfSystemsResponse {

    /**
     * Total items returned.
     */
    private final OptionalLong numRows;

    /**
     * Properties of each defined system.
     */
    private final Optional<DefinedSystem[]> definedSystems;

    /**
     * ZosmfListDefinedSystemsResponse constructor
     *
     * @param builder ZosmfListDefinedSystemsResponse.Builder Object
     * @author Frank Giordano
     */
    private ZosmfSystemsResponse(final Builder builder) {
        if (builder.numRows == null) {
            this.numRows = OptionalLong.empty();
        } else {
            this.numRows = OptionalLong.of(builder.numRows);
        }
        this.definedSystems = Optional.ofNullable(builder.definedSystems);
    }

    /**
     * Retrieve definedSystems specified
     *
     * @return definedSystems value
     */
    public Optional<DefinedSystem[]> getDefinedSystems() {
        return definedSystems;
    }

    /**
     * Retrieve numRows specified
     *
     * @return numRows value
     */
    public OptionalLong getNumRows() {
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
                ", definedSystems=" + definedSystems +
                '}';
    }

    /**
     * Builder class for ZosmfSystemsResponse
     */
    public static class Builder {

        private Long numRows;
        private DefinedSystem[] definedSystems;

        public Builder definedSystems(final DefinedSystem[] definedSystems) {
            this.definedSystems = definedSystems;
            return this;
        }

        public Builder numRows(final Long numRows) {
            this.numRows = numRows;
            return this;
        }

        public ZosmfSystemsResponse build() {
            return new ZosmfSystemsResponse(this);
        }

    }

}
