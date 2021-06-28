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

public class SubmitJclParms {

    private Optional<String> jcl;
    private Optional<String> internalReaderRecfm;
    private Optional<String> internalReaderLrecl;
    private Optional<String> jclSymbols = Optional.empty();

    public SubmitJclParms(String jcl, String internalReaderRecfm, String internalReaderLrecl) {
        this.jcl = Optional.ofNullable(jcl);
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
    }

    public SubmitJclParms(String jcl, String internalReaderRecfm, String internalReaderLrecl, String jclSymbols) {
        this.jcl = Optional.ofNullable(jcl);
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    public Optional<String> getJcl() {
        return jcl;
    }

    public void setJcl(String jcl) {
        this.jcl = Optional.ofNullable(jcl);;
    }

    public Optional<String> getInternalReaderRecfm() {
        return internalReaderRecfm;
    }

    public void setInternalReaderRecfm(String internalReaderRecfm) {
        this.internalReaderRecfm = Optional.ofNullable(internalReaderRecfm);;
    }

    public Optional<String> getInternalReaderLrecl() {
        return internalReaderLrecl;
    }

    public void setInternalReaderLrecl(String internalReaderLrecl) {
        this.internalReaderLrecl = Optional.ofNullable(internalReaderLrecl);
    }

    public Optional<String> getJclSymbols() {
        return jclSymbols;
    }

    public void setJclSymbols(String jclSymbols) {
        this.jclSymbols = Optional.ofNullable(jclSymbols);
    }

    @Override
    public String toString() {
        return "SubmitJclParms{" +
                "jcl=" + jcl +
                ", internalReaderRecfm=" + internalReaderRecfm +
                ", internalReaderLrecl=" + internalReaderLrecl +
                ", jclSymbols=" + jclSymbols +
                '}';
    }

}
