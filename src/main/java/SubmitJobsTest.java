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
import zosjobs.SubmitJobs;
import zosjobs.response.Job;

public class SubmitJobsTest {

    private static final Logger LOG = LogManager.getLogger(SubmitJobsTest.class);

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);
        LOG.info(SubmitJobsTest.tstSubmitJob(connection, "xxx.xxx.xxx.xxx(xxx)"));
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        LOG.info(SubmitJobsTest.tstSubmitJclJob(connection, jclString));
    }

    private static Job tstSubmitJclJob(ZOSConnection connection, String jclString) throws Exception {
        return SubmitJobs.submitJcl(connection, jclString, null, null);
    }

    private static Job tstSubmitJob(ZOSConnection connection, String dsMember) throws Exception {
        return SubmitJobs.submitJob(connection, dsMember);
    }

}
