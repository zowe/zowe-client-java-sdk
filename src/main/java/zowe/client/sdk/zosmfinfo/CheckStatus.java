package zowe.client.sdk.zosmf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.utility.UtilRest;
import zowe.client.sdk.zosjobs.JobsConstants;

public class CheckStatus {

    private static final Logger LOG = LoggerFactory.getLogger(CheckStatus.class);

    private final ZOSConnection connection;
    private ZoweRequest request;
    private String url;

    /**
     * CheckStatus Constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public CheckStatus(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
    }

    public String getZosmfInfo() throws Exception {

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosmfConstants.RESOURCE + ZosmfConstants.INFO;

        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, url, null, ZoweRequestType.VerbType.GET_JSON);
        } else {
            request.setRequest(url);
        }
        Response response = request.executeRequest();
        if (response.isEmpty()) {
            return null;
        }

        UtilRest.checkHttpErrors(response);

        return response.getResponsePhrase().get().toString();
    }


}
