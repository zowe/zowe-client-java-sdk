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
        Response response = ussWrite.writeText("name", "text");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssWriteBinarySuccess() throws Exception {
        StreamPutRequest streamPutRequest = Mockito.mock(StreamPutRequest.class);
        Mockito.when(streamPutRequest.executeRequest()).thenReturn(
                new Response(new byte[0], 200, "success"));
        UssWrite ussWrite = new UssWrite(connection, streamPutRequest);
        Response response = ussWrite.writeBinary("name", new byte[0]);
        assertTrue(response.getResponsePhrase().get() instanceof byte[]);
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssWriteTextNullFileNamePathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeText(null, "content");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path is null", errMsg);
    }

    @Test
    public void tstUssWriteTextEmptyFileNamePathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeText("", "content");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path not specified", errMsg);
    }

    @Test
    public void tstUssWriteTextEmptyFileNamePathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussWrite.writeText("    ", "content");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path not specified", errMsg);
    }

    @Test
    public void tstUssWriteBinaryNullFileNamePathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeBinary(null, new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path is null", errMsg);
    }

    @Test
    public void tstUssWriteBinaryEmptyFileNamePathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeBinary("", new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path not specified", errMsg);
    }

    @Test
    public void tstUssWriteBinaryEmptyFileNamePathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussWrite.writeBinary("   ", new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path not specified", errMsg);
    }

    @Test
    public void tstUssWriteCommonNullFileNamePathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon(null, new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path is null", errMsg);
    }

    @Test
    public void tstUssWriteCommonEmptyFileNamePathFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon("", new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path not specified", errMsg);
    }

    @Test
    public void tstUssWriteCommonEmptyFileNamePathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon("  ", new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file name path not specified", errMsg);
    }

    @Test
    public void tstUssWriteCommonNullParamsFailure() {
        String errMsg = "";
        try {
            ussWrite.writeCommon("name", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

}
