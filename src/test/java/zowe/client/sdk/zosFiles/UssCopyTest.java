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
import zowe.client.sdk.zosfiles.uss.input.CopyParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCopy;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssCopy.
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssCopyTest {

    private JsonPutRequest jsonPutRequest;
    private ZosConnection connection;

    @Before
    public void init() {
        jsonPutRequest = Mockito.mock(JsonPutRequest.class);
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssCopySuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssCopy ussCopy = new UssCopy(connection, jsonPutRequest);
        Response response = ussCopy.copy("/xxx/xx/xx", "/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssCopyRecursiveSuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssCopy ussCopy = new UssCopy(connection, jsonPutRequest);
        Response response = ussCopy.copy("/xxx/xx/xx",
                new CopyParams(new CopyParams.Builder().from("/xxx/xx/xx").recursive(true)));
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssCopyOverwriteSuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssCopy ussCopy = new UssCopy(connection, jsonPutRequest);
        Response response = ussCopy.copy("/xxx/xx/xx",
                new CopyParams(new CopyParams.Builder().from("/xxx/xx/xx").overwrite(true)));
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssCopyNullDestinationPathFailure() {
        UssCopy ussCopy = new UssCopy(connection);
        String errMsg = "";
        try {
            ussCopy.copy(null, "/xxx/xx/xx");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from is null", errMsg);
    }

    @Test
    public void tstUssCopyEmptyDestinationPathFailure() {
        UssCopy ussCopy = new UssCopy(connection);
        String errMsg = "";
        try {
            ussCopy.copy("", "/xxx/xx/xx");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyNullSourcePathFailure() {
        UssCopy ussCopy = new UssCopy(connection);
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", (String) null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssCopyEmptySourcePathFailure() {
        UssCopy ussCopy = new UssCopy(connection);
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", "");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCopyCommonNullParamsFailure() {
        UssCopy ussCopy = new UssCopy(connection);
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", (CopyParams) null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssCopyCommonNullFromFailure() {
        UssCopy ussCopy = new UssCopy(connection);
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", new CopyParams(new CopyParams.Builder().from(null)));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from is null", errMsg);
    }

    @Test
    public void tstUssCopyCommonEmptyFromFailure() {
        UssCopy ussCopy = new UssCopy(connection);
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", new CopyParams(new CopyParams.Builder().from("")));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

}
