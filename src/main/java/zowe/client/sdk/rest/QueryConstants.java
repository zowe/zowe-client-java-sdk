/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest;

/**
 * Constants for various Rest query info
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class QueryConstants {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private QueryConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Query parameter delimiter
     */
    public static final String COMBO_ID = "&";
    /**
     * Query identifier
     */
    public static final String QUERY_ID = "?";

}
