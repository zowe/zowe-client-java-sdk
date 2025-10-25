/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo.methods;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfinfo.model.DefinedSystem;
import zowe.client.sdk.zosmfinfo.response.ZosmfSystemsResponse;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class containing unit tests for ZosmfSystems.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosmfSystemsTest {

    private ZosConnection mockConnection;
    private GetJsonZosmfRequest mockRequest;
    private ZosmfSystems zosmfSystems;

    private final String json =
            "{\n" +
                    " \"items\":[\n" +
                    "   {\n" +
                    "    \"systemNickName\":\"sys1\",\n" +
                    "    \"systemName\":\"sys1\",\n" +
                    "    \"sysplexName\":\"plex1\",\n" +
                    "    \"groupNames\":\"test,development\",\n" +
                    "    \"url\":\"https://zosmf1.yourco.com/zosmf/\",\n" +
                    "    \"zosVR\":\"z/OS V2R1\",\n" +
                    "    \"jesMemberName\":\"SY1\",\n" +
                    "    \"jesType\":\"JES2\",\n" +
                    "    \"cpcName\":\"\",\n" +
                    "    \"cpcSerial\":\"\",\n" +
                    "    \"httpProxyName\":\"No Proxy\",\n" +
                    "    \"ftpDestinationName\":\"IBM-testcase-mvs\"\n" +
                    "   },\n" +
                    "   {\n" +
                    "    \"systemNickName\":\"sys2\",\n" +
                    "    \"systemName\":\"sys2\",\n" +
                    "    \"sysplexName\":\"plex2\",\n" +
                    "    \"groupNames\":\"production\",\n" +
                    "    \"url\":\"https://zosmf2.yourco.com/zosmf/\",\n" +
                    "    \"zosVR\":\"z/OS V2R1\",\n" +
                    "    \"jesMemberName\":\"SY2\",\n" +
                    "    \"jesType\":\"JES3\",\n" +
                    "    \"cpcName\":\"\",\n" +
                    "    \"cpcSerial\":\"\",\n" +
                    "    \"httpProxyName\":\"No Proxy\",\n" +
                    "    \"ftpDestinationName\":\"IBM-testcase-mvs-sftp\"\n" +
                    "   },\n" +
                    "   {\n" +
                    "    \"systemNickName\":\"sys3\",\n" +
                    "    \"systemName\":\"sys3\",\n" +
                    "    \"sysplexName\":\"plex3\",\n" +
                    "    \"groupNames\":\"test\",\n" +
                    "    \"url\":\"https://zosmf3.yourco.com/zosmf/\",\n" +
                    "    \"zosVR\":\"z/OS V2R1\",\n" +
                    "    \"jesMemberName\":\"SY3\",\n" +
                    "    \"jesType\":\"JES2\",\n" +
                    "    \"cpcName\":\"\",\n" +
                    "    \"cpcSerial\":\"\",\n" +
                    "    \"httpProxyName\":\"No Proxy\",\n" +
                    "    \"ftpDestinationName\":\"IBM-testcase-mvs\"\n" +
                    "   }\n" +
                    " ],\n" +
                    " \"numRows\":\"3\"\n" +
                    "}";

    @BeforeEach
    void setup() {
        mockConnection = mock(ZosConnection.class);
        mockRequest = mock(GetJsonZosmfRequest.class);
        zosmfSystems = new ZosmfSystems(mockConnection, mockRequest);
    }

    private static Stream<DefinedSystemTestData> systemTestData() {
        return Stream.of(
                new DefinedSystemTestData("sys1", "sys1", "plex1", "test,development",
                        "https://zosmf1.yourco.com/zosmf/", "z/OS V2R1", "SY1", "JES2",
                        "", "", "No Proxy", "IBM-testcase-mvs"),
                new DefinedSystemTestData("sys2", "sys2", "plex2", "production",
                        "https://zosmf2.yourco.com/zosmf/", "z/OS V2R1", "SY2", "JES3",
                        "", "", "No Proxy", "IBM-testcase-mvs-sftp"),
                new DefinedSystemTestData("sys3", "sys3", "plex3", "test",
                        "https://zosmf3.yourco.com/zosmf/", "z/OS V2R1", "SY3", "JES2",
                        "", "", "No Proxy", "IBM-testcase-mvs")
        );
    }

    @ParameterizedTest
    @MethodSource("systemTestData")
    void tstAllFieldsAreParsedCorrectlySuccess(DefinedSystemTestData data) throws Exception {
        when(mockRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        ZosmfSystems zosmfSystems = new ZosmfSystems(mockConnection, mockRequest);

        ZosmfSystemsResponse response = zosmfSystems.get();
        DefinedSystem[] systems = response.getDefinedSystems();
        assertNotNull(systems);

        DefinedSystem system = Stream.of(systems)
                .filter(s -> s.getSystemNickName().equals(data.systemNickName))
                .findFirst()
                .orElseThrow();

        assertEquals(data.systemNickName, system.getSystemNickName());
        assertEquals(data.systemName, system.getSystemName());
        assertEquals(data.sysplexName, system.getSysplexName());
        assertEquals(data.groupNames, system.getGroupNames());
        assertEquals(data.url, system.getUrl());
        assertEquals(data.zosVR, system.getZosVR());
        assertEquals(data.jesMemberName, system.getJesMemberName());
        assertEquals(data.jesType, system.getJesType());
        assertEquals(data.cpcName, system.getCpcName());
        assertEquals(data.cpcSerial, system.getCpcSerial());
        assertEquals(data.httpProxyName, system.getHttpProxyName());
        assertEquals(data.ftpDestinationName, system.getFtpDestinationName());
    }

    /**
     * Helper class for test data
     */
    private static class DefinedSystemTestData {
        final String systemNickName;
        final String systemName;
        final String sysplexName;
        final String groupNames;
        final String url;
        final String zosVR;
        final String jesMemberName;
        final String jesType;
        final String cpcName;
        final String cpcSerial;
        final String httpProxyName;
        final String ftpDestinationName;

        DefinedSystemTestData(String systemNickName, String systemName, String sysplexName, String groupNames,
                              String url, String zosVR, String jesMemberName, String jesType,
                              String cpcName, String cpcSerial, String httpProxyName, String ftpDestinationName) {
            this.systemNickName = systemNickName;
            this.systemName = systemName;
            this.sysplexName = sysplexName;
            this.groupNames = groupNames;
            this.url = url;
            this.zosVR = zosVR;
            this.jesMemberName = jesMemberName;
            this.jesType = jesType;
            this.cpcName = cpcName;
            this.cpcSerial = cpcSerial;
            this.httpProxyName = httpProxyName;
            this.ftpDestinationName = ftpDestinationName;
        }
    }

    @Test
    void tstGetReturnsParsedResponseSuccess() throws ZosmfRequestException {
        // Mock connection URL
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf1.yourco.com");

        // Mock request execution to return the sample JSON
        when(mockRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        // Execute the get() method
        ZosmfSystemsResponse response = zosmfSystems.get();

        assertNotNull(response);
        assertEquals(3, response.getNumRows());

        DefinedSystem[] items = response.getDefinedSystems();
        assertEquals(3, items.length);

        assertEquals("sys1", items[0].getSystemNickName());
        assertEquals("sys2", items[1].getSystemNickName());
        assertEquals("sys3", items[2].getSystemNickName());
    }

    @Test
    void tstGetThrowsExceptionWhenResponseEmptyFailure() throws ZosmfRequestException {
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf1.yourco.com");
        when(mockRequest.executeRequest()).thenReturn(
                new Response("", 200, "success"));

        ZosmfRequestException ex = assertThrows(ZosmfRequestException.class, () -> {
            zosmfSystems.get();
        });

        String errMsg = "Failed to parse JSON response for [get] into ZosmfSystemsResponse";
        assertEquals(errMsg, ex.getMessage());
    }

    @Test
    void tstConstructorThrowsOnInvalidRequestTypeFailure() {
        PutJsonZosmfRequest invalidRequest = mock(PutJsonZosmfRequest.class);
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> new ZosmfSystems(mockConnection, invalidRequest));
        assertEquals("GET_JSON request type required", ex.getMessage());
    }

    @Test
    void tstGetThrowsExceptionWhenInvalidResponseEmptyFailure() throws ZosmfRequestException {
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf1.yourco.com");
        when(mockRequest.executeRequest()).thenReturn(
                new Response("{{", 200, "success"));

        ZosmfRequestException ex = assertThrows(ZosmfRequestException.class,
                () -> zosmfSystems.get());

        String errMsg = "Failed to parse JSON response for [get] into ZosmfSystemsResponse";
        assertEquals(errMsg, ex.getMessage());
    }

}
