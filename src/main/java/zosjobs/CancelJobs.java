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
import rest.*;
import utility.Util;
import utility.UtilIO;
import zosjobs.input.CancelJobParams;

import java.util.HashMap;
import java.util.Map;

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

        String url = "https://" + connection.getHost() + ":" + connection.getPort() +
                JobsConstants.RESOURCE + UtilIO.FILE_DELIM + JobsConstants.REQUEST_CANCEL;
        LOG.debug(url);

        Map<String, String> headers = new HashMap<>();
        String key, value, version;

        if (params.getVersion().isEmpty()) {
            version = JobsConstants.DEFAULT_CANCEL_VERSION;
        } else {
            version = params.getVersion().get();
        }

        key = ZosmfHeaders.HEADERS.get("APPLICATION_JSON").get(0);
        value = ZosmfHeaders.HEADERS.get("APPLICATION_JSON").get(1);
        headers.put(key, value);

        String parameters = UtilIO.FILE_DELIM + params.getJobName().get() + UtilIO.FILE_DELIM + params.getJobId().get();

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url + parameters, null,
                ZoweRequestType.VerbType.PUT_JSON);

        request.setAdditionalHeaders(headers);
        return request.executeHttpRequest();
    }

}
