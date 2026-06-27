/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflows;

/**
 * Constants for various workflow-related info
 *
 * @author Jorge Samaniego
 * @version 7.0
 */
public final class WorkflowConstants {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private WorkflowConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * URI base workflows API
     */
    public static final String RESOURCE = "/workflow/rest/1.0/workflows";
    /**
     * Query identifier
     */
    public static final String QUERY_ID = "?";

    /**
     * Combo identifier for multiple query params
     */
    public static final String COMBO_ID = "&";

    /**
     * Query id for filtering by workflow name
     */
    public static final String QUERY_WORKFLOW_NAME = "workflowName=";

    /**
     * Query id for filtering by category
     */
    public static final String QUERY_CATEGORY = "category=";

    /**
     * Query id for filtering by system
     */
    public static final String QUERY_SYSTEM = "system=";

    /**
     * Query id for filtering by status
     */
    public static final String QUERY_STATUS_NAME = "statusName=";

    /**
     * Query id for filtering by owner
     */
    public static final String QUERY_OWNER = "owner=";

    /**
     * Query id for filtering by vendor
     */
    public static final String QUERY_VENDOR = "vendor=";

}