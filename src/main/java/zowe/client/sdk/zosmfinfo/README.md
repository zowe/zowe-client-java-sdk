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
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfinfo.methods.ZosmfStatus;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

import java.util.Arrays;

/**
 * Class example to showcase ZosmfStatus class functionality.
 *
 * @author Frank Giordano
 * @version 6.0
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
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        ZosmfStatus zosmfStatus = new ZosmfStatus(connection);
        ZosmfInfoResponse zosmfInfoResponse;
        try {
            zosmfInfoResponse = zosmfStatus.get();
} catch (ZosmfRequestException e) {
    String errMsg = e.getMessage();
    if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
        String response = e.getResponse().getResponsePhrase().get().toString();
        if (!resp.isBlank() && !"{}".equals(response)) {
            errMsg = response;
        }
    }
    throw new RuntimeException(errMsg, e);
}
        System.out.println(zosmfInfoResponse.toString());
        Arrays.stream(zosmfInfoResponse.getZosmfPluginsInfo()).forEach(System.out::println);
    }

}
`````

**List the systems defined to z/OSMF through the z/OSMF APIs**

```java
package zowe.client.sdk.examples.zosmfInfo;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfinfo.methods.ZosmfSystems;
import zowe.client.sdk.zosmfinfo.model.DefinedSystem;
import zowe.client.sdk.zosmfinfo.response.ZosmfSystemsResponse;

import java.util.Arrays;

/**
 * Class example to showcase ZosmfSystems class functionality.
 *
 * @author Frank Giordano
 * @version 6.0
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
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        ZosmfSystems zosmfSystems = new ZosmfSystems(connection);
        ZosmfSystemsResponse zosmfInfoResponse;
        try {
            zosmfInfoResponse = zosmfSystems.get();
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }
        System.out.println(zosmfInfoResponse.toString());
        Arrays.stream(zosmfInfoResponse.getDefinedSystems().orElse(new DefinedSystem[0])).forEach(System.out::println);
    }

}
```

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
 * @version 6.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final int zosmfPort = 0;
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
