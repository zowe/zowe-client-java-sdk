/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow;

/**
 * Constants for various workflow-related info.
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public final class WorkflowsConstants {

    /**
     * Private constructor defined to avoid instantiation of class.
     */
    private WorkflowsConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Version of the z/OSMF workflow service.
     */
    private static final String VERSION = "1.0";

    /**
     * File delimiter.
     */
    public static final String FILE_DELIM = "/";

    /**
     * Use for an illegal exception error message.
     */
    public static final String WORKFLOW_KEY_ILLEGAL_MSG = "workflowKey is either null or empty";

    /**
     * z/OSMF workflow service path.
     */
    public static final String RESOURCE = "/workflow/rest/" + VERSION;

    /**
     * Query parameter for filtering by workflow name.
     */
    public static final String QUERY_WORKFLOW_NAME = "workflowName=";

    /**
     * Query parameter for filtering by category.
     */
    public static final String QUERY_CATEGORY = "category=";

    /**
     * Query parameter for filtering by system.
     */
    public static final String QUERY_SYSTEM = "system=";

    /**
     * Query parameter for filtering by owner.
     */
    public static final String QUERY_OWNER = "owner=";

    /**
     * Query parameter for filtering by vendor.
     */
    public static final String QUERY_VENDOR = "vendor=";

    /**
     * Query parameter for filtering by status name.
     */
    public static final String QUERY_STATUS_NAME = "statusName=";

}