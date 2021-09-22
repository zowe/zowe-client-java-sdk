/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package examples.zosfiles;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosfiles.ZosDsn;

/**
 * Class example to showcase DeleteDataset functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class DeleteDataset {

    private static final Logger LOG = LogManager.getLogger(DeleteDataset.class);

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DeleteDataset functionality. Calls DeleteDataset example methods.
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

        deleteDataSet(dataSetName);
        deleteMember(dataSetName, member);
    }

    /**
     * @param dataSetName name of a dataset to delete (e.g. 'DATASET.LIB')
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void deleteDataSet(String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.deleteDsn(dataSetName);
        LOG.info("http response code " + response.getStatusCode());
    }

    /**
     * @param dataSetName name of a dataset where member should be located (e.g. 'DATASET.LIB')
     * @param member      name of member to delete
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void deleteMember(String dataSetName, String member) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.deleteDsn(dataSetName, member);
        LOG.info("http response code " + response.getStatusCode());
    }

}
