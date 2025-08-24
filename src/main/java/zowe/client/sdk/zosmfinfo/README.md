# z/OSMF INFO Package

Contains APIs to interact with retrieving z/OSMF information on z/OS (using z/OSMF jobs REST endpoints).

APIs are located in the methods package.

## API Examples

**Check the status of your z/OSMF instance**

````java
package zowe.client.sdk.examples.zosmfInfo;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfinfo.methods.ZosmfStatus;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

import java.util.Arrays;

/**
 * Class example to showcase ZosmfStatus class functionality.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosmfStatusExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * ZosmfStatus class functionality. This method performs API call to retrieve the status of the
     * running z/OSMF instance on the z/OS backend.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        ZosConnection connection = ZosConnectionFactory
                .createBasicConnection(hostName, zosmfPort, userName, password);
        ZosmfStatus zosmfStatus = new ZosmfStatus(connection);
        ZosmfInfoResponse zosmfInfoResponse;
        try {
            zosmfInfoResponse = zosmfStatus.get();
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        System.out.println(zosmfInfoResponse.toString());
        Arrays.stream(zosmfInfoResponse.getZosmfPluginsInfo().get()).forEach(i -> System.out.println(i.toString()));
    }

}
`````

**List the systems defined to z/OSMF through the z/OSMF APIs**

```java
package zowe.client.sdk.examples.zosmfInfo;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfinfo.methods.ZosmfSystems;
import zowe.client.sdk.zosmfinfo.response.ZosmfSystemsResponse;

import java.util.Arrays;

/**
 * Class example to showcase ZosmfSystems class functionality.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosmfSystemsExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * ZosmfSystems class functionality. This method performs API call to retrieve the entire list of
     * defined z/OSMF systems running on the z/OS backend.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        ZosConnection connection = ZosConnectionFactory
                .createBasicConnection(hostName, zosmfPort, userName, password);
        ZosmfSystems zosmfSystems = new ZosmfSystems(connection);
        ZosmfSystemsResponse zosmfInfoResponse;
        try {
            zosmfInfoResponse = zosmfSystems.get();
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        System.out.println(zosmfInfoResponse.toString());
        Arrays.stream(zosmfInfoResponse.getDefinedSystems().get()).forEach(i -> System.out.println(i.toString()));
    }

}
```

````java
package zowe.client.sdk.examples.utility;

import zowe.client.sdk.rest.Response;

/**
 * Utility class containing helper method(s).
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class Util {

    /**
     * Extract response phrase string value if any from a Response object.
     *
     * @param response object
     * @return string value
     * @author Frank Giordano
     */
    public static String getResponsePhrase(Response response) {
        if (response == null || response.getResponsePhrase().isEmpty()) {
            return null;
        }
        return response.getResponsePhrase().get().toString();
    }

}
`````

**Connection setup**

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.model.ProfileDao;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final String zosmfPort = "xxx";
    public static final String userName = "xxx";
    public static final String password = "xxx";

    // or use the following method to retrieve Zowe OS credential store for your
    // secure Zowe V2 credentials you entered when you initially set up Zowe Global Team Configuration.
    public static ZosConnection getSecureZosConnection() throws TeamConfigException {
        TeamConfig teamConfig = new TeamConfig();
        ProfileDao profile = teamConfig.getDefaultProfile("zosmf");
        return (ZosConnectionFactory.createBasicConnection(
                profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
