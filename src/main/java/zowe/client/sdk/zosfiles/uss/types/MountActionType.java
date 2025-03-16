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
 * MountActionType class provides type representation of Unix System Services (USS) mount action
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-mount-zos-unix-file-system">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 3.0
 */
public enum MountActionType {

    MOUNT("mount"),
    UNMOUNT("unmount");

    private final String value;

    MountActionType(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the mount action type.
     *
     * @return the value of the mount action type
     */
    public String getValue() {
        return value;
    }

}
