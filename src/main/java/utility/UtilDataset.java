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

import org.json.JSONObject;
import zosfiles.response.Dataset;

import java.util.function.Supplier;
import java.util.stream.Stream;

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
        Supplier<Stream<String>> keys = Util.getStreamSupplier(json);
        return new Dataset.Builder()
                .dsname(keys.get().filter("dsname"::equals).findFirst().isPresent() ? (String) json.get("dsname") : null)
                .blksz(keys.get().filter("blksz"::equals).findFirst().isPresent() ? (String) json.get("blksz") : null)
                .catnm(keys.get().filter("catnm"::equals).findFirst().isPresent() ? (String) json.get("catnm") : null)
                .cdate(keys.get().filter("cdate"::equals).findFirst().isPresent() ? (String) json.get("cdate") : null)
                .dev(keys.get().filter("dev"::equals).findFirst().isPresent() ? (String) json.get("dev") : null)
                .dsntp(keys.get().filter("dsntp"::equals).findFirst().isPresent() ? (String) json.get("dsntp") : null)
                .dsorg(keys.get().filter("dsorg"::equals).findFirst().isPresent() ? (String) json.get("dsorg") : null)
                .edate(keys.get().filter("edate"::equals).findFirst().isPresent() ? (String) json.get("edate") : null)
                .extx(keys.get().filter("extx"::equals).findFirst().isPresent() ? (String) json.get("extx") : null)
                .lrectl(keys.get().filter("lrectl"::equals).findFirst().isPresent() ? (String) json.get("lrectl") : null)
                .migr(keys.get().filter("migr"::equals).findFirst().isPresent() ? (String) json.get("migr") : null)
                .mvol(keys.get().filter("mvol"::equals).findFirst().isPresent() ? (String) json.get("mvol") : null)
                .ovf(keys.get().filter("ovf"::equals).findFirst().isPresent() ? (String) json.get("ovf") : null)
                .rdate(keys.get().filter("rdate"::equals).findFirst().isPresent() ? (String) json.get("rdate") : null)
                .recfm(keys.get().filter("recfm"::equals).findFirst().isPresent() ? (String) json.get("recfm") : null)
                .sizex(keys.get().filter("sizex"::equals).findFirst().isPresent() ? (String) json.get("sizex") : null)
                .spacu(keys.get().filter("spacu"::equals).findFirst().isPresent() ? (String) json.get("spacu") : null)
                .used(keys.get().filter("used"::equals).findFirst().isPresent() ? (String) json.get("used") : null)
                .vol(keys.get().filter("vol"::equals).findFirst().isPresent() ? (String) json.get("vol") : null)
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
