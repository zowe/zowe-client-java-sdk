# z/OS Console Package

Contains APIs to interact with the z/OS mvs console (using z/OSMF console REST endpoints).

APIs are located in the method package.

## API Examples

**Submit a command to the z/OS console**

````java
package zowe.client.sdk.examples.zosconsole;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.input.IssueConsoleInputData;
import zowe.client.sdk.zosconsole.method.IssueConsole;
import zowe.client.sdk.zosconsole.response.ConsoleResponse;

/**
 * Class example to showcase mvs console command functionality via IssueConsole class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class IssueConsoleExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection, and mvs command used for the example tests.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String command = "D IPLINFO";
        ZosConnection connection = ZosConnectionFactory
                .createBasicConnection(hostName, zosmfPort, userName, password);
        IssueConsoleExp.issueCommand(connection, command);
        IssueConsoleExp.issueCommandCommon(connection, command);
    }

    /**
     * Issue IssueConsole issueCommand method which will execute the given simple mvs console command
     * without a IssueConsoleInputData object.
     *
     * @param connection for connection information, see ZosConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void issueCommand(ZosConnection connection, String cmd) {
        ConsoleResponse response;
        try {
            IssueConsole issueConsole = new IssueConsole(connection);
            response = issueConsole.issueCommand(cmd);
        } catch (ZosmfRequestException e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.println(response.getCommandResponse().orElse("no command response"));
    }

    /**
     * Issue IssueConsole issueCommandCommon method which will execute the given mvs console command.
     *
     * @param connection for connection information, see ZosConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void issueCommandCommon(ZosConnection connection, String cmd) {
        ConsoleResponse response;
        try {
            IssueConsole issueConsole = new IssueConsole(connection);
            IssueConsoleInputData consoleInputData = new IssueConsoleInputData(cmd);
            consoleInputData.setProcessResponse();
            response = issueConsole.issueCommandCommon(ConsoleConstants.RES_DEF_CN, consoleInputData);
        } catch (ZosmfRequestException e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.println(response.getCommandResponse().orElse("no command response"));
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
