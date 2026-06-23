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

import static zowe.client.sdk.rest.UrlConstants.URL_PATH_DELIM;

/**
 * Constants to be used by the z/OSMF workflow API.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
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
     * z/OSMF base workflow service path.
     */
    public static final String BASE_RESOURCE = "/workflow/rest/" + VERSION;

    /**
     * Workflows resource segment.
     */
    public static final String WORKFLOWS = "workflows";

    /**
     * z/OSMF workflows service path.
     */
    public static final String WORKFLOWS_RESOURCE = BASE_RESOURCE + URL_PATH_DELIM + WORKFLOWS;

    /**
     * Archived workflows resource segment.
     */
    public static final String ARCHIVED_WORKFLOWS = "archivedworkflows";

    /**
     * z/OSMF archived workflows service path.
     */
    public static final String ARCHIVED_WORKFLOWS_RESOURCE = BASE_RESOURCE + URL_PATH_DELIM + ARCHIVED_WORKFLOWS;

    /**
     * Workflow definition resource segment.
     */
    public static final String WORKFLOW_DEFINITION = "workflowDefinition";

    /**
     * z/OSMF workflow definition service path.
     */
    public static final String WORKFLOW_DEFINITION_RESOURCE = BASE_RESOURCE + URL_PATH_DELIM + WORKFLOW_DEFINITION;

    /**
     * Operations start path segment for workflow start operation.
     */
    public static final String OPERATIONS_START = "operations" + URL_PATH_DELIM + "start";

}
