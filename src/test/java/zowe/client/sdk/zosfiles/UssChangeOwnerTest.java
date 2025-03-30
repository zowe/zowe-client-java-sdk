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
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.ChangeModeParams;
import zowe.client.sdk.zosfiles.uss.input.ChangeOwnerParams;
import zowe.client.sdk.zosfiles.uss.methods.UssChangeMode;
import zowe.client.sdk.zosfiles.uss.methods.UssChangeOwner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssChangeOwner.
 *
 * @author James Kostrewski
 * @version 3.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssChangeOwnerTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestAuth;
    private UssChangeOwner ussChangeOwner;

    @Before
    public void init() throws ZosmfRequestException {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));

        mockJsonPutRequestAuth = Mockito.mock(PutJsonZosmfRequest.class, withSettings().useConstructor(connection));
        Mockito.when(mockJsonPutRequestAuth.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestAuth).setHeaders(any(Map.class));
        doCallRealMethod().when(mockJsonPutRequestAuth).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestAuth).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestAuth).setCookie(any());
        doCallRealMethod().when(mockJsonPutRequestAuth).getHeaders();

        ussChangeOwner = new UssChangeOwner(connection);
    }

    @Test
    public void tstUssChangeOwnerSuccess() throws ZosmfRequestException {
        final UssChangeOwner ussChangeOwner = new UssChangeOwner(connection, mockJsonPutRequest);
        final Response response = ussChangeOwner.change("/xxx/xx/xx", "user");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeModeToggleAuthSuccess() throws ZosmfRequestException {
        final UssChangeOwner ussChangeOwner = new UssChangeOwner(connection, mockJsonPutRequestAuth);

        connection.setCookie(new Cookie("hello=hello"));
        Response response = ussChangeOwner.change("/xxx/xx/xx", "user");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestAuth.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));

        connection.setCookie(null);
        response = ussChangeOwner.change("/xxx/xx/xx", "user");
        assertEquals("{Authorization=Basic MTox, X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestAuth.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeOwnerRecursiveSuccess() throws ZosmfRequestException {
        final UssChangeOwner ussChangeOwner = new UssChangeOwner(connection, mockJsonPutRequest);
        final Response response = ussChangeOwner.changeCommon("/xxx/xx/xx",
                new ChangeOwnerParams.Builder().owner("user").recursive(true).build());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssChangeOwnerInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("name", "user");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change(null, "user");
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("", "user");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("   ", "user");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.changeCommon("/xxx/xx/xx", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNoOwnerSpecifiedInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.changeCommon("/xxx/xx/xx", new ChangeOwnerParams.Builder().build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerInvalidTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.changeCommon("name", new ChangeOwnerParams.Builder().build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullOwnerFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner is null", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyOwnerFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", "");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyOwnerWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", "  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

}
