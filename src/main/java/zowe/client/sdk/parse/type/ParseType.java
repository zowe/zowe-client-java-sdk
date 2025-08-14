/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parse.type;

/**
 * Represents JSON parse operation type
 *
 * @author Frank Giordano
 * @version 4.0
 */
public enum ParseType {

    /**
     * dataset type
     */
    DATASET,
    /**
     * job type
     */
    JOB,
    /**
     * job file type
     */
    JOB_FILE,
    /**
     * member type
     */
    MEMBER,
    /**
     * mvs console type
     */
    MVS_CONSOLE,
    /**
     * property type
     */
    PROPS,
    /**
     * tso console type
     */
    TSO_CONSOLE,
    /**
     * tso stop type
     */
    TSO_STOP,
    /**
     * unix file type
     */
    UNIX_FILE,
    /**
     * unix zfs type
     */
    UNIX_ZFS,
    /**
     * zos log item type
     */
    ZOS_LOG_ITEM,
    /**
     * zos log reply item
     */
    ZOS_LOG_REPLY,
    /**
     * zosmf systems type
     */
    ZOSMF_SYSTEMS,
    /**
     * zosmf info type
     */
    ZOSMF_INFO

}
