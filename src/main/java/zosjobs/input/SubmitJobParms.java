/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs.input;

import java.util.Optional;

public class SubmitJobParms {

    private Optional<String> jobDataSet;
    private Optional<String> jclSymbols = Optional.empty();

    public SubmitJobParms(String jobDataSet) {
        this.jobDataSet = Optional.ofNullable(jobDataSet);
    }

    public SubmitJobParms(String jobDataSet, String jclSymbols) {
        this.jobDataSet = Optional.ofNullable(jobDataSet);
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    public Optional<String> getJobDataSet() {
        return jobDataSet;
    }

    public void setJobDataSet(String jobDataSet) {
        this.jobDataSet = Optional.ofNullable(jobDataSet);
    }

    public Optional<String> getJclSymbols() {
        return jclSymbols;
    }

    public void setJclSymbols(String jclSymbols) {
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    @Override
    public String toString() {
        return "SubmitJobParms{" +
                "jobDataSet=" + jobDataSet +
                ", jclSymbols=" + jclSymbols +
                '}';
    }

}
