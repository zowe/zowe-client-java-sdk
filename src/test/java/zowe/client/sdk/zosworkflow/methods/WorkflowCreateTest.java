/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflow.methods;

import kong.unirest.core.Cookie;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosworkflow.input.WorkflowCreateInputData;
import zowe.client.sdk.zosworkflow.model.WorkflowVariable;
import zowe.client.sdk.zosworkflow.response.WorkflowCreateResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for WorkflowCreate.
 */
public class WorkflowCreateTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PostJsonZosmfRequest mockPostJsonZosmfRequest;
    private PostJsonZosmfRequest mockPostJsonZosmfRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        JSONObject workflowJson = getJsonObject();

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

    private static JSONObject getJsonObject() {
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("workflowKey", "workflow-key");
        jsonMap.put("workflowDescription", "workflow-description");
        jsonMap.put("workflowID", "workflow-id");
        jsonMap.put("workflowVersion", "1.0");
        jsonMap.put("vendor", "IBM");
        return new JSONObject(jsonMap);
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
        final JSONObject requestBody = (JSONObject) new JSONParser().parse(body);

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows", mockPostJsonZosmfRequest.getUrl());
        assertEquals("AutomationExample", requestBody.get("workflowName"));
        assertEquals("/usr/lpp/zosmf/samples/workflow_sample_automation.xml",
                requestBody.get("workflowDefinitionFile"));
        assertEquals("SY1", requestBody.get("workflowDefinitionFileSystem"));
        assertEquals("/tmp/workflow.properties", requestBody.get("variableInputFile"));
        assertEquals("input", requestBody.get("resolveGlobalConflictByUsing"));
        assertEquals("SY1", requestBody.get("system"));
        assertEquals("zosmfad", requestBody.get("owner"));
        assertEquals("ZOSMFAD", requestBody.get("workflowArchiveSAFID"));
        assertEquals("This workflow was created through the z/OSMF workflow services REST interface.",
                requestBody.get("comments"));
        assertEquals(Boolean.FALSE, requestBody.get("assignToOwner"));
        assertEquals("Restricted", requestBody.get("accessType"));
        assertEquals("ACCT123", requestBody.get("accountInfo"));
        assertEquals(Boolean.TRUE, requestBody.get("deleteCompletedJobs"));
        assertEquals("/u/IBMUSER/jobFiles", requestBody.get("jobsOutputDirectory"));
        assertEquals(Boolean.TRUE, requestBody.get("autoDeleteOnCompletion"));
        assertEquals("remoteuser", requestBody.get("targetSystemuid"));
        assertEquals("remotepwd", requestBody.get("targetSystempwd"));

        final JSONArray variables = (JSONArray) requestBody.get("variables");
        assertEquals(2, variables.size());
        assertEquals("user_name", ((JSONObject) variables.get(0)).get("name"));
        assertEquals("IBMUSER", ((JSONObject) variables.get(0)).get("value"));
        assertEquals("file_name", ((JSONObject) variables.get(1)).get("name"));
        assertEquals("textfile.txt", ((JSONObject) variables.get(1)).get("value"));

        final JSONArray jobStatement = (JSONArray) requestBody.get("jobStatement");
        assertEquals(2, jobStatement.size());
        assertEquals("//TESTJOB JOB (ACCT123)", jobStatement.get(0));
        assertEquals("//*", jobStatement.get(1));

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
                final JSONObject workflowJson = getJsonObject();
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

}