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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosfiles.ZosDsnList;
import zosfiles.input.ListParams;
import zosfiles.response.Dataset;

import java.io.IOException;
import java.util.List;

public class ListDatasetsTest {
    private static final Logger LOG = LogManager.getLogger(ListDatasetsTest.class);

    public static void main(String[] args) throws IOException {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String dataSetName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

        ListDatasetsTest.tstListDsn(connection, dataSetName);
    }

    private static void tstListDsn(ZOSConnection connection, String dataSetName) throws IOException {
        ListParams parms = new ListParams.Builder().build();
        List<Dataset> datasets = ZosDsnList.listDsn(connection, dataSetName, parms);
        datasets.forEach(LOG::info);
    }
}
