# z/OS USS Package

Contains API to execute USS (Unix System Serves) commands via SSH connection.

API located in method package.

## API Example

````java
package zowe.client.sdk.examples.zosuss;

import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.zosuss.exception.IssueUssException;
import zowe.client.sdk.zosuss.method.IssueUss;

/**
 * Class example to test uss command functionality via IssueUss class.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class IssueUssExp {

    /**
     * Main method defines SSH connection and showcases executing a USS command vis IssueUss class.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        int portNum = 0; // replace with valid value
        SshConnection conn = new SshConnection("xxx", portNum, "xxx", "xxx");
        IssueUss issueUss = new IssueUss(conn);
        // 10000 is the timeout value in milliseconds
        try {
            // value "frank" should display
            System.out.println(issueUss.issueCommand("mkdir test;cd test;touch frank;ls", 10000));
        } catch (IssueUssException e) {
            throw new RuntimeException(e);
        }
    }

}
`````
