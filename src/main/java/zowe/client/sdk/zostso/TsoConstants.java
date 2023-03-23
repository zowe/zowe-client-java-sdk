/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso;

/**
 * Constants for various tso related info
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class TsoConstants {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private TsoConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Default character-set value
     */
    public static final String DEFAULT_CHSET = "697";
    /**
     * Default number of columns value
     */
    public static final String DEFAULT_COLS = "80";
    /**
     * Default code page value
     */
    public static final String DEFAULT_CPAGE = "1047";
    /**
     * Default logonProcedure value
     */
    public static final String DEFAULT_PROC = "IZUFPROC";
    /**
     * Default number of rows value
     */
    public static final String DEFAULT_ROWS = "24";
    /**
     * Default region-size value
     */
    public static final String DEFAULT_RSIZE = "4096";
    /**
     * Query id of account number passed to z/OSMF URI
     */
    public static final String PARAM_ACCT = "acct";
    /**
     * Query id of character-set passed to z/OSMF URI
     */
    public static final String PARAM_CHSET = "chset";
    /**
     * Query id of columns passed to z/OSMF URI
     */
    public static final String PARAM_COLS = "cols";
    /**
     * Query id of code page passed to z/OSMF URI
     */
    public static final String PARAM_CPAGE = "cpage";
    /**
     * Query id of logonProcedure passed to z/OSMF URI
     */
    public static final String PARAM_PROC = "proc";
    /**
     * Query id of rows passed to z/OSMF URI
     */
    public static final String PARAM_ROWS = "rows";
    /**
     * Query id of region size passed to z/OSMF URI
     */
    public static final String PARAM_RSIZE = "rsize";
    /**
     * URI base for TSO API
     */
    public static final String RESOURCE = "/zosmf/tsoApp";
    /**
     * Param for not reading reply
     */
    public static final String RES_DONT_READ_REPLY = "?readReply=false";
    /**
     * URI for starting TSO
     */
    public static final String RES_START_TSO = "tso";
    /**
     * URI for TSO Ping API
     */
    public static final String RES_PING = TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/ping";
    /**
     * Tso response message type - message
     */
    public static final String TSO_MESSAGE = "TSO MESSAGE";
    /**
     * Tso response message type - prompt
     */
    public static final String TSO_PROMPT = "TSO PROMPT";
    /**
     * z/OSMF unknown error
     */
    public static final String ZOSMF_UNKNOWN_ERROR = "zOSMF unknown error response";

}
