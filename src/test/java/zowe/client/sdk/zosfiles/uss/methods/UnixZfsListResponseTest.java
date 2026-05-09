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
import zowe.client.sdk.zosfiles.uss.input.UssListZfsInputData;
import zowe.client.sdk.zosfiles.uss.model.UnixZfs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for UnixZfsListResponse.
 *
 * @author Ashish Kumar Dash
 * @version 6.0
 */
public class UnixZfsListResponseTest {

    @Test
    public void tstUnixZfsListResponseParseSuccess() throws ZosmfRequestException {
        String json = "{\n" +
                "  \"JSONversion\": 1,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"name\": \"ZFS1\",\n" +
                "      \"mountpoint\": \"/u\",\n" +
                "      \"fstname\": \"SYS1\",\n" +
                "      \"status\": \"active\",\n" +
                "      \"mode\": [\"rw\", \"x\"],\n" +
                "      \"dev\": 2718,\n" +
                "      \"fstype\": 1,\n" +
                "      \"bsize\": 1024,\n" +
                "      \"bavail\": 100,\n" +
                "      \"blocks\": 200,\n" +
                "      \"sysname\": \"SYS1\",\n" +
                "      \"readibc\": 10,\n" +
                "      \"writeibc\": 20,\n" +
                "      \"diribc\": 30,\n" +
                "      \"returnedRows\": 1,\n" +
                "      \"totalRows\": 1,\n" +
                "      \"moreRows\": true\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        GetJsonZosmfRequest mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(new Response(json, 200, "success"));

        UssList ussList = new UssList(connection, mockJsonGetRequest);
        List<UnixZfs> items = ussList.getZfsSystems(new UssListZfsInputData.Builder().path("/xxx/xx/x").build());

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("ZFS1", items.get(0).getName());
        assertEquals("rw,x", items.get(0).getMode());
        assertTrue(items.get(0).isMoreRows());
    }

    @Test
    public void tstUnixZfsListResponseDefaultsWhenMissingSuccess() throws ZosmfRequestException {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        GetJsonZosmfRequest mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(new Response("{}", 200, "success"));

        UssList ussList = new UssList(connection, mockJsonGetRequest);
        List<UnixZfs> items = ussList.getZfsSystems(new UssListZfsInputData.Builder().path("/xxx/xx/x").build());

        assertNotNull(items);
        assertEquals(0, items.size());
    }

}
