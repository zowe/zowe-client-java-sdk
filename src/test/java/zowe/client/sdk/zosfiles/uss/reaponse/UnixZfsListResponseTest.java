/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.reaponse;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.JsonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        UnixZfsListResponse response = JsonUtils.parseResponse(json, UnixZfsListResponse.class, "zfsList");

        assertEquals(1, response.getJsonVersion());
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        assertEquals("ZFS1", response.getItems().get(0).getName());
        assertEquals("rw,x", response.getItems().get(0).getMode());
        assertTrue(response.getItems().get(0).isMoreRows());
    }

    @Test
    public void tstUnixZfsListResponseDefaultsWhenMissingSuccess() throws ZosmfRequestException {
        UnixZfsListResponse response = JsonUtils.parseResponse("{}", UnixZfsListResponse.class, "zfsListEmpty");
        assertEquals(0, response.getJsonVersion());
        assertNull(response.getItems());
    }

}
