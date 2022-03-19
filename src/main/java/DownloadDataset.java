/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.examples.zosfiles;

//import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.ZosConnection;
import zowe.client.sdk.utility.UtilIO;
import zowe.client.sdk.zosfiles.ZosDsnDownload;
import zowe.client.sdk.zosfiles.input.DownloadParams;

import java.io.InputStream;
import java.io.StringWriter;

/**
 * Class example to showcase DownloadDataset functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class DownloadDataset extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(DownloadDataset.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DownloadDataset functionality. Calls DownloadDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String datasetMember = "CCSQA.CCS150.CAW0OPTN(CCIPCS64)";

        DownloadParams params = new DownloadParams.Builder().build();
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        DownloadDataset.downloadDsnMember(connection, datasetMember, params);
    }

    /**
     * Download dataset members
     *
     * @param connection ZOSConnection object
     * @param name       data set name
     * @param params     download parameters object
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public static void downloadDsnMember(zowe.client.sdk.core.ZOSConnection connection, String name, DownloadParams params) throws Exception {
        try (InputStream inputStream = new ZosDsnDownload(connection).downloadDsn(name, params)) {
            if (inputStream != null) {
                StringWriter writer = new StringWriter();
//                IOUtils.copy(inputStream, writer, UtilIO.UTF8);
                String content = writer.toString();
                LOG.info(content);
            }
        }
    }

}
