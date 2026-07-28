/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for JobStepData.
 *
 * @author Your Name
 * @version 7.0
 */
public class JobStepDataTest {

    @Test
    public void tstJobStepDataSuccess() {
        final JobStepData jobStepData = new JobStepData(
                true, "SY1", 1L, "IBMUSER", "MYPROG", "STEP1",
                "/usr/bin/prog", "PROC1", "CC 0000");

        assertNotNull(jobStepData);
        assertTrue(jobStepData.isActive());
        assertEquals("SY1", jobStepData.getSmfid());
        assertEquals(1L, jobStepData.getStepNumber());
        assertEquals("IBMUSER", jobStepData.getOwner());
        assertEquals("MYPROG", jobStepData.getProgramName());
        assertEquals("STEP1", jobStepData.getStepName());
        assertEquals("/usr/bin/prog", jobStepData.getPathName());
        assertEquals("PROC1", jobStepData.getProcStepName());
        assertEquals("CC 0000", jobStepData.getCompletion());
        assertEquals("JobStepData{active=true, smfid='SY1', stepNumber=1, owner='IBMUSER', " +
                        "programName='MYPROG', stepName='STEP1', pathName='/usr/bin/prog', " +
                        "procStepName='PROC1', completion='CC 0000'}",
                jobStepData.toString());
    }

    @Test
    public void tstJobStepDataWithNullValuesDefaultsToEmptyString() {
        final JobStepData jobStepData = new JobStepData(
                false, null, null, null, null, null, null, null, null);

        assertFalse(jobStepData.isActive());
        assertEquals("", jobStepData.getSmfid());
        assertEquals(0L, jobStepData.getStepNumber());
        assertEquals("", jobStepData.getOwner());
        assertEquals("", jobStepData.getProgramName());
        assertEquals("", jobStepData.getStepName());
        assertEquals("", jobStepData.getPathName());
        assertEquals("", jobStepData.getProcStepName());
        assertEquals("", jobStepData.getCompletion());
    }

}