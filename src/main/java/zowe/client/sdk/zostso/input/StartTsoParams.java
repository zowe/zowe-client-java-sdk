/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.input;

import java.util.Optional;

/**
 * TSO start command z/OSMF parameters
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class StartTsoParams {

    /**
     * User's z/OS permission account number
     */
    public Optional<String> account = Optional.empty();

    /**
     * Character set for address space
     */
    public Optional<String> characterSet = Optional.empty();

    /**
     * Code page for tso address space
     */
    public Optional<String> codePage = Optional.empty();

    /**
     * Number of columns
     */
    public Optional<String> columns = Optional.empty();

    /**
     * Name of the logonProcedure for address space
     */
    public Optional<String> logonProcedure = Optional.empty();

    /**
     * Region size for tso address space
     */
    public Optional<String> regionSize = Optional.empty();

    /**
     * Number of rows
     */
    public Optional<String> rows = Optional.empty();

    public StartTsoParams() {
    }

    /**
     * StartTsoParams constructor
     *
     * @param logonProcedure name of the logonProcedure for address space
     * @param characterSet   character set for address space
     * @param codePage       code page for tso address space
     * @param rows           number of rows
     * @param columns        number of columns
     * @param accountNumber  user's z/OS permission account number
     * @param regionSize     region size for tso address space
     * @author Frank Giordano
     */
    public StartTsoParams(final String logonProcedure, final String characterSet, final String codePage,
                          final String rows, final String columns, final String accountNumber,
                          final String regionSize) {
        this.logonProcedure = Optional.ofNullable(logonProcedure);
        this.characterSet = Optional.ofNullable(characterSet);
        this.codePage = Optional.ofNullable(codePage);
        this.rows = Optional.ofNullable(rows);
        this.columns = Optional.ofNullable(columns);
        this.account = Optional.ofNullable(accountNumber);
        this.regionSize = Optional.ofNullable(regionSize);
    }

    /**
     * Retrieve account specified
     *
     * @return account user account value
     */
    public Optional<String> getAccount() {
        return account;
    }

    /**
     * Assign account value
     *
     * @param account user's z/OS permission account number
     */
    public void setAccount(final String account) {
        this.account = Optional.ofNullable(account);
    }

    /**
     * Retrieve characterSet specified
     *
     * @return characterSet character set value for address space
     */
    public Optional<String> getCharacterSet() {
        return characterSet;
    }

    /**
     * Assign characterSet value
     *
     * @param characterSet character set for address space
     */
    public void setCharacterSet(final String characterSet) {
        this.characterSet = Optional.of(characterSet);
    }

    /**
     * Retrieve codePage specified
     *
     * @return codePage name of the logonProcedure for address space
     */
    public Optional<String> getCodePage() {
        return codePage;
    }

    /**
     * Assign codePage value
     *
     * @param codePage code page for tso address space
     */
    public void setCodePage(final String codePage) {
        this.codePage = Optional.ofNullable(codePage);
    }

    /**
     * Retrieve columns specified
     *
     * @return columns number value of columns
     */
    public Optional<String> getColumns() {
        return columns;
    }

    /**
     * Assign columns value
     *
     * @param columns number of columns
     */
    public void setColumns(final String columns) {
        this.columns = Optional.ofNullable(columns);
    }

    /**
     * Retrieve logonProcedure specified
     *
     * @return logonProcedure name value of the logonProcedure for address space
     */
    public Optional<String> getLogonProcedure() {
        return logonProcedure;
    }

    /**
     * Assign logonProcedure value
     *
     * @param logonProcedure name of the logonProcedure for address space
     */
    public void setLogonProcedure(final String logonProcedure) {
        this.logonProcedure = Optional.of(logonProcedure);
    }

    /**
     * Retrieve regionSize specified
     *
     * @return regionSize region size value for tso address space
     */
    public Optional<String> getRegionSize() {
        return regionSize;
    }

    /**
     * Assign regionSize value
     *
     * @param regionSize region size for tso address space
     */
    public void setRegionSize(final String regionSize) {
        this.regionSize = Optional.of(regionSize);
    }

    /**
     * Retrieve rows specified
     *
     * @return rows number value of rows
     */
    public Optional<String> getRows() {
        return rows;
    }

    /**
     * Assign rows value
     *
     * @param rows number of rows
     */
    public void setRows(String rows) {
        this.rows = Optional.ofNullable(rows);
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
