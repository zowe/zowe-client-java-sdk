/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutStreamZosmfRequest;
import zowe.client.sdk.rest.PutTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.WriteParams;
import zowe.client.sdk.zosfiles.uss.methods.UssWrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssWrite.
 *
 * @author Frank Giordano
 * @version 4.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssWriteTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private UssWrite ussWrite;

    @Before
    public void init() {
        ussWrite = new UssWrite(connection);
    }

    @Test
    public void tstUssWriteTextSuccess() throws ZosmfRequestException {
        final PutTextZosmfRequest mockTextPutRequest = Mockito.mock(PutTextZosmfRequest.class);
        Mockito.when(mockTextPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        final UssWrite ussWrite = new UssWrite(connection, mockTextPutRequest);
        final Response response = ussWrite.writeText("/xx/xx/x", "text");
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssWriteTextToggleTokenSuccess() throws ZosmfRequestException {
        PutTextZosmfRequest mockJsonPutRequestToken = Mockito.mock(PutTextZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonPutRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestToken).getHeaders();
        final UssWrite ussWrite = new UssWrite(connection, mockJsonPutRequestToken);

        Response response = ussWrite.writeText("/xx/xx/x", "text");
        String expectedResp = "{X-IBM-Data-Type=text;, X-CSRF-ZOSMF-HEADER=true, Content-Type=text/plain; charset=UTF-8}";
        Assertions.assertEquals(expectedResp, mockJsonPutRequestToken.getHeaders().toString());
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssWriteBinarySuccess() throws ZosmfRequestException {
        final PutStreamZosmfRequest mockStreamPutRequest = Mockito.mock(PutStreamZosmfRequest.class);
        Mockito.when(mockStreamPutRequest.executeRequest()).thenReturn(
                new Response(new byte[0], 200, "success"));
        final UssWrite ussWrite = new UssWrite(connection, mockStreamPutRequest);
        final Response response = ussWrite.writeBinary("/xx/xx/x", new byte[0]);
        assertTrue(response.getResponsePhrase().orElse(null) instanceof byte[]);
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssWriteTextNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeText(null, "content");
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssWriteTextEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeText("", "content");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteTextEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeText("    ", "content");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteTextInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeText("name", "text");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssWriteBinaryNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeBinary(null, new byte[0]);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssWriteBinaryEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeBinary("", new byte[0]);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteBinaryEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeBinary("   ", new byte[0]);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteBinaryInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeBinary("name", new byte[0]);
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssWriteCommonNullTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeCommon(null, new WriteParams.Builder().build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssWriteCommonEmptyTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeCommon("", new WriteParams.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteCommonEmptyTargetPathWithSpacesWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeCommon("  ", new WriteParams.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssWriteCommonNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeCommon("/xx/xx/x", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssWriteCommonInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussWrite.writeCommon("name", new WriteParams.Builder().build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstNullConnectionFailure() {
        try {
            new UssWrite(null);
        } catch (NullPointerException e) {
            assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

}
