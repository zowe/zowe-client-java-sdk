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
import zosfiles.ZosDsn;
import zosfiles.input.CreateParams;

/**
 * Class example to showcase CreateDataset functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class CreateDataset extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(CreateDataset.class);

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CreateDataset functionality. Calls CreateDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "XXX";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        createDataSet(dataSetName);
    }

    /**
     * Create a new partition data set
     *
     * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void createDataSet(String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.createDsn(dataSetName, partitioned());
        LOG.info("http response code " + response.getStatusCode());
    }

    /**
     * Example of a prebuilt CreateParams for creating a binary dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams binary() {
        return new CreateParams.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(10)
                .secondary(10)
                .dirblk(25)
                .recfm("U")
                .blksize(27998)
                .lrecl(27998)
                .build();
    }

    /**
     * Example of a prebuilt CreateParams for creating a c dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams c() {
        return new CreateParams.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(25)
                .recfm("VB")
                .blksize(32760)
                .lrecl(260)
                .build();
    }

    /**
     * Example of a prebuilt CreateParams for creating classic dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams classic() {
        return new CreateParams.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(25)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    /**
     * Example of a prebuilt CreateParams for creating partitioned dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams partitioned() {
        return new CreateParams.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(5)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    /**
     * Example of a prebuilt CreateParams for creating sequential dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams sequential() {
        return new CreateParams.Builder()
                .dsorg("PS")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

}
