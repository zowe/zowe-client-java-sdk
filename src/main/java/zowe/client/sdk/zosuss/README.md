# z/OS USS Package

Contains Shell class to execute USS (Unix System Serves) commands via SSH connection.

## Example

````java
import zowe.client.sdk.core.SSHConnection;
import zowe.client.sdk.core.Shell;

/**
 * Class example to showcase USS command(s) execution.
 */
public class USSCommand {

    /**
     * Main method defines SSH connection and showcases executing USS commands.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     */
    public static void main(String[] args) throws Exception {
        SSHConnection conn = new SSHConnection("XXX", XXX, "XXX", XXX);
        Shell shell = new Shell(conn);
        System.out.println(shell.executeSshCwd("mkdir test;cd test;ls"));
    }

}
`````