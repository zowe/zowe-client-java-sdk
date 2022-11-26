/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosuss;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import zowe.client.sdk.core.SSHConnection;
import zowe.client.sdk.utility.timer.WaitUtil;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * Shell Class provides a way to execute USS commands via SSH connection
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class Shell {

    private final SSHConnection connection;

    /**
     * Shell constructor
     *
     * @param connection SSHConnection object
     */
    public Shell(SSHConnection connection) {
        this.connection = connection;
    }

    /**
     * Executes USS command(s) specified within a string value
     *
     * @param command string value contain one or more USS commands
     * @param timeout int value in milliseconds for timeout duration on session connection
     * @return string output value
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public String executeSshCwd(String command, int timeout) throws Exception {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession(connection.getUser(), connection.getHost(), connection.getPort());
            session.setPassword(connection.getPassword());
            final Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");
            session.setConfig(config);
            session.connect(timeout);

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            final ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                WaitUtil.wait(1000);
            }

            return responseStream.toString();
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
