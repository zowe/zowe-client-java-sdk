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
 * @author Nikunj Goyal
 * @version 1.0
 */
public class UtilDataset {

    /**
     * Formulate and return a Dataset object based on incoming Json object.
     *
     * @param json JSONObject object
     * @return dataset document
     * @author Nikunj Goyal
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
     * Formulate and return a more redefined error exception message based on a CRUD operation.
     *
     * @param errorMsg    error message
     * @param dataSetName dataset representation
     * @param crudType    crud type value of operation taken place
     * @throws Exception with a possible customized error msg
     * @author Frank Giordano
     */
    public static void checkHttpErrors(String errorMsg, String dataSetName, String crudType) throws Exception {
        if (errorMsg.contains("404")) {
            throw new Exception(errorMsg + " You may have specified an invalid or non-existent data set or member.");
        }

        var type = crudType.toLowerCase();
        var permissionDataSetMsg = " You may not have permission to " + type + " '" + dataSetName + "'";
        var permissionDataSetMemberMsg = permissionDataSetMsg +
                ", the request is invalid, or the dataset or member does not exist.";

        if ("create".equals(type)) {
            if (errorMsg.contains("500")) {
                String exceptionMsg = permissionDataSetMsg + " or the dataset already exists.";
                throw new Exception(errorMsg + exceptionMsg);
            }
        }
        if ("read".equals(type)) {
            if (errorMsg.contains("500")) {
                throw new Exception(errorMsg + permissionDataSetMsg + " or the request is invalid.");
            }
        }
        if ("delete".equals(type) || "write".equals(type) || "copy".equals(type) || "download".equals(type)) {
            if (errorMsg.contains("500")) {
                throw new Exception(errorMsg + permissionDataSetMemberMsg + ".");
            }
        }
        throw new Exception(errorMsg);
    }

}
