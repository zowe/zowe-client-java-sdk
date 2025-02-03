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
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.ChangeModeParams;
import zowe.client.sdk.zosfiles.uss.methods.UssChangeMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class containing unit tests for UssChMod.
 *
 * @author James Kostrewski
 * @version 2.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssChangeModeTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private PutJsonZosmfRequest mockJsonPutRequest;
    private UssChangeMode ussChangeMode;

    @Before
    public void init() throws ZosmfRequestException {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        ussChangeMode = new UssChangeMode(connection);
    }

    @Test
    public void tstUssChangeModeSuccess() throws ZosmfRequestException {
        final UssChangeMode ussChangeMode = new UssChangeMode(connection, mockJsonPutRequest);
        final Response response = ussChangeMode.change("/xxx/xx/xx",
                new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeModeWithRecursiveInParamsSuccess() throws ZosmfRequestException {
        final UssChangeMode ussChangeMode = new UssChangeMode(connection, mockJsonPutRequest);
        final Response response = ussChangeMode.change("/xxx/xx/xx",
                new ChangeModeParams.Builder().mode("rwxrwxrwx").recursive(true).build());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeModeInvalidTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeMode.change("name", new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssChangeModeNullTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeMode.change(null, new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssChangeModeEmptyTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeMode.change("", new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeModeEmptyTargetPathWithSpacesWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeMode.change("  ", new ChangeModeParams.Builder().mode("rwxrwxrwx").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeModeNullModeInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeMode.change("/xxx/xx/xx", new ChangeModeParams.Builder().mode(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode is null", errMsg);
    }

    @Test
    public void tstUssChangeModeEmptyModeInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeMode.change("/xxx/xx/xx", new ChangeModeParams.Builder().mode("").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode not specified", errMsg);
    }

    @Test
    public void tstUssChangeModeEmptyModeInParamsWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeMode.change("/xxx/xx/xx", new ChangeModeParams.Builder().mode("  ").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode not specified", errMsg);
    }

}
