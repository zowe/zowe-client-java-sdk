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
import zowe.client.sdk.rest.JsonPostRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.input.CreateParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCreate;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssCreate.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssCreateTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private JsonPostRequest mockJsonPostRequest;
    private UssCreate ussCreate;

    @Before
    public void init() {
        mockJsonPostRequest = Mockito.mock(JsonPostRequest.class);
        ussCreate = new UssCreate(connection);
    }

    @Test
    public void tstUssCreateSuccess() throws Exception {
        Mockito.when(mockJsonPostRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssCreate ussCreate = new UssCreate(connection, mockJsonPostRequest);
        Response response = ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        Assertions.assertEquals("{}", response.getResponsePhrase().get().toString());
        Assertions.assertEquals("200", response.getStatusCode().get().toString());
        Assertions.assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssCreateInvalidTargetPathFailure() {
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssCreateNullTargetPathFailure() {
        String errMsg = "";
        try {
            ussCreate.create(null, new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssCreateEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussCreate.create("", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCreateEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussCreate.create("  ", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCreateNullParamsFailure() {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssCreateNullTypeInParamsFailure() {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(null, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("type is null", errMsg);
    }

    @Test
    public void tstUssCreateNullModeInParamsFailure() {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, null));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode is null", errMsg);
    }

    @Test
    public void tstUssCreateEmptyModeInParamsFailure() {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, ""));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify 9 character permission", errMsg);
    }

    @Test
    public void tstUssCreateEmptyModeInParamsWithSpacesFailure() {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "  "));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify 9 character permission", errMsg);
    }

    @Test
    public void tstUssCreateInvalidModeWithParamsFailure() {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "rwxrwxrwf"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid permission", errMsg);
    }

}
