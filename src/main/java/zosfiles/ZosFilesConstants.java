/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles;

public class ZosFilesConstants {

    /**
     * Specifies the z/OS data set and file REST interface
     *
     * @type {string}
     */
    public static final String RESOURCE = "/zosmf/restfiles";

    /**
     * Indicator of a data set request
     *
     * @type {string}
     */
    public static final String RES_DS_FILES = "/ds";

    /**
     * Indicator of a USS File request
     *
     * @type {string}
     */
    public static final String RES_USS_FILES = "/fs";

    /**
     * Indicator of a z/OS file system request
     *
     * @type {string}
     */
    public static final String RES_ZFS_FILES = "/mfs/zfs";

    /**
     * Indicator of a z/OS mfs
     *
     * @type {string}
     */
    public static final String RES_MFS = "/mfs";

    /**
     * Indicator of a members request
     *
     * @type {string}
     * @memberOf ZosFilesConstants
     */
    public static final String RES_DS_MEMBERS = "/member";

    /**
     * Indicator of an AMS request
     *
     * @type {string}
     */
    public static final String RES_AMS = "/ams";

    /**
     * Indicator of a USS File request
     *
     * @type {string}
     * @memberOf ZosFilesConstants
     */
    public static final String RES_PATH = "path";

    /**
     * Indicator of a ds file name
     *
     * @type {string}
     * @memberOf ZosFilesConstants
     */
    public static final String RES_FSNAME = "fsname";

    /**
     * Json response array field name
     *
     * @static
     * @type {string}
     * @memberof ZosFilesConstants
     */
    public static final String RESPONSE_ITEMS = "items";

    /**
     * Maximum value for primary and secondary allocation
     *
     * @type {number}
     */
    public static final int MAX_ALLOC_QUANTITY = 16777215;

    /**
     * Maximum length of an AMS statement
     *
     * @type {number}
     */
    public static final int MAX_AMS_LINE = 255;
    /**
     * Maximum numbers of characters to allow for the continuation character on AMS statements
     *
     * @type {number}
     */
    public static final int MAX_AMS_BUFFER = 2;

    /**
     * Minimum numbers of days for which to retain a dataset.
     *
     * @type {number}
     */
    public static final int MIN_RETAIN_DAYS = 0;

    /**
     * Maximum numbers of days for which to retain a dataset.
     *
     * @type {number}
     */
    public static final int MAX_RETAIN_DAYS = 93000;

    /**
     * The set of dataset organization choices for VSAM files
     *
     * @type {[string]}
     */
    public static final String[] VSAM_DSORG_CHOICES = new String[]{"INDEXED", "IXD", "LINEAR", "LIN", "NONINDEXED", "NIXD", "NUMBERED", "NUMD", "ZFS"};

    /**
     * The set of allocation unit choices for VSAM files
     *
     * @type {[string]}
     */
    public static final String[] VSAM_ALCUNIT_CHOICES = new String[]{"CYL", "TRK", "MB", "KB", "REC"};

    /**
     * A parameter that identifies the volume serials to be searched for data sets
     *
     * @static
     * @type {string}
     * @memberof ZosFilesConstants
     */
    public static final String QUERY_VOLUME = "volser=";

    /**
     * An optional parameter that specifies a pattern for the dataset members
     *
     * @static
     * @type {string}
     * @memberof ZosFilesConstants
     */
    public static final String QUERY_PATTERN = "pattern=";

    /**
     * An optional search parameter that specifies the first data set name to return in the response document
     *
     * @static
     * @type {string}
     * @memberof ZosFilesConstants
     */
    public static final String QUERY_START = "start=";

    /**
     * Indicator the query parameters used to qualify the request
     *
     * @static
     * @type {string}
     * @memberof ZosFilesConstants
     */
    public static final String QUERY_DS_LEVEL = "dslevel=";

}
