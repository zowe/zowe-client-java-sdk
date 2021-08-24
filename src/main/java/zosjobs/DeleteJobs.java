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

public class DeleteJobs {

    private static final Logger LOG = LogManager.getLogger(DeleteJobs.class);
    private final ZOSConnection connection;

    /**
     * DeleteJobs Constructor
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
     * @return job document with details about the submitted job
     * @throws Exception error on submitting
     * @author Frank Giordano
     */
    public Response deletejob( ) throws Exception {
        return this.deleteJobCommon(null);
    }

    public Response deleteJobCommon(DeleteJobParams parms) throws Exception {
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(!parms.getJobId().isPresent(), "jobid not specified");
        Util.checkStateParameter(!parms.getJobname().isPresent(), "jobname not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE;
        LOG.debug(url);

        Map<String, String> headers = new HashMap<>();
        String key, value;

        if (parms.getModifyVersion().get() == "1.0") {
            key = ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_1").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_1").get(1);
            headers.put(key, value);
        }   else if (parms.getModifyVersion().get() == "2.0") {
            key = ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_2").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_JOB_MODIFY_VERSION_2").get(1);
            headers.put(key, value);
        }

        String parameters = UtilIO.FILE_DELIM + parms.getJobname().get() + UtilIO.FILE_DELIM + parms.getJobId().get();

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url + parameters, null,
                ZoweRequestType.VerbType.DELETE_JSON);

        request.setAdditionalHeaders(headers);
        return request.executeHttpRequest();
    }

}
