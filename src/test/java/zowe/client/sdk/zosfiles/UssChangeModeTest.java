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
import zowe.client.sdk.zosfiles.uss.input.ChangeModeParams;
import zowe.client.sdk.zosfiles.uss.methods.UssChangeMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class containing unit tests for UssChMod.
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssChangeModeTest {

    private ZosConnection connection;
    private JsonPutRequest mockJsonPutRequest;
    private UssChangeMode ussChangeMode;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
        mockJsonPutRequest = Mockito.mock(JsonPutRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        ussChangeMode = new UssChangeMode(connection);
    }

    @Test
    public void tstUssChangeModeSuccess() throws Exception {
        UssChangeMode ussChangeMode = new UssChangeMode(connection, mockJsonPutRequest);
        Response response = ussChangeMode.change("/xxx/xx/xx",
                new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeModeWithRecursiveInParamsSuccess() throws Exception {
        UssChangeMode ussChangeMode = new UssChangeMode(connection, mockJsonPutRequest);
        Response response = ussChangeMode.change("/xxx/xx/xx",
                new ChangeModeParams.Builder().mode("rwxrwxrwx").recursive(true).build());
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeModeInvalidTargetPathWithParamsFailure() {
        String errMsg = "";
        try {
            ussChangeMode.change("name", new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssChangeModeNullTargetPathWithParamsFailure() {
        String errMsg = "";
        try {
            ussChangeMode.change(null, new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssChangeModeEmptyTargetPathWithParamsFailure() {
        String errMsg = "";
        try {
            ussChangeMode.change("", new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeModeEmptyTargetPathWithSpacesWithParamsFailure() {
        String errMsg = "";
        try {
            ussChangeMode.change("  ", new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeModeNullModeInParamsFailure() {
        String errMsg = "";
        try {
            ussChangeMode.change("/xxx/xx/xx", new ChangeModeParams.Builder().mode(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode is null", errMsg);
    }

    @Test
    public void tstUssChangeModeEmptyModeInParamsFailure() {
        String errMsg = "";
        try {
            ussChangeMode.change("/xxx/xx/xx", new ChangeModeParams.Builder().mode("").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode not specified", errMsg);
    }

    @Test
    public void tstUssChangeModeEmptyModeInParamsWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeMode.change("/xxx/xx/xx", new ChangeModeParams.Builder().mode("  ").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode not specified", errMsg);
    }

}
