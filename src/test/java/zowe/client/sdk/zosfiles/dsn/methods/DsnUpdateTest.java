/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.core.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DsnRenameInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for DsnUpdate.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class DsnUpdateTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response("{}", 200, "success"));
        doCallRealMethod().when(mockJsonPutRequest).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequest).getUrl();

        mockJsonPutRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonPutRequestToken.executeRequest()).thenReturn(
                new Response("{}", 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).getUrl();
    }

    @Test
    public void tstDsnUpdateRenameDatasetSuccess() throws ZosmfRequestException, JsonProcessingException {
        final DsnUpdate dsnUpdate = new DsnUpdate(connection, mockJsonPutRequest);
        final DsnRenameInputData renameInputData = DsnRenameInputData
                .forDataset("sourceDataset", "destinationDataset");
        final Response response = dsnUpdate.rename(renameInputData);
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockJsonPutRequest).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertEquals("rename", requestBody.get("request").asText());
        assertEquals("{\"dsn\":\"sourceDataset\"}", requestBody.get("from-dataset").toString());
        assertEquals("https://1:443/zosmf/restfiles/ds/destinationDataset", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstDsnUpdateRenameMemberTokenSuccess() throws ZosmfRequestException, JsonProcessingException {
        final DsnUpdate dsnUpdate = new DsnUpdate(connection, mockJsonPutRequestToken);
        final DsnRenameInputData renameInputData = DsnRenameInputData
                .forMember("sourceDataset", "memberName", "newMemberName");
        final Response response = dsnUpdate.rename(renameInputData);
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockJsonPutRequestToken).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertEquals("rename", requestBody.get("request").asText());
        assertEquals("{\"member\":\"memberName\",\"dsn\":\"sourceDataset\"}",
                requestBody.get("from-dataset").toString());
        assertEquals("https://1:443/zosmf/restfiles/ds/sourceDataset(newMemberName)",
                mockJsonPutRequestToken.getUrl());
    }

    @Test
    public void tstDsnUpdateConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        DsnUpdate dsnUpdate = new DsnUpdate(connection, request);
        assertNotNull(dsnUpdate);
    }

    @Test
    public void tstDsnUpdateSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnUpdate(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstDsnUpdateSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnUpdate(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstDsnUpdateSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new DsnUpdate(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstDsnUpdatePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        DsnUpdate dsnUpdate = new DsnUpdate(connection);
        assertNotNull(dsnUpdate);
    }

    @Test
    public void tstDsnUpdatePrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnUpdate(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstDsnUpdateMigrateDatasetSuccess() throws ZosmfRequestException, JsonProcessingException {
        final DsnUpdate dsnUpdate = new DsnUpdate(connection, mockJsonPutRequest);
        final Response response = dsnUpdate.migrate("TEST.DATASET");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockJsonPutRequest.getUrl());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockJsonPutRequest).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertEquals("hmigrate", requestBody.get("request").asText());
        assertNull(requestBody.get("wait"));
    }

    @Test
    public void tstDsnUpdateMigrateDatasetWithWaitSuccess() throws ZosmfRequestException, JsonProcessingException {
        final DsnUpdate dsnUpdate = new DsnUpdate(connection, mockJsonPutRequest);
        final Response response = dsnUpdate.migrate("TEST.DATASET", true);
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockJsonPutRequest.getUrl());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockJsonPutRequest).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertEquals("hmigrate", requestBody.get("request").asText());
        assertTrue(requestBody.get("wait").asBoolean());
    }

    @Test
    public void tstDsnUpdateDeleteMigratedDatasetSuccess() throws ZosmfRequestException, JsonProcessingException {
        final DsnUpdate dsnUpdate = new DsnUpdate(connection, mockJsonPutRequest);
        final Response response = dsnUpdate.deleteMigrated("TEST.MIGRATED.DATASET");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.MIGRATED.DATASET", mockJsonPutRequest.getUrl());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockJsonPutRequest).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertEquals("hdelete", requestBody.get("request").asText());
        assertNull(requestBody.get("wait"));
        assertNull(requestBody.get("purge"));
    }

    @Test
    public void tstDsnUpdateDeleteMigratedDatasetWithOptionsSuccess() throws ZosmfRequestException, JsonProcessingException {
        final DsnUpdate dsnUpdate = new DsnUpdate(connection, mockJsonPutRequest);
        final Response response = dsnUpdate.deleteMigrated("TEST.MIGRATED.DATASET", true, true);
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.MIGRATED.DATASET", mockJsonPutRequest.getUrl());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockJsonPutRequest).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertEquals("hdelete", requestBody.get("request").asText());
        assertTrue(requestBody.get("wait").asBoolean());
        assertTrue(requestBody.get("purge").asBoolean());
    }

    @Test
    public void tstDsnUpdateRecallMigratedDatasetSuccess() throws ZosmfRequestException, JsonProcessingException {
        final DsnUpdate dsnUpdate = new DsnUpdate(connection, mockJsonPutRequest);
        final Response response = dsnUpdate.recallMigrated("TEST.MIGRATED.DATASET");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.MIGRATED.DATASET", mockJsonPutRequest.getUrl());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockJsonPutRequest).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertEquals("hrecall", requestBody.get("request").asText());
        assertNull(requestBody.get("wait"));
    }

    @Test
    public void tstDsnUpdateRecallMigratedDatasetWithWaitSuccess() throws ZosmfRequestException, JsonProcessingException {
        final DsnUpdate dsnUpdate = new DsnUpdate(connection, mockJsonPutRequest);
        final Response response = dsnUpdate.recallMigrated("TEST.MIGRATED.DATASET", true);
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.MIGRATED.DATASET", mockJsonPutRequest.getUrl());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockJsonPutRequest).setBody(bodyCaptor.capture());
        final JsonNode requestBody = mapper.readTree(bodyCaptor.getValue().toString());
        assertEquals("hrecall", requestBody.get("request").asText());
        assertTrue(requestBody.get("wait").asBoolean());
    }

}
