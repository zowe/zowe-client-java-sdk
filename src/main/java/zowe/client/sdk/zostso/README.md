# z/OS TSO Package

Contains APIs to interact with TSO on z/OS (using z/OSMF TSO REST endpoints).

## API Examples

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zostso.IssueResponse;
import zostso.IssueTso;

import java.util.Arrays;

/**
 * Class example to test tso command functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueTsoCommand extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(IssueTsoCommand.class);

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection, and tso command parameters used for the example test.
     *
     * @param args for main not used
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String command = "XXX";
        String accountNumber = "XXX";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        IssueResponse response = IssueTsoCommand.tsoConsoleCmdByIssue(accountNumber, command);
        String[] results = response.getCommandResponses().orElse("").split("\n");
        Arrays.stream(results).sequential().forEach(LOG::info);
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
package examples;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZosConnection {

    public static final String hostName = "XXX";
    public static final String zosmfPort = "XXX";
    public static final String userName = "XXX";
    public static final String password = "XXX";

}
`````