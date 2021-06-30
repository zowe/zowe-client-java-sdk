/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso;

import core.ZOSConnection;
import utility.Util;
import zostso.input.StartTsoParams;

public class IssueTso {

    public static void issueTsoCommand(ZOSConnection connection, String accountNumber,
                                       String command, StartTsoParams startParams) {
        Util.checkConnection(connection);
        Util.checkNullParameter(accountNumber == null, "accountNumber not specified");
        Util.checkNullParameter(command == null, "command not specified");
        Util.checkStateParameter(accountNumber.isEmpty(), "accountNumber is empty");
        Util.checkStateParameter(command.isEmpty(), "command is empty");

        IssueResponse response = new IssueResponse(false, null, false, null,
                null, null);
        response.setStartResponse(StartTso.start(connection, accountNumber, startParams));


    }

}
