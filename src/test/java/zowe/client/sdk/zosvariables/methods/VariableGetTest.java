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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.zosvariables.input.factory.VariableGetInputData;
import zowe.client.sdk.zosvariables.input.factory.VariableGetInputFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for VariableGet.
 *
 * @author Adithe Das
 * @version 7.0
 */

public class VariableGetTest {
    private final ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");

    @Test
    public void tstVariableGetPrimaryConstructorWithValidConnectionSuccess() {
        VariableGet variableGet = new VariableGet(connection);

        assertNotNull(variableGet);
    }

    @Test
    public void tstVariableGetPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(NullPointerException.class,() -> new VariableGet(null));

        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstVariableGetSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(GetJsonZosmfRequest.class);
        VariableGet variableGet = new VariableGet(mockConnection, mockRequest);

        assertNotNull(variableGet);
    }

    @Test
    public void tstVariableGetSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest mockRequest = Mockito.mock(GetJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(NullPointerException.class,() -> new VariableGet(null, mockRequest));

        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstVariableGetSecondaryConstructorWithNullRequestFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(NullPointerException.class,() -> new VariableGet(mockConnection, null));

        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstVariableGetSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        ZosmfRequest mockRequest = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(IllegalStateException.class,() -> new VariableGet(mockConnection, mockRequest));

        assertEquals("GET_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstVariableGetNullSysplexNameFailure() {
        VariableGet variableGet = new VariableGet(connection);
        VariableGetInputData input = VariableGetInputFactory.createSystemInput(null, "SYS1");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> variableGet.get(input));

        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableGetEmptySysplexNameFailure() {
        VariableGet variableGet = new VariableGet(connection);
        VariableGetInputData input = VariableGetInputFactory.createSystemInput("", "SYS1");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> variableGet.get(input));

        assertEquals("sysplexName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableGetNullSystemNameFailure() {
        VariableGet variableGet = new VariableGet(connection);
        VariableGetInputData input = VariableGetInputFactory.createSystemInput("PLEX1", null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> variableGet.get(input));

        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableGetEmptySystemNameFailure() {
        VariableGet variableGet = new VariableGet(connection);
        VariableGetInputData input = VariableGetInputFactory.createSystemInput("PLEX1", "");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> variableGet.get(input));

        assertEquals("systemName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstVariableGetSuccess() throws Exception {
        GetJsonZosmfRequest mockRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockRequest.executeRequest()).thenReturn(new zowe.client.sdk.rest.Response("{\"system-variable-list\":[]}", 200, "OK"));
        Mockito.doCallRealMethod().when(mockRequest).setUrl(Mockito.any());
        Mockito.doCallRealMethod().when(mockRequest).getUrl();

        VariableGet variableGet = new VariableGet(connection, mockRequest);
        VariableGetInputData input = VariableGetInputFactory.createSystemInput("PLEX1", "SYS1");

        assertNotNull(variableGet.get(input));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1", mockRequest.getUrl());
    }

    @Test
    public void tstVariableGetLocalSuccess() throws Exception {
        GetJsonZosmfRequest mockRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockRequest.executeRequest()).thenReturn(new zowe.client.sdk.rest.Response("{\"system-variable-list\":[]}", 200, "OK"));
        Mockito.doCallRealMethod().when(mockRequest).setUrl(Mockito.any());
        Mockito.doCallRealMethod().when(mockRequest).getUrl();

        VariableGet variableGet = new VariableGet(connection, mockRequest);
        VariableGetInputData input = VariableGetInputFactory.createLocalInput();

        assertNotNull(variableGet.get(input));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/local", mockRequest.getUrl());
    }

    @Test
    public void tstVariableGetInvalidJsonFailure() throws Exception {
        GetJsonZosmfRequest mockRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockRequest.executeRequest()).thenReturn(new zowe.client.sdk.rest.Response("{{", 200, "OK"));

        VariableGet variableGet = new VariableGet(connection, mockRequest);

        VariableGetInputData input = VariableGetInputFactory.createSystemInput("PLEX1", "SYS1");
        assertThrows(zowe.client.sdk.rest.exception.ZosmfRequestException.class, () -> variableGet.get(input));
    }

    @Test
    public void tstVariableGetWithQueryParametersSuccess() throws Exception {
        GetJsonZosmfRequest mockRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockRequest.executeRequest()).thenReturn(new zowe.client.sdk.rest.Response("{\"system-variable-list\":[]}", 200, "OK"));
        Mockito.doCallRealMethod().when(mockRequest).setUrl(Mockito.any());
        Mockito.doCallRealMethod().when(mockRequest).getUrl();

        VariableGet variableGet = new VariableGet(connection, mockRequest);
        VariableGetInputData input = VariableGetInputFactory.createSystemInput("PLEX1", "SYS1", java.util.Arrays.asList("VAR1", "VAR2"), "variable");

        assertNotNull(variableGet.get(input));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1?var-name=VAR1&var-name=VAR2&variable-type=variable", mockRequest.getUrl());
    }

}
