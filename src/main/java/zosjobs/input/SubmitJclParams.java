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

/**
 * Submit jcl parameters
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SubmitJclParams {

    /**
     * JCL to submit which should contain syntactically correct JCL. Example value: IBMUSER.PUBLIC.CNTL(IEFBR14)
     * where IEFBR14 contains statements like:
     * "//IEFBR14 JOB ()" +
     * "//RUN     EXEC PGM=IEFBR14"
     */
    private Optional<String> jcl;

    /**
     * Specify internal reader RECFM and corresponding http(s) headers will be appended to the request accordingly
     * "F" (fixed) or "V" (variable)
     */
    private Optional<String> internalReaderRecfm;

    /**
     * Specify internal reader LRECL and corresponding http(s) headers will be appended to the request accordingly
     * "F" (fixed) or "V" (variable)
     */
    private Optional<String> internalReaderLrecl;

    /**
     * A string for JCL symbolic substitution
     */
    private Optional<String> jclSymbols = Optional.empty();

    /**
     * SubmitJclParams constructor
     *
     * @param jcl                 syntactically correct JCL
     * @param internalReaderRecfm internal reader RECFM
     * @param internalReaderLrecl internal reader LRECL
     * @author Frank Giordano
     */
    public SubmitJclParams(String jcl, String internalReaderRecfm, String internalReaderLrecl) {
        this.jcl = Optional.ofNullable(jcl);
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
    }

    /**
     * SubmitJclParams constructor
     *
     * @param jcl                 syntactically correct JCL
     * @param internalReaderRecfm internal reader RECFM
     * @param internalReaderLrecl internal reader LRECL
     * @param jclSymbols          JCL symbolic substitution
     * @author Frank Giordano
     */
    public SubmitJclParams(String jcl, String internalReaderRecfm, String internalReaderLrecl, String jclSymbols) {
        this.jcl = Optional.ofNullable(jcl);
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    /**
     * Retrieve jcl specified
     *
     * @return jcl value
     * @author Frank Giordano
     */
    public Optional<String> getJcl() {
        return jcl;
    }

    public void setJcl(String jcl) {
        this.jcl = Optional.ofNullable(jcl);
    }

    /**
     * Retrieve internalReaderRecfm specified
     *
     * @return internalReaderRecfm value
     * @author Frank Giordano
     */
    public Optional<String> getInternalReaderRecfm() {
        return internalReaderRecfm;
    }

    /**
     * Assign internalReaderRecfm value
     *
     * @param internalReaderRecfm specify internal reader RECFM and corresponding http(s) headers will
     *                            be appended to the request accordingly "F" (fixed) or "V" (variable)
     * @author Frank Giordano
     */
    public void setInternalReaderRecfm(String internalReaderRecfm) {
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
    }

    /**
     * Retrieve internalReaderLrecl specified
     *
     * @return internalReaderLrecl value
     * @author Frank Giordano
     */
    public Optional<String> getInternalReaderLrecl() {
        return internalReaderLrecl;
    }

    /**
     * Assign internalReaderRecfm value
     *
     * @param internalReaderLrecl specify internal reader LRECL and corresponding http(s) headers will
     *                            be appended to the request accordingly "F" (fixed) or "V" (variable)
     * @author Frank Giordano
     */
    public void setInternalReaderLrecl(String internalReaderLrecl) {
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
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
     * Assign JCL symbolic substitution value
     *
     * @param jclSymbols JCL symbolic substitution
     * @author Frank Giordano
     */
    public void setJclSymbols(String jclSymbols) {
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    @Override
    public String toString() {
        return "SubmitJclParams{" +
                "jcl=" + jcl +
                ", internalReaderRecfm=" + internalReaderRecfm +
                ", internalReaderLrecl=" + internalReaderLrecl +
                ", jclSymbols=" + jclSymbols +
                '}';
    }

}
