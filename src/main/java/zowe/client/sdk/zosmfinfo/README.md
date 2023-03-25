# z/OSMF INFO Package

Contains APIs to interact with retrieving z/OSMF information on z/OS (using z/OSMF jobs REST endpoints).

## API Examples

**Check the status of your z/OSMF instance**

````java
package zowe.client.sdk.examples.zosmfInfo;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosmfinfo.CheckStatus;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

import java.util.Arrays;

/**
 * Class example to showcase CheckStatus functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class CheckStatusTst extends TstZosConnection {

    private static CheckStatus checkStatus;

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

        checkStatus = new CheckStatus(connection);
        ZosmfInfoResponse zosmfInfoResponse = checkStatus.getZosmfInfo();
        System.out.println(zosmfInfoResponse.toString());
        Arrays.stream(zosmfInfoResponse.getZosmfPluginsInfo().get()).forEach(i -> System.out.println(i.toString()));
    }

}
`````

**List the systems defined to z/OSMF through the z/OSMF APIs**

`````java
package zowe.client.sdk.examples.zosmfInfo;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosmfinfo.ListDefinedSystems;
import zowe.client.sdk.zosmfinfo.response.ZosmfListDefinedSystemsResponse;

import java.util.Arrays;

/**
 * Class example to showcase ListDefinedSystems functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfDefinedSystemsTst extends TstZosConnection {

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
        System.out.println(zosmfInfoResponse.toString());
        Arrays.stream(zosmfInfoResponse.getDefinedSystems().get()).forEach(i -> System.out.println(i.toString()));
    }

`````

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.keytar.KeyTarImpl;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final String zosmfPort = "xxx";
    public static final String userName = "xxx";
    public static final String password = "xxx";

    // or use the following method to retrieve Zowe OS credential store for your
    // secure Zowe V2 credentials you entered when you initially set up Zowe Global Team Configuration.
    public static ZOSConnection getSecureZosConnection() throws Exception {
        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        return (new ZOSConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
