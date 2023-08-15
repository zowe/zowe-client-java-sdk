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
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseResponse;
import zowe.client.sdk.parse.JsonParseResponseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
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
 * @version 2.0
 */
public class JobSubmit {

    private static final Logger LOG = LoggerFactory.getLogger(JobSubmit.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * SubmitJobs Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public JobSubmit(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative SubmitJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public JobSubmit(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Submit a string of JCL to run
     *
     * @param jcl                 JCL content that you want to be submitted
     * @param internalReaderRecfm record format of the jcl you want to submit. "F" (fixed) or "V" (variable)
     * @param internalReaderLrecl logical record length of the jcl you want to submit
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitByJcl(String jcl, String internalReaderRecfm, String internalReaderLrecl) throws Exception {
        return this.submitJclCommon(new SubmitJclParams(jcl, internalReaderRecfm, internalReaderLrecl));
    }

    /**
     * Submit a JCL string to run which can contain JCL symbolic substitutions
     *
     * @param params submit jcl parameters, see SubmitJclParams object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitJclCommon(SubmitJclParams params) throws Exception {
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

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE;

        if (request == null || !(request instanceof TextPutRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_TEXT);
        }
        request.setUrl(url);
        request.setBody(params.getJcl().orElseThrow(() -> new IllegalArgumentException("jcl not specified")));
        request.setHeaders(headers);

        final String jsonStr = RestUtils.getResponse(request).getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no job jcl submit response phrase")).toString();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonStr);
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(jsonObject, ParseType.JOB);
        return (Job) parser.parseResponse();
    }

    /**
     * Submit a job that resides in a z/OS dataset.
     *
     * @param jobDataSet job dataset to be translated into SubmitJobParams object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submit(String jobDataSet) throws Exception {
        return this.submitCommon(new SubmitJobParams(jobDataSet));
    }

    /**
     * Submit a job that resides in a z/OS dataset.
     *
     * @param params submit job parameters, see SubmitJobParams object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitCommon(SubmitJobParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE;

        final String fullyQualifiedDataset = "//'" + EncodeUtils.encodeURIComponent(params.getJobDataSet()
                .orElseThrow(() -> new IllegalArgumentException("jobDataSet not specified"))) + "'";
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("file", fullyQualifiedDataset);

        if (request == null || !(request instanceof JsonPutRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        if (params.getJclSymbols().isPresent()) {
            request.setHeaders(getSubstitutionHeaders(params.getJclSymbols().get()));
        }

        final String jsonStr = RestUtils.getResponse(request).getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no job submit response phrase")).toString();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonStr);
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(jsonObject, ParseType.JOB);
        return (Job) parser.parseResponse();
    }

    /**
     * Parse input string for JCL substitution
     *
     * @param keyValues Map containing JCL substitution symbols e.g.: "{"SYMBOL","SYM"},{"SYMBOL2","SYM2"}
     * @return String Map containing all keys and values
     * @throws Exception error on submitting
     * @author Corinne DeStefano
     */
    private Map<String, String> getSubstitutionHeaders(Map<String, String> keyValues) throws Exception {
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
            LOG.debug("JCL Symbol Header: " + key + ":" + value);
            symbolMap.put(key, value);
        }

        return symbolMap;
    }

}
