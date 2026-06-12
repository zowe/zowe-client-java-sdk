/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.methods;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for WorkflowDelete.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-delete-workflow-instance">
 *     z/OSMF REST API
 * </a>
 *
 * Version: 7.0
 * Author: Adithe Das
 */
public class WorkflowDeleteTest {

    /**
     * Test secondary constructor with valid request type.
     */
    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);

        WorkflowDelete workflowDelete = new WorkflowDelete(connection, request);

        assertNotNull(workflowDelete);
    }

    /**
     * Test secondary constructor with null connection.
     */
    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDelete(null, request)
        );

        assertEquals("connection is null", exception.getMessage());
    }

    /**
     * Test secondary constructor with null request.
     */
    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDelete(connection, null)
        );

        assertEquals("request is null", exception.getMessage());
    }

    /**
     * Test secondary constructor with invalid request type.
     */
    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowDelete(connection, request)
        );

        assertEquals("DELETE_JSON request type required", exception.getMessage());
    }

    /**
     * Test primary constructor with valid connection.
     */
    @Test
    public void tstWorkflowDeletePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);

        WorkflowDelete workflowDelete = new WorkflowDelete(connection);

        assertNotNull(workflowDelete);
    }

    /**
     * Test primary constructor with null connection.
     */
    @Test
    public void tstWorkflowDeletePrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDelete(null)
        );

        assertEquals("connection is null", exception.getMessage());
    }
}
