# z/OS TSO Package

Contains APIs to interact with TSO on z/OS (using z/OSMF TSO REST endpoints).

APIs are located in the methods package.

## API Examples

````java
package zowe.client.sdk.zostso.methods;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zostso.input.StartTsoInputData;
import zowe.client.sdk.zostso.response.TsoCommonResponse;

import java.util.List;

/**
 * Example to showcase the tso package method classes. 
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoCmdExp {

    private static ZosConnection connection;

    public static void main(String[] args) throws ZosmfRequestException {
        String command = "xxx";
        String accountNumber = "xxx";

        connection = ZosConnectionFactory.createBasicConnection(
                "hostName", "zosmfPort", "userName", "password");
        List<String> result = TsoCmdExp.issueCommand(accountNumber, command);
        result.forEach(System.out::println);
        pingWorkflow(accountNumber);
    }

    /**
     * Issue issueCommand method from the TsoCmd class which will execute the given tso command.
     *
     * @param accountNumber user's z/OSMF permission account number
     * @param command       tso command to execute
     * @return list of messages result
     * @throws ZosmfRequestException exception thrown when issueCommand fails
     * @author Frank Giordano
     */
    public static List<String> issueCommand(String accountNumber, String command) throws ZosmfRequestException {
        TsoCmd tsoCmd = new TsoCmd(connection, accountNumber);
        return tsoCmd.issueCommand(command);
    }

    /**
     * Demonstrate starting, pinging, and stopping a TSO address space
     *
     * @param accountNumber user's z/OSMF permission account number
     * @throws ZosmfRequestException exception thrown when ping fails
     * @author Frank Giordano
     */
    public static void pingWorkflow(String accountNumber) throws ZosmfRequestException {

        StartTsoInputData inputData = new StartTsoInputData();
        inputData.setLogonProcedure(accountNumber);
        TsoStart tsoStart = new TsoStart(connection);
        // send tso start call and return the session id
        final String sessionId = tsoStart.start(inputData);
        System.out.println("TSO session id: " + sessionId);

        TsoPing tsoPing = new TsoPing(connection);
        // ping the session id
        TsoCommonResponse tsoCommonResponse = tsoPing.ping(sessionId);
        System.out.println(tsoCommonResponse);

        TsoStop tsoStop = new TsoStop(connection);
        // stop the tso session
        tsoCommonResponse = tsoStop.stop(sessionId);
        System.out.println(tsoCommonResponse);
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
