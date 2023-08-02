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

    private JsonPostRequest jsonPostrequest;
    private ZosConnection connection;

    @Before
    public void init() {
        jsonPostrequest = Mockito.mock(JsonPostRequest.class);
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssCreateSuccess() throws Exception {
        Mockito.when(jsonPostrequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssCreate ussCreate = new UssCreate(connection, jsonPostrequest);
        Response response = ussCreate.create("name", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        Assertions.assertEquals("{}", response.getResponsePhrase().get().toString());
        Assertions.assertEquals("200", response.getStatusCode().get().toString());
        Assertions.assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssCreateNullNameFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create(null, new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("name is null", errMsg);
    }

    @Test
    public void tstUssCreateEmptyNameFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("name not specified", errMsg);
    }

    @Test
    public void tstUssCreateEmptyNameWithSpacesFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("  ", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("name not specified", errMsg);
    }

    @Test
    public void tstUssCreateNullParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssCreateNullTypeParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(null, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("type is null", errMsg);
    }

    @Test
    public void tstUssCreateNullModeParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, null));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode is null", errMsg);
    }

    @Test
    public void tstUssCreateEmptyModeParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, ""));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify 9 character permission", errMsg);
    }

    @Test
    public void tstUssCreateEmptyModeParamsWithSpacesFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, "  "));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify 9 character permission", errMsg);
    }

    @Test
    public void tstUssCreateInvalidModeParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, "rwxrwxrwf"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid permission", errMsg);
    }

}
