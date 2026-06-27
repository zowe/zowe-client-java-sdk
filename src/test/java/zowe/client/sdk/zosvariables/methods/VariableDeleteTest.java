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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for VariableDelete.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class VariableDeleteTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private DeleteJsonZosmfRequest mockDeleteRequest;
    private DeleteJsonZosmfRequest mockDeleteRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockDeleteRequest.executeRequest()).thenReturn(
                new Response("No Content", 204, "No Content"));
        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();
        doCallRealMethod().when(mockDeleteRequest).setBody(any());

        mockDeleteRequestToken = Mockito.mock(DeleteJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockDeleteRequestToken.executeRequest()).thenReturn(
                new Response("No Content", 204, "No Content"));
        doCallRealMethod().when(mockDeleteRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockDeleteRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockDeleteRequestToken).setUrl(any());
        doCallRealMethod().when(mockDeleteRequestToken).getHeaders();
        doCallRealMethod().when(mockDeleteRequestToken).getUrl();
        doCallRealMethod().when(mockDeleteRequestToken).setBody(any());
    }

    @Test
    public void tstVariableDeletePoolSuccess() throws ZosmfRequestException {
        final VariableDelete variableDelete = new VariableDelete(connection, mockDeleteRequest);
        final Response response = variableDelete.delete("PLEX1", "SYS1");

        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1", mockDeleteRequest.getUrl());
        // whole-pool delete issues a bodyless request; any prior body is cleared
        verify(mockDeleteRequest).setBody(null);
    }

    @Test
    public void tstVariableDeletePoolClearsPriorBodySuccess() throws ZosmfRequestException {
        final VariableDelete variableDelete = new VariableDelete(connection, mockDeleteRequest);
        // a specific-variable delete sets a body, then a whole-pool delete on the same instance must clear it
        variableDelete.delete("PLEX1", "SYS1", Collections.singletonList("var1"));
        variableDelete.delete("PLEX1", "SYS1");

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockDeleteRequest, times(2)).setBody(bodyCaptor.capture());
        assertEquals("[\"var1\"]", bodyCaptor.getAllValues().get(0).toString());
        assertNull(bodyCaptor.getAllValues().get(1));
    }

    @Test
    public void tstVariableDeleteVariablesSuccess() throws ZosmfRequestException {
        final VariableDelete variableDelete = new VariableDelete(connection, mockDeleteRequest);
        final Response response = variableDelete.delete("PLEX1", "SYS1", Arrays.asList("var1", "var2"));

        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1", mockDeleteRequest.getUrl());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockDeleteRequest).setBody(bodyCaptor.capture());
        assertEquals("[\"var1\",\"var2\"]", bodyCaptor.getValue().toString());
    }

    @Test
    public void tstVariableDeleteVariablesTokenSuccess() throws ZosmfRequestException {
        final VariableDelete variableDelete = new VariableDelete(connection, mockDeleteRequestToken);
        variableDelete.delete("PLEX1", "SYS1", Collections.singletonList("var1"));

        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockDeleteRequestToken.getHeaders().toString());
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1", mockDeleteRequestToken.getUrl());
    }

    @Test
    public void tstVariableDeleteNullSysplexNameFailure() {
        final VariableDelete variableDelete = new VariableDelete(connection, mockDeleteRequest);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> variableDelete.delete(null, "SYS1"));
        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableDeleteEmptySystemNameFailure() {
        final VariableDelete variableDelete = new VariableDelete(connection, mockDeleteRequest);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> variableDelete.delete("PLEX1", ""));
        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableDeleteNullVariableNamesFailure() {
        final VariableDelete variableDelete = new VariableDelete(connection, mockDeleteRequest);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> variableDelete.delete("PLEX1", "SYS1", null));
        assertEquals("variableNames is null", exception.getMessage());
    }

    @Test
    public void tstVariableDeleteEmptyVariableNamesFailure() {
        final VariableDelete variableDelete = new VariableDelete(connection, mockDeleteRequest);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> variableDelete.delete("PLEX1", "SYS1", Collections.emptyList()));
        assertEquals("variableNames is empty", exception.getMessage());
    }

    @Test
    public void tstVariableDeleteSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        assertNotNull(new VariableDelete(mockConnection, mockRequest));
    }

    @Test
    public void tstVariableDeleteSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest mockRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new VariableDelete(null, mockRequest));
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstVariableDeleteSecondaryConstructorWithNullRequestFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new VariableDelete(mockConnection, null));
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstVariableDeleteSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new VariableDelete(mockConnection, mockRequest));
        assertEquals("DELETE_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstVariableDeletePrimaryConstructorWithValidConnectionSuccess() {
        assertNotNull(new VariableDelete(connection));
    }

    @Test
    public void tstVariableDeletePrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new VariableDelete(null));
        assertEquals("connection is null", exception.getMessage());
    }

}
