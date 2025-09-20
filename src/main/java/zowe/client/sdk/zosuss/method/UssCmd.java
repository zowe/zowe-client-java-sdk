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
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.timer.WaitUtil;
import zowe.client.sdk.zosuss.exception.UssCmdException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * UssCmd Class provides a way to execute USS commands via SSH connection
 *
 * @author Frank Giordano
 * @version 5.0
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
     *
     * @param command string value contains one or more USS commands
     * @param timeout int value in milliseconds for timeout duration on session connection
     * @return string output value
     * @throws UssCmdException SSH Unix System Services error request
     * @author Frank Giordano
     */
    public String issueCommand(final String command, final int timeout) throws UssCmdException {
        try (final ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
             final ManagedSession session = new ManagedSession(connection, timeout);
             final ManagedChannel channel = new ManagedChannel(session.get(), command, responseStream)) {

            // Wait for channel execution to complete
            while (channel.get().isConnected()) {
                WaitUtil.wait(1000);
            }

            return responseStream.toString();
        } catch (IOException e) {
            LOG.debug("IOException error {}", String.valueOf(e));
            throw new UssCmdException(e.getMessage(), e);
        } catch (JSchException e) {
            LOG.debug("JSchException error {}", String.valueOf(e));
            throw new UssCmdException(e.getMessage(), e);
        }
    }

    /**
     * AutoCloseable wrapper for JSch Session
     */
    static class ManagedSession implements AutoCloseable {
        private final Session session;

        ManagedSession(SshConnection connection, int timeout) throws JSchException {
            this.session = new JSch().getSession(connection.getUser(), connection.getHost(), connection.getPort());
            session.setPassword(connection.getPassword());
            final Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");
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

        ManagedChannel(Session session, String command, OutputStream responseStream) throws JSchException {
            this.channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setOutputStream(responseStream);
            channel.connect();
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
