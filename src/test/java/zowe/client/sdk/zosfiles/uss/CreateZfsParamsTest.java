/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss;

import org.junit.Test;
import zowe.client.sdk.zosfiles.uss.input.create.CreateZfsInput;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class containing unit tests for CreateZfsParams.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class CreateZfsParamsTest {

    @Test
    public void tstCreateZfsParamsValueCheckSuccess() {
        CreateZfsInput createZfsParams = new CreateZfsInput.Builder(10).build();
        assertEquals(10, createZfsParams.getCylsPri().getAsInt());
    }

    @Test
    public void tstCreateZfsParamsNullValueFailure() {
        boolean isException = false;
        try {
            new CreateZfsInput.Builder(null).build();
        } catch (Exception e) {
            isException = true;
        }
        assertTrue(isException);
    }

    @Test
    public void tstCreateZfsParamsNullValueMessageFailure() {
        String errMsg = null;
        try {
            new CreateZfsInput.Builder(null).build();
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("cylsPri is null", errMsg);
    }

    @Test
    public void tstCreateZfsParamsZeroValueMessageFailure() {
        String errMsg = null;
        try {
            new CreateZfsInput.Builder(0).build();
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify cylsPri greater than 0", errMsg);
    }

    @Test
    public void tstCreateZfsParamsNegativeValueMessageFailure() {
        String errMsg = null;
        try {
            new CreateZfsInput.Builder(-1).build();
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify cylsPri greater than 0", errMsg);
    }

}
