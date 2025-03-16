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
import zowe.client.sdk.rest.GetStreamZosmfRequest;
import zowe.client.sdk.rest.GetTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.GetParams;
import zowe.client.sdk.zosfiles.uss.methods.UssGet;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssGet.
 *
 * @author Frank Giordano
 * @version 3.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssGetTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private UssGet ussGet;

    @Before
    public void init() {
        ussGet = new UssGet(connection);
    }

    @Test
    public void tstGetTextFileTargetPathSuccess() throws ZosmfRequestException {
        final GetTextZosmfRequest mockTextGetRequest = Mockito.mock(GetTextZosmfRequest.class);
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response("text", 200, "success"));
        final UssGet ussGet = new UssGet(connection, mockTextGetRequest);
        final String response = ussGet.getText("/xxx/xx");
        Assertions.assertEquals("text", response);
    }

    @Test
    public void tstGetTextFileTargetPathDefaultResponseSuccess() throws ZosmfRequestException {
        final GetTextZosmfRequest mockTextGetRequest = Mockito.mock(GetTextZosmfRequest.class);
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response(null, 200, "success"));
        final UssGet ussGet = new UssGet(connection, mockTextGetRequest);
        final String response = ussGet.getText("/xxx/xx");
        Assertions.assertEquals("", response);
    }

    @Test
    public void tstGetBinaryFileTargetPathSuccess() throws ZosmfRequestException {
        final GetStreamZosmfRequest mockStreamGetRequest = Mockito.mock(GetStreamZosmfRequest.class);
        final byte[] data = "data".getBytes();
        Mockito.when(mockStreamGetRequest.executeRequest()).thenReturn(
                new Response(data, 200, "success"));
        final UssGet ussGet = new UssGet(connection, mockStreamGetRequest);
        final byte[] response = ussGet.getBinary("/xxx/xx");
        Assertions.assertEquals(data, response);
    }

    @Test
    public void tstGetBinaryFileTargetPathDefaultResponseSuccess() throws ZosmfRequestException {
        final GetStreamZosmfRequest mockStreamGetRequest = Mockito.mock(GetStreamZosmfRequest.class);
        final byte[] data = new byte[0];
        Mockito.when(mockStreamGetRequest.executeRequest()).thenReturn(
                new Response(data, 200, "success"));
        final UssGet ussGet = new UssGet(connection, mockStreamGetRequest);
        final byte[] response = ussGet.getBinary("/xxx/xx");
        Assertions.assertEquals(data, response);
    }

    @Test
    public void tstUssGetTextFileInvalidPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getText("name");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssGetTextFileNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getText(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssGetTextFileEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getText("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssGetTextFileEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getText("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getBinary(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getBinary("name");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getBinary("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getBinary("   ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssGetCommonNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getCommon("/xxx/xx/x", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssGetCommonInvalidTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getCommon("name", new GetParams.Builder().build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

}
