# Zos Logs Package

Contains API to interact with retrieving z/OS log (OPERLOG or SYSLOG) information on z/OS (using z/OSMF logs REST
endpoints).  

API located in method package.  
  
## API Examples

**Retrieve syslog from a start time and time range**

````java
package zowe.client.sdk.examples.zoslogs;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zoslogs.GetZosLog;
import zowe.client.sdk.zoslogs.input.DirectionType;
import zowe.client.sdk.zoslogs.input.HardCopyType;
import zowe.client.sdk.zoslogs.input.ZosLogParams;
import zowe.client.sdk.zoslogs.response.ZosLogReply;

/**
 * Class example to showcase GetZosLog functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosGetLogTst extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * z/OS SYSLOG retrieval functionality.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
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
        zosLogReply = getZosLog.getZosLog(zosLogParams);
        zosLogReply.getItemLst().forEach(i -> System.out.println(i.getTime().get() + " " + i.getMessage().get()));
    }

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
