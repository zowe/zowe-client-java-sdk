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
import zosjobs.DeleteJobs;
import zosjobs.input.DeleteJobParams;

public class DeleteJobsTest {

    private static final Logger LOG = LogManager.getLogger(DeleteJobsTest.class);

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String jobId = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);
        DeleteJobParams params = new DeleteJobParams.Builder()
                .jobId(jobId)
                .build();

        LOG.info(new DeleteJobs(connection).deleteJobCommon(params));
    }

}
