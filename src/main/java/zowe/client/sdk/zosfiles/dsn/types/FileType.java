/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.types;

/**
 * USS file types supported by the z/OSMF data set copy API.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public enum FileType {

    TEXT("text"),
    BINARY("binary"),
    EXECUTABLE("executable");

    private final String value;

    FileType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
