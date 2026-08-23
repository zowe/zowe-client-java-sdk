/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosconsole.methods.ConsoleCmd;
import zowe.client.sdk.zosconsole.methods.ZowexConsoleService;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests validating the ConsoleService Strategy Pattern and Factory functionality.
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class ConsoleServiceTest {

    private final ZosConnection zosConnection = ZosConnectionFactory
            .createBasicConnection("localhost", 443, "user", "pass");
    private final SshConnection sshConnection = new SshConnection("localhost", 22, "user", "pass");

    @Test
    public void tstFactoryCreatesZosmfConsoleServiceForZosConnection() {
        final ConsoleService consoleService = ConsoleServiceFactory.create(zosConnection);
        assertNotNull(consoleService);
        assertTrue(consoleService instanceof ConsoleCmd);
    }

    @Test
    public void tstFactoryCreatesZowexConsoleServiceForSshConnection() {
        final ConsoleService consoleService = ConsoleServiceFactory.create(sshConnection);
        assertNotNull(consoleService);
        assertTrue(consoleService instanceof ZowexConsoleService);
    }

    @Test
    public void tstZowexConsoleServicePolymorphicCommandExecution() throws ZosmfRequestException {
        final ConsoleService consoleService = ConsoleServiceFactory.create(sshConnection);

        final ConsoleCmdResponse response = consoleService.issueCommand("D IPLINFO");

        assertNotNull(response);
        assertTrue(response.getCmdResponse().contains("COMMAND ISSUED: D IPLINFO"));
        assertTrue(response.getCmdResponseUrl().contains("ssh://localhost:22/zowex/console/defcn"));
    }

    @Test
    public void tstZowexConsoleServiceSpecificConsoleNameCommandExecution() throws ZosmfRequestException {
        final ConsoleService consoleService = ConsoleServiceFactory.create(sshConnection);

        final ConsoleCmdResponse response = consoleService.issueCommand("D A,L", "MYCON");

        assertNotNull(response);
        assertTrue(response.getCmdResponse().contains("COMMAND ISSUED: D A,L"));
        assertTrue(response.getCmdResponseUrl().contains("ssh://localhost:22/zowex/console/MYCON"));
    }

    @Test
    public void tstConsoleServiceFactoryNullChecks() {
        assertThrows(NullPointerException.class, () -> ConsoleServiceFactory.create((ZosConnection) null));
        assertThrows(NullPointerException.class, () -> ConsoleServiceFactory.create((SshConnection) null));
    }

}
