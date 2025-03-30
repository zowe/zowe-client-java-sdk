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

import kong.unirest.core.Cookie;
import kong.unirest.core.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.ChangeModeParams;
import zowe.client.sdk.zosfiles.uss.input.ChangeTagParams;
import zowe.client.sdk.zosfiles.uss.methods.UssChangeMode;
import zowe.client.sdk.zosfiles.uss.methods.UssChangeTag;
import zowe.client.sdk.zosfiles.uss.types.ChangeTagAction;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssChangeTag.
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 3.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssChangeTagTest {
    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestAuth;
    private UssChangeTag ussChangeTag;

    @Before
    public void init() throws ZosmfRequestException {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));

        mockJsonPutRequestAuth = Mockito.mock(PutJsonZosmfRequest.class, withSettings().useConstructor(connection));
        Mockito.when(mockJsonPutRequestAuth.executeRequest()).thenReturn(
                new Response(new org.json.simple.JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestAuth).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPutRequestAuth).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestAuth).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestAuth).setCookie(any());
        doCallRealMethod().when(mockJsonPutRequestAuth).getHeaders();

        ussChangeTag = new UssChangeTag(connection);
    }

    @Test
    public void tstUssChangeTagChangeToBinarySuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.binary("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeTagToggleAuthSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequestAuth);

        connection.setCookie(new Cookie("hello=hello"));
        Response response = ussChangeTag.binary("/xxx/xx/xx");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestAuth.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));

        connection.setCookie(null);
        response = ussChangeTag.binary("/xxx/xx/xx");
        assertEquals("{Authorization=Basic MTox, X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestAuth.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeTagChangeToTextSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.text("/xxx/xx/xx", "IBM-1047");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeTagRemoveSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.remove("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeTagRetrieveSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.get("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeTagChangeCommonSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.changeCommon("/xxx/xx/xx",
                new ChangeTagParams.Builder().action(ChangeTagAction.LIST).build());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeTagChangeCommonWithSpacesInFileNamePathSuccess() throws ZosmfRequestException {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.changeCommon("/xx x/x x/x x",
                new ChangeTagParams.Builder().action(ChangeTagAction.LIST).build());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeTagChangeCommonWithOutBeginningSlashInFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("xxx/xx/xx",
                    new ChangeTagParams.Builder().action(ChangeTagAction.LIST).build());
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
                    new ChangeTagParams.Builder().action(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("action is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonNoActionInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("/xxx/xx/xx", new ChangeTagParams.Builder().build());
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
                    new ChangeTagParams.Builder().action(ChangeTagAction.SET).build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonEmptyFileNamePathPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("", new ChangeTagParams.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonEmptyFileNamePathPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("  ", new ChangeTagParams.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryEmptyFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.binary("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryEmptyFileNamePathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.binary("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryNullFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.binary(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text("", "codeset");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyFileNamePathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text("  ", "codeset");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextNullFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text(null, "codeset");
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextNullCodeSetFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text("/xx/xx/x", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyCodeSetFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text(null, "");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyCodeSetWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.text(null, "  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveEmptyFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.remove("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveEmptyFileNamePathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.remove("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveNullFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.remove(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveEmptyFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.get("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveEmptyFileNamePathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.get("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveNullFileNamePathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeTag.get(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

}
