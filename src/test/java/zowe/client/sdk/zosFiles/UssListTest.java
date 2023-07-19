/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosFiles;

import org.junit.Before;
import org.junit.Test;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.zosfiles.uss.input.ListParams;
import zowe.client.sdk.zosfiles.uss.input.ListZfsParams;
import zowe.client.sdk.zosfiles.uss.methods.UssList;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssList.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssListTest {

    private ZosConnection connection;

    @Before
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstUssListFileListParmsNullFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.fileList(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "params is null");
    }

    @Test
    public void tstUssListFileListParmsPathEmptyFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.fileList(new ListParams.Builder().path("").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "path not specified");
    }

    @Test
    public void tstUssListFileListParmsPathNullFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.fileList(new ListParams.Builder().path(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "path is null");
    }

    @Test
    public void tstUssListZfsListEmptyParmsFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "no path or fsname specified");
    }

    @Test
    public void tstUssListZfsListParmsFsnameEmptyFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().fsname("").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "fsname not specified");
    }

    @Test
    public void tstUssListZfsListParmsFsnameNullFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().fsname(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "fsname is null");
    }

    @Test
    public void tstUssListZfsListParmsPathAndFsnameFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().path("p").fsname("p").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "cannot specify both path and fsname parameters");
    }

    @Test
    public void tstUssListZfsListParmsPathEmptyFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().path("").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "path not specified");
    }

    @Test
    public void tstUssListZfsListParmsPathNullFailure() {
        UssList ussList = new UssList(connection);
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().path(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals(errMsg, "path is null");
    }

}
