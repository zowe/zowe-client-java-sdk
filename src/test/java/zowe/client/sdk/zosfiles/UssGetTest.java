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
import zowe.client.sdk.zosfiles.uss.methods.UssGet;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssGet.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssGetTest {

    private ZosConnection connection;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstGetTextFilePathSuccess() throws Exception {
        TextGetRequest textGetRequest = Mockito.mock(TextGetRequest.class);
        Mockito.when(textGetRequest.executeRequest()).thenReturn(
                new Response("text", 200, "success"));
        UssGet ussGet = new UssGet(connection, textGetRequest);
        String response = ussGet.getText("/xxx/xx");
        Assertions.assertEquals("text", response);
    }

    @Test
    public void tstGetTextFilePathDefaultResponseSuccess() throws Exception {
        TextGetRequest textGetRequest = Mockito.mock(TextGetRequest.class);
        Mockito.when(textGetRequest.executeRequest()).thenReturn(
                new Response(null, 200, "success"));
        UssGet ussGet = new UssGet(connection, textGetRequest);
        String response = ussGet.getText("/xxx/xx");
        Assertions.assertEquals("", response);
    }

    @Test
    public void tstGetBinaryFilePathSuccess() throws Exception {
        StreamGetRequest streamGetRequest = Mockito.mock(StreamGetRequest.class);
        byte[] data = "data".getBytes();
        Mockito.when(streamGetRequest.executeRequest()).thenReturn(
                new Response(data, 200, "success"));
        UssGet ussGet = new UssGet(connection, streamGetRequest);
        byte[] response = ussGet.getBinary("/xxx/xx");
        Assertions.assertEquals(data, response);
    }

    @Test
    public void tstGetBinaryFilePathDefaultResponseSuccess() throws Exception {
        StreamGetRequest streamGetRequest = Mockito.mock(StreamGetRequest.class);
        byte[] data = new byte[0];
        Mockito.when(streamGetRequest.executeRequest()).thenReturn(
                new Response(data, 200, "success"));
        UssGet ussGet = new UssGet(connection, streamGetRequest);
        byte[] response = ussGet.getBinary("/xxx/xx");
        Assertions.assertEquals(data, response);
    }

    @Test
    public void tstUssGetTextFilePathNullNameFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getText(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name is null", errMsg);
    }

    @Test
    public void tstUssGetTextFilePathEmptyNameFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getText("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name not specified", errMsg);
    }

    @Test
    public void tstUssGetTextFilePathEmptyNameWithSpacesFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getText("  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name not specified", errMsg);
    }

    @Test
    public void tstUssGetBinaryFilePathNullNameFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getBinary(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name is null", errMsg);
    }

    @Test
    public void tstUssGetBinaryFilePathEmptyNameFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getBinary("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name not specified", errMsg);
    }

    @Test
    public void tstUssGetBinaryFilePathEmptyNameWithSpacesFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getBinary("   ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name not specified", errMsg);
    }

    @Test
    public void tstUssGetCommonNullParamsFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getCommon("name", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

}
