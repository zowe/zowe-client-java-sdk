# z/OS TSO Package

Contains APIs to interact with TSO on z/OS (using z/OSMF TSO REST endpoints).

APIs located in method package.

## API Examples

````java
package zowe.client.sdk.examples.zostso;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.zostso.method.TsoCmd;

/**
 * Class example to test tso command functionality via IssueTso class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoCmdExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * The main method defines z/OSMF host and user connection, and tso command parameters used for the example test.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String command = "xxx";
        String accountNumber = "xxx";

        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        List<String> result = TsoCmdExp.issueCommand(accountNumber, command);
        result.forEach(System.out::println);
    }

    /**
     * Issue issueCommand method from the IssueTso class which will execute the given tso command.
     *
     * @param accountNumber user's z/OSMF permission account number
     * @param command           tso command to execute
     * @return list of messages result
     * @author Frank Giordano
     */
    public static List<String> issueCommand(String accountNumber, String command) {
        TsoCmd tsoCmd = new TsoCmd(connection, accountNumber);
        return tsoCmd.issueCommand(command);
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
