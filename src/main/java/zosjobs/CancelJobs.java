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
 * @version 1.0
 * @author Nikunj Goyal
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
        return this.cancelJobsCommon(new CancelJobParams(null));
    }

    /**
     * Cancel a job that resides in a z/OS data set.
     *
     * @param parms Cancel job parameters, see SubmitJobParms object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Nikunj goyal
     */
    public Response cancelJobsCommon(CancelJobParams parms) throws Exception {
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(parms.getJobId().isEmpty(), "jobid not specified");
        Util.checkStateParameter(parms.getJobId().get().isEmpty(), "jobid not specified");
        Util.checkStateParameter(parms.getJobname().isEmpty(), "jobname not specified");
        Util.checkStateParameter(parms.getJobname().get().isEmpty(), "jobname not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() +
                JobsConstants.RESOURCE + UtilIO.FILE_DELIM + JobsConstants.REQUEST_CANCEL;
        LOG.debug(url);

        Map<String, String> headers = new HashMap<>();
        String key, value, version;

        if (parms.getVersion().isEmpty()) {
            version = JobsConstants.DEFAULT_CANCEL_VERSION;
        } else {
            version = parms.getVersion().get();
        }

        key = ZosmfHeaders.HEADERS.get("APPLICATION_JSON").get(0);
        value = ZosmfHeaders.HEADERS.get("APPLICATION_JSON").get(1);
        headers.put(key, value);

        String parameters = UtilIO.FILE_DELIM + parms.getJobname().get() + UtilIO.FILE_DELIM + parms.getJobId().get();

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url + parameters, null,
                ZoweRequestType.VerbType.PUT_JSON);

        request.setAdditionalHeaders(headers);
        return request.executeHttpRequest();
    }
}
