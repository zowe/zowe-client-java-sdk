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
     * File delimiter.
     */
    public static final String FILE_DELIM = "/";

    /**
     * Version of the z/OSMF workflow service.
     */
    private static final String VERSION = "1.0";

    /**
     * z/OSMF base workflow service path.
     */
    public static final String BASE_RESOURCE = "/workflow/rest/" + VERSION;

    /**
     * z/OSMF workflows service path.
     */
    public static final String WORKFLOWS_RESOURCE = BASE_RESOURCE + FILE_DELIM + "workflows";

    /**
     * z/OSMF archived workflows service path.
     */
    public static final String ARCHIVED_RESOURCE = BASE_RESOURCE + FILE_DELIM + "archivedworkflows";

}
