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
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.SubmitJclParams;
import zowe.client.sdk.zosjobs.input.SubmitJobParams;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle submitting of z/OS batch jobs and started tasks via z/OSMF
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobSubmit {

    private static final Logger LOG = LoggerFactory.getLogger(JobSubmit.class);

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * SubmitJobs Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public JobSubmit(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative SubmitJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    JobSubmit(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        this.request = request;
        if (!(request instanceof PutJsonZosmfRequest) && !(request instanceof PutTextZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON or PUT_TEXT request type required");
        }
    }

    /**
     * Submit a string of JCL to run
     *
     * @param jcl                 JCL content that you want to be submitted
     * @param internalReaderRecfm record format of the jcl you want to submit. "F" (fixed) or "V" (variable)
     * @param internalReaderLrecl logical record length of the jcl you want to submit
     * @return job document with details about the submitted job
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Job submitByJcl(final String jcl, final String internalReaderRecfm, final String internalReaderLrecl)
            throws ZosmfRequestException {
        return this.submitJclCommon(new SubmitJclParams(jcl, internalReaderRecfm, internalReaderLrecl));
    }

    /**
     * Submit a JCL string to run which can contain JCL symbolic substitutions
     *
     * @param params submit jcl parameters, see SubmitJclParams object
     * @return job document with details about the submitted job
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Job submitJclCommon(final SubmitJclParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        String key, value;
        final Map<String, String> headers = new HashMap<>();

        key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_MODE_TEXT").get(0);
        value = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_MODE_TEXT").get(1);
        headers.put(key, value);

        if (params.getInternalReaderLrecl().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_LRECL").get(0);
            headers.put(key, params.getInternalReaderLrecl().get());
        } else {
            key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_LRECL_80").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_LRECL_80").get(1);
            headers.put(key, value);
        }
        if (params.getInternalReaderRecfm().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_RECFM").get(0);
            headers.put(key, params.getInternalReaderLrecl().get());
        } else {
            key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_RECFM_F").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_RECFM_F").get(1);
            headers.put(key, value);
        }

        if (params.getJclSymbols().isPresent()) {
            headers.putAll(getSubstitutionHeaders(params.getJclSymbols().get()));
        }

        key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_CLASS_A").get(0);
        value = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_CLASS_A").get(1);
        headers.put(key, value);

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") + JobsConstants.RESOURCE;

        if (request == null || !(request instanceof PutTextZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_TEXT);
        }
        request.setHeaders(headers);
        request.setUrl(url);
        request.setBody(params.getJcl().orElseThrow(() -> new IllegalArgumentException("jcl not specified")));

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no job jcl submit response phrase")).toString();
        final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
        return (Job) JsonParseFactory.buildParser(ParseType.JOB).parseResponse(jsonObject);
    }

    /**
     * Submit a job on z/OS.
     *
     * @param jobDataSet job dataset to be translated into SubmitJobParams object
     * @return job document with details about the submitted job
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Job submit(final String jobDataSet) throws ZosmfRequestException {
        return this.submitCommon(new SubmitJobParams(jobDataSet));
    }

    /**
     * Submit a job on z/OS.
     *
     * @param params submit job parameters, see SubmitJobParams object
     * @return job document with details about the submitted job
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent") // due to ValidateUtils done in SubmitJobParams
    public Job submitCommon(final SubmitJobParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") + JobsConstants.RESOURCE;

        final String fullyQualifiedDataset = "//'" + EncodeUtils.encodeURIComponent(params.getJobDataSet().get()) + "'";
        final Map<String, String> submitMap = new HashMap<>();
        submitMap.put("file", fullyQualifiedDataset);

        if (request == null || !(request instanceof PutJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        if (params.getJclSymbols().isPresent()) {
            request.setHeaders(getSubstitutionHeaders(params.getJclSymbols().get()));
        }
        request.setUrl(url);
        request.setBody(new JSONObject(submitMap).toString());

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no job submit response phrase")).toString();
        final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
        return (Job) JsonParseFactory.buildParser(ParseType.JOB).parseResponse(jsonObject);
    }

    /**
     * Parse input string for JCL substitution
     *
     * @param keyValues Map containing JCL substitution symbols e.g.: {"SYMBOL","SYM"},{"SYMBOL2","SYM2"}
     * @return String Map containing all keys and values
     * @author Corinne DeStefano
     */
    private Map<String, String> getSubstitutionHeaders(final Map<String, String> keyValues) {
        final Map<String, String> symbolMap = new HashMap<>();

        // Check for matching quotes
        for (final String value : keyValues.values()) {
            if (value.chars().filter(ch -> ch == '"').count() % 2 != 0) {
                throw new IllegalStateException("Encountered invalid key/value pair. Mismatched quotes.");
            }
            if (value.isEmpty()) {
                throw new IllegalStateException("Encountered invalid key/value pair. Must define a value for key/value pair.");
            }
        }

        for (String key : keyValues.keySet()) {
            final String value = keyValues.get(key);
            if (key.isEmpty()) {
                throw new IllegalStateException("Encountered invalid key/value pair. Must define a key for key/value pair.");
            }
            if (key.length() > 8) {
                throw new IllegalStateException("Encountered invalid key/value pair. Key must be 8 characters or less.");
            }
            key = ZosmfHeaders.HEADERS.get("X_IBM_JCL_SYMBOL_PARTIAL").get(0) + key;
            LOG.debug("JCL symbol header: {}:{}", key, value);
            symbolMap.put(key, value);
        }

        return symbolMap;
    }

}
