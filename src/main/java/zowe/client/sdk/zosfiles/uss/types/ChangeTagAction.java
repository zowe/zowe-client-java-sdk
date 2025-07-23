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
 * ChangeTagAction class provides action representation of Unix System Services (USS) chtag operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public enum ChangeTagAction {

    /**
     * Set the tag the file is tagged as specified in the "type" keyword
     */
    SET("set"),

    /**
     * Existing file tag is removed
     */
    REMOVE("remove"),

    /**
     * Existing tag information will be returned
     */
    LIST("list");

    private final String value;

    ChangeTagAction(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the change tag action.
     *
     * @return the value of the change tag action
     */
    public String getValue() {
        return value;
    }

}
