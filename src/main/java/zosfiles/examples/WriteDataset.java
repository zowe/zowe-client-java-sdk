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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosfiles.ZosDsn;

/**
 * Class example to showcase WriteDataset functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class WriteDataset {

    private static final Logger LOG = LogManager.getLogger(WriteDataset.class);

    private static ZOSConnection connection;

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
        String dataSetName = "XXX";
        String member = "XXX";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        var content = "NEW CONTENT\nTHE SECOND LINE UPDATED";
        WriteDataset.writeToDsnMember(dataSetName, member, content);
    }

    /**
     * Write to the given member name specified replacing its content. If it does exist, it will be created.
     *
     * @param dataSetName name of a dataset where member should be located (e.g. 'DATASET.LIB')
     * @param member      name of member to write
     * @param content     content for write
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void writeToDsnMember(String dataSetName, String member, String content) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.writeDsn(dataSetName, member, content);
        LOG.info("http response code " + response.getStatusCode());
    }

}
