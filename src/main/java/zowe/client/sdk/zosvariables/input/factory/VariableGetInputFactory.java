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
     * Create input data for retrieving variables from a specified system.
     *
     * @param sysplexName sysplex name
     * @param systemName system name
     * @return VariableGetInputData
     */
    public static VariableGetInputData createSystemInput(final String sysplexName, final String systemName) {
        return new VariableGetInputData.Builder().setSysplexName(sysplexName).setSystemName(systemName).setLocal(false).build();
    }

    /**
     * Create input data for retrieving variables from a specified system with optional filters.
     *
     * @param sysplexName sysplex name
     * @param systemName system name
     * @param variableNames variable names to retrieve
     * @param variableType variable type filter
     * @return VariableGetInputData
     */
    public static VariableGetInputData createSystemInput(final String sysplexName, final String systemName, final List<String> variableNames, final String variableType) {
        return new VariableGetInputData.Builder().setSysplexName(sysplexName).setSystemName(systemName).setVariableNames(variableNames).setVariableType(variableType).setLocal(false).build();
    }

    /**
     * Create input data for retrieving variables from the local system.
     *
     * @return VariableGetInputData
     */
    public static VariableGetInputData createLocalInput() {
        return new VariableGetInputData.Builder().setLocal(true).build();
    }

    /**
     * Create input data for retrieving variables from the local system with optional filters.
     *
     * @param variableNames variable names to retrieve
     * @param variableType variable type filter
     * @return VariableGetInputData
     */
    public static VariableGetInputData createLocalInput(final List<String> variableNames, final String variableType) {
        return new VariableGetInputData.Builder().setLocal(true).setVariableNames(variableNames).setVariableType(variableType).build();
    }

}
