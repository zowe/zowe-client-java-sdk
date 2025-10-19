/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.reaponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import zowe.client.sdk.zosfiles.uss.model.UnixFile;

import java.util.List;

/**
 * Response object representing a Unix File List response from USS.
 * Contains a list of UnixFile items and metadata about the response.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UnixFileListResponse {

    /**
     * JSON version of the response
     */
    private final int jsonVersion;

    /**
     * Number of filesystem items returned
     */
    private final int returnedRows;

    /**
     * Total number of filesystem items
     */
    private final int totalRows;

    /**
     * List of UnixFile items
     */
    private final List<UnixFile> items;

    /**
     * Jackson constructor for UnixFileListResponse
     *
     * @param jsonVersion  JSON version of the response
     * @param returnedRows number of items returned
     * @param totalRows    total number of items
     * @param items        list of UnixFile objects
     */
    @JsonCreator
    public UnixFileListResponse(
            @JsonProperty("JSONversion") int jsonVersion,
            @JsonProperty("returnedRows") int returnedRows,
            @JsonProperty("totalRows") int totalRows,
            @JsonProperty("items") List<UnixFile> items
    ) {
        this.jsonVersion = jsonVersion;
        this.returnedRows = returnedRows;
        this.totalRows = totalRows;
        this.items = items;
    }

    /**
     * Retrieve JSON version
     *
     * @return jsonVersion
     */
    public int getJsonVersion() {
        return jsonVersion;
    }

    /**
     * Retrieve the number of returned rows
     *
     * @return returnedRows
     */
    public int getReturnedRows() {
        return returnedRows;
    }

    /**
     * Retrieve the total number of rows
     *
     * @return totalRows
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * Retrieve list of UnixFile items
     *
     * @return list of UnixFile objects
     */
    public List<UnixFile> getItems() {
        return items;
    }

    /**
     * Return string value representing UnixFileListResponse object
     *
     * @return string representation of UnixFileListResponse
     */
    @Override
    public String toString() {
        return "UnixFileListResponse{" +
                "jsonVersion=" + jsonVersion +
                ", returnedRows=" + returnedRows +
                ", totalRows=" + totalRows +
                ", items=" + items +
                '}';
    }

}
