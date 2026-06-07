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
 * Constants for z/OS system variables REST processing.
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public final class VariableConstants {

    /**
     * Private constructor defined to avoid instantiation of class.
     */
    private VariableConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * z/OS system variables service path.
     */
    public static final String SYSTEM_VARIABLES = "/variables/rest/1.0/systems";

}
