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

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.*;
import utility.Util;
import utility.UtilIO;
import utility.UtilRest;
import zosjobs.input.DeleteJobParams;
import zosjobs.response.Job;

import java.util.HashMap;

/**
 * DeleteJobs class to handle Job delete
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class DeleteJobs {

    private static final Logger LOG = LogManager.getLogger(DeleteJobs.class);

    private final ZOSConnection connection;

    /**
     * DeleteJobs constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public DeleteJobs(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Cancel and purge job from spool.
     *
     * @param jobName name of job to delete
     * @param jobId   job id
     * @param version version number
     * @return http response object
     * @throws Exception error deleting
     * @author Nikunj goyal
     */
    public Response deleteJob(String jobName, String jobId, String version) throws Exception {
        return deleteJobCommon(
                new DeleteJobParams.Builder(jobName, jobId).modifyVersion(version).build());
    }

    /**
     * Cancel and purge job from spool.
     *
     * @param job     job document wanting to delete
     * @param version version number
     * @return http response object
     * @throws Exception error deleting
     * @author Frank Giordano
     */
    public Response deleteJobForJob(Job job, String version) throws Exception {
        return this.deleteJobCommon(
                new DeleteJobParams.Builder(job.getJobName().isPresent() ? job.getJobName().get() : null,
                        job.getJobId().isPresent() ? job.getJobId().get() : null).modifyVersion(version).build());
    }

    /**
     * Delete a job that resides in a z/OS data set.
     *
     * @param params delete job parameters, see DeleteJobParams object
     * @return http response object
     * @throws Exception error on deleting
     * @author Nikunj goyal
     */
    public Response deleteJobCommon(DeleteJobParams params) throws Exception {
        Util.checkNullParameter(params == null, "params is null");
        Util.checkStateParameter(params.getJobId().isEmpty(), "job id not specified");
        Util.checkStateParameter(params.getJobId().get().isEmpty(), "job id not specified");
        Util.checkStateParameter(params.getJobName().isEmpty(), "job name not specified");
        Util.checkStateParameter(params.getJobName().get().isEmpty(), "job name not specified");

        if (params.getModifyVersion().isPresent()) {
            if ("1.0".equals(params.getModifyVersion().get())) {
                throw new Exception("Modify version 1.0 for async processing is currently not supported");
            }
            if (!"2.0".equals(params.getModifyVersion().get())) {
                throw new Exception("Modify version specified is not supported");
            }
        }

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE +
                UtilIO.FILE_DELIM + params.getJobName().get() + UtilIO.FILE_DELIM + params.getJobId().get();
        LOG.debug(url);

        var headers = new HashMap<String, String>();

        String version = params.getModifyVersion().orElse(JobsConstants.DEFAULT_DELETE_VERSION);

        // To request asynchronous processing for this service (the default), set the "version" property to 1.0
        // or omit the property from the request. To request synchronous processing, set "version" to 2.0. If so,
        // the system will attempt to process the request synchronously, if such processing is supported on
        // the target JES2 subsystem.
        if (!version.isEmpty()) {
            if ("1.0".equals(version)) {
                LOG.debug("version 1.0 specified which will result in asynchronous processing for the request");
                headers.put(ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_1").get(0),
                        ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_1").get(1));
            } else if ("2.0".equals(version)) {
                LOG.debug("version 2.0 specified which will result in synchronous processing for the request");
                headers.put(ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_2").get(0),
                        ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_2").get(1));
            } else {
                throw new Exception("invalid version specified");
            }
        }

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.DELETE_JSON);

        request.setAdditionalHeaders(headers);

        Response response = request.executeHttpRequest();
        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("400"))
                throw new Exception(errorMsg + " JobId " + params.getJobId().get() + " may not exist.");
            throw new Exception(errorMsg);
        }

        // if synchronously response should contain job document that was cancelled and http return code
        // if asynchronously response should only contain http return code
        // let the caller handle the response json parsing
        return response;
    }

}
