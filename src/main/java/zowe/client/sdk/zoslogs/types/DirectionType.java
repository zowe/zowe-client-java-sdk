/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zowe.client.sdk.zoslogs.types;

/**
 * Enum class representing direction to gather log data from.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public enum DirectionType {

    /**
     * Forward direction
     */
    FORWARD("forward"),
    /**
     * backward direction
     */
    BACKWARD("backward");

    private final String value;

    DirectionType(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the direction type.
     *
     * @return the value of the direction type
     */
    public String getValue() {
        return value;
    }

}
