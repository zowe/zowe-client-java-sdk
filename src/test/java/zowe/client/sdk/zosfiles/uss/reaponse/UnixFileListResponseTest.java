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

        UnixFileListResponse response = JsonUtils.parseResponse(json, UnixFileListResponse.class, "fileList");

        assertEquals(1, response.getJsonVersion());
        assertEquals(2, response.getReturnedRows());
        assertEquals(2, response.getTotalRows());
        assertNotNull(response.getItems());
        assertEquals(2, response.getItems().size());
        assertEquals("/u/test", response.getItems().get(0).getName());
        assertEquals("/u/test2", response.getItems().get(1).getName());
    }

    @Test
    public void tstUnixFileListResponseDefaultsWhenMissingSuccess() throws ZosmfRequestException {
        UnixFileListResponse response = JsonUtils.parseResponse("{}", UnixFileListResponse.class, "fileListEmpty");
        assertEquals(0, response.getJsonVersion());
        assertEquals(0, response.getReturnedRows());
        assertEquals(0, response.getTotalRows());
        assertNull(response.getItems());
    }

}
