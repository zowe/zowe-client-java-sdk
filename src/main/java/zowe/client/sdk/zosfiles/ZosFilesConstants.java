/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles;

/**
 * Constants for various zos files related info
 *
 * @author Leonid Baranov
 * @version 2.0
 */
public class ZosFilesConstants {

    /**
     * Maximum value for primary and secondary allocation
     */
    public static final int MAX_ALLOC_QUANTITY = 16777215;

    /**
     * Maximum numbers of characters to allow for the continuation character on AMS statements
     */
    public static final int MAX_AMS_BUFFER = 2;

    /**
     * Maximum length of an AMS statement
     */
    public static final int MAX_AMS_LINE = 255;

    /**
     * Maximum numbers of days for which to retain a dataset.
     */
    public static final int MAX_RETAIN_DAYS = 93000;

    /**
     * Minimum numbers of days for which to retain a dataset.
     */
    public static final int MIN_RETAIN_DAYS = 0;

    /**
     * Indicator the query parameters used to qualify the request
     */
    public static final String QUERY_DS_LEVEL = "dslevel=";

    /**
     * An optional parameter that specifies a pattern for the dataset members
     */
    public static final String QUERY_PATTERN = "pattern=";

    /**
     * An optional search parameter that specifies the first data set name returning to the response document
     */
    public static final String QUERY_START = "start=";

    /**
     * A parameter that identifies the volume serials to be searched for data sets
     */
    public static final String QUERY_VOLUME = "volser=";

    /**
     * Specifies the z/OS data set and file REST interface
     */
    public static final String RESOURCE = "/zosmf/restfiles";

    /**
     * Json response array field name
     */
    public static final String RESPONSE_ITEMS = "items";

    /**
     * Indicator of an AMS request
     */
    public static final String RES_AMS = "/ams";

    /**
     * Indicator of a data set request
     */
    public static final String RES_DS_FILES = "/ds";

    /**
     * Indicator of a members request
     */
    public static final String RES_DS_MEMBERS = "/member";

    /**
     * Indicator of a ds file name
     */
    public static final String RES_FSNAME = "fsname";

    /**
     * Indicator of a z/OS mfs
     */
    public static final String RES_MFS = "/mfs";

    /**
     * Indicator of a USS File request
     */
    public static final String RES_PATH = "path";

    /**
     * Indicator of a USS File request
     */
    public static final String RES_USS_FILES = "/fs";

    /**
     * Indicator of a z/OS file system request
     */
    public static final String RES_ZFS_FILES = "/mfs/zfs";

    /**
     * The set of allocation unit choices for VSAM files
     */
    public static final String[] VSAM_ALCUNIT_CHOICES = new String[]{"CYL", "TRK", "MB", "KB", "REC"};
    
    /**
     * The set of dataset organization choices for VSAM files
     */
    public static final String[] VSAM_DSORG_CHOICES =
            new String[]{"INDEXED", "IXD", "LINEAR", "LIN", "NONINDEXED", "NIXD", "NUMBERED", "NUMD", "ZFS"};

}
