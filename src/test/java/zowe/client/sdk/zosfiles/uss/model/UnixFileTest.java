/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.model;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.JsonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                "  \"name\": \"/u/test\",\n" +
                "  \"mode\": \"-rw-r--r--\",\n" +
                "  \"size\": 5,\n" +
                "  \"uid\": 100,\n" +
                "  \"user\": \"USER\",\n" +
                "  \"gid\": 200,\n" +
                "  \"group\": \"GRP\",\n" +
                "  \"mtime\": \"2024-01-01\",\n" +
                "  \"target\": \"\"\n" +
                "}";

        UnixFile unixFile = JsonUtils.parseResponse(json, UnixFile.class, "unixFile");

        assertEquals("/u/test", unixFile.getName());
        assertEquals("-rw-r--r--", unixFile.getMode());
        assertEquals(5L, unixFile.getSize());
        assertEquals(100L, unixFile.getUid());
        assertEquals("USER", unixFile.getUser());
        assertEquals(200L, unixFile.getGid());
        assertEquals("GRP", unixFile.getGroup());
        assertEquals("2024-01-01", unixFile.getMtime());
        assertEquals("", unixFile.getTarget());
    }

    @Test
    public void tstUnixFileDefaultsWhenMissingSuccess() throws ZosmfRequestException {
        UnixFile unixFile = JsonUtils.parseResponse("{}", UnixFile.class, "unixFileEmpty");

        assertEquals("", unixFile.getName());
        assertEquals("", unixFile.getMode());
        assertEquals(0L, unixFile.getSize());
        assertEquals(0L, unixFile.getUid());
        assertEquals("", unixFile.getUser());
        assertEquals(0L, unixFile.getGid());
        assertEquals("", unixFile.getGroup());
        assertEquals("", unixFile.getMtime());
        assertEquals("", unixFile.getTarget());
    }

}
