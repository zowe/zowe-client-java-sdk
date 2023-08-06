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
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.input.ChangeOwnerParams;
import zowe.client.sdk.zosfiles.uss.methods.UssChangeOwner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class containing unit tests for UssChangeOwner.
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssChangeOwnerTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private JsonPutRequest mockJsonPutRequest;
    private UssChangeOwner ussChangeOwner;

    @Before
    public void init() {
        mockJsonPutRequest = Mockito.mock(JsonPutRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        ussChangeOwner = new UssChangeOwner(connection);
    }

    @Test
    public void tstUssChangeOwnerSuccess() throws Exception {
        final UssChangeOwner ussChangeOwner = new UssChangeOwner(connection, mockJsonPutRequest);
        final Response response = ussChangeOwner.change("/xxx/xx/xx", "user");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeOwnerRecursiveSuccess() throws Exception {
        final UssChangeOwner ussChangeOwner = new UssChangeOwner(connection, mockJsonPutRequest);
        final Response response = ussChangeOwner.change("/xxx/xx/xx",
                new ChangeOwnerParams.Builder().owner("user").recursive(true).build());
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeOwnerInvalidTargetPathFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("name", "user");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullTargetPathFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change(null, "user");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("", "user");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("   ", "user");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullParamsFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", (ChangeOwnerParams) null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNoOwnerSpecifiedInParamsFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", new ChangeOwnerParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerInvalidTargetPathWithParamsFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("name", new ChangeOwnerParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullOwnerFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", (String) null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner is null", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyOwnerFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", "");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyOwnerWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", "  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

}
