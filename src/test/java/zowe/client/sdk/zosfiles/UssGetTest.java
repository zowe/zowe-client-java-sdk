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

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.StreamGetRequest;
import zowe.client.sdk.rest.TextGetRequest;
import zowe.client.sdk.zosfiles.uss.input.GetParams;
import zowe.client.sdk.zosfiles.uss.methods.UssGet;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssGet.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssGetTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private UssGet ussGet;

    @Before
    public void init() {
        ussGet = new UssGet(connection);
    }

    @Test
    public void tstGetTextFileTargetPathSuccess() throws Exception {
        final TextGetRequest mockTextGetRequest = Mockito.mock(TextGetRequest.class);
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response("text", 200, "success"));
        final UssGet ussGet = new UssGet(connection, mockTextGetRequest);
        final String response = ussGet.getText("/xxx/xx");
        Assertions.assertEquals("text", response);
    }

    @Test
    public void tstGetTextFileTargetPathDefaultResponseSuccess() throws Exception {
        final TextGetRequest mockTextGetRequest = Mockito.mock(TextGetRequest.class);
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response(null, 200, "success"));
        final UssGet ussGet = new UssGet(connection, mockTextGetRequest);
        final String response = ussGet.getText("/xxx/xx");
        Assertions.assertEquals("", response);
    }

    @Test
    public void tstGetBinaryFileTargetPathSuccess() throws Exception {
        final StreamGetRequest mockStreamGetRequest = Mockito.mock(StreamGetRequest.class);
        final byte[] data = "data".getBytes();
        Mockito.when(mockStreamGetRequest.executeRequest()).thenReturn(
                new Response(data, 200, "success"));
        final UssGet ussGet = new UssGet(connection, mockStreamGetRequest);
        final byte[] response = ussGet.getBinary("/xxx/xx");
        Assertions.assertEquals(data, response);
    }

    @Test
    public void tstGetBinaryFileTargetPathDefaultResponseSuccess() throws Exception {
        final StreamGetRequest mockStreamGetRequest = Mockito.mock(StreamGetRequest.class);
        final byte[] data = new byte[0];
        Mockito.when(mockStreamGetRequest.executeRequest()).thenReturn(
                new Response(data, 200, "success"));
        final UssGet ussGet = new UssGet(connection, mockStreamGetRequest);
        final byte[] response = ussGet.getBinary("/xxx/xx");
        Assertions.assertEquals(data, response);
    }

    @Test
    public void tstUssGetTextFileInvalidPathFailure() {
        String errMsg = "";
        try {
            ussGet.getText("name");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssGetTextFileNullTargetPathFailure() {
        String errMsg = "";
        try {
            ussGet.getText(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssGetTextFileEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussGet.getText("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssGetTextFileEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussGet.getText("  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileNullTargetPathFailure() {
        String errMsg = "";
        try {
            ussGet.getBinary(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileInvalidTargetPathFailure() {
        String errMsg = "";
        try {
            ussGet.getBinary("name");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussGet.getBinary("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussGet.getBinary("   ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssGetCommonNullParamsFailure() {
        String errMsg = "";
        try {
            ussGet.getCommon("/xxx/xx/x", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssGetCommonInvalidTargetPathWithParamsFailure() {
        String errMsg = "";
        try {
            ussGet.getCommon("name", new GetParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

}
