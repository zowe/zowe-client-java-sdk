/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs.samples;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosjobs.CancelJobs;
import zosjobs.input.CancelJobParams;

public class CancelJobsTest {

    private static final Logger LOG = LogManager.getLogger(CancelJobsTest.class);

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String jobId = "XXX";
        String jobName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);
        CancelJobParams params = new CancelJobParams.Builder().jobId(jobId).jobName(jobName).build();

        LOG.info(new CancelJobs(connection).cancelJobsCommon(params));
    }

}
