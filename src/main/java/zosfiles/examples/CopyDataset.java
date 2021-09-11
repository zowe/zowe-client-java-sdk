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
import zosfiles.ZosDsnCopy;
import zosfiles.input.CopyParams;

/**
 * Class example to showcase CopyDataset functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class CopyDataset {

    private static final Logger LOG = LogManager.getLogger(CopyDataset.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CopyDataset functionality. Calls CopyDataset example methods.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        String hostName = "XXX";
        String zosmfPort = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String fromDataSetName = "XXX";
        String toDataSetName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        copyDataset(connection, fromDataSetName, toDataSetName);
        copyDatasetByCopyParams(connection, fromDataSetName, toDataSetName);
    }

    /**
     * Example on how to call ZosDsnCopy copy method.
     * copy accepts a from and to strings for copying
     *
     * @param connection      ZOSConnection
     * @param fromDataSetName source dataset (e.g. SOURCE.DATASET(MEMBER))
     * @param toDataSetName   destination dataset (e.g. TARGET.DATASET(MEMBER))
     * @author Frank Giordano
     */
    public static void copyDataset(ZOSConnection connection, String fromDataSetName, String toDataSetName) {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        Response response = zosDsnCopy.copy(fromDataSetName, toDataSetName, true);
        LOG.info("http response code " + response.getStatusCode());
    }

    /**
     * Example on how to call ZosDsnCopy copy method.
     * copy accepts a CopyParams object
     *
     * @param connection      ZOSConnection
     * @param fromDataSetName source dataset (e.g. SOURCE.DATASET(MEMBER))
     * @param toDataSetName   destination dataset (e.g. TARGET.DATASET(MEMBER))
     * @author Frank Giordano
     */
    public static void copyDatasetByCopyParams(ZOSConnection connection, String fromDataSetName,
                                               String toDataSetName) {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        // 'replace' here will be true by default if not specified in builder.
        CopyParams copyParams = new CopyParams.Builder().fromDataSet(fromDataSetName).toDataSet(toDataSetName).build();
        Response response = zosDsnCopy.copy(copyParams);
        LOG.info("http response code " + response.getStatusCode());
    }

}
