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
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.type.ZosmfRequestType;
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

    private ZosmfRequest request;

    /**
     * CancelJobs constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public JobCancel(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative CancelJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public JobCancel(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
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
    public Response cancel(final String jobName, final String jobId, final String version) throws Exception {
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
    public Response cancelByJob(final Job job, final String version) throws Exception {
        return this.cancelCommon(
                new ModifyJobParams.Builder(job.getJobName().orElse(""), job.getJobId().orElse(""))
                        .version(version)
                        .build());
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
    public Response cancelCommon(final ModifyJobParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        // generate full url request
        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                JobsConstants.RESOURCE + JobsConstants.FILE_DELIM + params.getJobName()
                .orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ERROR_MSG)) +
                JobsConstants.FILE_DELIM + params.getJobId()
                .orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ERROR_MSG));

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

        final Map<String, String> cancelMap = new HashMap<>();
        cancelMap.put("request", JobsConstants.REQUEST_CANCEL);
        cancelMap.put("version", version);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(cancelMap).toString());

        // if synchronously response should contain job document that was cancelled and http return code
        // if asynchronously response should only contain http return code
        // let the caller handle the response json parsing
        return request.executeRequest();
    }

}
