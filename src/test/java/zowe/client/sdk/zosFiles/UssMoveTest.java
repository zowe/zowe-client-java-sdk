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
import zowe.client.sdk.zosfiles.uss.methods.UssMove;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class containing unit tests for UssMove.
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssMoveTest {

    private JsonPutRequest jsonPutRequest;
    private ZosConnection connection;

    @Before
    public void init() {
        jsonPutRequest = Mockito.mock(JsonPutRequest.class);
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssMoveSuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssMove ussMove = new UssMove(connection, jsonPutRequest);
        Response response = ussMove.move("/xxx/xx/xx", "/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssMoveOverwriteSuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssMove ussMove = new UssMove(connection, jsonPutRequest);
        Response response = ussMove.move("/xxx/xx/xx", "/xxx/xx/xx", true);
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssMoveNullFromPathFailure() {
        UssMove ussMove = new UssMove(connection);
        String errMsg = "";
        try {
            ussMove.move(null, "/xxx/xx/xx");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fromPath is null", errMsg);
    }

    @Test
    public void tstUssMoveEmptyFromPathFailure() {
        UssMove ussMove = new UssMove(connection);
        String errMsg = "";
        try {
            ussMove.move("", "/xxx/xx/xx");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fromPath not specified", errMsg);
    }

    @Test
    public void tstUssMoveNullTargetPathFailure() {
        UssMove ussMove = new UssMove(connection);
        String errMsg = "";
        try {
            ussMove.move("/xxx/xx/xx", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssMoveEmptyTargetPathFailure() {
        UssMove ussMove = new UssMove(connection);
        String errMsg = "";
        try {
            ussMove.move("/xxx/xx/xx", "");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

}
