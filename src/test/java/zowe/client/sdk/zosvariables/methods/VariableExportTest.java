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
 * Class containing unit tests for VariableExport.
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class VariableExportTest {

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
                new Response("{}", 204, "No Content"));
        doCallRealMethod().when(mockPostRequest).setUrl(any());
        doCallRealMethod().when(mockPostRequest).getUrl();
        doCallRealMethod().when(mockPostRequest).setBody(any());

        mockPostRequestToken = Mockito.mock(PostJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPostRequestToken.executeRequest()).thenReturn(
                new Response("{}", 204, "No Content"));
        doCallRealMethod().when(mockPostRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPostRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPostRequestToken).setUrl(any());
        doCallRealMethod().when(mockPostRequestToken).getHeaders();
        doCallRealMethod().when(mockPostRequestToken).getUrl();
        doCallRealMethod().when(mockPostRequestToken).setBody(any());
    }

    @Test
    public void tstVariableExportSuccess() throws ZosmfRequestException {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        final Response response = variablesExport.export("PLEX1", "SYS1", "/path/to/export.csv");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1/actions/export", mockPostRequest.getUrl());
        Mockito.verify(mockPostRequest).setBody("{\"overwrite\":false,\"variables-export-file\":\"/path/to/export.csv\"}");
    }

    @Test
    public void tstVariableExportWithOverwriteTrueSuccess() throws ZosmfRequestException {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        final Response response = variablesExport.export("PLEX1", "SYS1", "/path/to/export.csv", true);
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1/actions/export", mockPostRequest.getUrl());
        Mockito.verify(mockPostRequest).setBody("{\"overwrite\":true,\"variables-export-file\":\"/path/to/export.csv\"}");
    }

    @Test
    public void tstVariableExportWithOverwriteFalseSuccess() throws ZosmfRequestException {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        final Response response = variablesExport.export("PLEX1", "SYS1", "/path/to/export.csv", false);
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1/actions/export", mockPostRequest.getUrl());
        Mockito.verify(mockPostRequest).setBody("{\"overwrite\":false,\"variables-export-file\":\"/path/to/export.csv\"}");
    }

    @Test
    public void tstVariableExportTokenSuccess() throws ZosmfRequestException {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequestToken);
        final Response response = variablesExport.export("PLEX1", "SYS1", "/path/to/export.csv");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPostRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1/actions/export", mockPostRequestToken.getUrl());
        Mockito.verify(mockPostRequestToken).setBody("{\"overwrite\":false,\"variables-export-file\":\"/path/to/export.csv\"}");
    }

    @Test
    public void tstVariableExportNullSysplexNameFailure() {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesExport.export(null, "SYS1", "/path/to/export.csv")
        );
        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableExportEmptySysplexNameFailure() {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesExport.export("", "SYS1", "/path/to/export.csv")
        );
        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableExportNullSystemNameFailure() {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesExport.export("PLEX1", null, "/path/to/export.csv")
        );
        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableExportEmptySystemNameFailure() {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesExport.export("PLEX1", "", "/path/to/export.csv")
        );
        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableExportNullVariablesDataFileFailure() {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesExport.export("PLEX1", "SYS1", null)
        );
        assertEquals("targetFile is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableExportEmptyVariablesDataFileFailure() {
        final VariableExport variablesExport = new VariableExport(connection, mockPostRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> variablesExport.export("PLEX1", "SYS1", "")
        );
        assertEquals("targetFile is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableExportSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        VariableExport variablesExport = new VariableExport(mockConnection, mockRequest);
        assertNotNull(variablesExport);
    }

    @Test
    public void tstVariableExportSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new VariableExport(null, mockRequest)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstVariableExportSecondaryConstructorWithNullRequestFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new VariableExport(mockConnection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstVariableExportSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new VariableExport(mockConnection, mockRequest)
        );
        assertEquals("POST_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstVariableExportPrimaryConstructorWithValidConnectionSuccess() {
        VariableExport variablesExport = new VariableExport(connection);
        assertNotNull(variablesExport);
    }

    @Test
    public void tstVariableExportPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new VariableExport(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
