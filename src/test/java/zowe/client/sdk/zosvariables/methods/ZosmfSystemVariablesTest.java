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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosvariables.input.SystemVariableSource;
import zowe.client.sdk.zosvariables.input.SystemVariablesInputData;
import zowe.client.sdk.zosvariables.model.SystemVariable;
import zowe.client.sdk.zosvariables.response.SystemVariablesResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ZosmfSystemVariablesTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "user", "pass");

    private GetJsonZosmfRequest mockJsonGetRequest;

    @BeforeEach
    void init() {
        mockJsonGetRequest = mock(GetJsonZosmfRequest.class);
    }

    @Test
    void tstGetLocalVariablesUrlSuccess() throws ZosmfRequestException {
        String json = "{ \"system-variable-list\": [] }";

        when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection, mockJsonGetRequest);
        SystemVariablesResponse response = systemVariables.getLocal();

        assertNotNull(response);
        assertEquals(0, response.getSystemVariableList().length);
        verify(mockJsonGetRequest).setUrl(
                "https://1:443/zosmf/variables/rest/1.0/systems/local");
    }

    @Test
    void tstGetSystemVariablesUrlSuccess() throws ZosmfRequestException {
        String json = "{ \"system-variable-list\": [] }";

        when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        SystemVariablesInputData inputData = new SystemVariablesInputData("PLEX1", "SYS1");

        ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection, mockJsonGetRequest);
        SystemVariablesResponse response = systemVariables.get(inputData);

        assertNotNull(response);
        verify(mockJsonGetRequest).setUrl(
                "https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1");
    }

    @Test
    void tstGetSystemSymbolsWithVariableNamesUrlSuccess() throws ZosmfRequestException {
        String json = "{ \"system-symbol-list\": [] }";

        when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        SystemVariablesInputData inputData = new SystemVariablesInputData("PLEX1", "SYS1")
                .withSource(SystemVariableSource.SYMBOL)
                .withVariableNames("SYSNAME", "SYSPLEX");

        ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection, mockJsonGetRequest);
        SystemVariablesResponse response = systemVariables.get(inputData);

        assertNotNull(response);
        verify(mockJsonGetRequest).setUrl(
                "https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1" +
                        "?source=symbol&var-name=SYSNAME&var-name=SYSPLEX");
    }

    @Test
    void tstGetSystemVariablesParsesVariableListSuccess() throws ZosmfRequestException {
        String json = "{ \"system-variable-list\": [" +
                "{ \"name\": \"sample1\", \"value\": \"20\", \"description\": \"value of sample1\" }" +
                "] }";

        when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection, mockJsonGetRequest);
        SystemVariablesResponse response = systemVariables.getLocal();

        SystemVariable[] variables = response.getSystemVariableList();

        assertEquals(1, variables.length);
        assertEquals("sample1", variables[0].getName());
        assertEquals("20", variables[0].getValue());
        assertEquals("value of sample1", variables[0].getDescription());
        assertEquals(0, response.getSystemSymbolList().length);
    }

    @Test
    void tstGetSystemVariablesParsesUppercaseDescriptionSuccess() throws ZosmfRequestException {
        String json = "{ \"system-variable-list\": [" +
                "{ \"name\": \"sample1\", \"value\": \"20\", \"Description\": \"value of sample1\" }" +
                "] }";

        when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection, mockJsonGetRequest);
        SystemVariablesResponse response = systemVariables.getLocal();

        SystemVariable[] variables = response.getSystemVariableList();

        assertEquals(1, variables.length);
        assertEquals("value of sample1", variables[0].getDescription());
    }

    @Test
    void tstGetSystemVariablesParsesSymbolListSuccess() throws ZosmfRequestException {
        String json = "{ \"system-symbol-list\": [" +
                "{ \"name\": \"SYSNAME\", \"value\": \"SYS1\", \"description\": \"\" }" +
                "] }";

        when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        SystemVariablesInputData inputData = new SystemVariablesInputData()
                .withSource(SystemVariableSource.SYMBOL);

        ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection, mockJsonGetRequest);
        SystemVariablesResponse response = systemVariables.get(inputData);

        SystemVariable[] symbols = response.getSystemSymbolList();

        assertEquals(1, symbols.length);
        assertEquals("SYSNAME", symbols[0].getName());
        assertEquals("SYS1", symbols[0].getValue());
        assertEquals("", symbols[0].getDescription());
        assertEquals(0, response.getSystemVariableList().length);
    }

    @Test
    void tstConstructorThrowsOnInvalidRequestTypeFailure() {
        PutJsonZosmfRequest invalidRequest = mock(PutJsonZosmfRequest.class);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> new ZosmfSystemVariables(connection, invalidRequest));

        assertEquals("GET_JSON request type required", ex.getMessage());
    }

    @Test
    void tstGetThrowsExceptionWhenInvalidResponseFailure() throws ZosmfRequestException {
        when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response("{{", 200, "success"));

        ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection, mockJsonGetRequest);

        ZosmfRequestException ex = assertThrows(ZosmfRequestException.class, systemVariables::getLocal);

        assertEquals("Failed to parse JSON response for [get] into SystemVariablesResponse", ex.getMessage());
    }

    @Test
    void tstInputDataThrowsOnMissingSysplexFailure() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new SystemVariablesInputData("", "SYS1"));

        assertEquals("sysplexName is either null or empty", ex.getMessage());
    }

    @Test
    void tstInputDataThrowsOnMissingSystemFailure() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new SystemVariablesInputData("PLEX1", ""));

        assertEquals("systemName is either null or empty", ex.getMessage());
    }

}