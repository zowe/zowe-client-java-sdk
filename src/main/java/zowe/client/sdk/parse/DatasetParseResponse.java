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
import zowe.client.sdk.zosfiles.dsn.response.Dataset;

/**
 * Extract Dataset from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class DatasetParseResponse extends JsonParseResponse {

    /**
     * DatasetParseResponse constructor
     *
     * @param data json data value to be parsed
     * @author Frank Giordano
     */
    public DatasetParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Transform data into Dataset object
     *
     * @return Dataset object
     * @author Frank Giordano
     */
    @Override
    public Dataset parseResponse() {
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
