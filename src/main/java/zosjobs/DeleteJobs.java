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
import zosjobs.input.DeleteJobParams;

import java.util.HashMap;
import java.util.Map;

/**
 * DeleteJobs class to handle Job delete
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class DeleteJobs {

    private static final Logger LOG = LogManager.getLogger(DeleteJobs.class);

    private final ZOSConnection connection;

    /**
     * DeleteJobs constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public DeleteJobs(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Delete a job that resides in a z/OS data set.
     *
     * @return response about the deleted job
     * @throws Exception error on submitting
     * @author Nikunj goyal
     */
    public Response deleteJob() throws Exception {
        return this.deleteJobCommon(new DeleteJobParams(new DeleteJobParams.Builder()));
    }

    /**
     * Delete a job that resides in a z/OS data set.
     *
     * @param params delete job parameters, see DeleteJobParams object
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Nikunj goyal
     */
    public Response deleteJobCommon(DeleteJobParams params) throws Exception {
        Util.checkNullParameter(params == null, "params is null");
        Util.checkStateParameter(params.getJobId().isEmpty(), "job id not specified");
        Util.checkStateParameter(params.getJobname().isEmpty(), "job name not specified");
        Util.checkStateParameter(params.getModifyVersion().isEmpty(), "modify version not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE;
        LOG.debug(url);

        Map<String, String> headers = new HashMap<>();
        String key, value;

        if (params.getModifyVersion().get() == "1.0") {
            key = ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_1").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_1").get(1);
            headers.put(key, value);
        } else if (params.getModifyVersion().get() == "2.0") {
            key = ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_2").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_2").get(1);
            headers.put(key, value);
        }

        String parameters = UtilIO.FILE_DELIM + params.getJobname().get() + UtilIO.FILE_DELIM + params.getJobId().get();

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url + parameters, null,
                ZoweRequestType.VerbType.DELETE_JSON);

        request.setAdditionalHeaders(headers);
        return request.executeHttpRequest();
    }

}
