/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole;

/**
 * Utility Class contains helper methods for console response commands response processing
 *
 * @author Frank Giordano
 * @version 3.0
 */
public final class ConsoleConstants {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ConsoleConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Functionality type
     */
    public static final String CLASS = "Consoles";
    /**
     * Base Resource for console
     */
    public static final String RESOURCE = "/zosmf/restconsoles/consoles";

    /**
     * Resource authorization for console
     */
    public static final String RES_DEF_CN = "defcn";

}
