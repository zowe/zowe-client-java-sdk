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
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.input.CopyParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCopy;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssCopy.
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssCopyTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private PutJsonZosmfRequest mockJsonPutRequest;
    private UssCopy ussCopy;

    @Before
    public void init() {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        ussCopy = new UssCopy(connection);
    }

    @Test
    public void tstUssCopySuccess() throws Exception {
        final UssCopy ussCopy = new UssCopy(connection, mockJsonPutRequest);
        final Response response = ussCopy.copy("/xxx/xx/xx", "/xxx/xx/xx");
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssCopyRecursiveSuccess() {
        final UssCopy ussCopy = new UssCopy(connection, mockJsonPutRequest);
        final Response response = ussCopy.copyCommon("/xxx/xx/xx",
                new CopyParams.Builder().from("/xxx/xx/xx").recursive(true).build());
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssCopyOverwriteSuccess() {
        final UssCopy ussCopy = new UssCopy(connection, mockJsonPutRequest);
        final Response response = ussCopy.copyCommon("/xxx/xx/xx",
                new CopyParams.Builder().from("/xxx/xx/xx").overwrite(true).build());
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssCopyInvalidTargetPathPathFailure() {
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", "name");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssCopyInvalidFromPathPathFailure() {
        String errMsg = "";
        try {
            ussCopy.copy("name", "/xxx/xx/xx");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssCopyNullFromPathFailure() {
        String errMsg = "";
        try {
            ussCopy.copy(null, "/xxx/xx/xx");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from is null", errMsg);
    }

    @Test
    public void tstUssCopyEmptyFromPathFailure() {
        String errMsg = "";
        try {
            ussCopy.copy("", "/xxx/xx/xx");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyEmptyFromPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussCopy.copy("   ", "/xxx/xx/xx");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyNullTargetPathFailure() {
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssCopyEmptyTargetPathFailure() {
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", "");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCopyEmptyTargetPathWithSpacesFailure() {
        String errMsg = "";
        try {
            ussCopy.copy("/xxx/xx/xx", "  ");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssCopyNullParamsFailure() {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssCopyNullFromInParamsFailure() {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", new CopyParams.Builder().from(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from is null", errMsg);
    }

    @Test
    public void tstUssCopyEmptyFromInParamsFailure() {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", new CopyParams.Builder().from("").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyEmptyFromInParamsWithSpacesFailure() {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", new CopyParams.Builder().from("   ").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("from not specified", errMsg);
    }

    @Test
    public void tstUssCopyInvalidTargetPathWithParamsFailure() {
        String errMsg = "";
        try {
            ussCopy.copyCommon("name", new CopyParams.Builder().from("/xxx/xx/xx").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssCopyInvalidFromPathInParamsFailure() {
        String errMsg = "";
        try {
            ussCopy.copyCommon("/xxx/xx/xx", new CopyParams.Builder().from("name").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

}
