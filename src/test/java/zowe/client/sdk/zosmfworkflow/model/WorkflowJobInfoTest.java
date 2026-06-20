/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.model;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.utility.JsonUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for WorkflowJobInfo, WorkflowJobStatus, and WorkflowJobFile.
 */
public class WorkflowJobInfoTest {

    @Test
    public void tstWorkflowJobInfoParseSuccess() throws Exception {
        final WorkflowJobInfo jobInfo = JsonUtils.parseResponse(
                "{\"jobstatus\":{\"retcode\":\"CC 0000\",\"jobname\":\"TESTJOB\",\"status\":\"OUTPUT\"," +
                        "\"owner\":\"IBMUSER\",\"subsystem\":\"JES2\",\"class\":\"A\",\"type\":\"JOB\",\"jobid\":\"JOB00001\"}," +
                        "\"jobfiles\":[{\"id\":1,\"ddname\":\"JESMSGLG\",\"byte-count\":500,\"record-count\":10," +
                        "\"class\":\"A\",\"stepname\":\"STEP1\",\"procstep\":\"PROC1\"}]}",
                WorkflowJobInfo.class, "test");

        final WorkflowJobStatus jobStatus = jobInfo.getJobstatus();
        assertNotNull(jobStatus);
        assertEquals("CC 0000", jobStatus.getRetcode());
        assertEquals("TESTJOB", jobStatus.getJobname());
        assertEquals("OUTPUT", jobStatus.getStatus());
        assertEquals("IBMUSER", jobStatus.getOwner());
        assertEquals("JES2", jobStatus.getSubsystem());
        assertEquals("A", jobStatus.getClasss());
        assertEquals("JOB", jobStatus.getType());
        assertEquals("JOB00001", jobStatus.getJobid());

        assertEquals(1, jobInfo.getJobfiles().size());
        final WorkflowJobFile jobFile = jobInfo.getJobfiles().get(0);
        assertEquals(1, jobFile.getId().intValue());
        assertEquals("JESMSGLG", jobFile.getDdname());
        assertEquals(500, jobFile.getByteCount().intValue());
        assertEquals(10, jobFile.getRecordCount().intValue());
        assertEquals("A", jobFile.getClasss());
        assertEquals("STEP1", jobFile.getStepname());
        assertEquals("PROC1", jobFile.getProcstep());
    }

    @Test
    public void tstWorkflowJobInfoNullDefaultsSuccess() {
        final WorkflowJobInfo jobInfo = new WorkflowJobInfo(null, null);

        assertNull(jobInfo.getJobstatus());
        assertTrue(jobInfo.getJobfiles().isEmpty());
    }

}
