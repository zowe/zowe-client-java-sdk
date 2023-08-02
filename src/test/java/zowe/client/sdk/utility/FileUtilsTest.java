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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for FileUtils.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class FileUtilsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstFileUtilsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        Utils.validateClass(FileUtils.class, privateConstructorExceptionMsg);
    }

    @Test
    public void tstValidatePermissionSuccess() {
        final String value = "rwxrwxrwx";
        final String result = FileUtils.validatePermission(value);
        assertEquals(value, result);
    }

    @Test
    public void tstValidatePermissionLengthFailure() {
        final String value = "rwxrwxrwxx";
        String errMsg = "";
        try {
            FileUtils.validatePermission(value);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals( "specify 9 character permission", errMsg);
    }

    @Test
    public void tstValidatePermissionInvalidFailure() {
        final String value = "rwxrwxrkk";
        String errMsg = "";
        try {
            FileUtils.validatePermission(value);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid permission", errMsg);
    }

    @Test
    public void tstValidatePermissionNullFailure() {
        String errMsg = "";
        try {
            FileUtils.validatePermission(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("permission value is null", errMsg);
    }

}
