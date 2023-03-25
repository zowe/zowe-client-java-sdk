# z/OS TSO Package

Contains APIs to interact with TSO on z/OS (using z/OSMF TSO REST endpoints).

## API Examples

````java
package zowe.client.sdk.examples.zostso;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zostso.IssueResponse;
import zowe.client.sdk.zostso.IssueTso;

import java.util.Arrays;

/**
 * Class example to test tso command functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueTsoCommandTst extends TstZosConnection {

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection, and tso command parameters used for the example test.
     *
     * @param args for main not used
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String command = "xxx";
        String accountNumber = "xxx";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        IssueResponse response = IssueTsoCommandTst.tsoConsoleCmdByIssue(accountNumber, command);
        String[] results = response.getCommandResponses().orElse("").split("\n");
        Arrays.stream(results).sequential().forEach(System.out::println);
    }

    /**
     * Issue issueTsoCommand method from IssueTso class which will execute the given tso command
     *
     * @param accountNumber user's z/OSMF permission account number
     * @param cmd           tso command to execute
     * @return issue response object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static IssueResponse tsoConsoleCmdByIssue(String accountNumber, String cmd) throws Exception {
        IssueTso issueTso = new IssueTso(connection);
        return issueTso.issueTsoCommand(accountNumber, cmd);
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