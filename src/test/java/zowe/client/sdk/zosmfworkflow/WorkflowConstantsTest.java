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

import org.junit.jupiter.api.Test;
import zowe.client.sdk.utility.UtilsTestHelper;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class containing unit tests for WorkflowConstants.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowConstantsTest {

    @Test
    public void tstWorkflowConstantsValuesSuccess() {
        assertEquals("/workflow/rest/1.0", WorkflowConstants.BASE_RESOURCE);
    }

    @Test
    public void tstWorkflowConstantsUrlPathDelimSuccess() {
        assertEquals("/", WorkflowConstants.URL_PATH_DELIM);
    }

    @Test
    public void tstWorkflowConstantsWorkflowsSuccess() {
        assertEquals("workflows", WorkflowConstants.WORKFLOWS);
    }

    @Test
    public void tstWorkflowConstantsWorkflowsResourceSuccess() {
        assertEquals("/workflow/rest/1.0/workflows", WorkflowConstants.WORKFLOWS_RESOURCE);
    }

    @Test
    public void tstWorkflowConstantsArchivedWorkflowsSuccess() {
        assertEquals("archivedworkflows", WorkflowConstants.ARCHIVED_WORKFLOWS);
    }

    @Test
    public void tstWorkflowConstantsArchivedWorkflowsResourceSuccess() {
        assertEquals("/workflow/rest/1.0/archivedworkflows", WorkflowConstants.ARCHIVED_WORKFLOWS_RESOURCE);
    }
    @Test
    public void tstWorkflowConstantsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Constants class";
        UtilsTestHelper.validateClass(WorkflowConstants.class, privateConstructorExceptionMsg);
    }

}
