/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.types;

/**
 * ChangeTagType class provides tag type representation of Unix System Services (USS) chtag operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public enum ChangeTagType {

    BINARY("binary"),
    MIXED("mixed"),
    TEXT("text");

    private final String value;

    ChangeTagType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
