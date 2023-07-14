/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.methods;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.JobUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.ModifyJobParams;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.HashMap;
import java.util.Map;

/**
 * CancelJobs class to handle Job cancel
 *
 * @author Nikunj Goyal
 * @author Frank Giordano
 * @version 2.0
 */
public class JobCancel {

    private static final Logger LOG = LoggerFactory.getLogger(JobCancel.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * CancelJobs constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public JobCancel(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative CancelJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public JobCancel(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Cancel a job that resides in a z/OS data set.
     *
     * @param jobName name of job to cancel
     * @param jobId   job id
     * @param version version number - 1.0 or 2.0
     *                To request asynchronous processing for this service (the default), set the "version" property to 1.0
     *                or omit the property from the request. To request synchronous processing, set "version" to 2.0. If so,
     *                the system will attempt to process the request synchronously, if such processing is supported on
     *                the target JES2 subsystem.
     * @return job document with details about the submitted job
     * @throws Exception error canceling
     * @author Nikunj goyal
     */
    public Response cancel(String jobName, String jobId, String version) throws Exception {
        return this.cancelCommon(new ModifyJobParams.Builder(jobName, jobId).version(version).build());
    }

    /**
     * Cancel a job that resides in a z/OS data set.
     *
     * @param job     job document wanting to cancel
     * @param version version number - 1.0 or 2.0
     *                To request asynchronous processing for this service (the default), set the "version" property to 1.0
     *                or omit the property from the request. To request synchronous processing, set "version" to 2.0. If so,
     *                the system will attempt to process the request synchronously, if such processing is supported on
     *                the target JES2 subsystem.
     * @return job document with details about the submitted job
     * @throws Exception error canceling
     * @author Frank Giordano
     */
    public Response cancelByJob(Job job, String version) throws Exception {
        return this.cancelCommon(
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
    public Response cancelCommon(ModifyJobParams params) throws Exception {
        JobUtils.checkModifyJobParameters(params);

        // generate full url request
        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE +
                JobsConstants.FILE_DELIM + params.getJobName().get() + JobsConstants.FILE_DELIM + params.getJobId().get();
        LOG.debug(url);

        // generate json string body for the request
        final String version = params.getVersion().orElse(JobsConstants.DEFAULT_CANCEL_VERSION);

        // To request asynchronous processing for this service (the default), set the "version" property to 1.0
        // or omit the property from the request. To request synchronous processing, set "version" to 2.0. If so,
        // the system will attempt to process the request synchronously, if such processing is supported on
        // the target JES2 subsystem.
        if ("1.0".equals(version)) {
            LOG.debug("version 1.0 specified which will result in asynchronous processing for the request");
        } else if ("2.0".equals(version)) {
            LOG.debug("version 2.0 specified which will result in synchronous processing for the request");
        } else {
            throw new IllegalArgumentException("invalid version specified");
        }

        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("request", JobsConstants.REQUEST_CANCEL);
        jsonMap.put("version", version);
        final JSONObject jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(String.valueOf(jsonRequestBody));

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(jsonRequestBody.toString());

        // if synchronously response should contain job document that was cancelled and http return code
        // if asynchronously response should only contain http return code
        // let the caller handle the response json parsing
        return RestUtils.getResponse(request);
    }

}
