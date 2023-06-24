/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.type;

/**
 * UssType class provides type representation of UNIX objects
 *
 * @author James Kostrewski
 * @version 2.0
 */
public enum UssType {

    FILE("file"),
    DIR("dir");

    private final String value;

    UssType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}