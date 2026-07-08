/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class containing unit test for JsonParserUtils.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class JsonUtilsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstJsonParserUtilsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        UtilsTestHelper.validateClass(JsonUtils.class, privateConstructorExceptionMsg);
    }

    @Test
    public void tstParseSuccess() throws ZosmfRequestException {
        final JsonNode json = JsonUtils.parse("{\"key\":\"value\"}");

        assertEquals("value", json.get("key").asText());
    }

    @Test
    public void tstParseInvalidJsonFailure() {
        assertThrows(ZosmfRequestException.class, () -> JsonUtils.parse("{\"key\":\"value\""));
    }

}
