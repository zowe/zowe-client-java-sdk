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
     * isProcessResponse is null message
     */
    public static final String IS_PROCESS_RESPONSE_NULL_MSG = "isProcessResponse is null";

    /**
     * Mode is null message
     */
    public static final String MODE_NULL_STR_MSG = "mode is null";

    /**
     * Mode not specified message
     */
    public static final String MODE_EMPTY_STR_MSG = "mode not specified";

    /**
     * List of ZosLogItem is null message
     */
    public static final String LIST_OF_ZOS_LOG_ITEM_NULL_MSG = "data is null";

}
