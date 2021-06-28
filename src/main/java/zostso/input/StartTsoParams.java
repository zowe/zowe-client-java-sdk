/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso.input;

import java.util.Optional;

public class StartTsoParams {

    public Optional<String> logonProcedure = Optional.empty();
    public Optional<String> characterSet = Optional.empty();
    public Optional<String> codePage = Optional.empty();
    public Optional<String> rows = Optional.empty();
    public Optional<String> columns = Optional.empty();
    public Optional<String> account = Optional.empty();
    public Optional<String> regionSize = Optional.empty();

    public Optional<String> getLogonProcedure() {
        return logonProcedure;
    }

    public void setLogonProcedure(String logonProcedure) {
        this.logonProcedure = Optional.of(logonProcedure);
    }

    public Optional<String> getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = Optional.of(characterSet);
    }

    public Optional<String> getCodePage() {
        return codePage;
    }

    public void setCodePage(String codePage) {
        this.codePage = Optional.of(codePage);;
    }

    public Optional<String> getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = Optional.of(rows);;
    }

    public Optional<String> getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = Optional.of(columns);;
    }

    public Optional<String> getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = Optional.of(account);;
    }

    public Optional<String> getRegionSize() {
        return regionSize;
    }

    public void setRegionSize(String regionSize) {
        this.regionSize = Optional.of(regionSize);;
    }

    @Override
    public String toString() {
        return "StartTsoParams{" +
                "logonProcedure=" + logonProcedure +
                ", characterSet=" + characterSet +
                ", codePage=" + codePage +
                ", rows=" + rows +
                ", columns=" + columns +
                ", account=" + account +
                ", regionSize=" + regionSize +
                '}';
    }

}
