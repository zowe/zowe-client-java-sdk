# z/OS Console Package

Contains APIs to interact with the z/OS console (using z/OSMF console REST endpoints).

## API Examples

**Submit a command to the z/OS console**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosconsole.ConsoleResponse;
import zosconsole.input.IssueParams;
import zosconsole.zosmf.ZosmfIssueParams;
import zosconsole.zosmf.ZosmfIssueResponse;

/**
 * Class example to showcase mvs console command functionality.
 */
public class IssueCommand extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(IssueCommand.class);

    /**
     * Main method defines z/OSMF host and user connection, and mvs command used for the example test.
     *
     * @param args for main not used
     */
    public static void main(String[] args) {
        String command = "D IPLINFO";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        IssueCommand.consoleCmdByIssue(connection, command);
        IssueCommand.consoleCmdByIssueSimple(connection, command);
        IssueCommand.consoleCmdByIssueDefConsoleCommon(connection, command);
    }

    /**
     * Issue IssueCommend issue method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     */
    public static void consoleCmdByIssue(ZOSConnection connection, String cmd) {
        IssueParams params = new IssueParams();
        params.setCommand(cmd);
        ConsoleResponse response;
        zosconsole.IssueCommand issueCommand = new zosconsole.IssueCommand(connection);
        try {
            response = issueCommand.issue(params);
            LOG.info(response.getCommandResponse().orElse(""));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Issue IssueCommend issueSimple method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     */
    public static void consoleCmdByIssueSimple(ZOSConnection connection, String cmd) {
        ConsoleResponse response;
        zosconsole.IssueCommand issueCommand = new zosconsole.IssueCommand(connection);
        try {
            response = issueCommand.issueSimple(cmd);
            LOG.info(response.getCommandResponse().orElse(""));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Issue IssueCommend issueDefConsoleCommon method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     */
    public static void consoleCmdByIssueDefConsoleCommon(ZOSConnection connection, String cmd) {
        ZosmfIssueParams params = new ZosmfIssueParams();
        params.setCmd(cmd);
        ZosmfIssueResponse zResponse;
        zosconsole.IssueCommand issueCommand = new zosconsole.IssueCommand(connection);
        try {
            zResponse = issueCommand.issueDefConsoleCommon(params);
            LOG.info(zResponse.getCmdResponse().orElse(""));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

}
`````

````java
package examples;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 */
public class ZosConnection {

    public static final String hostName = "XXX";
    public static final String zosmfPort = "XXX";
    public static final String userName = "XXX";
    public static final String password = "XXX";

}
`````