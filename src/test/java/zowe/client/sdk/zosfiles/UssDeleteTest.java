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
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.methods.UssDelete;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssDelete.
 *
 * @author Frank Giordano
 * @version 2.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssDeleteTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private DeleteJsonZosmfRequest mockJsonDeleteRequest;
    private UssDelete ussDelete;

    @Before
    public void init() throws ZosmfRequestException {
        mockJsonDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockJsonDeleteRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success", null));
        ussDelete = new UssDelete(connection);
    }

    @Test
    public void tstUssDeleteSuccess() throws ZosmfRequestException {
        final UssDelete ussDelete = new UssDelete(connection, mockJsonDeleteRequest);
        final Response response = ussDelete.delete("/xxx/xx/xx");
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssDeleteRecursiveSuccess() throws ZosmfRequestException {
        final UssDelete ussDelete = new UssDelete(connection, mockJsonDeleteRequest);
        final Response response = ussDelete.delete("/xxx/xx/xx", true);
        Assertions.assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        Assertions.assertEquals(200, response.getStatusCode().orElse(-1));
        Assertions.assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstUssDeleteInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.delete("name");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssDeleteInvalidTargetPathWithRecursiveFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.delete("name", true);
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssDeleteNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.delete(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssDeleteEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.delete("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssDeleteEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.delete("   ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssDeleteNullTargetPathWithRecursiveFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.delete(null, true);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssDeleteEmptyTargetPathWithRecursiveFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.delete("", true);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath not specified", errMsg);
    }

    @Test
    public void tstUssDeleteZfsNullFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.deleteZfs(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName is null", errMsg);
    }

    @Test
    public void tstUssDeleteZfsEmptyFileSystemNameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.deleteZfs("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName not specified", errMsg);
    }

    @Test
    public void tstUssDeleteZfsEmptyFileSystemNameWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussDelete.deleteZfs("   ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileSystemName not specified", errMsg);
    }

}
