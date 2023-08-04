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
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.StreamPutRequest;
import zowe.client.sdk.rest.TextPutRequest;
import zowe.client.sdk.zosfiles.uss.input.WriteParams;
import zowe.client.sdk.zosfiles.uss.methods.UssWrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class containing unit tests for UssWrite.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssWriteTest {

    private ZosConnection connection;
    private UssWrite ussWrite;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
        ussWrite = new UssWrite(connection);
    }

    @Test
    public void tstUssWriteTextSuccess() throws Exception {
        TextPutRequest textPutRequest = Mockito.mock(TextPutRequest.class);
        Mockito.when(textPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssWrite ussWrite = new UssWrite(connection, textPutRequest);
        Response response = ussWrite.writeText("/xx/xx/x", "text");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssWriteBinarySuccess() throws Exception {
        StreamPutRequest streamPutRequest = Mockito.mock(StreamPutRequest.class);
        Mockito.when(streamPutRequest.executeRequest()).thenReturn(
                new Response(new byte[0], 200, "success"));
        UssWrite ussWrite = new UssWrite(connection, streamPutRequest);
        Response response = ussWrite.writeBinary("/xx/xx/x", new byte[0]);
        assertTrue(response.getResponsePhrase().get() instanceof byte[]);
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssWriteTextNullTargetPathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeText(null, "content");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssWriteTextEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeText("", "content");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteTextEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussWrite.writeText("    ", "content");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteTextInvalidTargetPathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeText("name", "text");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssWriteBinaryNullTargetPathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeBinary(null, new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssWriteBinaryEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeBinary("", new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteBinaryEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussWrite.writeBinary("   ", new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteBinaryInvalidTargetPathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeBinary("name", new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssWriteCommonNullTargetPathWithParamsFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon(null, new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssWriteCommonEmptyTargetPathWithParamsFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon("", new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteCommonEmptyTargetPathWithSpacesWithParamsFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon("  ", new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteCommonNullParamsFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon("/xx/xx/x", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssWriteCommonInvalidTargetPathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon("name", new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

}
