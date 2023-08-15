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
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonDeleteRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.methods.UssDelete;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssDelete.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssDeleteTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private JsonDeleteRequest mockJsonDeleteRequest;
    private UssDelete ussDelete;

    @Before
    public void init() {
        mockJsonDeleteRequest = Mockito.mock(JsonDeleteRequest.class);
        Mockito.when(mockJsonDeleteRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        ussDelete = new UssDelete(connection);
    }

    @Test
    public void tstUssDeleteSuccess() throws Exception {
        final UssDelete ussDelete = new UssDelete(connection, mockJsonDeleteRequest);
        final Response response = ussDelete.delete("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssDeleteRecursiveSuccess() throws Exception {
        final UssDelete ussDelete = new UssDelete(connection, mockJsonDeleteRequest);
        final Response response = ussDelete.delete("/xxx/xx/xx", true);
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssDeleteInvalidTargetPathFailure() {
        String errMsg = "";
        try {
            ussDelete.delete("name");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssDeleteInvalidTargetPathWithRecursiveFailure() {
        String errMsg = "";
        try {
            ussDelete.delete("name", true);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssDeleteNullTargetPathFailure() {
        String errMsg = "";
        try {
            ussDelete.delete(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssDeleteEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussDelete.delete("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssDeleteEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussDelete.delete("   ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssDeleteNullTargetPathWithRecursiveFailure() {
        String errMsg = "";
        try {
            ussDelete.delete(null, true);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssDeleteEmptyTargetPathWithRecursiveFailure() {
        String errMsg = "";
        try {
            ussDelete.delete("", true);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssDeleteZfsNullFileSystemNameFailure() {
        String errMsg = "";
        try {
            ussDelete.zfsDelete(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name is null", errMsg);
    }

    @Test
    public void tstUssDeleteZfsEmptyFileSystemNameFailure() {
        String errMsg = "";
        try {
            ussDelete.zfsDelete("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name not specified", errMsg);
    }

    @Test
    public void tstUssDeleteZfsEmptyFileSystemNameWithSpacesFailure() {
        String errMsg = "";
        try {
            ussDelete.zfsDelete("   ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file system name not specified", errMsg);
    }

}
