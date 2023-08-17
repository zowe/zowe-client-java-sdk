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

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.input.MountParams;
import zowe.client.sdk.zosfiles.uss.methods.UssMount;
import zowe.client.sdk.zosfiles.uss.types.MountActionType;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssMount.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssMountTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private UssMount ussMount;

    @Before
    public void init() {
        ussMount = new UssMount(connection);
    }

    @Test
    public void tstUssMountSuccess() throws Exception {
        final JsonPutRequest mockJsonPutRequest = Mockito.mock(JsonPutRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        final UssMount ussMount = new UssMount(connection, mockJsonPutRequest);
        final Response response = ussMount.mount("name", "mountpoint", "fstype");
        Assertions.assertEquals("{}", response.getResponsePhrase().get().toString());
        Assertions.assertEquals(200, response.getStatusCode().getAsInt());
        Assertions.assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssMountCommonCountActionWithNoFsTypeFailure() {
        String errMsg = "";
        try {
            ussMount.mountCommon("name",
                    new MountParams.Builder().action(MountActionType.MOUNT).mountPoint("mountpoint").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsType not specified", errMsg);
    }

    @Test
    public void tstUssMountCommonCountActionWithNoMountPointFailure() {
        String errMsg = "";
        try {
            ussMount.mountCommon("name",
                    new MountParams.Builder().action(MountActionType.MOUNT).fsType("fstype").build());
        } catch (Exception e) {
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
        assertEquals("file system name not specified", errMsg);
    }

    @Test
    public void tstUssMountCommonNullFileSystemNameFailure() {
        String errMsg = "";
        try {
            ussMount.mountCommon(null,
                    new MountParams.Builder().action(MountActionType.MOUNT).mountPoint("mountpoint").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name is null", errMsg);
    }

    @Test
    public void tstUssMountCommonNullParamsFailure() {
        String errMsg = "";
        try {
            ussMount.mountCommon("name", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssMountEmptyActionFailure() {
        String errMsg = "";
        try {
            ussMount.mountCommon("name", new MountParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params action not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyFilSystemNameFailure() {
        String errMsg = "";
        try {
            ussMount.mount("", "mount", "hfs");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyFilSystemNameWithSpacesFailure() {
        String errMsg = "";
        try {
            ussMount.mount("   ", "mount", "hfs");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyFsTypeFailure() {
        String errMsg = "";
        try {
            ussMount.mount("name", "mount", "");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsType not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyFsTypeWithSpacesFailure() {
        String errMsg = "";
        try {
            ussMount.mount("name", "mount", "   ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsType not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyMountPointFailure() {
        String errMsg = "";
        try {
            ussMount.mount("name", "", "hfs");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mountPoint not specified", errMsg);
    }

    @Test
    public void tstUssMountEmptyMountPointWithSpacesFailure() {
        String errMsg = "";
        try {
            ussMount.mount("name", "   ", "hfs");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mountPoint not specified", errMsg);
    }

    @Test
    public void tstUssMountNullFilSystemNameFailure() {
        String errMsg = "";
        try {
            ussMount.mount(null, "mount", "hfs");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name is null", errMsg);
    }

    @Test
    public void tstUssMountNullFsTypeFailure() {
        String errMsg = "";
        try {
            ussMount.mount("name", "mount", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsType is null", errMsg);
    }

    @Test
    public void tstUssMountNullMountPointFailure() {
        String errMsg = "";
        try {
            ussMount.mount("name", null, "hfs");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mountPoint is null", errMsg);
    }

    @Test
    public void tstUssMountUnMountEmptyFileSystemNameFailure() {
        String errMsg = "";
        try {
            ussMount.unmount("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name not specified", errMsg);
    }

    @Test
    public void tstUssMountUnMountEmptyFileSystemNameWithSpacesFailure() {
        String errMsg = "";
        try {
            ussMount.unmount("  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name not specified", errMsg);
    }

    @Test
    public void tstUssMountUnMountNullFileSystemNameFailure() {
        String errMsg = "";
        try {
            ussMount.unmount(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name is null", errMsg);
    }

}
