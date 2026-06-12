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
 * Workflow API constants.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-delete-workflow-instance">
 *     z/OSMF REST API
 * </a>
 *
 * Version: 7.0
 * Author: Adithe Das
 */
public final class WorkflowConstants {

    /**
     * Private constructor defined to avoid instantiation of class.
     */
    private WorkflowConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Version of the z/OSMF workflow service.
     */
    private static final String VERSION = "1.0";

    /**
     * Workflow REST resource path.
     */
    public static final String RESOURCE = "/zosmf/workflow/rest/" + VERSION + "/workflows";
}