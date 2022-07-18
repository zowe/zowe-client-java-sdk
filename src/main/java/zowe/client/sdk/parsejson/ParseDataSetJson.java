/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parsejson;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosfiles.response.Dataset;

/**
 * Class to transform JSON into Dataset object
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ParseDataSetJson implements IParseJson<Dataset> {

    /**
     * Transform JSON into Dataset object
     *
     * @param jsonObject JSON object
     * @return Dataset object
     * @author Frank Giordano
     */
    @Override
    public Dataset parse(JSONObject jsonObject) {
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

}
