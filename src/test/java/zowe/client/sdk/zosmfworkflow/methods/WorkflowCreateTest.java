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

import kong.unirest.core.Cookie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.methods.UssDelete;
import zowe.client.sdk.zosfiles.uss.methods.UssWrite;
import zowe.client.sdk.zosmfworkflow.input.WorkflowCreateInputData;
import zowe.client.sdk.zosmfworkflow.model.WorkflowVariable;
import zowe.client.sdk.zosmfworkflow.response.WorkflowCreateLocalResponse;
import zowe.client.sdk.zosmfworkflow.response.WorkflowCreateResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for WorkflowCreate.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowCreateTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PostJsonZosmfRequest mockPostJsonZosmfRequest;
    private PostJsonZosmfRequest mockPostJsonZosmfRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        ObjectNode workflowJson = getJsonObject();

        mockPostJsonZosmfRequest = Mockito.mock(PostJsonZosmfRequest.class);
        Mockito.when(mockPostJsonZosmfRequest.executeRequest()).thenReturn(
                new Response(workflowJson, 201, "Created"));
        doCallRealMethod().when(mockPostJsonZosmfRequest).setUrl(any());
        doCallRealMethod().when(mockPostJsonZosmfRequest).getUrl();
        doCallRealMethod().when(mockPostJsonZosmfRequest).setBody(any());

        mockPostJsonZosmfRequestToken = Mockito.mock(PostJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPostJsonZosmfRequestToken.executeRequest()).thenReturn(
                new Response(workflowJson, 201, "Created"));
        doCallRealMethod().when(mockPostJsonZosmfRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPostJsonZosmfRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPostJsonZosmfRequestToken).setUrl(any());
        doCallRealMethod().when(mockPostJsonZosmfRequestToken).getHeaders();
        doCallRealMethod().when(mockPostJsonZosmfRequestToken).getUrl();
        doCallRealMethod().when(mockPostJsonZosmfRequestToken).setBody(any());
    }

    private static ObjectNode getJsonObject() {
        final ObjectMapper m = new ObjectMapper();
        final ObjectNode node = m.createObjectNode();
        node.put("workflowKey", "workflow-key");
        node.put("workflowDescription", "workflow-description");
        node.put("workflowID", "workflow-id");
        node.put("workflowVersion", "1.0");
        node.put("vendor", "IBM");
        return node;
    }

    private static WorkflowCreateInputData createInputData() {
        return WorkflowCreateInputData.builder()
                .workflowName("AutomationExample")
                .workflowDefinitionFile("/usr/lpp/zosmf/samples/workflow_sample_automation.xml")
                .workflowDefinitionFileSystem("SY1")
                .variableInputFile("/tmp/workflow.properties")
                .variables(Arrays.asList(
                        new WorkflowVariable("user_name", "IBMUSER"),
                        new WorkflowVariable("file_name", "textfile.txt")
                ))
                .resolveGlobalConflictByUsing("input")
                .system("SY1")
                .owner("zosmfad")
                .workflowArchiveSAFID("ZOSMFAD")
                .comments("This workflow was created through the z/OSMF workflow services REST interface.")
                .assignToOwner(Boolean.FALSE)
                .accessType("Restricted")
                .accountInfo("ACCT123")
                .jobStatement(Arrays.asList("//TESTJOB JOB (ACCT123)", "//*"))
                .deleteCompletedJobs(Boolean.TRUE)
                .jobsOutputDirectory("/u/IBMUSER/jobFiles")
                .autoDeleteOnCompletion(Boolean.TRUE)
                .targetSystemuid("remoteuser")
                .targetSystempwd("remotepwd")
                .build();
    }

    @Test
    public void tstWorkflowCreateSuccess() throws Exception {
        final WorkflowCreate workflowCreate = new WorkflowCreate(connection, mockPostJsonZosmfRequest);
        final WorkflowCreateResponse response = workflowCreate.create(createInputData());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPostJsonZosmfRequest).setBody(bodyCaptor.capture());
        final String body = bodyCaptor.getValue().toString();
        final JsonNode requestBody = mapper.readTree(body);

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows", mockPostJsonZosmfRequest.getUrl());
        assertEquals("AutomationExample", requestBody.get("workflowName").asText());
        assertEquals("/usr/lpp/zosmf/samples/workflow_sample_automation.xml",
                requestBody.get("workflowDefinitionFile").asText());
        assertEquals("SY1", requestBody.get("workflowDefinitionFileSystem").asText());
        assertEquals("/tmp/workflow.properties", requestBody.get("variableInputFile").asText());
        assertEquals("input", requestBody.get("resolveGlobalConflictByUsing").asText());
        assertEquals("SY1", requestBody.get("system").asText());
        assertEquals("zosmfad", requestBody.get("owner").asText());
        assertEquals("ZOSMFAD", requestBody.get("workflowArchiveSAFID").asText());
        assertEquals("This workflow was created through the z/OSMF workflow services REST interface.",
                requestBody.get("comments").asText());
        assertEquals(false, requestBody.get("assignToOwner").asBoolean());
        assertEquals("Restricted", requestBody.get("accessType").asText());
        assertEquals("ACCT123", requestBody.get("accountInfo").asText());
        assertEquals(true, requestBody.get("deleteCompletedJobs").asBoolean());
        assertEquals("/u/IBMUSER/jobFiles", requestBody.get("jobsOutputDirectory").asText());
        assertEquals(true, requestBody.get("autoDeleteOnCompletion").asBoolean());
        assertEquals("remoteuser", requestBody.get("targetSystemuid").asText());
        assertEquals("remotepwd", requestBody.get("targetSystempwd").asText());

        final JsonNode variables = requestBody.get("variables");
        assertEquals(2, variables.size());
        assertEquals("user_name", variables.get(0).get("name").asText());
        assertEquals("IBMUSER", variables.get(0).get("value").asText());
        assertEquals("file_name", variables.get(1).get("name").asText());
        assertEquals("textfile.txt", variables.get(1).get("value").asText());

        final JsonNode jobStatement = requestBody.get("jobStatement");
        assertEquals(2, jobStatement.size());
        assertEquals("//TESTJOB JOB (ACCT123)", jobStatement.get(0).asText());
        assertEquals("//*", jobStatement.get(1).asText());

        assertEquals("workflow-key", response.getWorkflowKey());
        assertEquals("workflow-description", response.getWorkflowDescription());
        assertEquals("workflow-id", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("IBM", response.getVendor());
    }

    @Test
    public void tstWorkflowCreateTokenSuccess() throws Exception {
        final WorkflowCreate workflowCreate = new WorkflowCreate(connection, mockPostJsonZosmfRequestToken);
        final WorkflowCreateResponse response = workflowCreate.create(createInputData());

        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPostJsonZosmfRequestToken.getHeaders().toString());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows", mockPostJsonZosmfRequestToken.getUrl());
        assertEquals("workflow-key", response.getWorkflowKey());
        assertEquals("workflow-description", response.getWorkflowDescription());
    }

    @Test
    public void tstWorkflowCreateJsonStringResponseSuccess() throws Exception {
        final ObjectNode workflowJson = getJsonObject();
        final PostJsonZosmfRequest mockPostJsonZosmfRequestString = Mockito.mock(PostJsonZosmfRequest.class);
        Mockito.when(mockPostJsonZosmfRequestString.executeRequest()).thenReturn(
                new Response(workflowJson.toString(), 201, "Created"));
        doCallRealMethod().when(mockPostJsonZosmfRequestString).setUrl(any());
        doCallRealMethod().when(mockPostJsonZosmfRequestString).getUrl();
        doCallRealMethod().when(mockPostJsonZosmfRequestString).setBody(any());

        final WorkflowCreate workflowCreate = new WorkflowCreate(connection, mockPostJsonZosmfRequestString);
        final WorkflowCreateResponse response = workflowCreate.create(createInputData());

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows",
                mockPostJsonZosmfRequestString.getUrl());
        assertEquals("workflow-key", response.getWorkflowKey());
        assertEquals("workflow-description", response.getWorkflowDescription());
        assertEquals("workflow-id", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("IBM", response.getVendor());
    }

    @Test
    public void tstWorkflowCreateSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PostJsonZosmfRequest.class);
        WorkflowCreate workflowCreate = new WorkflowCreate(connection, request);
        assertNotNull(workflowCreate);
    }

    @Test
    public void tstWorkflowCreateSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PostJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowCreate(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowCreateSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowCreate(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowCreateSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowCreate(connection, request)
        );
        assertEquals("POST_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowCreatePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        WorkflowCreate workflowCreate = new WorkflowCreate(connection);
        assertNotNull(workflowCreate);
    }

    @Test
    public void tstWorkflowCreatePrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowCreate(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    private WorkflowCreate workflowCreateWithUss(final UssWrite ussWrite, final UssDelete ussDelete) {
        final WorkflowCreate workflowCreate = new WorkflowCreate(connection, mockPostJsonZosmfRequest);
        workflowCreate.ussWrite = ussWrite;
        workflowCreate.ussDelete = ussDelete;
        return workflowCreate;
    }

    private static WorkflowCreateInputData localInputData(final Path tempDir, final boolean withVarFile)
            throws Exception {
        final Path definitionFile = Files.write(tempDir.resolve("workflow.xml"), "<workflow/>".getBytes());
        final WorkflowCreateInputData.Builder builder = WorkflowCreateInputData.builder()
                .workflowName("AutomationExample")
                .workflowDefinitionFile(definitionFile.toString())
                .system("SY1")
                .owner("zosmfad");
        if (withVarFile) {
            final Path variableFile = Files.write(tempDir.resolve("vars.properties"), "k=v".getBytes());
            builder.variableInputFile(variableFile.toString());
        }
        return builder.build();
    }

    @Test
    public void tstWorkflowCreateLocalDeletesTempFilesSuccess(@TempDir Path tempDir) throws Exception {
        final UssWrite mockUssWrite = Mockito.mock(UssWrite.class);
        final UssDelete mockUssDelete = Mockito.mock(UssDelete.class);
        final WorkflowCreate workflowCreate = workflowCreateWithUss(mockUssWrite, mockUssDelete);

        final WorkflowCreateLocalResponse response = workflowCreate.createLocal(localInputData(tempDir, true));

        // definition and variable files uploaded as binary, then both temp files deleted
        verify(mockUssWrite, times(2)).writeBinary(any(), any());
        verify(mockUssDelete, times(2)).delete(any());

        // create request body points at the uploaded USS temp paths, not the local paths
        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPostJsonZosmfRequest).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertTrue(requestBody.get("workflowDefinitionFile").asText().startsWith("/tmp/"));
        assertTrue(requestBody.get("variableInputFile").asText().startsWith("/tmp/"));
        // non-file fields are preserved through toBuilder
        assertEquals("AutomationExample", requestBody.get("workflowName").asText());
        assertEquals("SY1", requestBody.get("system").asText());
        assertEquals("zosmfad", requestBody.get("owner").asText());

        assertEquals("workflow-key", response.getWorkflow().getWorkflowKey());
        assertTrue(response.getFilesKept().isEmpty());
        assertTrue(response.getFailedToDelete().isEmpty());
    }

    @Test
    public void tstWorkflowCreateLocalKeepFilesSuccess(@TempDir Path tempDir) throws Exception {
        final UssWrite mockUssWrite = Mockito.mock(UssWrite.class);
        final UssDelete mockUssDelete = Mockito.mock(UssDelete.class);
        final WorkflowCreate workflowCreate = workflowCreateWithUss(mockUssWrite, mockUssDelete);

        final WorkflowCreateLocalResponse response =
                workflowCreate.createLocal(localInputData(tempDir, true), true, null);

        verify(mockUssDelete, never()).delete(any());
        assertEquals(2, response.getFilesKept().size());
        assertTrue(response.getFailedToDelete().isEmpty());
    }

    @Test
    public void tstWorkflowCreateLocalCustomDirSuccess(@TempDir Path tempDir) throws Exception {
        final UssWrite mockUssWrite = Mockito.mock(UssWrite.class);
        final UssDelete mockUssDelete = Mockito.mock(UssDelete.class);
        final WorkflowCreate workflowCreate = workflowCreateWithUss(mockUssWrite, mockUssDelete);

        workflowCreate.createLocal(localInputData(tempDir, false), true, "/u/user/uploads");

        final ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockUssWrite).writeBinary(pathCaptor.capture(), any());
        assertEquals("/u/user/uploads/workflow.xml", pathCaptor.getValue());
    }

    @Test
    public void tstWorkflowCreateLocalFailedDeleteTracked(@TempDir Path tempDir) throws Exception {
        final UssWrite mockUssWrite = Mockito.mock(UssWrite.class);
        final UssDelete mockUssDelete = Mockito.mock(UssDelete.class);
        Mockito.when(mockUssDelete.delete(any())).thenThrow(new ZosmfRequestException("delete failed"));
        final WorkflowCreate workflowCreate = workflowCreateWithUss(mockUssWrite, mockUssDelete);

        final WorkflowCreateLocalResponse response =
                workflowCreate.createLocal(localInputData(tempDir, false));

        assertEquals(1, response.getFailedToDelete().size());
        assertTrue(response.getFailedToDelete().get(0).startsWith("/tmp/"));
        assertTrue(response.getFilesKept().isEmpty());
    }

    @Test
    public void tstWorkflowCreateLocalNullInputData() {
        final WorkflowCreate workflowCreate = new WorkflowCreate(connection, mockPostJsonZosmfRequest);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> workflowCreate.createLocal(null));
        assertEquals("createInputData is null", exception.getMessage());
    }

}
