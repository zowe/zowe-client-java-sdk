/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parse;

/**
 * Constants definitions for parse package
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class ParseConstants {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ParseConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Data is null message
     */
    public static final String DATA_NULL_MSG = "data is null";

    /**
     * Required action for parse processing message
     */
    public static final String REQUIRED_ACTION_MSG =
            "each parseResponse call requires data to be reset via setJsonObject first";

    /**
     * Required action for parse processing message
     */
    public static final String REQUIRED_ACTION_MODE_STR_MSG =
            "each parseResponse call requires data to be reset via modeStr first";

    /**
     * Required action for parse processing message
     */
    public static final String REQUIRED_ACTION_ZOS_LOG_ITEMS_MSG =
            "each parseResponse call requires data to be reset via setZosLogItems first";

}
