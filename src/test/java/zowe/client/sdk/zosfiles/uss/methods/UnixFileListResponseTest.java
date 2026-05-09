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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.UssListInputData;
import zowe.client.sdk.zosfiles.uss.model.UnixFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class containing unit tests for UnixFileListResponse.
 *
 * @author Ashish Kumar Dash
 * @version 6.0
 */
public class UnixFileListResponseTest {

    @Test
    public void tstUnixFileListResponseParseSuccess() throws ZosmfRequestException {
        String json = "{\n" +
                "  \"JSONversion\": 1,\n" +
                "  \"returnedRows\": 2,\n" +
                "  \"totalRows\": 2,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"name\": \"/u/test\",\n" +
                "      \"mode\": \"-rw-r--r--\",\n" +
                "      \"size\": 5,\n" +
                "      \"uid\": 100,\n" +
                "      \"user\": \"USER\",\n" +
                "      \"gid\": 200,\n" +
                "      \"group\": \"GRP\",\n" +
                "      \"mtime\": \"2024-01-01\",\n" +
                "      \"target\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"/u/test2\",\n" +
                "      \"mode\": \"drwxr-xr-x\",\n" +
                "      \"size\": 10,\n" +
                "      \"uid\": 101,\n" +
                "      \"user\": \"USER2\",\n" +
                "      \"gid\": 201,\n" +
                "      \"group\": \"GRP2\",\n" +
                "      \"mtime\": \"2024-01-02\",\n" +
                "      \"target\": \"link\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        GetJsonZosmfRequest mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(new Response(json, 200, "success"));

        UssList ussList = new UssList(connection, mockJsonGetRequest);
        List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("/u/test", items.get(0).getName());
        assertEquals("/u/test2", items.get(1).getName());
    }

    @Test
    public void tstUnixFileListResponseDefaultsWhenMissingSuccess() throws ZosmfRequestException {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        GetJsonZosmfRequest mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(new Response("{}", 200, "success"));

        UssList ussList = new UssList(connection, mockJsonGetRequest);
        List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());

        assertNotNull(items);
        assertEquals(0, items.size());
    }

}
