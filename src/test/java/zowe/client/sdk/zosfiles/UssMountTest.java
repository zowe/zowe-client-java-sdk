/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import zowe.client.sdk.core.AuthenicationType;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.MountParams;
import zowe.client.sdk.zosfiles.uss.methods.UssMount;
import zowe.client.sdk.zosfiles.uss.types.MountActionType;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssMount.
 *
 * @author Frank Giordano
 * @version 3.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssMountTest {

    private final ZosConnection connection = new ZosConnection.Builder(AuthenicationType.CLASSIC)
            .host("1").password("1").user("1").zosmfPort("1").build();
    private final ZosConnection cookieConnection = new ZosConnection.Builder(AuthenicationType.COOKIE)
            .host("1").zosmfPort("1").cookie(new Cookie("hello=hello")).build();
    private UssMount ussMount;

    @Before
    public void init() {
        ussMount = new UssMount(connection);
    }

    @Test
    public void tstUssMountSuccess() throws ZosmfRequestException {
        final PutJsonZosmfRequest mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        final UssMount ussMount = new UssMount(connection, mockJsonPutRequest);
        final Response response = ussMount.mount("name", "mountpoint", "fstype");
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssMountToggleAuthSuccess() throws ZosmfRequestException {
        final PutJsonZosmfRequest mockJsonPutRequestAuth = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(cookieConnection));
        Mockito.when(mockJsonPutRequestAuth.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestAuth).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPutRequestAuth).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestAuth).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestAuth).getHeaders();

        final UssMount ussMount = new UssMount(cookieConnection, mockJsonPutRequestAuth);
        Response response = ussMount.mount("name", "mountpoint", "fstype");
        Assertions.assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestAuth.getHeaders().toString());
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssMountCommonCountActionWithNoFsTypeFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mountCommon("name",
                    new MountParams.Builder().action(MountActionType.MOUNT).mountPoint("mountpoint").build());
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
                    new MountParams.Builder().action(MountActionType.MOUNT).fsType("fstype").build());
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
                    new MountParams.Builder().action(MountActionType.MOUNT).mountPoint("mountpoint").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName not specified", errMsg);
    }

    @Test
    public void tstUssMountCommonNullFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mountCommon(null,
                    new MountParams.Builder().action(MountActionType.MOUNT).mountPoint("mountpoint").build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is null", errMsg);
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
            ussMount.mountCommon("name", new MountParams.Builder().build());
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
        assertEquals("fileSystemName not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyFilSystemNameWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.mount("   ", "mount", "hfs");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName not specified", errMsg);
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
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is null", errMsg);
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
        assertEquals("fileSystemName not specified", errMsg);
    }

    @Test
    public void tstUssMountUnMountEmptyFileSystemNameWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.unmount("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName not specified", errMsg);
    }

    @Test
    public void tstUssMountUnMountNullFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussMount.unmount(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is null", errMsg);
    }

}
