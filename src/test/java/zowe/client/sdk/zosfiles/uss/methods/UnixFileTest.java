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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for UnixFile.
 *
 * @author Ashish Kumar Dash
 * @version 6.0
 */
public class UnixFileTest {

    @Test
    public void tstUnixFileParseWithValuesSuccess() throws ZosmfRequestException {
        String json = "{\n" +
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
                "    }\n" +
                "  ]\n" +
                "}";

        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        GetJsonZosmfRequest mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(new Response(json, 200, "success"));

        UssList ussList = new UssList(connection, mockJsonGetRequest);
        List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());

        assertEquals("/u/test", items.get(0).getName());
        assertEquals("-rw-r--r--", items.get(0).getMode());
        assertEquals(5L, items.get(0).getSize());
        assertEquals(100L, items.get(0).getUid());
        assertEquals("USER", items.get(0).getUser());
        assertEquals(200L, items.get(0).getGid());
        assertEquals("GRP", items.get(0).getGroup());
        assertEquals("2024-01-01", items.get(0).getMtime());
        assertEquals("", items.get(0).getTarget());
    }

    @Test
    public void tstUnixFileDefaultsWhenMissingSuccess() throws ZosmfRequestException {
        String json = "{\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"size\": 0\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        GetJsonZosmfRequest mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(new Response(json, 200, "success"));

        UssList ussList = new UssList(connection, mockJsonGetRequest);
        List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());

        assertEquals(1, items.size());
        assertEquals("", items.get(0).getName());
        assertEquals("", items.get(0).getMode());
        assertEquals(0L, items.get(0).getSize());
        assertEquals(0L, items.get(0).getUid());
        assertEquals("", items.get(0).getUser());
        assertEquals(0L, items.get(0).getGid());
        assertEquals("", items.get(0).getGroup());
        assertEquals("", items.get(0).getMtime());
        assertEquals("", items.get(0).getTarget());
    }

}
