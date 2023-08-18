/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo;

/**
 * Constants to be used by the z/OSMF API
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class ZosmfConstants {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ZosmfConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Indicator for get info request
     */
    public final static String INFO = "/info";

    /**
     * Specifies the z/OS data set and file REST interface
     */
    public final static String RESOURCE = "/zosmf";

    /**
     * Indicator for get defined systems in zosmf
     */
    public final static String SYSTEMS = "/systems";

    /**
     * Indicator for get topology services
     */
    public final static String TOPOLOGY = "/resttopology";

}
