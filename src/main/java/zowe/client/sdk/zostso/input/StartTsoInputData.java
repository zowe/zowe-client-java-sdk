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
 * @version 4.0
 */
public class StartTsoInputData {

    /**
     * User's z/OS permission account number
     */
    public String account;

    /**
     * Character set for address space
     */
    public String characterSet;

    /**
     * Code page for tso address space
     */
    public String codePage;

    /**
     * Number of columns
     */
    public String columns;

    /**
     * Name of the logonProcedure for address space
     */
    public String logonProcedure;

    /**
     * Region size for tso address space
     */
    public String regionSize;

    /**
     * Number of rows
     */
    public String rows;

    /**
     * StartTsoInputData default constructor
     */
    public StartTsoInputData() {
    }

    /**
     * StartTsoInputData constructor
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
    public StartTsoInputData(final String logonProcedure,
                             final String characterSet,
                             final String codePage,
                             final String rows,
                             final String columns,
                             final String accountNumber,
                             final String regionSize) {
        this.logonProcedure = logonProcedure;
        this.characterSet = characterSet;
        this.codePage = codePage;
        this.rows = rows;
        this.columns = columns;
        this.account = accountNumber;
        this.regionSize = regionSize;
    }

    /**
     * Retrieve an account specified
     *
     * @return account user account value
     */
    public Optional<String> getAccount() {
        return Optional.ofNullable(account);
    }

    /**
     * Assign account value
     *
     * @param account user's z/OS permission account number
     */
    public void setAccount(final String account) {
        this.account = account;
    }

    /**
     * Retrieve characterSet specified
     *
     * @return characterSet character set value for address space
     */
    public Optional<String> getCharacterSet() {
        return Optional.ofNullable(characterSet);
    }

    /**
     * Assign characterSet value
     *
     * @param characterSet character set for address space
     */
    public void setCharacterSet(final String characterSet) {
        this.characterSet = characterSet;
    }

    /**
     * Retrieve codePage specified
     *
     * @return codePage name of the logonProcedure for address space
     */
    public Optional<String> getCodePage() {
        return Optional.ofNullable(codePage);
    }

    /**
     * Assign codePage value
     *
     * @param codePage code page for tso address space
     */
    public void setCodePage(final String codePage) {
        this.codePage = codePage;
    }

    /**
     * Retrieve columns specified
     *
     * @return columns number value of columns
     */
    public Optional<String> getColumns() {
        return Optional.ofNullable(columns);
    }

    /**
     * Assign columns value
     *
     * @param columns number of columns
     */
    public void setColumns(final String columns) {
        this.columns = columns;
    }

    /**
     * Retrieve logonProcedure specified
     *
     * @return logonProcedure name value of the logonProcedure for address space
     */
    public Optional<String> getLogonProcedure() {
        return Optional.ofNullable(logonProcedure);
    }

    /**
     * Assign logonProcedure value
     *
     * @param logonProcedure name of the logonProcedure for address space
     */
    public void setLogonProcedure(final String logonProcedure) {
        this.logonProcedure = logonProcedure;
    }

    /**
     * Retrieve regionSize specified
     *
     * @return regionSize region size value for tso address space
     */
    public Optional<String> getRegionSize() {
        return Optional.ofNullable(regionSize);
    }

    /**
     * Assign regionSize value
     *
     * @param regionSize region size for tso address space
     */
    public void setRegionSize(final String regionSize) {
        this.regionSize = regionSize;
    }

    /**
     * Retrieve rows specified
     *
     * @return rows number value of rows
     */
    public Optional<String> getRows() {
        return Optional.ofNullable(rows);
    }

    /**
     * Assign rows value
     *
     * @param rows number of rows
     */
    public void setRows(String rows) {
        this.rows = rows;
    }

    /**
     * Return string value representing StartTsoInputData object
     *
     * @return string representation of StartTsoInputData
     */
    @Override
    public String toString() {
        return "StartTsoInputData{" +
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
