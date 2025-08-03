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
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.CopyParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCopy;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssCopy.
 *
 * @author James Kostrewski
 * @version 4.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssCopyTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestToken;
    private UssCopy ussCopy;

    @Before
    public void init() throws ZosmfRequestException {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));

        mockJsonPutRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonPutRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestToken).getHeaders();

        ussCopy = new UssCopy(connection);
    }

    @Test
    public void tstUssCopySuccess() throws ZosmfRequestException {
        final UssCopy ussCopy = new UssCopy(connection, mockJsonPutRequest);
        final Response response = ussCopy.copy("/xxx/xx/xx", "/xxx/xx/xx");
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssCopyToggleTokenSuccess() throws ZosmfRequestException {
        final UssCopy ussCopy = new UssCopy(tokenConnection, mockJsonPutRequestToken);

        Response response = ussCopy.copy("/xxx/xx/xx", "/xxx/xx/xx");
        Assertions.assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestToken.getHeaders().toString());
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssCopyRecursiveSuccess() throws ZosmfRequestException {
        final UssCopy ussCopy = new UssCopy(connection, mockJsonPutRequest);
        final Response response = ussCopy.copyCommon("/xxx/xx/xx",
                new CopyParams.Builder().from("/xxx/xx/xx").recursive(true).build());
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssCopyOverwriteSuccess() throws ZosmfRequestException {
        final UssCopy ussCopy = new UssCopy(connection, mockJsonPutRequest);
        final Response response = ussCopy.copyCommon("/xxx/xx/xx",
                new CopyParams.Builder().from("/xxx/xx/xx").overwrite(true).build());
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssCopyInvalidTargetPathPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", "name");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssCopyInvalidFromPathPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copy("name", "/xxx/xx/xx");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssCopyNullFromPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copy(null, "/xxx/xx/xx");
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("from is null", errMsg);
    }

    @Test
    public void tstUssCopyEmptyFromPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copy("", "/xxx/xx/xx");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyEmptyFromPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copy("   ", "/xxx/xx/xx");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssCopyEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", "");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCopyEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", "  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCopyNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssCopyNullFromInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", new CopyParams.Builder().from(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("from is null", errMsg);
    }

    @Test
    public void tstUssCopyEmptyFromInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", new CopyParams.Builder().from("").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyEmptyFromInParamsWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", new CopyParams.Builder().from("   ").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyInvalidTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copyCommon("name", new CopyParams.Builder().from("/xxx/xx/xx").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssCopyInvalidFromPathInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", new CopyParams.Builder().from("name").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstNullConnectionFailure() {
        try {
            new UssCopy(null);
        } catch (NullPointerException e) {
            assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

}
