package zosfiles.constants;

public class ZosFilesConstants {
    /**
     * Specifies the z/OS data set and file REST interface
     * @type {string}
     */
    public static final String RESOURCE = "/zosmf/restfiles";

    /**
     * Indicator of a data set request
     * @type {string}
     */
    public static final String RES_DS_FILES = "/ds";

    /**
     * Indicator of a USS File request
     * @type {string}
     */
    public static final String RES_USS_FILES = "/fs";

    /**
     * Indicator of a z/OS file system request
     * @type {string}
     */
    public static final String RES_ZFS_FILES = "/mfs/zfs";

    /**
     * Indicator of a z/OS mfs
     * @type {string}
     */
    public static final String RES_MFS = "/mfs";

    /**
     * Indicator of a members request
     * @type {string}
     * @memberOf ZosFilesConstants
     */
    public static final String RES_DS_MEMBERS= "/member";

    /**
     * Indicator of an AMS request
     * @type {string}
     */
    public static final String RES_AMS= "/ams";

    /**
     * Indicator of a USS File request
     * @type {string}
     * @memberOf ZosFilesConstants
     */
    public static final String RES_PATH= "path";

    /**
     * Indicator of a ds file name
     * @type {string}
     * @memberOf ZosFilesConstants
     */
    public static final String RES_FSNAME = "fsname";

    /**
     * Indicator the query parameters used to qualify the request
     */
    public static final String RES_DS_LEVEL = "dslevel";

    /**
     * Maximum value for primary and secondary allocation
     * @type {number}
     */
    public static final int MAX_ALLOC_QUANTITY = 16777215;

    /**
     * Maximum length of an AMS statement
     * @type {number}
     */
    public static final int  MAX_AMS_LINE = 255;
    /**
     * Maximum numbers of characters to allow for the continuation character on AMS statements
     * @type {number}
     */
    public static final int MAX_AMS_BUFFER = 2;

    /**
     * Minimum numbers of days for which to retain a dataset.
     * @type {number}
     */
    public static final int MIN_RETAIN_DAYS = 0;

    /**
     * Maximum numbers of days for which to retain a dataset.
     * @type {number}
     */
    public static final int MAX_RETAIN_DAYS = 93000;

    /**
     * The set of dataset organization choices for VSAM files
     * @type {[string]}
     */
    public static final String[] VSAM_DSORG_CHOICES = new String[] {"INDEXED", "IXD", "LINEAR", "LIN", "NONINDEXED", "NIXD", "NUMBERED", "NUMD", "ZFS"};

    /**
     * The set of allocation unit choices for VSAM files
     * @type {[string]}
     */
    public static final String[] VSAM_ALCUNIT_CHOICES = new String[] {"CYL", "TRK", "MB", "KB", "REC"};
}
