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
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.CreateParams;
import zowe.client.sdk.zosfiles.uss.input.CreateZfsParams;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssCreate.
 *
 * @author Frank Giordano
 * @version 4.0
 */
@SuppressWarnings("ALL")
public class UssCreateTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private PostJsonZosmfRequest mockJsonPostRequest;
    private PostJsonZosmfRequest mockJsonPostRequestToken;
    private UssCreate ussCreate;

    @Before
    public void init() throws ZosmfRequestException {
        mockJsonPostRequest = Mockito.mock(PostJsonZosmfRequest.class);
        Mockito.when(mockJsonPostRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPostRequest).setUrl(any());
        doCallRealMethod().when(mockJsonPostRequest).getUrl();

        mockJsonPostRequestToken = Mockito.mock(PostJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonPostRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPostRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPostRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonPostRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonPostRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonPostRequestToken).getUrl();

        ussCreate = new UssCreate(connection);
    }

    @Test
    public void tstUssCreateSuccess() throws ZosmfRequestException {
        final UssCreate ussCreate = new UssCreate(connection, mockJsonPostRequest);
        final Response response = ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxx%2Fxx%2Fx", mockJsonPostRequest.getUrl());
    }

    @Test
    public void tstUssCreateToggleTokenSuccess() throws ZosmfRequestException {
        final UssCreate ussCreate = new UssCreate(connection, mockJsonPostRequestToken);
        Response response = ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPostRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxx%2Fxx%2Fx", mockJsonPostRequestToken.getUrl());
    }

    @Test
    public void tstUssCreateInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssCreateNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create(null, new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssCreateEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCreateEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("  ", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCreateNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssCreateNullTypeInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(null, "rwxrwxrwx"));
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("type is null", errMsg);
    }

    @Test
    public void tstUssCreateNullModeInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, null));
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("mode is null", errMsg);
    }

    @Test
    public void tstUssCreateEmptyModeInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, ""));
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify 9 character permission", errMsg);
    }

    @Test
    public void tstUssCreateEmptyModeInParamsWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "  "));
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify 9 character permission", errMsg);
    }

    @Test
    public void tstUssCreateInvalidModeWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "rwxrwxrwf"));
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid permission value", errMsg);
    }


    @Test
    public void tstUssCreateZfsCommonNullCylsPriFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.createZfsCommon("xx.xx.x", new CreateZfsParams.Builder(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("cylsPri is null", errMsg);
    }

    @Test
    public void tstUssCreateZfsCommonZeroCylsPriFailure2() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.createZfsCommon("xx.xx.x", new CreateZfsParams.Builder(0).build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify cylsPri greater than 0", errMsg);
    }

    @Test
    public void tstUssCreateZfsCommonNullFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.createZfsCommon(null, new CreateZfsParams.Builder(2).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is null", errMsg);
    }

    @Test
    public void tstUssCreateZfsCommonEmptyFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.createZfsCommon("", new CreateZfsParams.Builder(2).build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName not specified", errMsg);
    }

    @Test
    public void tstUssCreateZfsCommonNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.createZfsCommon("xx.xx.x", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssCreateZfsNullFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.createZfs(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is null", errMsg);
    }

    @Test
    public void tstUssCreateZfEmptyFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussCreate.createZfs("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName not specified", errMsg);
    }

    @Test
    public void tstNullConnectionFailure() {
        try {
            new UssCreate(null);
        } catch (NullPointerException e) {
            assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

    @Test
    public void tstSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PostJsonZosmfRequest.class);
        UssCreate ussCreate = new UssCreate(connection, request);
        assertNotNull(ussCreate);
    }

    @Test
    public void tstSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PostJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssCreate(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssCreate(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PostJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new UssCreate(connection, request)
        );
        assertEquals("POST_JSON request type required", exception.getMessage());
    }

}
