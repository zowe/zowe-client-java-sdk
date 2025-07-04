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
import zowe.client.sdk.core.AuthType;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.CreateParams;
import zowe.client.sdk.zosfiles.uss.input.CreateZfsParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCreate;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssCreate.
 *
 * @author Frank Giordano
 * @version 3.0
 */
@SuppressWarnings("ALL")
public class UssCreateTest {

    private final ZosConnection connection = new ZosConnection.Builder(AuthType.CLASSIC)
            .host("1").password("1").user("1").zosmfPort("1").build();
    private final ZosConnection tokenConnection = new ZosConnection.Builder(AuthType.TOKEN)
            .host("1").zosmfPort("1").cookie(new Cookie("hello=hello")).build();
    private PostJsonZosmfRequest mockJsonPostRequest;
    private PostJsonZosmfRequest mockJsonPostRequestToken;
    private UssCreate ussCreate;

    @Before
    public void init() {
        mockJsonPostRequest = Mockito.mock(PostJsonZosmfRequest.class);
        ussCreate = new UssCreate(connection);
    }

    @Test
    public void tstUssCreateSuccess() throws ZosmfRequestException {
        Mockito.when(mockJsonPostRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        final UssCreate ussCreate = new UssCreate(connection, mockJsonPostRequest);
        final Response response = ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssCreateToggleTokenSuccess() throws ZosmfRequestException {
        mockJsonPostRequestToken = Mockito.mock(PostJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonPostRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPostRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPostRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonPostRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonPostRequestToken).getHeaders();
        final UssCreate ussCreate = new UssCreate(tokenConnection, mockJsonPostRequestToken);

        Response response = ussCreate.create("/xx/xx/x", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        Assertions.assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPostRequestToken.getHeaders().toString());
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
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

}
