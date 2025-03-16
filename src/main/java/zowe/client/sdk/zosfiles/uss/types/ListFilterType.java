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
 * Type of file to filer for during Unix System Services (USS) list operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-files-directories-unix-file-path">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 3.0
 */
public enum ListFilterType {

    /**
     * Character special file
     */
    CHAR_SPECIAL_FILE("c"),

    /**
     * Directory
     */
    DIRECTORY("d"),

    /**
     * File
     */
    FILE("f"),

    /**
     * Symbolic link
     */
    SYMBOLIC_LINK("l"),

    /**
     * FIFO (named pipe)
     */
    FIFO_NAMED_PIPE("p"),

    /**
     * Socket
     */
    SOCKET("s");

    private final String value;

    ListFilterType(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the list filter type.
     *
     * @return the value of the list filter type
     */
    public String getValue() {
        return value;
    }

}
