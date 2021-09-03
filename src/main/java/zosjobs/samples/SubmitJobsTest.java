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
import zosjobs.MonitorJobs;
import zosjobs.SubmitJobs;
import zosjobs.response.Job;
import zosjobs.types.JobStatus;

/**
 * Class example to showcase SubmitJobs functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SubmitJobsTest {

    private static final Logger LOG = LogManager.getLogger(SubmitJobsTest.class);

    /**
     * Main method defines host and user connection needed to showcase
     * SubmitJobs functionality. Calls SubmitJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

        // Submit a job already defined as a dataset
        LOG.info(SubmitJobsTest.tstSubmitJob(connection, "xxx.xxx.xxx.xxx(xxx)"));

        // Submit a job from a string
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\n// EXEC PGM=IEFBR14";
        Job submitJobsTest = SubmitJobsTest.tstSubmitJclJob(connection, jclString);
        // Wait for the job to complete
        MonitorJobs monitorJobs = new MonitorJobs(connection);
        submitJobsTest = monitorJobs.waitForJobStatus(submitJobsTest, JobStatus.Type.OUTPUT);
        System.out.println(submitJobsTest);
        // Get the return code
        String retCode = submitJobsTest.getRetCode().get();
        System.out.println("Expected Return Code = CC 0000 [" + retCode + "]");
    }

    /**
     * Example on how to call SubmitJobs submitJcl method.
     * submitJcl is given a jcl string to use to submit it as a job.
     *
     * @return job document
     * @param connection ZOSConnection object
     * @param jclString jcl formatted string
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static Job tstSubmitJclJob(ZOSConnection connection, String jclString) throws Exception {
        SubmitJobs submitJobs = new SubmitJobs(connection);
        return submitJobs.submitJcl(jclString, null, null);
    }

    /**
     * Example on how to call SubmitJobs submitJcl method.
     * submitJcl is given a Dataset member value to use to submit it as a job.
     *
     * @param connection ZOSConnection object
     * @param dsMember dataset member value
     * @return job document
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static Job tstSubmitJob(ZOSConnection connection, String dsMember) throws Exception {
        SubmitJobs submitJobs = new SubmitJobs(connection);
        return submitJobs.submitJob(dsMember);
    }

}
