/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.response;

import java.util.Collections;
import java.util.List;

/**
 * API response for a local create workflow. Wraps the create workflow response together with the outcome of the
 * temporary USS files that were uploaded to drive the create.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-create-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public final class WorkflowCreateLocalResponse {

    /**
     * Workflow details returned by the create workflow request.
     */
    private final WorkflowCreateResponse workflow;

    /**
     * Temporary USS files that were kept because keepFiles was requested. Empty when the files were deleted.
     */
    private final List<String> filesKept;

    /**
     * Temporary USS files that could not be deleted during cleanup. Empty when all files were deleted successfully.
     */
    private final List<String> failedToDelete;

    /**
     * WorkflowCreateLocalResponse constructor.
     *
     * @param workflow       workflow details returned by the create workflow request
     * @param filesKept      temporary USS files that were kept
     * @param failedToDelete temporary USS files that could not be deleted
     */
    public WorkflowCreateLocalResponse(final WorkflowCreateResponse workflow,
                                       final List<String> filesKept,
                                       final List<String> failedToDelete) {
        this.workflow = workflow;
        this.filesKept = filesKept == null ? Collections.emptyList() : filesKept;
        this.failedToDelete = failedToDelete == null ? Collections.emptyList() : failedToDelete;
    }

    /**
     * Retrieve workflow value.
     *
     * @return workflow value
     */
    public WorkflowCreateResponse getWorkflow() {
        return workflow;
    }

    /**
     * Retrieve filesKept value.
     *
     * @return filesKept value
     */
    public List<String> getFilesKept() {
        return filesKept;
    }

    /**
     * Retrieve failedToDelete value.
     *
     * @return failedToDelete value
     */
    public List<String> getFailedToDelete() {
        return failedToDelete;
    }

    /**
     * Return a string value representing a WorkflowCreateLocalResponse object.
     *
     * @return string representation of WorkflowCreateLocalResponse
     */
    @Override
    public String toString() {
        return "WorkflowCreateLocalResponse{" +
                "workflow=" + workflow +
                ", filesKept=" + filesKept +
                ", failedToDelete=" + failedToDelete +
                '}';
    }

}
