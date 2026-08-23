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

import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.methods.ConsoleCmd;
import zowe.client.sdk.zosconsole.methods.ZowexConsoleService;

/**
 * Factory providing dynamic creation of ConsoleService strategy implementations based on
 * connection type (z/OSMF REST API vs zowex SSH native services).
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public final class ConsoleServiceFactory {

    private ConsoleServiceFactory() {
        throw new IllegalStateException("Factory class");
    }

    /**
     * Create a ConsoleService instance backed by z/OSMF REST API protocol provider.
     *
     * @param connection z/OSMF ZosConnection object
     * @return ConsoleService implementation (ConsoleCmd)
     */
    public static ConsoleService create(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        return new ConsoleCmd(connection);
    }

    /**
     * Create a ConsoleService instance backed by zowex SSH native service protocol provider.
     *
     * @param connection z/OS SshConnection object
     * @return ConsoleService implementation (ZowexConsoleService)
     */
    public static ConsoleService create(final SshConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        return new ZowexConsoleService(connection);
    }

}
