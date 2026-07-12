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
 * Class containing unit tests for VariableGetInputFactory.
 *
 * @author Adithe Das
 * @version 7.0
 */
public class VariableGetInputFactoryTest {

    @Test
    public void tstCreateSystemInputSuccess() {
        VariableGetInputData input =
                VariableGetInputFactory.createZosVariable("PLEX1", "SYS1");
        assertEquals("PLEX1", input.getSysplexName().orElse(null));
        assertEquals("SYS1", input.getSystemName().orElse(null));
        assertEquals(VariableType.VARIABLE, input.getVariableType().orElse(null));
        assertFalse(input.isLocal());
    }

    @Test
    public void tstCreateSystemInputWithFiltersSuccess() {
        VariableGetInputData input = VariableGetInputFactory.createZosVariable(
                "PLEX1", "SYS1", Arrays.asList("VAR1", "VAR2"));
        assertEquals("PLEX1", input.getSysplexName().orElse(null));
        assertEquals("SYS1", input.getSystemName().orElse(null));
        assertEquals(Arrays.asList("VAR1", "VAR2"), input.getVariableNames().orElse(null));
        assertEquals(VariableType.VARIABLE, input.getVariableType().orElse(null));
        assertFalse(input.isLocal());
    }

    @Test
    public void tstCreateLocalInputSuccess() {
        VariableGetInputData input = VariableGetInputFactory.createZosVariableLocal();
        assertTrue(input.isLocal());
        assertFalse(input.getSysplexName().isPresent());
        assertFalse(input.getSystemName().isPresent());
        assertEquals(VariableType.VARIABLE, input.getVariableType().orElse(null));
    }

    @Test
    public void tstCreateLocalInputWithFiltersSuccess() {
        VariableGetInputData input =
                VariableGetInputFactory.createZosVariableLocal(Arrays.asList("VAR1", "VAR2"));
        assertTrue(input.isLocal());
        assertEquals(Arrays.asList("VAR1", "VAR2"), input.getVariableNames().orElse(null));
        assertEquals(VariableType.VARIABLE, input.getVariableType().orElse(null));
    }

    @Test
    public void tstCreateSymbolInputSuccess() {
        VariableGetInputData input =
                VariableGetInputFactory.createZosmfSymbol("PLEX1", "SYS1");
        assertEquals("PLEX1", input.getSysplexName().orElse(null));
        assertEquals("SYS1", input.getSystemName().orElse(null));
        assertEquals(VariableType.SYMBOL, input.getVariableType().orElse(null));
        assertFalse(input.isLocal());
    }

    @Test
    public void tstCreateLocalSymbolInputSuccess() {
        VariableGetInputData input = VariableGetInputFactory.createZosmfSymbolLocal();
        assertTrue(input.isLocal());
        assertEquals(VariableType.SYMBOL, input.getVariableType().orElse(null));
    }

}
