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
 * GetAclType class provides ACL type representation of Unix System Services (USS) objects
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 4.0
 */
public enum GetAclType {

    /**
     * Displays the access ACL entries. This is the default if others are not specified.
     */
    ACCESS("access"),

    /**
     * Displays the directory default ACL entries.
     */
    DIR("dir"),

    /**
     * Displays the file default ACL entries.
     */
    FILE("file");

    private final String value;

    GetAclType(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the GetAclType type.
     *
     * @return the value of the GetAclType type
     */
    public String getValue() {
        return value;
    }

}
