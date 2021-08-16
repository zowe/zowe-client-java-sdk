/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs;

/**
 * Constants for various job related info
 *
 * @version 1.0
 */
public class JobsConstants {

    /**
     * Step data query string
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String STEP_DATA = "step-data=Y";

    /**
     * Query id for getting job by an owner
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String QUERY_OWNER = "owner=";

    /**
     * Query id for getting a job by prefix
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String QUERY_PREFIX = "prefix=";

    /**
     * Query id for getting a specific job id
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String QUERY_JOBID = "jobid=";

    /**
     * Query id for getting max jobs
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String QUERY_MAX_JOBS = "max-jobs=";

    /**
     * Wildcard prefix
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String DEFAULT_PREFIX = "*";

    /**
     * Maximum number of jobs to obtain
     *
     * @static
     * @type {number}
     * @memberof JobsConstants
     */
    public static final int DEFAULT_MAX_JOBS = 1000;

    /**
     * URI base jobs API
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String RESOURCE = "/zosmf/restjobs/jobs";

    /**
     * URI endpoint for getting spool file content
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String RESOURCE_SPOOL_FILES = "/files";

    /**
     * URI endpoint for getting JCL
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String RESOURCE_JCL_CONTENT = "/JCL";

    /**
     * URI endpoint for getting spool files
     *
     * @static
     * @type {string}
     * @memberof JobsConstants
     */
    public static final String RESOURCE_SPOOL_CONTENT = "/records";

    /**
     * Cancel request constant
     *
     * @static
     * @memberof JobsConstants
     */
    public static final String REQUEST_CANCEL = "cancel";

    /**
     * Default version of cancel
     *
     * @static
     * @memberof JobsConstants
     */
    public static final String DEFAULT_CANCEL_VERSION = "1.0";

}
