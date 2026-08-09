/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosuss.method;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.rest.RestConstant;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.WaitUtil;
import zowe.client.sdk.zosuss.exception.UssCmdException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * UssCmd Class provides a way to execute USS commands via ssh connection
 * <p>
 * The underlying SSH session verifies the z/OS host's SSH host key against the current user's
 * default known_hosts file inside their home directory ({@code .ssh/known_hosts}, e.g.,
 * {@code ~/.ssh/known_hosts} on Unix or {@code %USERPROFILE%\.ssh\known_hosts} on Windows)
 * and rejects unknown or changed host keys (StrictHostKeyChecking=yes). This means the
 * target host's key must already be present in that known_hosts file (for example, by
 * connecting once with a standard ssh client, or via {@code ssh-keyscan}) before
 * {@link #issueCommand(String, int)} will succeed.
 * <p>
 * If host key verification cannot be satisfied and the risk is understood, set the system property
 * "zowe.sdk.allow.insecure.connection" to {@code true} (the same opt-in used elsewhere in the SDK for
 * insecure TLS processing) to fall back to StrictHostKeyChecking=no. Doing so allows any host key,
 * including one presented by a man-in-the-middle attacker, and logs a warning each time it is used.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class UssCmd {

    private static final Logger LOG = LoggerFactory.getLogger(UssCmd.class);

    private final SshConnection connection;

    /**
     * UssCmd constructor
     *
     * @param connection SshConnection object
     */
    public UssCmd(final SshConnection connection) {
        ValidateUtils.checkSshConnection(connection);
        this.connection = connection;
    }

    /**
     * Executes USS command(s) specified within a string value
     * <p>
     * The SSH host key of the target system must already be present in the current
     * user's default known_hosts file inside their home directory ({@code .ssh/known_hosts},
     * e.g., {@code ~/.ssh/known_hosts} on Unix or {@code %USERPROFILE%\.ssh\known_hosts} on Windows),
     * or this call fails with a JSchException wrapped in UssCmdException with a message like:
     * 'reject HostKey'. The host key verification is enabled by default; see the class-level Javadoc
     * for the "zowe.sdk.allow.insecure.connection" opt-out.
     *
     * @param command string value containing one or more USS commands
     * @param timeout int value in milliseconds used independently as the timeout for
     *                the SSH session connection, SSH channel connection, and remote
     *                command execution. The timeout is not cumulative across these
     *                operations.
     * @return string output value from the USS command
     * @throws UssCmdException if an SSH connection, channel connection, or command
     *                         execution fails or times out
     * @author Frank Giordano
     */
    public String issueCommand(final String command, final int timeout) throws UssCmdException {
        if (timeout <= 0) {
            throw new IllegalArgumentException("Timeout must be greater than zero");
        }

        try (final ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
             final ManagedSession session = new ManagedSession(connection, timeout);
             final ManagedChannel channel = new ManagedChannel(session.get(), command, responseStream, timeout)) {

            final long startTime = System.nanoTime();

            // loop checks isClosed() to catch normal process completion
            while (!channel.get().isClosed()) {
                WaitUtil.wait(100);

                // protect against network hangs or stuck processes
                if ((System.nanoTime() - startTime) > TimeUnit.MILLISECONDS.toNanos(timeout)) {
                    throw new UssCmdException(
                            "Command execution timed out after " + timeout + " ms",
                            new java.util.concurrent.TimeoutException("SSH execution limit exceeded.")
                    );
                }
            }

            return responseStream.toString();
        } catch (IOException | JSchException e) {
            if (isSocketTimeout(e)) {
                throw new UssCmdException(
                        "SSH operation timed out after " + timeout
                                + " ms. Consider increasing the timeout value if "
                                + "the operation requires more time to complete.",
                        e
                );
            }

            throw new UssCmdException(e.getMessage(), e);
        }
    }

    /**
     * Determines whether the specified throwable or any of its causes is a {@link SocketTimeoutException}.
     *
     * @param throwable throwable to inspect
     * @return true if the throwable or any of its causes is a {@link SocketTimeoutException};
     *         false otherwise
     */
    private static boolean isSocketTimeout(final Throwable throwable) {
        Throwable cause = throwable;

        while (cause != null) {
            if (cause instanceof SocketTimeoutException) {
                return true;
            }
            cause = cause.getCause();
        }

        return false;
    }

    /**
     * AutoCloseable wrapper for JSch Session
     * <p>
     * Verifies the remote host's SSH key against the current user's default known_hosts file
     * ({@code ~/.ssh/known_hosts}) via StrictHostKeyChecking=yes, so an unknown or changed host key
     * (a possible man-in-the-middle) causes connect() to fail rather than being silently accepted.
     * Set the "zowe.sdk.allow.insecure.connection" system property to {@code true} to bypass this
     * check (StrictHostKeyChecking=no); doing so is logged as a warning.
     */
    static class ManagedSession implements AutoCloseable {

        private static final String DEFAULT_KNOWN_HOSTS_PATH =
                System.getProperty("user.home") + File.separator + ".ssh" + File.separator + "known_hosts";

        private final Session session;

        ManagedSession(final SshConnection connection, final int timeout) throws JSchException {
            final JSch jsch = new JSch();
            this.session = jsch.getSession(connection.getUser(), connection.getHost(), connection.getPort());
            session.setPassword(connection.getPassword());
            final Properties config = new Properties();
            config.put("PreferredAuthentications", "password");
            final boolean inSecure = Boolean.parseBoolean(
                    System.getProperty(RestConstant.INSECURE_PROPERTY_NAME, "false"));
            if (inSecure) {
                // Insecure mode
                LOG.warn("{} is enabled; SSH host key verification is disabled for this connection",
                        RestConstant.INSECURE_PROPERTY_NAME);
                config.put("StrictHostKeyChecking", "no");
            } else {
                // Secure mode
                jsch.setKnownHosts(DEFAULT_KNOWN_HOSTS_PATH);
                config.put("StrictHostKeyChecking", "yes");
            }
            session.setConfig(config);
            session.connect(timeout);
        }

        Session get() {
            return session;
        }

        @Override
        public void close() {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * AutoCloseable wrapper for JSch ChannelExec
     */
    static class ManagedChannel implements AutoCloseable {
        private final ChannelExec channel;

        ManagedChannel(final Session session,
                       final String command,
                       final OutputStream responseStream,
                       final int timeout)
                throws JSchException {
            this.channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setOutputStream(responseStream);
            channel.connect(timeout);
        }

        ChannelExec get() {
            return channel;
        }

        @Override
        public void close() {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

}
