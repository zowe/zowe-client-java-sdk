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

import java.util.Map;
import java.util.Optional;

/**
 * Submit jcl parameters
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SubmitJclParams {

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
     * For example, this accepts a Map of key/value pairs: {"SYM","SYM"}, {"SYM2","SYM2"}, etc..
     */
    private Optional<Map<String,String>> jclSymbols = Optional.empty();

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
     * @param jclSymbols          Map of JCL symbolic substitution
     * @author Frank Giordano
     */
    public SubmitJclParams(String jcl, String internalReaderRecfm, String internalReaderLrecl,
                           Map<String,String> jclSymbols) {
        this.jcl = Optional.ofNullable(jcl);
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
        this.jclSymbols = Optional.ofNullable(jclSymbols);
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
     * Assign internalReaderLrecl value
     *
     * @param internalReaderLrecl specify internal reader LRECL and corresponding http(s) headers will
     *                            be appended to the request accordingly
     *                            An integer value that specifies the internal reader logical record length (LRECL).
     * @author Frank Giordano
     */
    public void setInternalReaderLrecl(String internalReaderLrecl) {
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
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
     *                            be appended to the request accordingly
     *                            A single character that specifies the internal reader record format: F or V.
     * @author Frank Giordano
     */
    public void setInternalReaderRecfm(String internalReaderRecfm) {
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
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
     * Retrieve jclSymbols specified
     *
     * @return jclSymbols value
     * @author Frank Giordano
     */
    public Optional<Map<String,String>> getJclSymbols() {
        return jclSymbols;
    }

    /**
     * Assign JCL symbolic substitution value
     *
     * @param jclSymbols Map of JCL symbolic substitution
     * @author Frank Giordano
     */
    public void setJclSymbols(Map<String,String> jclSymbols) {
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
