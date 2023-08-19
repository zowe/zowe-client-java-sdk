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
import zowe.client.sdk.zosfiles.dsn.response.Dataset;

/**
 * Extract Dataset from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class DatasetJsonParse implements JsonParse {

    /**
     * Represents one singleton instance
     */
    private static JsonParse INSTANCE;

    /**
     * JSON data value to be parsed
     */
    private JSONObject data;

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private DatasetJsonParse() {
    }

    /**
     * Get singleton instance
     *
     * @return ConsoleResponseService object
     * @author Frank Giordano
     */
    public synchronized static JsonParse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatasetJsonParse();
        }
        return INSTANCE;
    }

    /**
     * Transform data into Dataset object
     *
     * @return Dataset object
     * @author Frank Giordano
     */
    @Override
    public Dataset parseResponse() {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.REQUIRED_ACTION_MSG);
        final Dataset dataset = new Dataset.Builder()
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
        data = null;
        return dataset;
    }

    /**
     * Set the data to be parsed
     *
     * @param data json data to parse
     * @return JsonParseResponse this object
     * @author Frank Giordano
     */
    @Override
    public JsonParse setJsonObject(final JSONObject data) {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.DATA_NULL_MSG);
        this.data = data;
        return this;
    }

}
