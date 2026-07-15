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
 * Supported rename operation types.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public enum RenameType {

    /**
     * Rename a dataset.
     */
    DATASET,

    /**
     * Rename a member within a partitioned dataset (PDS).
     */
    MEMBER

}
