/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosfiles.dsn.response.Dataset;
import zowe.client.sdk.zosfiles.dsn.response.Member;

/**
 * Utility Class for dataset (DSN) related static helper methods.
 *
 * @author Nikunj Goyal
 * @version 2.0
 */
public final class DataSetUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private DataSetUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Transform JSON into Dataset object
     *
     * @param jsonObject JSON object
     * @return Dataset object
     * @author Frank Giordano
     */
    public static Dataset parseJsonDSResponse(JSONObject jsonObject) {
        return new Dataset.Builder().dsname((String) jsonObject.get("dsname"))
                .blksz((String) jsonObject.get("blksz"))
                .catnm((String) jsonObject.get("catnm"))
                .cdate((String) jsonObject.get("cdate"))
                .dev((String) jsonObject.get("dev"))
                .dsntp((String) jsonObject.get("dsntp"))
                .dsorg((String) jsonObject.get("dsorg"))
                .edate((String) jsonObject.get("edate"))
                .extx((String) jsonObject.get("extx"))
                .lrectl((String) jsonObject.get("lrectl"))
                .migr((String) jsonObject.get("migr"))
                .mvol((String) jsonObject.get("mvol"))
                .ovf((String) jsonObject.get("ovf"))
                .rdate((String) jsonObject.get("rdate"))
                .recfm((String) jsonObject.get("recfm"))
                .sizex((String) jsonObject.get("sizex"))
                .spacu((String) jsonObject.get("spacu"))
                .used((String) jsonObject.get("used"))
                .vol((String) jsonObject.get("vol"))
                .build();
    }

    /**
     * Transform JSON into Member object
     *
     * @param jsonObject JSON object
     * @return Member object
     * @author Frank Giordano
     */
    public static Member parseJsonMemberResponse(JSONObject jsonObject) {
        return new Member.Builder().member((String) jsonObject.get("member"))
                .vers((Long) jsonObject.get("vers"))
                .mod((Long) jsonObject.get("mod"))
                .c4date((String) jsonObject.get("c4date"))
                .m4date((String) jsonObject.get("m4date"))
                .cnorc((Long) jsonObject.get("cnorc"))
                .inorc((Long) jsonObject.get("inorc"))
                .mnorc((Long) jsonObject.get("mnorc"))
                .mtime((String) jsonObject.get("mtime"))
                .msec((String) jsonObject.get("msec"))
                .user((String) jsonObject.get("user"))
                .sclm((String) jsonObject.get("sclm"))
                .build();
    }

}
