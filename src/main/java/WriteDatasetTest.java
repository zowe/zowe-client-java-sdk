/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */

import core.ZOSConnection;
import zosfiles.ZosDsn;

public class WriteDatasetTest {

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String datasetMember = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

        WriteDatasetTest.writeToDsnMember(connection, datasetMember, "NEW CONTENT\r\nTHE SECOND LINE");
    }

    private static void writeToDsnMember(ZOSConnection connection, String datasetMember, String content) {
        ZosDsn.writeDsn(connection, datasetMember, content);
    }

}
