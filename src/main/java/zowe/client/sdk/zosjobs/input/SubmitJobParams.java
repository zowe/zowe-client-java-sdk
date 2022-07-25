/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.input;

import java.util.Optional;

/**
 * Submit job parameters
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SubmitJobParams {

    /**
     * z/OS data set which should contain syntactically correct JCL. Example value: IBMUSER.PUBLIC.CNTL(IEFBR14)
     * where IEFBR14 contains statements like:
     * //IEFBR14 JOB ()
     * //RUN     EXEC PGM=IEFBR14
     */
    private Optional<String> jobDataSet;

    /**
     * A string for JCL symbolic substitution
     */
    private Optional<String> jclSymbols = Optional.empty();

    /**
     * SubmitJobParams constructor
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     * @author Frank Giordano
     */
    public SubmitJobParams(String jobDataSet) {
        this.jobDataSet = Optional.ofNullable(jobDataSet);
    }

    /**
     * SubmitJobParams constructor
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     * @param jclSymbols string for JCL symbolic substitution
     * @author Frank Giordano
     */
    public SubmitJobParams(String jobDataSet, String jclSymbols) {
        this.jobDataSet = Optional.ofNullable(jobDataSet);
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    /**
     * Retrieve jclSymbols specified
     *
     * @return jclSymbols value
     * @author Frank Giordano
     */
    public Optional<String> getJclSymbols() {
        return jclSymbols;
    }

    /**
     * Assign jclSymbols value
     *
     * @param jclSymbols string for JCL symbolic substitution
     * @author Frank Giordano
     */
    public void setJclSymbols(String jclSymbols) {
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    /**
     * Retrieve dataset specified
     *
     * @return jobDataSet value
     * @author Frank Giordano
     */
    public Optional<String> getJobDataSet() {
        return jobDataSet;
    }

    /**
     * Assign dataset value
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     * @author Frank Giordano
     */
    public void setJobDataSet(String jobDataSet) {
        this.jobDataSet = Optional.ofNullable(jobDataSet);
    }

    @Override
    public String toString() {
        return "SubmitJobParams{" +
                "jobDataSet=" + jobDataSet +
                ", jclSymbols=" + jclSymbols +
                '}';
    }

}
