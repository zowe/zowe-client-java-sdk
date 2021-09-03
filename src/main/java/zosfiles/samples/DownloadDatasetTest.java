/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles.samples;

import core.ZOSConnection;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosfiles.ZosDsnDownload;
import zosfiles.input.DownloadParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class DownloadDatasetTest {

    private static final Logger LOG = LogManager.getLogger(DownloadDatasetTest.class);

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String zosmfPort = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String datasetMember = "XXX";

        DownloadParams params = new DownloadParams.Builder().build();
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        DownloadDatasetTest.downloadDsnMember(connection, datasetMember, params);
    }

    private static void downloadDsnMember(ZOSConnection connection, String name, DownloadParams params) throws IOException {
        try (InputStream inputStream = new ZosDsnDownload(connection).downloadDsn(name, params)) {
            if (inputStream != null) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStream, writer, "UTF8");
                String content = writer.toString();
                LOG.info(content);
            }
        }
    }

}
