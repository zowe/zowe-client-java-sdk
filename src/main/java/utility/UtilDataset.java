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

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @return dataset document
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
     * @param errorMsg    error message
     * @param dataSetName dataset representation
     * @throws Exception execution with error msg
     */
    public static void checkHttpErrors(String errorMsg, String dataSetName) throws Exception {
        if (errorMsg.contains("404")) {
            throw new Exception(errorMsg + " You may have specified an invalid or non-existent data set.");
        }
        if (errorMsg.contains("500")) {
            throw new Exception(errorMsg + " You may not have permission to view " + dataSetName + ", or if creating" +
                    " this dataset, it may already exist!");
        }
        throw new Exception(errorMsg);
    }

    /**
     * Verifies that a dataset follows the naming conventions that IBM has defined:
     * Segment naming rules
     * - A data set name must be composed of at least two joined character segments (qualifiers), separated by a
     * period (.); for example, HLQ.ABC.XYZ.
     * - A data set name cannot be longer than 44 characters.
     * - A data set name cannot contain two successive periods; for example, HLQ..ABC.
     * - A data set name cannot end with a period.
     * - Each segment in a data set name represents a level of qualification. For example, the first segment
     * represents the high-level qualifier (HLQ), and the last segment represents the lowest-level qualifier (LLQ).
     * - A segment cannot be longer than eight characters.
     * - The first segment character must be either a letter or one of the following three special
     * characters: #, *, @, $.
     * - The remaining seven characters in a segment can be letters, numbers,
     * special characters (only #, @, or * $), or a hyphen (-).
     * - A data set name cannot contain accented characters (à, é, è, and so on).
     *
     * @param dataSetName     Dataset name to validate as per the above rules
     * @param additionalTests Set to true to check that a dataset has more than one segment (for example, when used by
     *                        listMembers()). Set too false to ignore this check (for example, when used by listDsn()).
     * @throws Exception if given dataset format is invalid
     * @author Corinne DeStefano
     */
    public static void checkDatasetName(String dataSetName, boolean additionalTests) {
        dataSetName = dataSetName.toUpperCase(Locale.ROOT);
        String invalidDatasetMsg = "Invalid data set name '" + dataSetName + "'.";

        // Check that the dataset contains more than one segment
        // This could be valid for additionalTests
        String[] segments = dataSetName.split("\\.");
        if (additionalTests) {
            if (segments.length < 2) {
                throw new IllegalArgumentException(invalidDatasetMsg);
            }
        }

        // The length cannot be longer than 44
        if (dataSetName.length() > 44) {
            throw new IllegalArgumentException(invalidDatasetMsg);
        }

        // The name cannot contain two successive periods
        if (dataSetName.contains("..")) {
            throw new IllegalArgumentException(invalidDatasetMsg);
        }

        // Cannot end in a period
        if (dataSetName.endsWith(".")) {
            throw new IllegalArgumentException(invalidDatasetMsg);
        }

        // Each segment cannot be more than 8 characters
        // Each segment's first letter is a letter or #, @, $.
        // The remaining seven characters in a segment can be letters, numbers, and #, @, $, -
        for (String segment : segments) {
            if (segment.length() > 8) {
                throw new IllegalArgumentException(invalidDatasetMsg);
            }
            Pattern p = Pattern.compile("[A-Z#@\\$]{1}[A-Z0-9#@\\$\\-]{1,7}");
            Matcher m = p.matcher(segment);
            if (!m.matches()) {
                throw new IllegalArgumentException(invalidDatasetMsg);
            }
        }
    }

}
