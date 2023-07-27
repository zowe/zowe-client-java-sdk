/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosFiles;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.input.ChModParams;
import zowe.client.sdk.zosfiles.uss.methods.UssChMod;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class containing unit tests for UssChMod.
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssChModTest {

    private JsonPutRequest jsonPutRequest;
    private ZosConnection connection;

    @Before
    public void init() {
        jsonPutRequest = Mockito.mock(JsonPutRequest.class);
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssChModSuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssChMod ussChMod = new UssChMod(connection, jsonPutRequest);
        Response response = ussChMod.chMod("/xxx/xx/xx",
                new ChModParams(new ChModParams.Builder().mode("rwxrwxrwx")));
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssChModRecursiveSuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssChMod ussChMod = new UssChMod(connection, jsonPutRequest);
        Response response = ussChMod.chMod("/xxx/xx/xx",
                new ChModParams(new ChModParams.Builder().mode("rwxrwxrwx").recursive(true)));
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssChModNullPathFailure() throws Exception {
        UssChMod ussChMod = new UssChMod(connection);
        String errMsg = "";
        try{
            ussChMod.chMod(null, new ChModParams(new ChModParams.Builder().mode("rwxrwxrwx")));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("path is null", errMsg);
    }

    @Test
    public void tstUssChModEmptyPathFailure() throws Exception {
        UssChMod ussChMod = new UssChMod(connection);
        String errMsg = "";
        try{
            ussChMod.chMod("", new ChModParams(new ChModParams.Builder().mode("rwxrwxrwx")));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssChModNullModeFailure() throws Exception {
        UssChMod ussChMod = new UssChMod(connection);
        String errMsg = "";
        try{
            ussChMod.chMod("/xxx/xx/xx", new ChModParams(new ChModParams.Builder().mode(null)));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode is null", errMsg);
    }

    @Test
    public void tstUssChModEmptyModeFailure() throws Exception {
        UssChMod ussChMod = new UssChMod(connection);
        String errMsg = "";
        try{
            ussChMod.chMod("/xxx/xx/xx", new ChModParams(new ChModParams.Builder().mode("")));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode not specified", errMsg);
    }

}
