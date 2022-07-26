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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JobUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.input.SubmitJclParams;
import zowe.client.sdk.zosjobs.input.SubmitJobParams;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to handle submitting of z/OS batch jobs via z/OSMF
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SubmitJobs {

    private static final Logger LOG = LoggerFactory.getLogger(SubmitJobs.class);
    private final ZOSConnection connection;
    private ZoweRequest request;

    /**
     * SubmitJobs Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public SubmitJobs(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative SubmitJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author Frank Giordano
     */
    public SubmitJobs(ZOSConnection connection, ZoweRequest request) {
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
    public Job submitJcl(String jcl, String internalReaderRecfm, String internalReaderLrecl) throws Exception {
        return this.submitJclCommon(new SubmitJclParams(jcl, internalReaderRecfm, internalReaderLrecl));
    }

    /**
     * Submit a JCL string to run
     *
     * @param params submit jcl parameters, see SubmitJclParams object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitJclCommon(SubmitJclParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getJcl().isEmpty(), "jcl not specified");
        ValidateUtils.checkIllegalParameter(params.getJcl().get().isEmpty(), "jcl not specified");

        String key, value;
        Map<String, String> headers = new HashMap<>();

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

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE;
        LOG.debug(url);

        String body = params.getJcl().get();
        if (request == null || !(request instanceof TextPutRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.VerbType.PUT_TEXT);
        }
        request.setRequest(url, body);
        request.setHeaders(headers);

        Response response = request.executeRequest();
        if (response.isEmpty()) {
            return new Job.Builder().build();
        }
        try {
            RestUtils.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("400")) {
                throw new Exception("Body sent may be invalid. " + errorMsg);
            }
            if (errorMsg.contains("401")) {
                throw new Exception("Unauthorized user specified. " + errorMsg);
            }
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse((String) response.getResponsePhrase().orElse(""));
        } catch (ParseException e) {
            throw new Exception(e.getMessage());
        }
        return JobUtils.parseJsonJobResponse(json);
    }

    /**
     * Submit a job that resides in a z/OS data set.
     *
     * @param jobDataSet job Dataset to be translated into SubmitJobParams object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitJob(String jobDataSet) throws Exception {
        return this.submitJobCommon(new SubmitJobParams(jobDataSet));
    }

    /**
     * Submit a job that resides in a z/OS data set.
     *
     * @param params submit job parameters, see SubmitJobParams object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitJobCommon(SubmitJobParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getJobDataSet().isEmpty(), "jobDataSet not specified");
        ValidateUtils.checkIllegalParameter(params.getJobDataSet().get().isEmpty(), "jobDataSet not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE;
        LOG.debug(url);

        String fullyQualifiedDataset = "//'" + EncodeUtils.encodeURIComponent(params.getJobDataSet().get()) + "'";
        var jsonMap = new HashMap<String, String>();
        jsonMap.put("file", fullyQualifiedDataset);
        var jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(String.valueOf(jsonRequestBody));

        if (request == null || !(request instanceof JsonPutRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.VerbType.PUT_JSON);
        }

        request.setRequest(url, jsonRequestBody.toString());

        if (params.getJclSymbols().isPresent()) {
            request.setHeaders(getSubstitutionHeaders(params.getJclSymbols().get()));
        }

        Response response = request.executeRequest();
        if (response.isEmpty()) {
            return new Job.Builder().build();
        }

        try {
            RestUtils.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("400")) {
                throw new Exception("Body sent may be invalid. " + errorMsg);
            }
            if (errorMsg.contains("500")) {
                throw new Exception("File does not exist. " + errorMsg);
            }
            e.printStackTrace();
            throw new Exception("No results for submitted job. " + errorMsg);
        }

        return JobUtils.parseJsonJobResponse(((JSONObject) response.getResponsePhrase()
                .orElseThrow(() -> new Exception("response phrase missing"))));
    }

    /**
     * Parse input string for JCL substitution
     *
     * @param symbols JCL substitution symbols e.g.: "TEST=TESTSYMBOL1 TSET=TESTSYMBOL2"
     * @return Map(String,String) containing all keys and values
     * @throws Exception error on submitting
     * @author Corinne DeStefano
     */
    private Map<String, String> getSubstitutionHeaders(String symbols) throws Exception {

        Map<String, String> symbolMap = new HashMap<>();

        // Input range:
        // KEY="abc cde fg" KEY2="none" KEY3=none KEY4="nospace"
        // REGEX: (\w*)=("(.*?)"|\S*)

        // Check overall structure of input string
        if (symbols.split("=").length % 2 !=0) {
            throw new Exception("Invalid key/value pair.  Use the format KEY=VALUE KEY2=VALUE2 KEY3=\"VAL THREE\"");
        }

        // Check for matching quotes
        if (symbols.chars().filter(ch -> ch == '"').count() % 2 !=0) {
            throw new Exception("Invalid key/value pair.  Mismatched quotes.");
        }

        // Now get the groups
        Pattern pattern = Pattern.compile("(\\w*)=(\"(.*?)\"|\\S*)");
        Matcher matcher = pattern.matcher(symbols);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            if (key.length() == 0 || value.length()==0) {
                throw new Exception("Invalid key/value pair.  Must define a value for key/value pairs.");
            }

            if (key.length() > 8) {
                throw new Exception("Invalid key/value pair.  Keys must be 8 characters or less.");
            }

            key = ZosmfHeaders.HEADERS.get("X_IBM_JCL_SYMBOL_PARTIAL").get(0) + key;

            LOG.debug("JCL Symbol Header: " + key + ":" + value);
            symbolMap.put(key, value);

        }

        return symbolMap;
    }
}
