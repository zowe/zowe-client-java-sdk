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
     * @param sysplexName sysplex name
     * @param systemName system name
     * @return VariableGetInputData
     */
    public static VariableGetInputData createGetInputForZosVariable(final String sysplexName, final String systemName) {
        return new VariableGetInputData.Builder()
                .setSysplexName(sysplexName)
                .setSystemName(systemName)
                .setVariableType(VariableType.VARIABLE)
                .setLocal(false).build();
    }

    /**
     * Create input for z/OS variables with filters.
     *
     * @param sysplexName sysplex name
     * @param systemName system name
     * @param variableNames variable names
     * @return VariableGetInputData
     */
    public static VariableGetInputData createGetInputForZosVariable(final String sysplexName, final String systemName, final List<String> variableNames) {
        return new VariableGetInputData.Builder()
                .setSysplexName(sysplexName)
                .setSystemName(systemName)
                .setVariableNames(variableNames)
                .setVariableType(VariableType.VARIABLE)
                .setLocal(false).build();
    }

    /**
     * Create input for z/OSMF symbols.
     *
     * @param sysplexName sysplex name
     * @param systemName system name
     * @return VariableGetInputData
     */
    public static VariableGetInputData createGetInputForZosmfSymbol(final String sysplexName, final String systemName) {
        return new VariableGetInputData.Builder()
                .setSysplexName(sysplexName)
                .setSystemName(systemName)
                .setVariableType(VariableType.SYMBOL)
                .setLocal(false).build();
    }

    /**
     * Create input for z/OSMF symbols with filters.
     *
     * @param sysplexName sysplex name
     * @param systemName system name
     * @param variableNames variable names
     * @return VariableGetInputData
     */
    public static VariableGetInputData createGetInputForZosmfSymbol(final String sysplexName, final String systemName, final List<String> variableNames) {
        return new VariableGetInputData.Builder()
                .setSysplexName(sysplexName)
                .setSystemName(systemName)
                .setVariableNames(variableNames)
                .setVariableType(VariableType.SYMBOL)
                .setLocal(false).build();
    }

    /**
     * Create local z/OS variable input.
     *
     * @return VariableGetInputData
     */
    public static VariableGetInputData createGetInputForZosVariableLocal() {
        return new VariableGetInputData.Builder()
                .setVariableType(VariableType.VARIABLE)
                .setLocal(true).build();
    }

    /**
     * Create local z/OS variable input with filters.
     *
     * @param variableNames variable names
     * @return VariableGetInputData
     */
    public static VariableGetInputData createGetInputForZosVariableLocal(final List<String> variableNames) {
        return new VariableGetInputData.Builder()
                .setVariableNames(variableNames)
                .setVariableType(VariableType.VARIABLE)
                .setLocal(true).build();
    }

    /**
     * Create local z/OSMF symbol input.
     *
     * @return VariableGetInputData
     */
    public static VariableGetInputData createGetInputForZosmfSymbolLocal() {
        return new VariableGetInputData.Builder()
                .setVariableType(VariableType.SYMBOL)
                .setLocal(true).build();
    }

    /**
     * Create local z/OSMF symbol input with filters.
     *
     * @param variableNames variable names
     * @return VariableGetInputData
     */
    public static VariableGetInputData createGetInputForZosmfSymbolLocal(final List<String> variableNames) {
        return new VariableGetInputData.Builder()
                .setVariableNames(variableNames)
                .setVariableType(VariableType.SYMBOL)
                .setLocal(true).build();
    }

}
