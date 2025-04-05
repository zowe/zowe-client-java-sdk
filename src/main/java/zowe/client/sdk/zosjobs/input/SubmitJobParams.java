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

import zowe.client.sdk.utility.ValidateUtils;

import java.util.Map;
import java.util.Optional;

/**
 * Submit job parameters
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class SubmitJobParams {

    /**
     * z/OS dataset which should contain syntactically correct JCL. Example value: IBMUSER.PUBLIC.CNTL(IEFBR14)
     * where IEFBR14 contains statements like:
     * //IEFBR14 JOB ()
     * //RUN     EXEC PGM=IEFBR14
     */
    private Optional<String> jobDataSet;

    /**
     * A Map for JCL symbolic substitution, specify key,value for symbol substitution
     */
    private Optional<Map<String, String>> jclSymbols = Optional.empty();

    /**
     * SubmitJobParams constructor
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     * @author Frank Giordano
     */
    public SubmitJobParams(final String jobDataSet) {
        this.jobDataSet = Optional.ofNullable(jobDataSet);
    }

    /**
     * SubmitJobParams constructor
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     * @param jclSymbols Map for JCL symbolic substitution
     * @author Frank Giordano
     */
    public SubmitJobParams(final String jobDataSet, final Map<String, String> jclSymbols) {
        ValidateUtils.checkNullParameter(jobDataSet == null, "jobDataSet is null");
        ValidateUtils.checkIllegalParameter(jobDataSet.isBlank(), "jobDataSet not specified");
        this.jobDataSet = Optional.of(jobDataSet);
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    /**
     * Retrieve jclSymbols specified
     *
     * @return jclSymbols value
     */
    public Optional<Map<String, String>> getJclSymbols() {
        return jclSymbols;
    }

    /**
     * Assign jclSymbols value
     *
     * @param jclSymbols Map for JCL symbolic substitution
     */
    public void setJclSymbols(final Map<String, String> jclSymbols) {
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    /**
     * Retrieve dataset specified
     *
     * @return jobDataSet value
     */
    public Optional<String> getJobDataSet() {
        return jobDataSet;
    }

    /**
     * Assign dataset value
     *
     * @param jobDataSet z/OS data set which should contain syntactically correct JCL
     */
    public void setJobDataSet(final String jobDataSet) {
        this.jobDataSet = Optional.ofNullable(jobDataSet);
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
