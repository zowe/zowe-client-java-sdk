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
import examples.ZosConnection;
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
public class CopyDataset extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(CopyDataset.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CopyDataset functionality. Calls CopyDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error processing examples
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String fromDataSetName = "XXX";
        String toDataSetName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        copyDataset(connection, fromDataSetName, toDataSetName);
        copyDatasetByCopyParams(connection, fromDataSetName, toDataSetName);
        fromDataSetName = "XXX";  // specify a partition dataset only no member
        toDataSetName = "XXX"; // specify a partition dataset only no member
        copyFullPartitionDatasetByCopyParams(connection, fromDataSetName, toDataSetName);
    }

    /**
     * Example on how to call ZosDsnCopy copy method.
     * Copy method accepts a from and too strings for copying.
     * <p>
     * This copy method allows the following copy operations:
     * <p>
     * - sequential dataset to sequential dataset
     * - sequential dataset to partition dataset member
     * - partition dataset member to partition dataset member
     * - partition dataset member to partition dataset non-existing member
     * <p>
     * This example sends false value for copyAllMembers parameter in copy method to indicate we
     * are not copying all members in a partition dataset to another.
     *
     * @param connection      ZOSConnection
     * @param fromDataSetName source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.DATASET' or 'TARGET.DATASET(MEMBER)')
     * @throws Exception error processing copy request
     * @author Frank Giordano
     */
    public static void copyDataset(ZOSConnection connection, String fromDataSetName, String toDataSetName)
            throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        Response response = zosDsnCopy.copy(fromDataSetName, toDataSetName, true, false);
        LOG.info("http response code " + response.getStatusCode());
    }

    /**
     * Example on how to call ZosDsnCopy copy method.
     * Copy method accepts a CopyParams object.
     * <p>
     * This copy method allows the following copy operations:
     * <p>
     * - sequential dataset to sequential dataset
     * - sequential dataset to partition dataset member
     * - partition dataset member to partition dataset member
     * - partition dataset member to partition dataset non-existing member
     *
     * @param connection      ZOSConnection
     * @param fromDataSetName source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.DATASET' or 'TARGET.DATASET(MEMBER)')
     * @throws Exception error processing copy request
     * @author Frank Giordano
     */
    public static void copyDatasetByCopyParams(ZOSConnection connection, String fromDataSetName, String toDataSetName)
            throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        // 'replace' builder variable here will be true by default if not specified in builder.
        // 'copyAllMembers' builder variable here will be false by default
        CopyParams copyParams = new CopyParams.Builder().fromDataSet(fromDataSetName).toDataSet(toDataSetName).build();
        Response response = zosDsnCopy.copy(copyParams);
        LOG.info("http response code " + response.getStatusCode());
    }

    /**
     * Example on how to call ZosDsnCopy copy method.
     * Copy method accepts a CopyParams object.
     * <p>
     * This copy method is different from the other two examples above as it
     * sets the copyAllMember variable true to indicate that the copy operation will be performed
     * on a partition dataset to another partition dataset copying all its members to the target.
     *
     * @param connection      ZOSConnection
     * @param fromDataSetName source dataset (e.g. 'SOURCE.PARTITION.DATASET')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.PARTITION.DATASET')
     * @throws Exception error processing copy request
     * @author Frank Giordano
     */
    public static void copyFullPartitionDatasetByCopyParams(ZOSConnection connection, String fromDataSetName,
                                                            String toDataSetName) throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        // 'replace' here will be true by default if not specified in builder.
        CopyParams copyParams = new CopyParams.Builder()
                .fromDataSet(fromDataSetName).toDataSet(toDataSetName).copyAllMembers(true).build();
        Response response = zosDsnCopy.copy(copyParams);
        LOG.info("http response code " + response.getStatusCode());
    }

}
