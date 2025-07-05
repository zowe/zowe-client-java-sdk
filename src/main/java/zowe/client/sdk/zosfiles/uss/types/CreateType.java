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
 * CreateType class provides type representation of Unix System Services (USS) objects
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-create-unix-file-directory">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public enum CreateType {

    /**
     * file mode
     */
    FILE("file"),
    /**
     * dir mode
     */
    DIR("dir");

    private final String value;

    CreateType(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the CreateType type.
     *
     * @return the value of the CreateType type
     */
    public String getValue() {
        return value;
    }

}
