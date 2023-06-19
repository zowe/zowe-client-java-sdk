# z/OS Console Package
  
Contains APIs to interact with the z/OS mvs console (using z/OSMF console REST endpoints).  
    
APIs located in method package.     
  
## API Examples

**Submit a command to the z/OS console**

````java
package zowe.client.sdk.examples.zosconsole;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosconsole.method.IssueConsole;
import zowe.client.sdk.zosconsole.response.ConsoleResponse;
import zowe.client.sdk.zosconsole.input.IssueParams;
import zowe.client.sdk.zosconsole.input.ZosmfIssueParams;
import zowe.client.sdk.zosconsole.response.ZosmfIssueResponse;

/**
 * Class example to showcase mvs console command functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class IssueCommandTst extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection, and mvs command used for the example test.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String command = "D IPLINFO";
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        IssueCommandTst.consoleCmdByIssue(connection, command);
        IssueCommandTst.consoleCmdByIssueSimple(connection, command);
        IssueCommandTst.consoleCmdByIssueDefConsoleCommon(connection, command);
    }

    /**
     * Issue IssueCommend issue method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void consoleCmdByIssue(ZOSConnection connection, String cmd) {
        IssueParams params = new IssueParams();
        params.setCommand(cmd);
        ConsoleResponse response;
        IssueConsole issueCommand = new IssueConsole(connection);
        try {
            response = issueCommand.issueCommand(params);
            System.out.println(response.getCommandResponse().orElse(""));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Issue IssueCommend issueSimple method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void consoleCmdByIssueSimple(ZOSConnection connection, String cmd) {
        ConsoleResponse response;
        IssueConsole issueCommand = new IssueConsole(connection);
        try {
            response = issueCommand.issueSimpleCommand(cmd);
            System.out.println(response.getCommandResponse().orElse(""));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Issue IssueCommend issueDefConsoleCommon method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void consoleCmdByIssueDefConsoleCommon(ZOSConnection connection, String cmd) {
        ZosmfIssueParams params = new ZosmfIssueParams();
        params.setCmd(cmd);
        ZosmfIssueResponse response;
        IssueConsole issueCommand = new IssueConsole(connection);
        try {
            response = issueCommand.issueDefConsoleCommon(params);
            System.out.println(response.getCmdResponse().orElse(""));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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