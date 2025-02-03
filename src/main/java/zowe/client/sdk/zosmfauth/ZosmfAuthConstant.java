/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfauth;

/**
 * Constants for zosmfauth package
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfAuthConstant {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ZosmfAuthConstant() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Specifies the z/OSMF authenticate resource
     */
    public static final String RESOURCE = "/zosmf/services/authenticate";

}
