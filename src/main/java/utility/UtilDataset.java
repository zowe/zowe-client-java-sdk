/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

import org.json.simple.JSONObject;
import zosfiles.response.Dataset;

/**
 * Utility Class for Dataset related static helper methods.
 *
 * @version 1.0
 */
public class UtilDataset {

    /**
     * Formulate and return a Dataset object based on incoming Json object.
     *
     * @param json JSONObject object
     * @return A Dataset document
     */
    public static Dataset createDatasetObjFromJson(JSONObject json) {
        return new Dataset.Builder().dsname((String) json.get("dsname"))
                .blksz((String) json.get("blksz"))
                .catnm((String) json.get("catnm"))
                .cdate((String) json.get("cdate"))
                .dev((String) json.get("dev"))
                .dsntp((String) json.get("dsntp"))
                .dsorg((String) json.get("dsorg"))
                .edate((String) json.get("edate"))
                .extx((String) json.get("extx"))
                .lrectl((String) json.get("lrectl"))
                .migr((String) json.get("migr"))
                .mvol((String) json.get("mvol"))
                .ovf((String) json.get("ovf"))
                .rdate((String) json.get("rdate"))
                .recfm((String) json.get("recfm"))
                .sizex((String) json.get("sizex"))
                .spacu((String) json.get("spacu"))
                .used((String) json.get("used"))
                .vol((String) json.get("vol"))
                .build();
    }

    /**
     * Formulate and return a Dataset object based on incoming Json object.
     *
     * @param errorMsg    A String representing an error message
     * @param dataSetName A string representing a Dataset
     */
    public static void checkHttpErrors(String errorMsg, String dataSetName) throws Exception {
        if (errorMsg.contains("404")) {
            throw new Exception(errorMsg + " You may have specified an invalid or non-existent data set.");
        }
        if (errorMsg.contains("500")) {
            throw new Exception(errorMsg + " You may not have permission to view " + dataSetName + ".");
        }
        throw new Exception(errorMsg);
    }

}
