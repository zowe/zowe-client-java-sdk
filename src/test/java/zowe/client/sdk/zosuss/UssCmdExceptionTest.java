/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosuss;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.zosuss.exception.UssCmdException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for UssCmdException.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class UssCmdExceptionTest {

    @Test
    public void tstMessageIsStoredCorrectly() {
        UssCmdException ex = new UssCmdException("Test error", null);
        assertEquals("Test error", ex.getMessage());
    }

    @Test
    public void tstCauseIsStoredCorrectly() {
        Throwable cause = new RuntimeException("Root cause");
        UssCmdException ex = new UssCmdException("Wrapper message", cause);

        assertEquals("Wrapper message", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void tstToStringContainsClassNameAndMessage() {
        UssCmdException ex = new UssCmdException("Something went wrong", null);
        String toStringValue = ex.toString();

        assertTrue(toStringValue.contains("UssCmdException"));
        assertTrue(toStringValue.contains("Something went wrong"));
    }

    @Test
    public void tstExceptionWithNullMessage() {
        Throwable cause = new IllegalArgumentException("Bad arg");
        UssCmdException ex = new UssCmdException(null, cause);

        assertNull(ex.getMessage());
        assertSame(cause, ex.getCause());
    }

}
