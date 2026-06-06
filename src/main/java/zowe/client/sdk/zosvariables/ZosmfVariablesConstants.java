/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables;

/**
 * Constants for z/OSMF system variables REST processing.
 */
public final class ZosmfVariablesConstants {

    /**
     * Private constructor defined to avoid instantiation of class.
     */
    private ZosmfVariablesConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * z/OSMF system variables service path.
     */
    public static final String SYSTEM_VARIABLES = "/variables/rest/1.0/systems";

    /**
     * Local system path segment.
     */
    public static final String LOCAL = "/local";

    /**
     * source query parameter name.
     */
    public static final String SOURCE_QUERY_PARAMETER = "source";

    /**
     * var-name query parameter name.
     */
    public static final String VAR_NAME_QUERY_PARAMETER = "var-name";

}