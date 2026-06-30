/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for VariableResponse.
 *
 * @author Adithe Das
 * @version 7.0
 */

public class VariableResponseTest {

    @Test
    public void tstVariableResponseConstructorSuccess() {
        VariableResponse response = new VariableResponse("SYSNAME", "ZOS01", "System name");

        assertEquals("SYSNAME", response.getName());
        assertEquals("ZOS01", response.getValue());
        assertEquals("System name", response.getDescription());
    }

    @Test
    public void tstVariableResponseNullValuesSuccess() {
        VariableResponse response = new VariableResponse(null, null, null);

        assertEquals("", response.getName());
        assertEquals("", response.getValue());
        assertEquals("", response.getDescription());
    }

    @Test
    public void tstVariableResponseToStringSuccess() {
        VariableResponse response = new VariableResponse("SYSNAME", "ZOS01", "System name");

        String result = response.toString();

        assertTrue(result.contains("SYSNAME"));
        assertTrue(result.contains("ZOS01"));
        assertTrue(result.contains("System name"));
    }

}
