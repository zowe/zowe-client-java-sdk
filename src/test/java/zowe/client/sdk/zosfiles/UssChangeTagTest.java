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
 * @version 2.0
 */
public class UssChangeTagTest {
    private ZosConnection connection;
    private JsonPutRequest jsonPutRequest;
    private UssChangeTag ussChangeTag;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
        jsonPutRequest = Mockito.mock(JsonPutRequest.class);
        ussChangeTag = new UssChangeTag(connection);
    }

    @Test
    public void tstUssChangeTagSuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssChangeTag ussChangeTag = new UssChangeTag(connection, jsonPutRequest);
        Response response = ussChangeTag.change("/xxx/xx/xx", ChangeTagAction.SET);
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssChangeTagRecursiveSuccess() throws Exception {
        Mockito.when(jsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssChangeTag ussChangeTag = new UssChangeTag(connection, jsonPutRequest);
        Response response = ussChangeTag.change("/xxx/xx/xx", ChangeTagAction.SET);
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

    @Test
    public void tstUssChangeTagNullActionFailure() {
        String errMsg = "";
        try {
            ussChangeTag.change("/xxx/xx/xx", new ChangeTagParams.Builder().action(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("action not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagNoActionFailure() {
        String errMsg = "";
        try {
            ussChangeTag.change("/xxx/xx/xx", new ChangeTagParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("action not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagInvalidTargetPathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.change("hello", new ChangeTagParams.Builder().action(ChangeTagAction.SET).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path", errMsg);
    }

    @Test
    public void tstUssChangeTagEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussChangeTag.change("", new ChangeTagParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeTagEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussChangeTag.change("  ", new ChangeTagParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

}
