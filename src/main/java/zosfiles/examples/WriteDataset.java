/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles.examples;

import core.ZOSConnection;
import zosfiles.ZosDsn;

/**
 * Class example to showcase WriteDataset functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class WriteDataset {

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * WriteDataset functionality. Calls WriteDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String zosmfPort = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String datasetMember = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        WriteDataset.writeToDsnMember(connection, datasetMember, "NEW CONTENT\nTHE SECOND LINE UPDATED");
    }

    private static void writeToDsnMember(ZOSConnection connection, String datasetMember, String content) throws Exception {
        new ZosDsn(connection).writeDsn(datasetMember, content);
    }

}
