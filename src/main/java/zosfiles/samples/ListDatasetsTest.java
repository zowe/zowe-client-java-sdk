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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosfiles.ZosDsnList;
import zosfiles.input.ListParams;
import zosfiles.response.Dataset;

import java.util.List;

public class ListDatasetsTest {

    private static final Logger LOG = LogManager.getLogger(ListDatasetsTest.class);

    public static void main(String[] args) {
        String hostName = "XXX";
        String zosmfPort = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String dataSetMask = "XXX";
        String dataSetName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        ListDatasetsTest.tstListDsn(connection, dataSetMask);
        ListDatasetsTest.tstListMembers(connection, dataSetName);
    }

    private static void tstListMembers(ZOSConnection connection, String dataSetName) {
        ListParams params = new ListParams.Builder().build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<String> datasets = zosDsnList.listMembers(dataSetName, params);
        datasets.forEach(LOG::info);
    }

    private static void tstListDsn(ZOSConnection connection, String dataSetName) {
        ListParams params = new ListParams.Builder().build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<Dataset> datasets = zosDsnList.listDsn(dataSetName, params);
        datasets.forEach(LOG::info);
    }

}
