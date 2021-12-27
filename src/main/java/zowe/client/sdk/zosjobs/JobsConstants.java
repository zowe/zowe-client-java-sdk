/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs;

/**
 * Constants for various job related info
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class JobsConstants {

    /**
     * Step data query string
     */
    public static final String STEP_DATA = "step-data=Y";

    /**
     * Query id for getting job by an owner
     */
    public static final String QUERY_OWNER = "owner=";

    /**
     * Query id for getting a job by prefix
     */
    public static final String QUERY_PREFIX = "prefix=";

    /**
     * Query id for getting a specific job id
     */
    public static final String QUERY_JOBID = "jobid=";

    /**
     * Query id for getting max jobs
     */
    public static final String QUERY_MAX_JOBS = "max-jobs=";

    /**
     * Wildcard prefix
     */
    public static final String DEFAULT_PREFIX = "*";

    /**
     * Maximum number of jobs to obtain
     */
    public static final int DEFAULT_MAX_JOBS = 1000;

    /**
     * URI base jobs API
     */
    public static final String RESOURCE = "/zosmf/restjobs/jobs";

    /**
     * URI endpoint for getting spool file content
     */
    public static final String RESOURCE_SPOOL_FILES = "/files";

    /**
     * URI endpoint for getting JCL
     */
    public static final String RESOURCE_JCL_CONTENT = "/JCL";

    /**
     * URI endpoint for getting spool files
     */
    public static final String RESOURCE_SPOOL_CONTENT = "/records";

    /**
     * Cancel request constant
     */
    public static final String REQUEST_CANCEL = "cancel";

    /**
     * Default version of cancel
     */
    public static final String DEFAULT_CANCEL_VERSION = "1.0";

    /**
     * Default version of delete
     */
    public static final String DEFAULT_DELETE_VERSION = "1.0";

}
