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
import rest.*;
import utility.Util;
import utility.UtilIO;
import zosjobs.input.CancelJobParams;

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
        this.connection = connection;
    }

    /**
     * Cancel a job that resides in a z/OS data set.
     *
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Nikunj goyal
     */
    public Response cancelJob() throws Exception {
        return this.cancelJobsCommon(new CancelJobParams(new CancelJobParams.Builder()));
    }

    /**
     * Cancel a job that resides in a z/OS data set.
     *
     * @param params cancel job parameters, see cancelJobsCommon object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Nikunj goyal
     */
    public Response cancelJobsCommon(CancelJobParams params) throws Exception {
        Util.checkNullParameter(params == null, "params is null");
        Util.checkStateParameter(params.getJobId().isEmpty(), "job id not specified");
        Util.checkStateParameter(params.getJobId().get().isEmpty(), "job id not specified");
        Util.checkStateParameter(params.getJobName().isEmpty(), "job name not specified");
        Util.checkStateParameter(params.getJobName().get().isEmpty(), "job name not specified");

        // generate full url request
        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE +
                UtilIO.FILE_DELIM + params.getJobName().get() + UtilIO.FILE_DELIM + params.getJobId().get();
        LOG.info(url);

        // generate json string body for the request
        String version = params.getVersion().orElse(JobsConstants.DEFAULT_CANCEL_VERSION);
        var jsonMap = new HashMap<String, String>();
        jsonMap.put("request", JobsConstants.REQUEST_CANCEL);
        jsonMap.put("version", version);
        var jsonBody = new JSONObject(jsonMap);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, jsonBody.toString(),
                ZoweRequestType.VerbType.PUT_JSON);

        return request.executeHttpRequest();
    }

}
