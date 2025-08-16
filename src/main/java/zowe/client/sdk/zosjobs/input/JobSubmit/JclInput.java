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
 * Submit jcl parameters
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JclInput {

    /**
     * JCL string to submit which should contain syntactically correct JCL.
     * Example:
     * "//IEFBR14 JOB ()" +
     * "//RUN     EXEC PGM=IEFBR14"
     */
    private Optional<String> jcl;

    /**
     * Specify internal reader RECFM and corresponding http(s) headers will be appended to the request accordingly
     * A single character that specifies the internal reader record format: F or V.
     * If you omit this header or specify a value other than F or V, the default of F is used.
     */
    private Optional<String> internalReaderRecfm;

    /**
     * Specify internal reader LRECL and corresponding http(s) headers will be appended to the request accordingly
     * An integer value that specifies the internal reader logical record length (LRECL).
     * If you omit this header or specify a non-integer value, the default of 80 is used.
     */
    private Optional<String> internalReaderLrecl;

    /**
     * A Map for JCL symbolic substitution
     * <p>
     * For example, this accepts a Map of key/value pairs: {"SYM","SYM"}, {"SYM2","\"SYM 2\""}, etc...
     * Values with spaces should be enclosed in double quotes.
     */
    private Optional<Map<String, String>> jclSymbols = Optional.empty();

    /**
     * SubmitJclParams constructor
     *
     * @param jcl                 syntactically correct JCL
     * @param internalReaderRecfm internal reader RECFM
     * @param internalReaderLrecl internal reader LRECL
     * @author Frank Giordano
     */
    public JclInput(final String jcl, final String internalReaderRecfm, final String internalReaderLrecl) {
        this.jcl = Optional.ofNullable(jcl);
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
    }

    /**
     * SubmitJclParams constructor with Jcl Symbols
     *
     * @param jcl                 syntactically correct JCL
     * @param internalReaderRecfm internal reader RECFM
     * @param internalReaderLrecl internal reader LRECL
     * @param jclSymbols          Map of JCL symbolic substitution
     * @author Frank Giordano
     */
    public JclInput(final String jcl, final String internalReaderRecfm, final String internalReaderLrecl,
                    final Map<String, String> jclSymbols) {
        this.jcl = Optional.ofNullable(jcl);
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    /**
     * Retrieve internalReaderLrecl specified
     *
     * @return internalReaderLrecl value
     */
    public Optional<String> getInternalReaderLrecl() {
        return internalReaderLrecl;
    }

    /**
     * Assign internalReaderLrecl value
     *
     * @param internalReaderLrecl specify internal reader LRECL and corresponding http(s) headers will
     *                            be appended to the request accordingly
     *                            An integer value that specifies the internal reader logical record length (LRECL).
     */
    public void setInternalReaderLrecl(final String internalReaderLrecl) {
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
    }

    /**
     * Retrieve internalReaderRecfm specified
     *
     * @return internalReaderRecfm value
     */
    public Optional<String> getInternalReaderRecfm() {
        return internalReaderRecfm;
    }

    /**
     * Assign internalReaderRecfm value
     *
     * @param internalReaderRecfm specify internal reader RECFM and corresponding http(s) headers will
     *                            be appended to the request accordingly
     *                            A single character that specifies the internal reader record format: F or V.
     */
    public void setInternalReaderRecfm(final String internalReaderRecfm) {
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
    }

    /**
     * Retrieve jcl specified
     *
     * @return jcl value
     */
    public Optional<String> getJcl() {
        return jcl;
    }

    /**
     * set the jcl value
     *
     * @param jcl string representing jcl content
     */
    public void setJcl(final String jcl) {
        this.jcl = Optional.ofNullable(jcl);
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
     * Assign JCL symbolic substitution value
     *
     * @param jclSymbols Map of JCL symbolic substitution
     */
    public void setJclSymbols(final Map<String, String> jclSymbols) {
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    /**
     * Return string value representing SubmitJclParams object
     *
     * @return string representation of SubmitJclParams
     */
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
