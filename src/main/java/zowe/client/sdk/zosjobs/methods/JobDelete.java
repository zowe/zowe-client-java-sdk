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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.JobModifyInputData;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.HashMap;
import java.util.Map;

/**
 * DeleteJobs class to handle Job delete
 *
 * @author Nikunj Goyal
 * @version 4.0
 */
public class JobDelete {

    private static final Logger LOG = LoggerFactory.getLogger(JobDelete.class);

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * DeleteJobs constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Nikunj Goyal
     */
    public JobDelete(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative DeleteJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    JobDelete(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Cancel and purge a job from spool.
     *
     * @param jobName name of a job to delete
     * @param jobId   job id
     * @param version version number, see JobModifyInputData object for version options
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Nikunj goyal
     */
    public Response delete(final String jobName, final String jobId, final String version) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(jobName, "jobName");
        ValidateUtils.checkIllegalParameter(jobId, "jobId");
        return deleteCommon(new JobModifyInputData.Builder(jobName, jobId).version(version).build());
    }

    /**
     * Cancel and purge a job from spool.
     *
     * @param job     job document wanting to delete
     * @param version version number, see JobModifyInputData object for version options
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response deleteByJob(final Job job, final String version) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        final String jobName = job.getJobName().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ILLEGAL_MSG));
        final String jobId = job.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ILLEGAL_MSG));
        return this.deleteCommon(new JobModifyInputData.Builder(jobName, jobId).version(version).build());
    }

    /**
     * Delete a job on z/OS by JobModifyInputData object.
     *
     * @param modifyInputData delete job parameters, see JobModifyInputData object
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Nikunj Goyal
     * @author Frank Giordano
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent") // due to ValidateUtils done in JobModifyInputData
    public Response deleteCommon(final JobModifyInputData modifyInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(modifyInputData == null, "jobModifyInputData is null");

        final String url = connection.getZosmfUrl() + JobsConstants.RESOURCE + JobsConstants.FILE_DELIM +
                modifyInputData.getJobName().get() + JobsConstants.FILE_DELIM + modifyInputData.getJobId().get();

        final Map<String, String> headers = new HashMap<>();

        // set version to default value if none given
        final String version = modifyInputData.getVersion().orElse(JobsConstants.DEFAULT_DELETE_VERSION);

        // To request asynchronous processing for this service (the default), set the "version" property to 1.0
        // or omit the property from the request. To request synchronous processing, set "version" to 2.0. If 2.0,
        // the system will attempt to process the request synchronously if such processing is supported on
        // the target JES2 subsystem.
        if ("1.0".equals(version)) {
            LOG.debug("version 1.0 specified which will result in asynchronous processing for the request");
            headers.put(ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_1").get(0),
                    ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_1").get(1));
        } else if ("2.0".equals(version)) {
            LOG.debug("version 2.0 specified which will result in synchronous processing for the request");
            headers.put(ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_2").get(0),
                    ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_2").get(1));
        } else {
            throw new IllegalArgumentException("invalid version specified");
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }
        request.setHeaders(headers);
        request.setUrl(url);

        // if synchronously response should contain a job document that was canceled and http return code
        // if asynchronously response should only contain http return code, let the caller handle the response JSON parsing
        return request.executeRequest();
    }

}
