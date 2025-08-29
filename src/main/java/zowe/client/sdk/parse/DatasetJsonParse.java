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
import zowe.client.sdk.zosfiles.dsn.model.Dataset;

/**
 * Extract Dataset from JSON response
 *
 * @author Frank Giordano
 * @version 5.0
 */
public final class DatasetJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final DatasetJsonParse instance = new DatasetJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private DatasetJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return DatasetJsonParse object
     * @author Frank Giordano
     */
    public static DatasetJsonParse getInstance() {
        return DatasetJsonParse.Holder.instance;
    }

    /**
     * Transform data into Dataset object
     *
     * @param args json data to parse
     * @return Dataset object
     * @author Frank Giordano
     */
    @Override
    public synchronized Dataset parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        return new Dataset.Builder()
                .dsname(data.get("dsname") != null ? (String) data.get("dsname") : null)
                .blksz(data.get("blksz") != null ? (String) data.get("blksz") : null)
                .catnm(data.get("catnm") != null ? (String) data.get("catnm") : null)
                .cdate(data.get("cdate") != null ? (String) data.get("cdate") : null)
                .dev(data.get("dev") != null ? (String) data.get("dev") : null)
                .dsntp(data.get("dsntp") != null ? (String) data.get("dsntp") : null)
                .dsorg(data.get("dsorg") != null ? (String) data.get("dsorg") : null)
                .edate(data.get("edate") != null ? (String) data.get("edate") : null)
                .extx(data.get("extx") != null ? (String) data.get("extx") : null)
                .lrectl(data.get("lrectl") != null ? (String) data.get("lrectl") : null)
                .migr(data.get("migr") != null ? (String) data.get("migr") : null)
                .mvol(data.get("mvol") != null ? (String) data.get("mvol") : null)
                .ovf(data.get("ovf") != null ? (String) data.get("ovf") : null)
                .rdate(data.get("rdate") != null ? (String) data.get("rdate") : null)
                .recfm(data.get("recfm") != null ? (String) data.get("recfm") : null)
                .sizex(data.get("sizex") != null ? (String) data.get("sizex") : null)
                .spacu(data.get("spacu") != null ? (String) data.get("spacu") : null)
                .used(data.get("used") != null ? (String) data.get("used") : null)
                .vol(data.get("vol") != null ? (String) data.get("vol") : null)
                .build();
    }

}
