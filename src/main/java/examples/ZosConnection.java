/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package examples;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZosConnection {

    public static final String hostName = "XXX";
    public static final String zosmfPort = "XXX";
    public static final String userName = "XXX";
    public static final String password = "XXX";

}
