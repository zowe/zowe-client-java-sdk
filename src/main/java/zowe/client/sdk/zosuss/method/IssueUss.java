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
import zowe.client.sdk.zosuss.exception.IssueUssException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * IssueUss Class provides a way to execute USS commands via SSH connection
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class IssueUss {

    private static final Logger LOG = LoggerFactory.getLogger(IssueUss.class);

    private final SshConnection connection;

    /**
     * Shell constructor
     *
     * @param connection SshConnection object
     */
    public IssueUss(final SshConnection connection) {
        ValidateUtils.checkSshConnection(connection);
        this.connection = connection;
    }

    /**
     * Executes USS command(s) specified within a string value
     *
     * @param command string value contain one or more USS commands
     * @param timeout int value in milliseconds for timeout duration on session connection
     * @return string output value
     * @throws IssueUssException SSH Unix System Services error request
     * @author Frank Giordano
     */
    public String issueCommand(final String command, final int timeout) throws IssueUssException {
        Session session = null;
        ChannelExec channel = null;

        try (final ByteArrayOutputStream responseStream = new ByteArrayOutputStream()) {
            session = new JSch().getSession(connection.getUser(), connection.getHost(), connection.getPort());
            session.setPassword(connection.getPassword());
            final Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");
            session.setConfig(config);
            session.connect(timeout);

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                WaitUtil.wait(1000);
            }

            return responseStream.toString();
        } catch (IOException e) {
            LOG.debug("IOException error " + e);
            throw new IssueUssException(e.getMessage(), e);
        } catch (JSchException e) {
            LOG.debug("JSchException error " + e);
            throw new IssueUssException(e.getMessage(), e);
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

}
