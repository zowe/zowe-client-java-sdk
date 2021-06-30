/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso;

public class TsoConstants {
    /**
     * Quert id of logonProcedure passed to z/OSMF URI
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String PARM_PROC = "proc";

    /**
     * Quert id of character-set passed to z/OSMF URI
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String PARM_CHSET = "chset";

    /**
     * Quert id of code page passed to z/OSMF URI
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String PARM_CPAGE = "cpage";

    /**
     * Quert id of rows passed to z/OSMF URI
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String PARM_ROWS = "rows";

    /**
     * Quert id of columns passed to z/OSMF URI
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String PARM_COLS = "cols";

    /**
     * Quert id of account number passed to z/OSMF URI
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String PARM_ACCT = "acct";

    /**
     * Quert id of region size passed to z/OSMF URI
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String PARM_RSIZE = "rsize";

    /**
     * Default character-set value
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String DEFAULT_CHSET = "697";

    /**
     * Default code page value
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String DEFAULT_CPAGE = "1047";

    /**
     * Default number of rows value
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String DEFAULT_ROWS = "24";

    /**
     * Default number of columns value
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String DEFAULT_COLS = "80";

    /**
     * Default region-size value
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String DEFAULT_RSIZE = "4096";

    /**
     * Default logonProcedure value
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String DEFAULT_PROC = "IZUFPROC";

    /**
     * URI base for TSO API
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String RESOURCE = "/zosmf/tsoApp";

    /**
     * URI for starting TSO
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String RES_START_TSO = "tso";

    /**
     * Param for not reading reply
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String RES_DONT_READ_REPLY = "?readReply=false";

    /**
     * URI for TSO Ping API
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String RES_PING = TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/ping";
    /**
     * Tso response message type - prompt
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String TSO_PROMPT = "TSO PROMPT";
    /**
     * Tso response message type - message
     * @static
     * @type {string}
     * @memberof TsoConstants
     */
    public static final String TSO_MESSAGE = "TSO MESSAGE";

}
