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
package zowe.client.sdk.zoslogs.input;

public enum DirectionType {

    FORWARD("forward"),
    BACKWARD("backward");

    private final String value;

    DirectionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
