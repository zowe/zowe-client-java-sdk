# Zos Logs Package

Contains API to interact with retrieving z/OS log (OPERLOG or SYSLOG) information on z/OS (using z/OSMF logs REST
endpoints).

API located in the method package.

## API Examples

**Retrieve syslog from a start time and time range**

````java
package zowe.client.sdk.examples.zoslogs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zoslogs.input.ZosLogInputData;
import zowe.client.sdk.zoslogs.method.ZosLog;
import zowe.client.sdk.zoslogs.response.ZosLogReply;
import zowe.client.sdk.zoslogs.types.DirectionType;
import zowe.client.sdk.zoslogs.types.HardCopyType;

/**
 * Class example to showcase ZosLog class functionality.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosLogExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * z/OS SYSLOG retrieval functionality via ZosLog class.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        ZosConnection connection = ZosConnectionFactory
                .createBasicConnection(hostName, zosmfPort, userName, password);
        ZosLog zosLog = new ZosLog(connection);
        ZosLogInputData zosLogInputData = new ZosLogInputData.Builder()
                .startTime("2022-11-27T05:06Z")
                .hardCopy(HardCopyType.SYSLOG)
                .timeRange("24h")
                .direction(DirectionType.BACKWARD)
                .processResponses(true)
                .build();
        ZosLogReply zosLogReply;
        try {
            zosLogReply = zosLog.issueCommand(zosLogInputData);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        zosLogReply.getItemLst().forEach(i -> System.out.println(i.getTime().get() + " " + i.getMessage().get()));

        // get the last one minute of syslog from the date/time of now backwards...
        zosLogInputData = new ZosLogInputData.Builder()
                .hardCopy(HardCopyType.SYSLOG)
                .timeRange("1m")
                .direction(DirectionType.BACKWARD)
                .processResponses(true)
                .build();
        try {
            zosLogReply = zosLog.issueCommand(zosLogInputData);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        zosLogReply.getItemLst().forEach(i -> System.out.println(i.getTime().get() + " " + i.getMessage().get()));
    }

}
`````

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
