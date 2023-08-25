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
 * DeleteType class provides type representation of Unix System Services (USS) objects
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public enum DeleteType {

    /**
     * Access ACL
     */
    ACCESS("access"),

    /**
     * Directory default ACL
     */
    DIR("dir"),

    /**
     * File default ACL
     */
    FILE("file"),

    /**
     * Every extended ACL for all ACL types that are applicable for the current path name.
     */
    EVERY("every");

    private final String value;

    DeleteType(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the delete type.
     *
     * @return the value of the delete type
     */
    public String getValue() {
        return value;
    }

}
