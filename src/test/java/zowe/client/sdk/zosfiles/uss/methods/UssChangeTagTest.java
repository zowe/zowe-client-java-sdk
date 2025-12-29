/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.methods;

import kong.unirest.core.Cookie;
import kong.unirest.core.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.UssChangeTagInputData;
import zowe.client.sdk.zosfiles.uss.types.ChangeTagAction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssChangeTag.
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 6.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssChangeTagTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestToken;
    private UssChangeTag ussChangeTag;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequest).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequest).getUrl();

        mockJsonPutRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonPutRequestToken.executeRequest()).thenReturn(
                new Response(new org.json.simple.JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).getUrl();

        ussChangeTag = new UssChangeTag(connection);
    }

    @Test
    public void tstUssChangeTagChangeToBinarySuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.binary("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssChangeTagToggleTokenSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(tokenConnection, mockJsonPutRequestToken);
        Response response = ussChangeTag.binary("/xxx/xx/xx");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequestToken.getUrl());
    }

    @Test
    public void tstUssChangeTagChangeToTextSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.text("/xxx/xx/xx", "IBM-1047");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssChangeTagRemoveSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.remove("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssChangeTagRetrieveSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.get("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssChangeTagChangeCommonSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.changeCommon("/xxx/xx/xx",
                new UssChangeTagInputData.Builder().action(ChangeTagAction.LIST).build());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssChangeTagChangeCommonWithSpacesInFileNamePathSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.changeCommon("/xx x/x x/x x",
                new UssChangeTagInputData.Builder().action(ChangeTagAction.LIST).build());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Fxx%20x%2Fx%20x%2Fx%20x", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssChangeTagChangeCommonWithOutBeginningSlashInFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("xxx/xx/xx",
                    new UssChangeTagInputData.Builder().action(ChangeTagAction.LIST).build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonNullActionInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("/xxx/xx/xx",
                    new UssChangeTagInputData.Builder().action(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("action is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonNoActionInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("/xxx/xx/xx", new UssChangeTagInputData.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("action not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonInvalidFileNamePathPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("hello",
                    new UssChangeTagInputData.Builder().action(ChangeTagAction.SET).build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonEmptyFileNamePathPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("", new UssChangeTagInputData.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonEmptyFileNamePathPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("  ", new UssChangeTagInputData.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryEmptyFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.binary("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryEmptyFileNamePathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.binary("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryNullFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.binary(null);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text("", "codeset");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyFileNamePathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text("  ", "codeset");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextNullFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text(null, "codeset");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextNullCodeSetFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text("/xx/xx/x", null);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyCodeSetFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text("fg", "");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyCodeSetWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text("fg", "  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveEmptyFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.remove("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveEmptyFileNamePathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.remove("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveNullFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.remove(null);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveEmptyFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.get("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveEmptyFileNamePathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.get("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveNullFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.get(null);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeTagNullConnectionFailure() {
        try {
            new UssChangeTag(null);
        } catch (NullPointerException e) {
            assertEquals("connection is null", e.getMessage());
        }
    }

    @Test
    public void tstUssChangeTagSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        UssChangeTag ussChangeTag = new UssChangeTag(connection, request);
        assertNotNull(ussChangeTag);
    }

    @Test
    public void tstUssChangeTagSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssChangeTag(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstUssChangeTagSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssChangeTag(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstUssChangeTagSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new UssChangeTag(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

}
