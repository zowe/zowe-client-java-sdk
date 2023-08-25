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

import org.junit.Test;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.zosfiles.uss.methods.UssExtAttr;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssExtAttr.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssExtAttrTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");

    @Test
    public void tstUssExtAttrSetValueFailure1() {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "pp");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure2() {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "at");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure3() {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "yu");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure4() {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "apap");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure1() {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "pp");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure2() {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "at");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure3() {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "yu");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure4() {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "apap");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

}
