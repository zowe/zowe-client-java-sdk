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
 * Constants for various job-related info
 *
 * @author Frank Giordano
 * @version 6.0
 */
public final class JobsConstants {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private JobsConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Use request asynchronous processing represented by the default version 1.0
     */
    public static final String DEFAULT_VERSION = "1.0";

    /**
     * Maximum number of jobs to obtain
     */
    public static final int DEFAULT_MAX_JOBS = 1000;

    /**
     * Wildcard prefix
     */
    public static final String DEFAULT_PREFIX = "*";

    /**
     * File delimiter
     */
    public static final String FILE_DELIM = "/";

    /**
     * Use for an illegal exception error message
     */
    public static final String JOB_NAME_ILLEGAL_MSG = "jobName is either null or empty";

    /**
     * Use for an illegal exception error message
     */
    public static final String JOB_ID_ILLEGAL_MSG = "jobId is either null or empty";

    /**
     * Use for a null exception error message
     */
    public static final String JOB_NAME_NULL_MSG = "jobName is either null or empty";

    /**
     * Use for a null exception error message
     */
    public static final String JOB_ID_NULL_MSG = "jobId is either null or empty";

    /**
     * Query identifier
     */
    public static final String QUERY_ID = "?";

    /**
     * Query id for getting a specific job id
     */
    public static final String QUERY_JOBID = "jobid=";

    /**
     * Query id for getting max jobs
     */
    public static final String QUERY_MAX_JOBS = "max-jobs=";

    /**
     * Query id for getting a job by an owner
     */
    public static final String QUERY_OWNER = "owner=";

    /**
     * Query id for getting a job by prefix
     */
    public static final String QUERY_PREFIX = "prefix=";

    /**
     * Cancel request constant
     */
    public static final String REQUEST_CANCEL = "cancel";

    /**
     * URI base jobs API
     */
    public static final String RESOURCE = "/restjobs/jobs";

    /**
     * URI endpoint for getting JCL
     */
    public static final String RESOURCE_JCL_CONTENT = "/JCL";

    /**
     * URI endpoint for getting spool files
     */
    public static final String RESOURCE_SPOOL_CONTENT = "/records";

    /**
     * URI endpoint for getting spool file content
     */
    public static final String RESOURCE_SPOOL_FILES = "/files";

    /**
     * Step data query string
     */
    public static final String STEP_DATA = "step-data=Y";

}
