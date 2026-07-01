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

import kong.unirest.core.Cookie;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssGetAcl.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class UssGetAclTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestToken;
    public UssGetAcl ussGetAcl;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequest).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequest).getUrl();

        mockJsonPutRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonPutRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).getUrl();

        ussGetAcl = new UssGetAcl(connection);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void tstUssGetAclGetSuccess() throws ZosmfRequestException {
        final UssGetAcl ussGetAcl = new UssGetAcl(connection, mockJsonPutRequest);
        final JSONArray stdout = new JSONArray();
        stdout.add("#file: /etc/inetd.conf");
        stdout.add("#owner: USER1");
        stdout.add("#group: GROUP1");
        stdout.add("user::rwx");
        stdout.add("group::r-x");
        stdout.add("other::r--");
        final JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("stdout", stdout);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(jsonResponse, 200, "success"));

        final List<String> attributes = ussGetAcl.get("/etc/inetd.conf", false);

        assertEquals(List.of(
                "#file: /etc/inetd.conf",
                "#owner: USER1",
                "#group: GROUP1",
                "user::rwx",
                "group::r-x",
                "other::r--"
        ), attributes);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void tstUssGetAclGetWithCommasSuccess() throws ZosmfRequestException {
        final UssGetAcl ussGetAcl = new UssGetAcl(connection, mockJsonPutRequest);
        final JSONArray stdout = new JSONArray();
        stdout.add("user::rwx,group::r-x,other::r--");
        final JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("stdout", stdout);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(jsonResponse, 200, "success"));

        final List<String> attributes = ussGetAcl.get("/etc/inetd.conf", true);

        assertEquals(List.of("user::rwx,group::r-x,other::r--"), attributes);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void tstUssGetAclGetEmptyStdoutSuccess() throws ZosmfRequestException {
        final UssGetAcl ussGetAcl = new UssGetAcl(connection, mockJsonPutRequest);
        final JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("stdout", new JSONArray());
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(jsonResponse, 200, "success"));

        final List<String> attributes = ussGetAcl.get("/etc/inetd.conf", false);

        assertTrue(attributes.isEmpty());
    }

    @Test
    public void tstUssGetAclGetNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGetAcl.get(null, false);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is either null or empty", errMsg);
    }

    @Test
    public void tstUssGetAclNullConnectionFailure() {
        try {
            new UssGetAcl(null);
        } catch (NullPointerException e) {
            assertEquals("connection is null", e.getMessage());
        }
    }

    @Test
    public void tstUssGetAclSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        UssGetAcl ussGetAcl = new UssGetAcl(connection, request);
        assertNotNull(ussGetAcl);
    }

    @Test
    public void tstUssGetAclSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssGetAcl(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstUssGetAclSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssGetAcl(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstUssGetAclSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new UssGetAcl(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

}
