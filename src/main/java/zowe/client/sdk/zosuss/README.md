# z/OS USS Package

Contains API to execute USS (Unix System Serves) commands via SSH connection.

API located in the method package.

## SSH Host Key Verification

UssCmd verifies the target z/OS host's SSH host key against the current user's default known_hosts file inside their
home directory ({.ssh/known_hosts}, e.g., {~/.ssh/known_hosts} on Unix or {%USERPROFILE%\.ssh\known_hosts} on Windows)
and rejects unknown or changed host keys (StrictHostKeyChecking=yes). This means the host's key must already be present
in that file before `issueCommand` will succeed - for example, by connecting once with a standard ssh client, or via
`ssh-keyscan`, to populate the entry.

If host key verification cannot be satisfied and the risk is understood, set the system property
`zowe.sdk.allow.insecure.connection` to `true` (the same opt-in used elsewhere in the SDK for insecure TLS processing)
to fall back to StrictHostKeyChecking=no. Doing so allows any host key, including one presented by a man-in-the-middle
attacker, and is logged as a warning each time it is used.

An example for setting the system property is as follows:

    System.setProperty("zowe.sdk.allow.insecure.connection`", "true");

Alternatively, the property can be specified when launching the JVM.

## SSH Exec Channel Environment Profile

The SSH exec channel used by UssCmd does not automatically load the user's shell profile. As a result, environment
variables, aliases, and other shell configuration settings defined in files such as /etc/profile or ~/.profile may not
be available when executing commands remotely.

If a command requires settings from the user's shell environment, the command string should source the appropriate
profile files before execution.

Example:

    // The SSH "exec" channel does not load the user's shell profile.
    // Source the profile before executing the requested command.
    command = "[ -f /etc/profile ] && . /etc/profile 2>/dev/null; " +
    "[ -f ~/.profile ] && . ~/.profile 2>/dev/null; " +
    command;  

This ensures that required environment settings are loaded before the USS command is executed. Applications should only
source profile files when those environment settings are required, as profile scripts may contain user-specific logic or
commands that are not intended for non-interactive execution.

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
 * @version 7.0
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