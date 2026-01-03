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
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.method.ConsoleCmd;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

public class IssueConsoleExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection, and mvs command used for the example tests.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String command = "D IPLINFO";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        IssueConsoleExp.issueCommand(connection, command);
        IssueConsoleExp.issueCommandCommon(connection, command);
    }

    /**
     * Issue issueCommand method for ConsoleCmd which will execute the given simple mvs console command
     * without an IssueConsoleInputData object.
     *
     * @param connection for connection information, see ZosConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void issueCommand(ZosConnection connection, String cmd) {
        ConsoleCmdResponse response;
        try {
            ConsoleCmd consoleCmd = new ConsoleCmd(connection);
            response = consoleCmd.issueCommand(cmd);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.getCmdResponse());
    }

    /**
     * Issue issueCommandCommon method for ConsoleCmd which will execute the given mvs console command.
     *
     * @param connection for connection information, see ZosConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void issueCommandCommon(ZosConnection connection, String cmd) {
        ConsoleCmdResponse response;
        try {
            ConsoleCmd consoleCmd = new ConsoleCmd(connection);
            ConsoleCmdInputData consoleInputData = new ConsoleCmdInputData(cmd);
            consoleInputData.setProcessResponse();
            response = consoleCmd.issueCommandCommon(ConsoleConstants.RES_DEF_CN, consoleInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.getCmdResponse());
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
 * @version 6.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final int zosmfPort = 0;
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
