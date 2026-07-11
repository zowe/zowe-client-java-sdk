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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for VariableGetResponse.
 *
 * @author Adithe Das
 * @version 7.0
 */

public class VariableGetResponseTest {

    @Test
    public void tstVariableGetResponseConstructorSuccess() {
        List<VariableResponse> variables = List.of(new VariableResponse("SYSNAME", "ZOS01", "System name"));
        List<VariableResponse> symbols = List.of(new VariableResponse("&SYSNAME", "ZOS01", "System symbol"));
        VariableGetResponse response = new VariableGetResponse(variables, symbols);
        assertEquals(1, response.getSystemVariableList().size());
        assertEquals(1, response.getSystemSymbolList().size());
    }

    @Test
    public void tstVariableGetResponseNullListsSuccess() {
        VariableGetResponse response = new VariableGetResponse(null, null);
        assertEquals(Collections.emptyList(), response.getSystemVariableList());
        assertEquals(Collections.emptyList(), response.getSystemSymbolList());
    }

    @Test
    public void tstVariableGetResponseToStringSuccess() {
        List<VariableResponse> variables = List.of(new VariableResponse("SYSNAME", "ZOS01", "System name"));
        VariableGetResponse response = new VariableGetResponse(variables, Collections.emptyList());
        String result = response.toString();
        assertTrue(result.contains("systemVariableList"));
        assertTrue(result.contains("SYSNAME"));
    }

}
