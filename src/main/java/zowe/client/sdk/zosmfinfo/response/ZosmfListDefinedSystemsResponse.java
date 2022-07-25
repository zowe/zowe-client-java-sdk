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

/**
 * API response for list systems defined to z/OSMF.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZosmfListDefinedSystemsResponse {

    /**
     * Total items returned.
     */
    private final Optional<Long> numRows;

    /**
     * Properties of each defined system.
     */
    private final Optional<DefinedSystem[]> definedSystems;

    /**
     * ZosmfListDefinedSystemsResponse Constructor.
     *
     * @param builder ZosmfListDefinedSystemsResponse.Builder Object
     * @author Frank Giordano
     */
    private ZosmfListDefinedSystemsResponse(ZosmfListDefinedSystemsResponse.Builder builder) {
        this.numRows = Optional.ofNullable(builder.numRows);
        this.definedSystems = Optional.ofNullable(builder.definedSystems);
    }

    /**
     * Retrieve definedSystems specified
     *
     * @return definedSystems value
     * @author Frank Giordano
     */
    public Optional<DefinedSystem[]> getDefinedSystems() {
        return definedSystems;
    }

    /**
     * Retrieve numRows specified
     *
     * @return numRows value
     * @author Frank Giordano
     */
    public Optional<Long> getNumRows() {
        return numRows;
    }

    @Override
    public String toString() {
        return "ZosmfListDefinedSystemsResponse{" +
                "numRows=" + numRows +
                ", definedSystems=" + definedSystems +
                '}';
    }

    public static class Builder {

        private Long numRows;
        private DefinedSystem[] definedSystems;

        public ZosmfListDefinedSystemsResponse build() {
            return new ZosmfListDefinedSystemsResponse(this);
        }

        public ZosmfListDefinedSystemsResponse.Builder definedSystems(DefinedSystem[] definedSystems) {
            this.definedSystems = definedSystems;
            return this;
        }

        public ZosmfListDefinedSystemsResponse.Builder numRows(Long numRows) {
            this.numRows = numRows;
            return this;
        }

    }

}
