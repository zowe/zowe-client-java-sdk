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
import zowe.client.sdk.zosfiles.uss.input.CreateParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCreate;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssCreate.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssCreateTest {

    private ZosConnection connection;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssCreateNullNameFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create(null, new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "name is null");
    }

    @Test
    public void tstUssCreateEmptyNameFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("", new CreateParams(CreateType.FILE, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "name not specified");
    }

    @Test
    public void tstUssCreateNullParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "params is null");
    }

    @Test
    public void tstUssCreateNullTypeParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(null, "rwxrwxrwx"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "type is null");
    }

    @Test
    public void tstUssCreateNullModeParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, null));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "mode is null");
    }

    @Test
    public void tstUssCreateEmptyModeParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, ""));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "specify 9 char permission");
    }

    @Test
    public void tstUssCreateInvalidModeParamsFailure() {
        UssCreate ussCreate = new UssCreate(connection);
        String errMsg = "";
        try {
            ussCreate.create("name", new CreateParams(CreateType.FILE, "rwxrwxrwf"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "specify valid permission");
    }

}
