/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.methods;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.factory.SetAclInputFactory;
import zowe.client.sdk.zosfiles.uss.input.factory.UssSetAclInputData;
import zowe.client.sdk.zosfiles.uss.types.DeleteAclType;
import zowe.client.sdk.zosfiles.uss.types.LinkType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UssSetAcl.
 */
class UssSetAclTest {

    private static final String ZOSMF_URL = "https://example.com/zosmf";
    private static final String TARGET_PATH = "/u/test/file.txt";

    private ZosConnection connection;
    private PutJsonZosmfRequest request;
    private Response response;
    private final ObjectMapper mapper = new ObjectMapper();
    private UssSetAcl ussSetAcl;

    @BeforeEach
    void setUp() throws ZosmfRequestException {
        connection = mock(ZosConnection.class);
        request = mock(PutJsonZosmfRequest.class);
        response = mock(Response.class);

        when(connection.getZosmfUrl()).thenReturn(ZOSMF_URL);
        when(request.executeRequest()).thenReturn(response);

        ussSetAcl = new UssSetAcl(connection, request);
    }

    @Test
    void tstDefaultConstructorRejectsNullConnectionFailure() {
        assertThrows(NullPointerException.class, () -> new UssSetAcl(null));
    }

    @Test
    void tstConstructorRejectsNullConnectionFailure() {
        assertThrows(NullPointerException.class, () -> new UssSetAcl(null, request));
    }

    @Test
    void tstConstructorRejectsNullRequestFailure() {
        assertThrows(NullPointerException.class, () -> new UssSetAcl(connection, null));
    }

    @Test
    void tstConstructorRejectsNonPutJsonRequestFailure() {
        final ZosmfRequest invalidRequest = mock(ZosmfRequest.class);

        assertThrows(IllegalStateException.class, () -> new UssSetAcl(connection, invalidRequest));
    }

    @Test
    void tstSetCreatesSetAclRequestBodyWithSetValueSuccess() throws Exception {
        final Response actualResponse = ussSetAcl.set(TARGET_PATH, "user:test:rwx");

        assertSame(response, actualResponse);
        verify(request).setUrl(getExpectedUrl());
        final JsonNode body = getRequestBody();

        assertEquals("setfacl", body.get("request").asText());
        assertEquals(LinkType.FOLLOW.getValue(), body.get("links").asText());
        assertEquals("user:test:rwx", body.get("set").asText());
        assertFalse(body.has("abort"));
        assertFalse(body.has("modify"));
        assertFalse(body.has("delete"));
        assertFalse(body.has("delete-type"));
    }

    @Test
    void tstModifyCreatesSetAclRequestBodyWithModifyValueSuccess() throws Exception {
        final Response actualResponse = ussSetAcl.modify(TARGET_PATH, "user:test:rwx");

        assertSame(response, actualResponse);
        verify(request).setUrl(getExpectedUrl());
        final JsonNode body = getRequestBody();

        assertEquals("setfacl", body.get("request").asText());
        assertEquals(LinkType.FOLLOW.getValue(), body.get("links").asText());
        assertEquals("user:test:rwx", body.get("modify").asText());
        assertFalse(body.has("abort"));
        assertFalse(body.has("set"));
        assertFalse(body.has("delete"));
        assertFalse(body.has("delete-type"));
    }

    @Test
    void tstDeleteCreatesSetAclRequestBodyWithDeleteValueSuccess() throws Exception {
        final Response actualResponse = ussSetAcl.delete(TARGET_PATH, "user:test:rwx");

        assertSame(response, actualResponse);
        verify(request).setUrl(getExpectedUrl());
        final JsonNode body = getRequestBody();

        assertEquals("setfacl", body.get("request").asText());
        assertEquals(LinkType.FOLLOW.getValue(), body.get("links").asText());
        assertEquals("user:test:rwx", body.get("delete").asText());
        assertFalse(body.has("abort"));
        assertFalse(body.has("set"));
        assertFalse(body.has("modify"));
        assertFalse(body.has("delete-type"));
    }

    @Test
    void tstDeleteByTypeCreatesSetAclRequestBodyWithDeleteTypeValueSuccess() throws Exception {
        final Response actualResponse = ussSetAcl.deleteByType(TARGET_PATH, DeleteAclType.ACCESS);

        assertSame(response, actualResponse);
        verify(request).setUrl(getExpectedUrl());
        final JsonNode body = getRequestBody();

        assertEquals("setfacl", body.get("request").asText());
        assertEquals(LinkType.FOLLOW.getValue(), body.get("links").asText());
        assertEquals(DeleteAclType.ACCESS.getValue(), body.get("delete-type").asText());
        assertFalse(body.has("abort"));
        assertFalse(body.has("set"));
        assertFalse(body.has("modify"));
        assertFalse(body.has("delete"));
    }

    @Test
    void tstModifyAndDeleteCreatesSetAclRequestBodyWithModifyAndDeleteValuesSuccess() throws Exception {
        final Response actualResponse = ussSetAcl.modifyAndDelete(
                TARGET_PATH,
                "user:test:rwx",
                "group:test:r-x"
        );

        assertSame(response, actualResponse);
        verify(request).setUrl(getExpectedUrl());
        final JsonNode body = getRequestBody();

        assertEquals("setfacl", body.get("request").asText());
        assertEquals(LinkType.FOLLOW.getValue(), body.get("links").asText());
        assertEquals("user:test:rwx", body.get("modify").asText());
        assertEquals("group:test:r-x", body.get("delete").asText());
        assertFalse(body.has("abort"));
        assertFalse(body.has("set"));
        assertFalse(body.has("delete-type"));
    }

    @Test
    void tstSetAclCommonCreatesRequestBodyWithAbortAndSuppressLinksValuesSuccess() throws Exception {
        final UssSetAclInputData inputData = SetAclInputFactory.createSetInput(
                "user:test:rwx",
                true,
                LinkType.SUPPRESS
        );

        final Response actualResponse = ussSetAcl.setAclCommon(TARGET_PATH, inputData);

        assertSame(response, actualResponse);
        verify(request).setUrl(getExpectedUrl());
        final JsonNode body = getRequestBody();

        assertEquals("setfacl", body.get("request").asText());
        assertTrue(body.get("abort").asBoolean());
        assertEquals(LinkType.SUPPRESS.getValue(), body.get("links").asText());
        assertEquals("user:test:rwx", body.get("set").asText());
        assertFalse(body.has("modify"));
        assertFalse(body.has("delete"));
        assertFalse(body.has("delete-type"));
    }

    @Test
    void tstSetAclCommonRejectsNullInputDataFailure() {
        assertThrows(NullPointerException.class, () -> ussSetAcl.setAclCommon(TARGET_PATH, null));
    }

    @Test
    void tstSetAclCommonRejectsBlankTargetPathFailure() {
        final UssSetAclInputData inputData = SetAclInputFactory.createSetInput("user:test:rwx");

        assertThrows(IllegalArgumentException.class, () -> ussSetAcl.setAclCommon("", inputData));
    }

    @Test
    void tstSetAclCommonExecutesRequestSuccess() throws Exception {
        final UssSetAclInputData inputData = SetAclInputFactory.createSetInput("user:test:rwx");

        ussSetAcl.setAclCommon(TARGET_PATH, inputData);

        verify(request).executeRequest();
    }

    private String getExpectedUrl() {
        return ZOSMF_URL +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(TARGET_PATH));
    }

    private JsonNode getRequestBody() throws Exception {
        final ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(request).setBody(bodyCaptor.capture());

        return mapper.readTree(bodyCaptor.getValue());
    }

}
