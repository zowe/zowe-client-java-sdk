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

import java.io.IOException;

public class SubmitJobsTest {

    private static final Logger LOG = LogManager.getLogger(SubmitJobsTest.class);

    public static void main(String[] args) throws IOException {
        String hostName = "xxxxxxxx";
        String port = "xxxxxxxx";
        String userName = "xxxxxxxx";
        String password = "xxxxxxxx";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);
        LOG.info(SubmitJobsTest.tstSubmitJob(connection, "xxx.xxx.xxx.xxx(xxx)"));
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        LOG.info(SubmitJobsTest.tstSubmitJclJob(connection, jclString));
    }

    private static Job tstSubmitJclJob(ZOSConnection connection, String jclString) throws IOException {
        return SubmitJobs.submitJcl(connection, jclString, null, null);
    }

    private static Job tstSubmitJob(ZOSConnection connection, String dsMember) throws IOException {
        return SubmitJobs.submitJob(connection, dsMember);
    }

}
