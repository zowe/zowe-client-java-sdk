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
import zowe.client.sdk.zosfiles.uss.response.UnixZfs;

/**
 * Extract UNIX zfs from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UnixZfsParseResponse extends JsonParseResponse {

    private String modeStr;

    /**
     * UnixZfsParseResponse constructor
     *
     * @param data json object
     * @author Frank Giordano
     */
    public UnixZfsParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Transform UNIX zfs json into UssZfsItem object
     *
     * @return UssZfsItem object
     * @author Frank Giordano
     */
    @Override
    public UnixZfs parseResponse() {
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
                .moreRows(data.get("moreRows") != null ? (Boolean) data.get("moreRows") : false)
                .build();
    }

    /**
     * Special field that holds a String comma delimited with mode permission values.
     * Value is parsed by parent calling and set here to be included in main parse object
     * done in parseResponse() method.
     *
     * @param modeStr mode permission(s) string value
     * @author Frank Giordano
     */
    public void setModeStr(String modeStr) {
        this.modeStr = modeStr;
    }

}
