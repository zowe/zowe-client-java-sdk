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

import kong.unirest.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.input.ChangeTagParams;
import zowe.client.sdk.zosfiles.uss.methods.UssChangeTag;
import zowe.client.sdk.zosfiles.uss.types.ChangeTagAction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class containing unit tests for UssChangeTag.
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 2.0
 */
public class UssChangeTagTest {
    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private JsonPutRequest mockJsonPutRequest;
    private UssChangeTag ussChangeTag;

    @Before
    public void init() {
        mockJsonPutRequest = Mockito.mock(JsonPutRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        ussChangeTag = new UssChangeTag(connection);
    }

    @Test
    public void tstUssChangeTagChangeToBinarySuccess() throws Exception {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.changeToBinary("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeTagChangeToTextSuccess() throws Exception {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.changeToText("/xxx/xx/xx", "IBM-1047");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeTagRemoveSuccess() throws Exception {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.remove("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeTagRetrieveSuccess() throws Exception {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.retrieve("/xxx/xx/xx");
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeTagChangeCommonSuccess() throws Exception {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.changeCommon("/xxx/xx/xx",
                new ChangeTagParams.Builder().action(ChangeTagAction.LIST).build());
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeTagChangeCommonWithSpacesInFileNamePathSuccess() throws Exception {
        final UssChangeTag ussChangeTag = new UssChangeTag(connection, mockJsonPutRequest);
        final Response response = ussChangeTag.changeCommon("/xx x/x x/x x",
                new ChangeTagParams.Builder().action(ChangeTagAction.LIST).build());
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get());
    }

    @Test
    public void tstUssChangeTagChangeCommonWithOutBeginningSlashInFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("xxx/xx/xx",
                    new ChangeTagParams.Builder().action(ChangeTagAction.LIST).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonNullActionInParamsFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("/xxx/xx/xx",
                    new ChangeTagParams.Builder().action(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("action is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonNoActionInParamsFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("/xxx/xx/xx", new ChangeTagParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("action not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonInvalidFileNamePathPathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("hello",
                    new ChangeTagParams.Builder().action(ChangeTagAction.SET).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonEmptyFileNamePathPathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("", new ChangeTagParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeCommonEmptyFileNamePathPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeCommon("  ", new ChangeTagParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryEmptyFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToBinary("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryEmptyFileNamePathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToBinary("  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToBinaryNullFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToBinary(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToText("", "codeset");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyFileNamePathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToText("  ", "codeset");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextNullFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToText(null, "codeset");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextNullCodeSetFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToText("/xx/xx/x", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet is null", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyCodeSetFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToText(null, "");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagChangeToTextEmptyCodeSetWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeTag.changeToText(null, "  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("codeSet not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveEmptyFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.remove("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveEmptyFileNamePathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeTag.remove("  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRemoveNullFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.remove(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveEmptyFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.retrieve("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveEmptyFileNamePathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeTag.retrieve("  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagRetrieveNullFileNamePathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.retrieve(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is null", errMsg);
    }

}
