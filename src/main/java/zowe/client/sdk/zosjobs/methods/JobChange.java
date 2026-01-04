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
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.JobModifyInputData;
import zowe.client.sdk.zosjobs.model.Job;
import zowe.client.sdk.zosjobs.response.JobFeedback;

import java.util.HashMap;
import java.util.Map;

/**
 * JobChange class to handle changing a running job on z/OS.
 * <p>
 * The following actions on a job are provided: change class, hold and release.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class JobChange {

    private static final Logger LOG = LoggerFactory.getLogger(JobChange.class);
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * JobChange constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public JobChange(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative JobChange constructor with ZosmfRequest object.
     * This is mainly used for internal unit testing with mockito.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information
     * @param request    compatible ZosmfRequest object
     */
    JobChange(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Change the class of a job by job name and job id.
     *
     * @param jobName job name
     * @param jobId   job id
     * @param jobClass new job class (for example A, B, C)
     * @param version version number - 1.0 or 2.0
     * @return JobFeedback object
     * @throws ZosmfRequestException request error state
     */
    public JobFeedback changeClass(final String jobName, final String jobId,
                                   final String jobClass, final String version) throws ZosmfRequestException {

        return this.changeClassCommon(
                new JobModifyInputData.Builder(jobName, jobId)
                        .jobClass(jobClass)
                        .version(version)
                        .build()
        );
    }

    /**
     * Change the class of a job using a Job object.
     *
     * @param job      job document
     * @param jobClass new job class (for example A, B, C)
     * @param version  version number - 1.0 or 2.0
     * @return JobFeedback object
     * @throws ZosmfRequestException request error state
     */
    public JobFeedback changeClassByJob(final Job job, final String jobClass, final String version)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");

        return this.changeClassCommon(
                new JobModifyInputData.Builder(job.getJobName(), job.getJobId())
                        .jobClass(jobClass)
                        .version(version)
                        .build()
        );
    }

    /**
     * Common change job class implementation.
     *
     * @param modifyInputData job modify parameters
     * @return JobFeedback object
     * @throws ZosmfRequestException request error state
     */
    public JobFeedback changeClassCommon(final JobModifyInputData modifyInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(modifyInputData == null, "modifyInputData is null");
        ValidateUtils.checkIllegalParameter(modifyInputData.getJobClass().isEmpty(), JobsConstants.JOB_CLASS_ILLEGAL_MSG);

        final String url = getUrl(connection.getZosmfUrl(), modifyInputData);
        final String version = getVersion(modifyInputData);

        final Map<String, String> changeMap = new HashMap<>();
        changeMap.put("class", modifyInputData.getJobClass().get());
        changeMap.put("version", version);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(changeMap).toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no job change response phrase"))
                .toString();

        final String context = "changeClassCommon";
        return JsonUtils.parseResponse(String.valueOf(responsePhrase), JobFeedback.class, context);
    }

    /**
     * Hold a job by job name and job id.
     *
     * @param jobName job name
     * @param jobId   job id
     * @param version version number - 1.0 or 2.0
     * @return JobFeedback object
     * @throws ZosmfRequestException request error state
     */
    public JobFeedback hold(final String jobName, final String jobId, final String version)
            throws ZosmfRequestException {

        return this.holdCommon(
                new JobModifyInputData.Builder(jobName, jobId)
                        .version(version)
                        .build()
        );
    }

    /**
     * Hold a job using a Job object.
     *
     * @param job     job document
     * @param version version number - 1.0 or 2.0
     * @return JobFeedback object
     * @throws ZosmfRequestException request error state
     */
    public JobFeedback holdByJob(final Job job, final String version)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");

        return this.holdCommon(
                new JobModifyInputData.Builder(job.getJobName(), job.getJobId())
                        .version(version)
                        .build()
        );
    }

    /**
     * Common hold job implementation.
     *
     * @param modifyInputData job modify parameters
     * @return JobFeedback object
     * @throws ZosmfRequestException request error state
     */
    public JobFeedback holdCommon(final JobModifyInputData modifyInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(modifyInputData == null, "modifyInputData is null");

        final String url = getUrl(connection.getZosmfUrl(), modifyInputData);
        final String version = getVersion(modifyInputData);

        final Map<String, String> holdMap = new HashMap<>();
        holdMap.put("request", "hold");
        holdMap.put("version", version);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(holdMap).toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no job hold response phrase"))
                .toString();

        final String context = "holdCommon";
        return JsonUtils.parseResponse(responsePhrase, JobFeedback.class, context);
    }

    private static String getUrl(final String zosmfUrl, final JobModifyInputData modifyInputData) {
        ValidateUtils.checkIllegalParameter(modifyInputData.getJobName().isEmpty(), JobsConstants.JOB_NAME_ILLEGAL_MSG);
        ValidateUtils.checkIllegalParameter(modifyInputData.getJobId().isEmpty(), JobsConstants.JOB_ID_ILLEGAL_MSG);
        return zosmfUrl +
                JobsConstants.RESOURCE +
                JobsConstants.FILE_DELIM +
                modifyInputData.getJobName().get() +
                JobsConstants.FILE_DELIM +
                modifyInputData.getJobId().get();
    }

    private static String getVersion(final JobModifyInputData modifyInputData) {
        final String version = modifyInputData.getVersion().orElse(JobsConstants.DEFAULT_VERSION);

        if ("1.0".equals(version)) {
            LOG.debug("version 1.0 specified – asynchronous processing");
        } else if ("2.0".equals(version)) {
            LOG.debug("version 2.0 specified – synchronous processing");
        } else {
            throw new IllegalArgumentException("invalid version specified");
        }
        return version;
    }

}
