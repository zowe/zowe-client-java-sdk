/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.input.JobSubmit;

import java.util.Map;
import java.util.Optional;

/**
 * Parameters for JobSubmit API input data
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobSubmitInputData {

    /**
     * z/OS dataset which should contain syntactically correct JCL. Example value: IBMUSER.PUBLIC.CNTL(IEFBR14)
     * where IEFBR14 contains statements like:
     * //IEFBR14 JOB ()
     * //RUN     EXEC PGM=IEFBR14
     */
    private String jobDataSet;

    /**
     * A Map for JCL symbolic substitution, specify key,value for symbol substitution
     */
    private Map<String, String> jclSymbols;

    /**
     * SubmitJobParams constructor
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     * @author Frank Giordano
     */
    public JobSubmitInputData(final String jobDataSet) {
        this.jobDataSet = jobDataSet;
    }

    /**
     * SubmitJobParams constructor
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     * @param jclSymbols Map for JCL symbolic substitution
     * @author Frank Giordano
     */
    public JobSubmitInputData(final String jobDataSet, final Map<String, String> jclSymbols) {
        this.jobDataSet = jobDataSet;
        this.jclSymbols = jclSymbols;
    }

    /**
     * Retrieve jclSymbols specified
     *
     * @return jclSymbols value
     */
    public Optional<Map<String, String>> getJclSymbols() {
        return Optional.ofNullable(jclSymbols);
    }

    /**
     * Assign jclSymbols value
     *
     * @param jclSymbols Map for JCL symbolic substitution
     */
    public void setJclSymbols(final Map<String, String> jclSymbols) {
        this.jclSymbols = jclSymbols;
    }

    /**
     * Retrieve dataset specified
     *
     * @return jobDataSet value
     */
    public Optional<String> getJobDataSet() {
        return Optional.ofNullable(jobDataSet);
    }

    /**
     * Assign dataset value
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     */
    public void setJobDataSet(final String jobDataSet) {
        this.jobDataSet = jobDataSet;
    }

    /**
     * Return string value representing SubmitJobParams object
     *
     * @return string representation of SubmitJobParams
     */
    @Override
    public String toString() {
        return "SubmitJobParams{" +
                "jobDataSet=" + jobDataSet +
                ", jclSymbols=" + jclSymbols +
                '}';
    }

}
