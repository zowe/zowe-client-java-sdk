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

import java.util.List;

/**
 * Utility Class for Dataset related static helper methods.
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class UtilDataset {

    /**
     * Attribute enum for querying a dataset and how its returned data will be retrieved with what properties.
     * <p>
     * BASE return all properties of a dataset and its values.
     * VOL return volume and dataset name properties and its values only.
     *
     * @author Frank Giordano
     */
    public enum Attribute {
        BASE, VOL
    }

    /**
     * Formulate and return a Dataset document/object based on incoming Json object.
     *
     * @param json JSONObject object
     * @return dataset document/Object
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
     * @param errMsg  error message
     * @param dsNames dataset representations
     * @param type    crud type value of operation taken place
     * @throws Exception with a possible customized error msg
     * @author Frank Giordano
     */
    public static void checkHttpErrors(String errMsg, List<String> dsNames, Crud.type type) throws Exception {
        Util.checkNullParameter(errMsg == null, "errMsg is null");
        Util.checkIllegalParameter(errMsg.isEmpty(), "errMsg not specified");
        Util.checkNullParameter(dsNames == null, "dsNames is null");
        Util.checkIllegalParameter(dsNames.isEmpty(), "dsNames not specified");
        Util.checkNullParameter(type == null, "crudType is null");

        String http404 = "is invalid or non-existent.";
        String http500Pre = "You may not have permission to";
        String http500 = ", the request is invalid,";
        String http500Create = "or the dataset(s) or member already exists.";
        String http500Post = "or the dataset(s) or member does not exist.";

        // remove "." period at the end of the string
        errMsg = errMsg.substring(0, errMsg.length() - 1);
        // append further info about the type of request that occurred
        errMsg += " for " + type.toString().toUpperCase() + " request.";

        StringBuilder datasets = new StringBuilder();

        // if 404 is seen handle it generically for all types
        if (errMsg.contains("404")) {
            if (dsNames.size() == 1) {
                throw new Exception(String.format("%s '%s' %s", errMsg, dsNames.get(0), http404));
            } else {
                datasets.append(errMsg);
                appendAllDS(dsNames, datasets);
                datasets.append(" ");
                datasets.append(http404);
                throw new Exception(datasets.toString());
            }
        }

        // if we see create request handle it
        if ("create".equals(type.toString())) {
            if (errMsg.contains("500")) {
                String newErrMsg = String.format("%s %s '%s' %s %s", errMsg, http500Pre, dsNames.get(0), http500Create,
                        http500Post);
                throw new Exception(newErrMsg);
            }
            throw new Exception(errMsg);
        }

        // if we see a copy request handle it
        if ("copy".equals(type.toString())) {
            if (errMsg.contains("500")) {
                if (dsNames.size() == 1) {
                    String newErrMsg = formatPrePostMsg(errMsg, dsNames, http500Pre, http500, http500Post);
                    throw new Exception(newErrMsg);
                } else {
                    datasets.append(errMsg);
                    datasets.append(" ");
                    datasets.append(http500Pre);
                    appendAllDS(dsNames, datasets);
                    datasets.append(http500);
                    datasets.append(" ");
                    datasets.append(http500Post);
                    datasets.append(" Check CopyParams copyAllMembers setting.");
                    datasets.append(" You may have specified a partition data set but expected a sequential data set.");
                    throw new Exception(datasets.toString());
                }
            }
            throw new Exception(errMsg);
        }

        // at this point lets handle all other types: read, delete, write, download
        if (errMsg.contains("500")) {
            String newErrMsg = formatPrePostMsg(errMsg, dsNames, http500Pre, http500, http500Post);
            throw new Exception(newErrMsg);
        }
        throw new Exception(errMsg);
    }

    /**
     * Formulate and return a more specialized error message string
     *
     * @param errMsg      main error message
     * @param dsNames     dataset representations
     * @param http500Pre  http500Pre message
     * @param http500     http500 message
     * @param http500Post http500Post message
     * @author Frank Giordano
     */
    private static String formatPrePostMsg(String errMsg, List<String> dsNames, String http500Pre, String http500,
                                           String http500Post) {
        return String.format("%s %s '%s' %s %s", errMsg, http500Pre, dsNames.get(0), http500, http500Post);
    }

    /**
     * Generate string with all datasets
     *
     * @param dsNames  dataset representations
     * @param datasets string holder
     * @author Frank Giordano
     */
    private static void appendAllDS(List<String> dsNames, StringBuilder datasets) {
        dsNames.forEach(ds -> {
            datasets.append(" '");
            datasets.append(ds);
            datasets.append("'");
        });
    }

}
