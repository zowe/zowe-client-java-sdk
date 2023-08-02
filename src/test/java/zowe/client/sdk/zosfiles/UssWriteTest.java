/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles;

import org.junit.Before;
import org.junit.Test;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.zosfiles.uss.input.WriteParams;
import zowe.client.sdk.zosfiles.uss.methods.UssWrite;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssWrite.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssWriteTest {

    private ZosConnection connection;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssWriteTextNullFileNamePathFailure() {
        UssWrite ussWrite = new UssWrite(connection);
        String errMsg = "";
        try {
            ussWrite.writeText(null, "content");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "file name path is null");
    }

    @Test
    public void tstUssWriteTextEmptyFileNamePathFailure() {
        UssWrite ussWrite = new UssWrite(connection);
        String errMsg = "";
        try {
            ussWrite.writeText("", "content");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "file name path not specified");
    }

    @Test
    public void tstUssWriteBinaryNullFileNamePathFailure() {
        UssWrite ussWrite = new UssWrite(connection);
        String errMsg = "";
        try {
            ussWrite.writeBinary(null, new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "file name path is null");
    }

    @Test
    public void tstUssWriteBinaryEmptyFileNamePathFailure() {
        UssWrite ussWrite = new UssWrite(connection);
        String errMsg = "";
        try {
            ussWrite.writeBinary("", new byte[0]);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "file name path not specified");
    }

    @Test
    public void tstUssWriteCommonNullFileNamePathFailure() {
        UssWrite ussWrite = new UssWrite(connection);
        String errMsg = "";
        try {
            ussWrite.writeCommon(null, new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "file name path is null");
    }

    @Test
    public void tstUssWriteCommonEmptyFileNamePathFailure() {
        UssWrite ussWrite = new UssWrite(connection);
        String errMsg = "";
        try {
            ussWrite.writeCommon("", new WriteParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "file name path not specified");
    }

    @Test
    public void tstUssWriteCommonNullParamsFailure() {
        UssWrite ussWrite = new UssWrite(connection);
        String errMsg = "";
        try {
            ussWrite.writeCommon("name", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "params is null");
    }

}
