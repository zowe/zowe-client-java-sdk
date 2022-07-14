# z/OSMF INFO Package

Contains APIs to interact with retrieving information about your z/OSMF instance on z/OS (using z/OSMF jobs REST
endpoints).

## API Examples

**Check the status of your z/OSMF instance**

````java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.ZosConnection;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

import java.util.Arrays;

public class CheckStatus extends ZosConnection {

    private static final Logger LOG = LoggerFactory.getLogger(CheckStatus.class);

    private static zowe.client.sdk.zosmfinfo.CheckStatus checkStatus;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CheckStatus functionality. Calls
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        checkStatus = new zowe.client.sdk.zosmfinfo.CheckStatus(connection);
        ZosmfInfoResponse zosmfInfoResponse = checkStatus.getZosmfInfo();
        LOG.info(zosmfInfoResponse.toString());
        Arrays.stream(zosmfInfoResponse.getZosmfPluginsInfo.get()).forEach(i -> LOG.info(i.toString()));
    }

}
`````

**List the systems defined to z/OSMF through the z/OSMF APIs**

`````java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.ZosConnection;
import zowe.client.sdk.zosmfinfo.ListDefinedSystems;
import zowe.client.sdk.zosmfinfo.response.ZosmfListDefinedSystemsResponse;

import java.util.Arrays;

public class ZosmfDefinedSystems extends ZosConnection {

    private static final Logger LOG = LoggerFactory.getLogger(CheckStatus.class);

    private static ListDefinedSystems listDefinedSystems;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * ListDefinedSystems functionality.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        listDefinedSystems = new ListDefinedSystems(connection);
        ZosmfListDefinedSystemsResponse zosmfInfoResponse = listDefinedSystems.listDefinedSystems();
        LOG.info(zosmfInfoResponse.toString());
        Arrays.stream(zosmfInfoResponse.getDefinedSystems.get()).forEach(i -> LOG.info(i.toString()));

    }

}
`````