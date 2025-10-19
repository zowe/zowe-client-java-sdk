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
import com.fasterxml.jackson.annotation.JsonProperty;
import zowe.client.sdk.zosfiles.uss.model.UnixZfs;

import java.util.List;

/**
 * UssZfsResponse object representing a zfs response from Unix System Services (USS) list operation.
 * Immutable class using Jackson for JSON parsing.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public final class UnixZfsResponse {

    private final int jsonVersion;
    private final List<UnixZfs> items;

    /**
     * UssZfsItem Constructor for Jackson
     *
     * @param jsonVersion int value
     * @param items UnixZfs items list
     */
    @JsonCreator
    public UnixZfsResponse(
            @JsonProperty("JSONversion") int jsonVersion,
            @JsonProperty("items") List<UnixZfs> items) {
        this.jsonVersion = jsonVersion;
        this.items = items;
    }

    /**
     * Retrieve JSON version
     *
     * @return JSON version int value
     */
    public int getJsonVersion() {
        return jsonVersion;
    }

    /**
     * Retrieve zfs items
     *
     * @return zfs UnixZfs items list
     */
    public List<UnixZfs> getItems() {
        return items;
    }

}
