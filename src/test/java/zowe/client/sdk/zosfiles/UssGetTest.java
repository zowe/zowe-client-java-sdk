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
import zowe.client.sdk.zosfiles.uss.methods.UssGet;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssGet.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssGetTest {

    private ZosConnection connection;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssGetTextFilePathNullNameFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getText(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name is null", errMsg);
    }

    @Test
    public void tstUssGetTextFilePathEmptyNameFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getText("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name not specified", errMsg);
    }

    @Test
    public void tstUssGetBinaryFilePathNullNameFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getText(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name is null", errMsg);
    }

    @Test
    public void tstUssGetBinaryFilePathEmptyNameFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getText("");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("file path name not specified", errMsg);
    }

    @Test
    public void tstUssGetCommonNullParamsFailure() {
        UssGet ussGet = new UssGet(connection);
        String errMsg = "";
        try {
            ussGet.getCommon("name", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

}
