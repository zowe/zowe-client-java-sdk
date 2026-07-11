/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.input.factory;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.zosvariables.type.VariableType;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for VariableGetInputData.
 *
 * @author Adithe Das
 * @version 7.0
 */
public class VariableGetInputDataTest {

    @Test
    public void tstVariableGetInputDataBuilderSystemSuccess() {
        VariableGetInputData input = new VariableGetInputData.Builder().setSysplexName("PLEX1").setSystemName("SYS1").setVariableNames(Arrays.asList("VAR1", "VAR2")).setVariableType(VariableType.VARIABLE).setLocal(false).build();

        assertEquals("PLEX1", input.getSysplexName().orElse(null));
        assertEquals("SYS1", input.getSystemName().orElse(null));
        assertEquals(Arrays.asList("VAR1", "VAR2"), input.getVariableNames().orElse(null));
        assertEquals(VariableType.VARIABLE, input.getVariableType().orElse(null));
        assertFalse(input.isLocal());
    }

    @Test
    public void tstVariableGetInputDataBuilderLocalSuccess() {
        VariableGetInputData input = new VariableGetInputData.Builder().setLocal(true).build();

        assertTrue(input.isLocal());
        assertFalse(input.getSysplexName().isPresent());
        assertFalse(input.getSystemName().isPresent());
        assertFalse(input.getVariableNames().isPresent());
        assertFalse(input.getVariableType().isPresent());
    }

}
