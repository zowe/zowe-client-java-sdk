# z/OS Console Package

Contains APIs to interact with the z/OS mvs console (using z/OSMF console REST endpoints).

APIs located in method package.

## API Examples

**Submit a command to the z/OS console**

````java
package zowe.client.sdk.examples.zosconsole;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosconsole.method.IssueConsole;
import zowe.client.sdk.zosconsole.response.ConsoleResponse;
import zowe.client.sdk.zosconsole.input.IssueConsoleParams;
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
        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);
        IssueCommandTst.issueCommand(connection, command);
        IssueCommandTst.issueCommandCommon(connection, command);
    }

    /**
     * Issue IssueConsole issueCommand method which will execute the given simple mvs console command
     * without params object.
     *
     * @param connection connection information, see ZosConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void issueCommand(ZosConnection connection, String cmd) {
        IssueConsole issueConsole = new IssueConsole(connection);
        ConsoleResponse response;
        try {
            response = issueConsole.issueCommand(cmd);
            System.out.println(response.getCommandResponse().orElse(""));
        } catch (ZosmfRequestException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Issue IssueConsole issueCommandCommon method which will execute the given mvs console command.
     *
     * @param connection connection information, see ZosConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void issueCommandCommon(ZosConnection connection, String cmd) {
        IssueConsole issueConsole = new IssueConsole(connection);
        ConsoleResponse response;
        try {
            response = issueConsole.issueCommandCommon(ConsoleConstants.RES_DEF_CN, new IssueConsoleParams(cmd));
            System.out.println(response.getCommandResponse().orElse(""));
        } catch (ZosmfRequestException e) {
            System.out.println(e.getMessage());
        }
    }

}

`````

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
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
    public static ZosConnection getSecureZosConnection() throws Exception {
        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        return (new ZosConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````