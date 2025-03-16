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
 * LinkType class provides type representation of Unix System Services (USS) objects
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 3.0
 */
public enum LinkType {

    /**
     * The default is 'follow' encountered links.
     * This applies a mode change to the file or directory pointed to by any encountered links.
     */
    FOLLOW("follow"),

    /**
     * 'suppress' is a mode change for the file or directory pointed to by any encountered symbolic links.
     */
    SUPPRESS("suppress"),

    /**
     * 'change' does not follow the link, but instead changes the link itself (chown -h).
     */
    CHANGE("change");

    private final String value;

    LinkType(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the link type.
     *
     * @return the value of the link type
     */
    public String getValue() {
        return value;
    }

}
