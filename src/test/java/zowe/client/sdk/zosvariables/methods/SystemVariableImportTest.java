/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.methods;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for SystemVariableImport.
 *
 * @author Frank Giordano
 * @author Chaitanya Katore
 * @version 6.0
 */
public class SystemVariableImportTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PostJsonZosmfRequest mockPostRequest;
    private PostJsonZosmfRequest mockPostRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockPostRequest = Mockito.mock(PostJsonZosmfRequest.class);
        Mockito.when(mockPostRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 204, "No Content"));
        doCallRealMethod().when(mockPostRequest).setUrl(any());
        doCallRealMethod().when(mockPostRequest).getUrl();
        doCallRealMethod().when(mockPostRequest).setBody(any());

        mockPostRequestToken = Mockito.mock(PostJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPostRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 204, "No Content"));
        doCallRealMethod().when(mockPostRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPostRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPostRequestToken).setUrl(any());
        doCallRealMethod().when(mockPostRequestToken).getHeaders();
        doCallRealMethod().when(mockPostRequestToken).getUrl();
        doCallRealMethod().when(mockPostRequestToken).setBody(any());
    }

    @Test
    public void tstSystemVariableImportSuccess() throws ZosmfRequestException {
        final SystemVariableImport variablesImport = new SystemVariableImport(connection, mockPostRequest);
        final Response response = variablesImport.importVariables("PLEX1", "SYS1", "/path/to/import.csv");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1/actions/import", mockPostRequest.getUrl());
    }

    @Test
    public void tstSystemVariableImportTokenSuccess() throws ZosmfRequestException {
        final SystemVariableImport variablesImport = new SystemVariableImport(connection, mockPostRequestToken);
        final Response response = variablesImport.importVariables("PLEX1", "SYS1", "/path/to/import.csv");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPostRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1/actions/import", mockPostRequestToken.getUrl());
    }

    @Test
    public void tstSystemVariableImportNullSysplexName() {
        final SystemVariableImport variablesImport = new SystemVariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.importVariables(null, "SYS1", "/path/to/import.csv")
        );
        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportEmptySysplexName() {
        final SystemVariableImport variablesImport = new SystemVariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.importVariables("", "SYS1", "/path/to/import.csv")
        );
        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportNullSystemName() {
        final SystemVariableImport variablesImport = new SystemVariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.importVariables("PLEX1", null, "/path/to/import.csv")
        );
        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportEmptySystemName() {
        final SystemVariableImport variablesImport = new SystemVariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.importVariables("PLEX1", "", "/path/to/import.csv")
        );
        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportNullVariablesImportFile() {
        final SystemVariableImport variablesImport = new SystemVariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.importVariables("PLEX1", "SYS1", null)
        );
        assertEquals("variablesImportFile is either null or empty", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportEmptyVariablesImportFile() {
        final SystemVariableImport variablesImport = new SystemVariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.importVariables("PLEX1", "SYS1", "")
        );
        assertEquals("variablesImportFile is either null or empty", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportSecondaryConstructorWithValidRequestType() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        SystemVariableImport variablesImport = new SystemVariableImport(mockConnection, mockRequest);
        assertNotNull(variablesImport);
    }

    @Test
    public void tstSystemVariableImportSecondaryConstructorWithNullConnection() {
        ZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new SystemVariableImport(null, mockRequest)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportSecondaryConstructorWithNullRequest() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new SystemVariableImport(mockConnection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportSecondaryConstructorWithInvalidRequestType() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(ZosmfRequest.class); // Not a PostJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new SystemVariableImport(mockConnection, mockRequest)
        );
        assertEquals("POST_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstSystemVariableImportPrimaryConstructorWithValidConnection() {
        SystemVariableImport variablesImport = new SystemVariableImport(connection);
        assertNotNull(variablesImport);
    }

    @Test
    public void tstSystemVariableImportPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new SystemVariableImport(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
