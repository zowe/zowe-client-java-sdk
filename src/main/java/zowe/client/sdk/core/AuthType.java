/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.core;

/**
 * Class to represent an authentication type used for the http request.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public enum AuthType {

    /**
     * Authentication classic type. This represents the bearer header with the requirement
     * for the username and password to be specified within ZosConnection object.
     */
    CLASSIC,
    /**
     * Authentication cookie type. This case represents using a cookie value to use for
     * authentication for the http request.
     */
    COOKIE,
    /**
     * Authentication ssl type. This case represents using a certificate file for
     * authentication for the http request.
     */
    SSL

}
