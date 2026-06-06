/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.input;

import zowe.client.sdk.utility.ValidateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Input data for z/OSMF get system variables processing.
 */
public final class SystemVariablesInputData {

    private final boolean local;
    private final String sysplexName;
    private final String systemName;
    private final SystemVariableSource source;
    private final List<String> variableNames;

    /**
     * Create input data for the local system.
     */
    public SystemVariablesInputData() {
        this(true, null, null, null, Collections.emptyList());
    }

    /**
     * Create input data for a named sysplex and system.
     *
     * @param sysplexName sysplex name
     * @param systemName  system name
     */
    public SystemVariablesInputData(final String sysplexName, final String systemName) {
        ValidateUtils.checkIllegalParameter(sysplexName, "sysplexName");
        ValidateUtils.checkIllegalParameter(systemName, "systemName");
        this.local = false;
        this.sysplexName = sysplexName;
        this.systemName = systemName;
        this.source = null;
        this.variableNames = Collections.emptyList();
    }

    private SystemVariablesInputData(final boolean local,
                                     final String sysplexName,
                                     final String systemName,
                                     final SystemVariableSource source,
                                     final List<String> variableNames) {
        this.local = local;
        this.sysplexName = sysplexName;
        this.systemName = systemName;
        this.source = source;
        this.variableNames = Collections.unmodifiableList(variableNames);
    }

    /**
     * Return a new input object with the source value specified.
     *
     * @param source source value
     * @return new SystemVariablesInputData object
     */
    public SystemVariablesInputData withSource(final SystemVariableSource source) {
        ValidateUtils.checkNullParameter(source, "source");
        return new SystemVariablesInputData(local, sysplexName, systemName, source, variableNames);
    }

    /**
     * Return a new input object with variable names specified.
     *
     * @param variableNames variable or symbol names
     * @return new SystemVariablesInputData object
     */
    public SystemVariablesInputData withVariableNames(final String... variableNames) {
        ValidateUtils.checkNullParameter(variableNames, "variableNames");

        final List<String> names = new ArrayList<>();
        for (String variableName : variableNames) {
            ValidateUtils.checkIllegalParameter(variableName, "variableName");
            names.add(variableName);
        }

        return new SystemVariablesInputData(local, sysplexName, systemName, source, names);
    }

    /**
     * Return true when local system should be used.
     *
     * @return true for local system
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * Retrieve sysplex name.
     *
     * @return sysplex name
     */
    public String getSysplexName() {
        return sysplexName;
    }

    /**
     * Retrieve system name.
     *
     * @return system name
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * Retrieve source.
     *
     * @return source
     */
    public SystemVariableSource getSource() {
        return source;
    }

    /**
     * Retrieve variable names.
     *
     * @return variable names
     */
    public List<String> getVariableNames() {
        return variableNames;
    }

}