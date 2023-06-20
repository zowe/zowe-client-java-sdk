# z/OS USS Package

Contains API to execute USS (Unix System Serves) commands via SSH connection.

API located in method package.

## API Example

````java
package zowe.client.sdk.examples.zosuss;

import zowe.client.sdk.core.SSHConnection;
import zowe.client.sdk.zosuss.method.IssueUss;

/**
 * Class example to showcase USS command(s) execution.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class USSCommand {

    /**
     * Main method defines SSH connection and showcases executing USS commands.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        int portNum = 0; // replace with valid value
        SSHConnection conn = new SSHConnection("xxx", portNum, "xxx", "xxx");
        IssueUss shell = new IssueUss(conn);
        // 10000 is the timeout value in milliseconds
        System.out.println(shell.executeSshCmd("mkdir test;cd test;touch frank;ls", 10000));
        // value "frank" should display
    }

}
`````