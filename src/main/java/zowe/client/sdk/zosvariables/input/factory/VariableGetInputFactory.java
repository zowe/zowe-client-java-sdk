/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.input.factory;

import zowe.client.sdk.zosvariables.type.VariableType;

import java.util.List;

/**
 * Factory class for creating VariableGetInputData instances.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-system-variables">z/OSMF REST API</a>
 *
 * @author Adithe Das
 * @version 7.0
 */
public final class VariableGetInputFactory {

    /**
     * Private constructor defined to avoid instantiation of a static factory class.
     */
    private VariableGetInputFactory() {
        throw new IllegalStateException("Factory class");
    }

    /**
     * Create input for z/OS variables.
     *
     * @param sysplexName name of the target sysplex
     * @param systemName  name of the target z/OS system
     * @return VariableGetInputData
     */
    public static VariableGetInputData createZosVariable(final String sysplexName,
                                                         final String systemName) {
        return new VariableGetInputData.Builder()
                .setSysplexName(sysplexName)
                .setSystemName(systemName)
                .setVariableType(VariableType.VARIABLE)
                .setLocal(false)
                .build();
    }

    /**
     * Create input for z/OS variables with filters.
     *
     * @param sysplexName   name of the target sysplex
     * @param systemName    name of the target z/OS system
     * @param variableNames names of the variables to retrieve; if {@code null} or empty,
     *                      all variables are returned
     * @return VariableGetInputData
     */
    public static VariableGetInputData createZosVariable(final String sysplexName,
                                                         final String systemName,
                                                         final List<String> variableNames) {
        return new VariableGetInputData.Builder()
                .setSysplexName(sysplexName)
                .setSystemName(systemName)
                .setVariableNames(variableNames)
                .setVariableType(VariableType.VARIABLE)
                .setLocal(false)
                .build();
    }

    /**
     * Create input for z/OSMF system symbols.
     *
     * @param sysplexName name of the target sysplex (local sysplex only)
     * @param systemName  name of the target z/OS system
     * @return VariableGetInputData
     */
    public static VariableGetInputData createZosmfSymbol(final String sysplexName,
                                                         final String systemName) {
        return new VariableGetInputData.Builder()
                .setSysplexName(sysplexName)
                .setSystemName(systemName)
                .setVariableType(VariableType.SYMBOL)
                .setLocal(false)
                .build();
    }

    /**
     * Create input for z/OSMF system symbols with filters.
     *
     * @param sysplexName   name of the target sysplex (local sysplex only)
     * @param systemName    name of the target z/OS system
     * @param variableNames names of the variables to retrieve; if {@code null} or empty,
     *                      all variables are returned
     * @return VariableGetInputData
     */
    public static VariableGetInputData createZosmfSymbol(final String sysplexName,
                                                         final String systemName,
                                                         final List<String> variableNames) {
        return new VariableGetInputData.Builder()
                .setSysplexName(sysplexName)
                .setSystemName(systemName)
                .setVariableNames(variableNames)
                .setVariableType(VariableType.SYMBOL)
                .setLocal(false)
                .build();
    }

    /**
     * Create input for local z/OS variables.
     *
     * @return VariableGetInputData
     */
    public static VariableGetInputData createZosVariableLocal() {
        return new VariableGetInputData.Builder()
                .setVariableType(VariableType.VARIABLE)
                .setLocal(true)
                .build();
    }

    /**
     * Create local z/OS variable input with filters.
     *
     * @param variableNames names of the variables to retrieve; if {@code null} or empty,
     *                      all variables are returned
     * @return VariableGetInputData
     */
    public static VariableGetInputData createZosVariableLocal(final List<String> variableNames) {
        return new VariableGetInputData.Builder()
                .setVariableNames(variableNames)
                .setVariableType(VariableType.VARIABLE)
                .setLocal(true)
                .build();
    }

    /**
     * Create input for local z/OSMF system symbols.
     *
     * @return VariableGetInputData
     */
    public static VariableGetInputData createZosmfSymbolLocal() {
        return new VariableGetInputData.Builder()
                .setVariableType(VariableType.SYMBOL)
                .setLocal(true)
                .build();
    }

    /**
     * Create input for local z/OSMF system symbols with filters.
     *
     * @param variableNames names of the variables to retrieve; if {@code null} or empty,
     *                      all variables are returned
     * @return VariableGetInputData
     */
    public static VariableGetInputData createZosmfSymbolLocal(final List<String> variableNames) {
        return new VariableGetInputData.Builder()
                .setVariableNames(variableNames)
                .setVariableType(VariableType.SYMBOL)
                .setLocal(true)
                .build();
    }

}
