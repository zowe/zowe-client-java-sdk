# Zos Logs Package

Contains APIs to interact with retrieving z/OS log (OPERLOG or SYSLOG) information on z/OS (using z/OSMF logs REST
endpoints).

## API Examples

**Retrieve syslog from a start time and time range**

````java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.ZosConnection;

import java.util.Arrays;

/**
 * Class example to showcase z/OS Syslog retrieval functionality.
 */
public class ZosSysLog extends ZosConnection {

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * z/OS SYSLOG retrieval functionality.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     */
    public static void main(String[] args) throws Exception {
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        GetZosLog getZosLog = new GetZosLog(connection);
        ZosLogParams zosLogParams = new ZosLogParams.Builder()
                .startTime("2022-11-27T05:06:20Z")
                .hardCopy(HardCopyType.SYSLOG)
                .timeRange("24h")
                .direction(DirectionType.BACKWARD)
                .processResponses(true)
                .build();
        ZosLogReply zosLogReply = getZosLog.getZosLog(zosLogParams);
        zosLogReply.getItemLst().forEach(i -> System.out.println(i.getTime().get() + " " + i.getMessage().get()));

        // get the last one minute of syslog from the date/time of now backwards...
        zosLogParams = new ZosLogParams.Builder()
                .hardCopy(HardCopyType.SYSLOG)
                .timeRange("1m")
                .direction(DirectionType.BACKWARD)
                .processResponses(true)
                .build();
        zosLogReply.getItemLst().forEach(i -> System.out.println(i.getTime().get() + " " + i.getMessage().get()));
    }
}
`````

````java
package examples;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 */
public class ZosConnection {

    public static final String hostName = "XXX";
    public static final String zosmfPort = "XXX";
    public static final String userName = "XXX";
    public static final String password = "XXX";

}
`````
