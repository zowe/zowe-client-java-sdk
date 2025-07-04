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
import zowe.client.sdk.core.AuthType;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.methods.UssExtAttr;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssExtAttr.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class UssExtAttrTest {

    private final ZosConnection connection = new ZosConnection.Builder(AuthType.CLASSIC)
            .host("1").password("1").user("1").zosmfPort("1").build();

    @Test
    public void tstUssExtAttrSetValueFailure1() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "pp");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure2() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "at");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure3() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "yu");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure4() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "apap");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure1() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "pp");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure2() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "at");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure3() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "yu");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure4() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "apap");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetNullTargetPathFailure4() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.reset(null, "a");
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssExtAttrSetNullTargetPathFailure4() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.set(null, "a");
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

    @Test
    public void tstUssExtAttrDisplayNullTargetPathFailure4() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection);
        String errMsg = "";
        try {
            ussExtAttr.display(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is null", errMsg);
    }

}
