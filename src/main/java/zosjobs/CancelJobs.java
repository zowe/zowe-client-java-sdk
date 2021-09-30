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
import org.json.simple.JSONObject;
import rest.Response;
import rest.ZoweRequest;
import rest.ZoweRequestFactory;
import rest.ZoweRequestType;
import utility.Util;
import utility.UtilIO;
import utility.UtilJobs;
import utility.UtilRest;
import zosjobs.input.ModifyJobParams;
import zosjobs.response.Job;

import java.util.HashMap;

/**
 * CancelJobs class to handle Job cancel
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class CancelJobs {

    private static final Logger LOG = LogManager.getLogger(CancelJobs.class);

    private final ZOSConnection connection;

    /**
     * CancelJobs constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public CancelJobs(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Cancel a job that resides in a z/OS data set.
     *
     * @param jobName name of job to cancel
     * @param jobId   job id
     * @param version version number
     * @return job document with details about the submitted job
     * @throws Exception error canceling
     * @author Nikunj goyal
     */
    public Response cancelJob(String jobName, String jobId, String version) throws Exception {
        return this.cancelJobsCommon(
                new ModifyJobParams.Builder(jobName, jobId).version(version).build());
    }

    /**
     * Cancel a job that resides in a z/OS data set.
     *
     * @param job     job document wanting to cancel
     * @param version version number
     * @return job document with details about the submitted job
     * @throws Exception error canceling
     * @author Frank Giordano
     */
    public Response cancelJobForJob(Job job, String version) throws Exception {
        return this.cancelJobsCommon(
                new ModifyJobParams.Builder(job.getJobName().orElse(null), job.getJobId().orElse(null))
                        .version(version).build());
    }

    /**
     * Cancel a job that resides in a z/OS data set.
     *
     * @param params cancel job parameters, see cancelJobsCommon object
     * @return job document with details about the submitted job
     * @throws Exception error canceling
     * @author Nikunj Goyal
     * @author Frank Giordano
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Response cancelJobsCommon(ModifyJobParams params) throws Exception {
        UtilJobs.checkForModifyJobParamsExceptions(params);

        // generate full url request
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE +
                UtilIO.FILE_DELIM + params.getJobName().get() + UtilIO.FILE_DELIM + params.getJobId().get();
        LOG.debug(url);

        // generate json string body for the request
        String version = params.getVersion().orElse(JobsConstants.DEFAULT_CANCEL_VERSION);

        // To request asynchronous processing for this service (the default), set the "version" property to 1.0
        // or omit the property from the request. To request synchronous processing, set "version" to 2.0. If so,
        // the system will attempt to process the request synchronously, if such processing is supported on
        // the target JES2 subsystem.
        if (!version.isEmpty()) {
            if ("1.0".equals(version)) {
                LOG.debug("version 1.0 specified which will result in asynchronous processing for the request");
            } else if ("2.0".equals(version)) {
                LOG.debug("version 2.0 specified which will result in synchronous processing for the request");
            } else {
                throw new Exception("invalid version specified");
            }
        }

        var jsonMap = new HashMap<String, String>();
        jsonMap.put("request", JobsConstants.REQUEST_CANCEL);
        jsonMap.put("version", version);
        var jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(jsonRequestBody);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, jsonRequestBody.toString(),
                ZoweRequestType.VerbType.PUT_JSON);

        Response response = request.executeHttpRequest();
        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilJobs.throwHttpException(params, e);
        }

        // if synchronously response should contain job document that was cancelled and http return code
        // if asynchronously response should only contain http return code
        // let the caller handle the response json parsing
        return response;
    }

}
