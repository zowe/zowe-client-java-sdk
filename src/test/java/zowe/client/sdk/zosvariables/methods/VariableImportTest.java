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
 * Class containing unit tests for VariableImport.
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class VariableImportTest {

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
    public void tstVariableImportSuccess() throws ZosmfRequestException {
        final VariableImport variablesImport = new VariableImport(connection, mockPostRequest);
        final Response response = variablesImport.load("PLEX1", "SYS1", "/path/to/import.csv");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1/actions/import", mockPostRequest.getUrl());
    }

    @Test
    public void tstVariableImportTokenSuccess() throws ZosmfRequestException {
        final VariableImport variablesImport = new VariableImport(connection, mockPostRequestToken);
        final Response response = variablesImport.load("PLEX1", "SYS1", "/path/to/import.csv");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPostRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1/actions/import", mockPostRequestToken.getUrl());
    }

    @Test
    public void tstVariableImportNullSysplexNameFailure() {
        final VariableImport variablesImport = new VariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.load(null, "SYS1", "/path/to/import.csv")
        );
        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableImportEmptySysplexNameFailure() {
        final VariableImport variablesImport = new VariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.load("", "SYS1", "/path/to/import.csv")
        );
        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableImportNullSystemNameFailure() {
        final VariableImport variablesImport = new VariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.load("PLEX1", null, "/path/to/import.csv")
        );
        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableImportEmptySystemNameFailure() {
        final VariableImport variablesImport = new VariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.load("PLEX1", "", "/path/to/import.csv")
        );
        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableImportNullVariablesDataFileFailure() {
        final VariableImport variablesImport = new VariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.load("PLEX1", "SYS1", null)
        );
        assertEquals("variablesImportFile is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableImportEmptyVariablesDataFileFailure() {
        final VariableImport variablesImport = new VariableImport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesImport.load("PLEX1", "SYS1", "")
        );
        assertEquals("variablesImportFile is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableImportSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        VariableImport variablesImport = new VariableImport(mockConnection, mockRequest);
        assertNotNull(variablesImport);
    }

    @Test
    public void tstVariableImportSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new VariableImport(null, mockRequest)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstVariableImportSecondaryConstructorWithNullRequestFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new VariableImport(mockConnection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstVariableImportSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new VariableImport(mockConnection, mockRequest)
        );
        assertEquals("POST_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstVariableImportPrimaryConstructorWithValidConnectionSuccess() {
        VariableImport variablesImport = new VariableImport(connection);
        assertNotNull(variablesImport);
    }

    @Test
    public void tstVariableImportPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new VariableImport(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
