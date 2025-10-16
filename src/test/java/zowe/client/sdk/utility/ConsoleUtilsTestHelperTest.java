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
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Class containing unit test for ConsoleUtil.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ConsoleUtilsTestHelperTest {

    @Test
    public void tstConsoleUtilPrivateConstructorThrowsException() {
        Constructor<?> constructor = ConsoleUtils.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                () -> constructor.newInstance()
        );
        assertEquals("java.lang.IllegalStateException: Utility class", exception.getCause().toString());
    }

    @Test
    public void tstProcessCmdResponseConvertsCRToLF() {
        String input = "LINE1\rLINE2";
        String output = ConsoleUtils.processCmdResponse(input);
        assertEquals("LINE1\nLINE2\n", output);
    }

    @Test
    public void tstProcessCmdResponseAddsTrailingNewline() {
        String input = "LINE1";
        String output = ConsoleUtils.processCmdResponse(input);
        assertEquals("LINE1\n", output);
    }

    @Test
    public void tstProcessCmdResponseKeepsExistingNewline() {
        String input = "LINE1\n";
        String output = ConsoleUtils.processCmdResponse(input);
        assertEquals("LINE1\n", output);
    }

    @Test
    public void tstProcessCmdResponseBlankString() {
        String input = "";
        String output = ConsoleUtils.processCmdResponse(input);
        assertEquals("", output);
    }

    @Test
    public void tstProcessCmdResponseWhitespaceOnly() {
        String input = "   ";
        String output = ConsoleUtils.processCmdResponse(input);
        assertEquals("   ", output); // Space is considered blank; no newline appended
    }

}
