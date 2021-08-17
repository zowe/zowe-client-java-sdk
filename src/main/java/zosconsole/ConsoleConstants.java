/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosconsole;

/**
 * Utility Class contains helper methods for console response commands response processing
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ConsoleConstants {

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


    /**
     * Solmsgs url query part
     */
    public static final String SOL_MSGS = "/solmsgs";

    /**
     * Num of TSO prompt Attempts
     */
    public static final int DEFAULT_FOLLOWUP_ATTEMPTS = 1;

    /**
     * Default timeout
     */
    public static final int DEFAULT_TIMEOUT = 0;

}
