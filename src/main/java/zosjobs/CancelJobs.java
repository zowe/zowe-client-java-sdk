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

public class CancelJobs {

    private static final Logger LOG = LogManager.getLogger(CancelJobs.class);
    private final ZOSConnection connection;

    /**
     * CancelJobs Constructor
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
     * @author Frank Giordano
     */
    public Response cancelJob() throws Exception {
        return this.cancelJobsCommon(null);
    }


    public Response cancelJobsCommon(CancelJobParams parms) throws Exception {
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(!parms.getJobId().isPresent(), "jobid not specified");
        Util.checkStateParameter(!parms.getJobname().isPresent(), "jobname not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE + JobsConstants.REQUEST_CANCEL;
        LOG.debug(url);

        Map<String, String> headers = new HashMap<>();
        String key, value, version;

        if(parms.getVersion().get() == null) {
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
