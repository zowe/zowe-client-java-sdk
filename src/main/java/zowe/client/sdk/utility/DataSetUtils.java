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
import zowe.client.sdk.zosfiles.response.Dataset;
import zowe.client.sdk.zosfiles.types.OperationType;

import java.util.List;

/**
 * Utility Class for Dataset related static helper methods.
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public final class DataSetUtils {

    private DataSetUtils() {
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

    /**
     * Formulate and return a more redefined error exception message based on a CRUD operation.
     *
     * @param errMsg  error message
     * @param dsNames dataset representations
     * @param type    crud type value of operation taken place
     * @throws Exception with a possible customized error msg
     * @author Frank Giordano
     */
    public static void checkHttpErrors(String errMsg, List<String> dsNames, OperationType type) throws Exception {
        ValidateUtils.checkNullParameter(errMsg == null, "errMsg is null");
        ValidateUtils.checkIllegalParameter(errMsg.isEmpty(), "errMsg not specified");
        ValidateUtils.checkNullParameter(dsNames == null, "dsNames is null");
        ValidateUtils.checkIllegalParameter(dsNames.isEmpty(), "dsNames not specified");
        ValidateUtils.checkNullParameter(type == null, "crudType is null");

        final String http404 = "is invalid or non-existent.";
        final String http500Pre = "You may not have permission to";
        final String http500 = ", the request is invalid,";
        final String http500Create = "or the dataset(s) or member already exists.";
        final String http500Post = "or the dataset(s) or member does not exist.";

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
        if ("create".equalsIgnoreCase(type.toString())) {
            if (errMsg.contains("500")) {
                final String newErrMsg = String.format("%s %s '%s' %s %s", errMsg, http500Pre, dsNames.get(0), http500Create,
                        http500Post);
                throw new Exception(newErrMsg);
            }
            throw new Exception(errMsg);
        }

        // if we see a copy request handle it
        if ("copy".equalsIgnoreCase(type.toString())) {
            if (errMsg.contains("500")) {
                if (dsNames.size() == 1) {
                    final String newErrMsg = formatPrePostMsg(errMsg, dsNames, http500Pre, http500, http500Post);
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
                    datasets.append(" Check for data set space capacity.");
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

}
