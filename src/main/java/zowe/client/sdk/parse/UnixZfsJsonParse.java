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

import org.json.simple.JSONObject;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.uss.response.UnixZfs;

/**
 * Extract UNIX zfs from JSON response
 *
 * @author Frank Giordano
 * @version 5.0
 */
public final class UnixZfsJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final UnixZfsJsonParse instance = new UnixZfsJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private UnixZfsJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return UnixZfsJsonParse object
     * @author Frank Giordano
     */
    public static UnixZfsJsonParse getInstance() {
        return UnixZfsJsonParse.Holder.instance;
    }

    /**
     * Transform data into UnixZfs object
     *
     * @param args first arg json data to parse, second arg mode permission string value comma delimited
     * @return UssZfsItem object
     * @author Frank Giordano
     */
    @Override
    public synchronized UnixZfs parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        ValidateUtils.checkNullParameter(args[1] == null, ParseConstants.MODE_NULL_STR_MSG);
        final JSONObject data = (JSONObject) args[0];
        final String modeStr = (String) args[1];
        ValidateUtils.checkNullParameter(modeStr.isBlank(), ParseConstants.MODE_EMPTY_STR_MSG);
        return new UnixZfs.Builder()
                .name(data.get("name") != null ? (String) data.get("name") : null)
                .mountpoint(data.get("mountpoint") != null ? (String) data.get("mountpoint") : null)
                .fstname(data.get("fstname") != null ? (String) data.get("fstname") : null)
                .status(data.get("status") != null ? (String) data.get("status") : null)
                .mode(modeStr)
                .dev(data.get("dev") != null ? (Long) data.get("dev") : null)
                .fstype(data.get("fstype") != null ? (Long) data.get("fstype") : null)
                .bsize(data.get("bsize") != null ? (Long) data.get("bsize") : null)
                .bavail(data.get("bavail") != null ? (Long) data.get("bavail") : null)
                .blocks(data.get("blocks") != null ? (Long) data.get("blocks") : null)
                .sysname(data.get("sysname") != null ? (String) data.get("sysname") : null)
                .readibc(data.get("readibc") != null ? (Long) data.get("readibc") : null)
                .writeibc(data.get("writeibc") != null ? (Long) data.get("writeibc") : null)
                .diribc(data.get("diribc") != null ? (Long) data.get("diribc") : null)
                .returnedRows(data.get("returnedRows") != null ? (Long) data.get("returnedRows") : null)
                .totalRows(data.get("totalRows") != null ? (Long) data.get("totalRows") : null)
                .moreRows(data.get("moreRows") != null && (boolean) data.get("moreRows"))
                .build();
    }

}
