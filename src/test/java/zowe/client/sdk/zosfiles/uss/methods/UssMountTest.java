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
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.MountInputData;
import zowe.client.sdk.zosfiles.uss.types.MountActionType;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssMount.
 *
 * @author Frank Giordano
 * @version 4.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssMountTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestToken;
    private UssMount ussMount;

    @Before
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

        ussMount = new UssMount(connection);
    }

    @Test
    public void tstUssMountSuccess() throws ZosmfRequestException {
        final UssMount ussMount = new UssMount(connection, mockJsonPutRequest);
        final Response response = ussMount.mount("name", "mountpoint", "fstype");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/mfs/name", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssMountToggleTokenSuccess() throws ZosmfRequestException {
        final UssMount ussMount = new UssMount(tokenConnection, mockJsonPutRequestToken);
        Response response = ussMount.mount("name", "mountpoint", "fstype");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/mfs/name", mockJsonPutRequestToken.getUrl());
    }

    @Test
    public void tstUssMountCommonCountActionWithNoFsTypeFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mountCommon("name",
                    new MountInputData.Builder().action(MountActionType.MOUNT).mountPoint("mountpoint").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsType not specified", errMsg);
    }

    @Test
    public void tstUssMountCommonCountActionWithNoMountPointFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mountCommon("name",
                    new MountInputData.Builder().action(MountActionType.MOUNT).fsType("fstype").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("mountPoint not specified", errMsg);
    }

    @Test
    public void tstUssMountCommonEmptyFileSystemNameFailure() {
        String errMsg = "";
        try {
            ussMount.mountCommon("",
                    new MountInputData.Builder().action(MountActionType.MOUNT).mountPoint("mountpoint").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is either null or empty", errMsg);
    }

    @Test
    public void tstUssMountCommonNullFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mountCommon(null,
                    new MountInputData.Builder().action(MountActionType.MOUNT).mountPoint("mountpoint").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is either null or empty", errMsg);
    }

    @Test
    public void tstUssMountCommonNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mountCommon("name", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssMountEmptyActionFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mountCommon("name", new MountInputData.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params action not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyFilSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("", "mount", "hfs");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is either null or empty", errMsg);
    }

    @Test
    public void tstUssMountEmptyFilSystemNameWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("   ", "mount", "hfs");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is either null or empty", errMsg);
    }

    @Test
    public void tstUssMountEmptyFsTypeFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("name", "mount", "");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsType not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyFsTypeWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("name", "mount", "   ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsType not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyMountPointFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("name", "", "hfs");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("mountPoint not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyMountPointWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("name", "   ", "hfs");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("mountPoint not specified", errMsg);
    }

    @Test
    public void tstUssMountNullFilSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount(null, "mount", "hfs");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is either null or empty", errMsg);
    }

    @Test
    public void tstUssMountNullFsTypeFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("name", "mount", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsType is null", errMsg);
    }

    @Test
    public void tstUssMountNullMountPointFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("name", null, "hfs");
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("mountPoint is null", errMsg);
    }

    @Test
    public void tstUssMountUnMountEmptyFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.unmount("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is either null or empty", errMsg);
    }

    @Test
    public void tstUssMountUnMountEmptyFileSystemNameWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.unmount("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is either null or empty", errMsg);
    }

    @Test
    public void tstUssMountUnMountNullFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.unmount(null);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is either null or empty", errMsg);
    }

    @Test
    public void tstUssMountNullConnectionFailure() {
        try {
            new UssMount(null);
        } catch (NullPointerException e) {
            assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

    @Test
    public void tstUssMountSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        UssMount ussChangeMode = new UssMount(connection, request);
        assertNotNull(ussChangeMode);
    }

    @Test
    public void tstUssMountSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssMount(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstUssMountSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssMount(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstUssMountSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new UssMount(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

}
