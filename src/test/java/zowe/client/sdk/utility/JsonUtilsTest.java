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

import org.junit.jupiter.api.Test;

/**
 * Class containing unit test for JsonParserUtils.
 *
 * @author Frank Giordano
 * @version 6.0
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

}
