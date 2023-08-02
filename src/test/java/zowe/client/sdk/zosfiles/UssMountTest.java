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

    private ZosConnection connection;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssMountSuccess() throws Exception {
        JsonPutRequest jsonPutRequest = Mockito.mock(JsonPutRequest.class);
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssMount ussMount = new UssMount(connection, jsonPutRequest);
        Response response = ussMount.mount("name", "mountpoint", "fstype");
        Assertions.assertEquals("{}", response.getResponsePhrase().get().toString());
        Assertions.assertEquals("200", response.getStatusCode().get().toString());
        Assertions.assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssMountCommonCountActionWithNoFsTypeFailure() {
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
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
        UssMount ussMount = new UssMount(connection);
        String errMsg = "";
        try {
            ussMount.unmount(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name is null", errMsg);
    }

}
