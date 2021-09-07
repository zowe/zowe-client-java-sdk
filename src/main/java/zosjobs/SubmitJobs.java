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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rest.*;
import utility.Util;
import utility.UtilJobs;
import utility.UtilRest;
import zosjobs.input.SubmitJclParms;
import zosjobs.input.SubmitJobParms;
import zosjobs.response.Job;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle submitting of z/OS batch jobs via z/OSMF
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SubmitJobs {

    private static final Logger LOG = LogManager.getLogger(SubmitJobs.class);

    private final ZOSConnection connection;

    /**
     * SubmitJobs Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public SubmitJobs(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Submit a job that resides in a z/OS data set.
     *
     * @param jobDataSet job Dataset to be translated into parms object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitJob(String jobDataSet) throws Exception {
        return this.submitJobCommon(new SubmitJobParms(jobDataSet));
    }

    /**
     * Submit a job that resides in a z/OS data set.
     *
     * @param parms submit job parameters, see SubmitJobParms object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitJobCommon(SubmitJobParms parms) throws Exception {
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(parms.getJobDataSet().isEmpty(), "jobDataSet not specified");
        Util.checkStateParameter(parms.getJobDataSet().get().isEmpty(), "jobDataSet not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE;
        LOG.debug(url);

        String fullyQualifiedDataset = "//'" + parms.getJobDataSet().get() + "'";
        var jsonMap = new HashMap<String, String>();
        jsonMap.put("file", fullyQualifiedDataset);
        var jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(jsonRequestBody);

        if (parms.getJclSymbols().isPresent()) {
            // TODO..
        }

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, jsonRequestBody.toString(),
                ZoweRequestType.VerbType.PUT_JSON);

        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return new Job.Builder().build();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("400")) {
                throw new Exception("Body sent may be invalid. " + errorMsg);
            }
            e.printStackTrace();
            throw new Exception("No results for submitted job. " + errorMsg);
        }

        return UtilJobs.createJobObjFromJson((JSONObject) response.getResponsePhrase().orElseThrow(Exception::new));
    }

    /**
     * Submit a string of JCL to run
     *
     * @param jcl                 JCL content that you want to be submit
     * @param internalReaderRecfm record format of the jcl you want to submit. "F" (fixed) or "V" (variable)
     * @param internalReaderLrecl logical record length of the jcl you want to submit
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitJcl(String jcl, String internalReaderRecfm, String internalReaderLrecl) throws Exception {
        return this.submitJclCommon(new SubmitJclParms(jcl, internalReaderRecfm, internalReaderLrecl));
    }

    /**
     * Submit a JCL string to run
     *
     * @param parms submit jcl parameters, see SubmitJclParms object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Job submitJclCommon(SubmitJclParms parms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(parms.getJcl().isEmpty(), "jcl not specified");
        Util.checkStateParameter(parms.getJcl().get().isEmpty(), "jcl not specified");

        String key, value;
        Map<String, String> headers = new HashMap<>();

        key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_MODE_TEXT").get(0);
        value = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_MODE_TEXT").get(1);
        headers.put(key, value);

        if (parms.getInternalReaderLrecl().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_LRECL").get(0);
            headers.put(key, parms.getInternalReaderLrecl().get());
        } else {
            key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_LRECL_80").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_LRECL_80").get(1);
            headers.put(key, value);
        }
        if (parms.getInternalReaderRecfm().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_RECFM").get(0);
            headers.put(key, parms.getInternalReaderLrecl().get());
        } else {
            key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_RECFM_F").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_RECFM_F").get(1);
            headers.put(key, value);
        }
        if (parms.getJclSymbols().isPresent()) {
            // TODO..
        }

        key = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_CLASS_A").get(0);
        value = ZosmfHeaders.HEADERS.get("X_IBM_INTRDR_CLASS_A").get(1);
        headers.put(key, value);

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE;
        LOG.debug(url);

        String body = parms.getJcl().get();
        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, body, ZoweRequestType.VerbType.PUT_TEXT);
        request.setAdditionalHeaders(headers);

        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return new Job.Builder().build();
        try {
            UtilRest.checkHttpErrors(response);
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
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse((String) response.getResponsePhrase().get());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return UtilJobs.createJobObjFromJson(json);
    }

}
