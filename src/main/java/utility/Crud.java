/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

/**
 * Crud type to use for dataset error handling
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class Crud {

    public enum type {
        create, read, copy, delete, write, download
    }

}
