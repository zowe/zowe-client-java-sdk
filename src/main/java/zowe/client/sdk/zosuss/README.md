# z/OS USS Package

Contains API to execute USS (Unix System Serves) commands via SSH connection.

API located in the method package.

## API Example

````java
package zowe.client.sdk.examples.zosuss;

import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.zosuss.exception.UssCmdException;
import zowe.client.sdk.zosuss.method.UssCmd;

/**
 * Class example to test USS command functionality via UssCmd class.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class UssCmdExp {

    /**
     * The main method defines ssh connection and showcases executing a USS command via UssCmd class.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        int portNum = 0; // replace with valid value
        SshConnection conn = new SshConnection("xxx", portNum, "xxx", "xxx");
        UssCmd ussCmd = new UssCmd(conn);
        // 10000 is the timeout value in milliseconds
        try {
            // value "frank" should display
            System.out.println(ussCmd.issueCommand("mkdir test;cd test;touch frank;ls", 10000));
        } catch (UssCmdException e) {
            throw new RuntimeException(e);
        }
    }

}
`````
