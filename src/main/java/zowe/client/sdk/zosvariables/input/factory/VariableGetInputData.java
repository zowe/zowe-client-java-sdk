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

import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosvariables.type.VariableType;

import java.util.List;
import java.util.Optional;

/**
 * Parameter container class for z/OS system variables GET operation.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-system-variables">z/OSMF REST API</a>
 *
 * @author Adithe Das
 * @version 7.0
 */
public class VariableGetInputData {

    /**
     * Required sysplex name when local is false.
     */
    private final String sysplexName;

    /**
     * Required system name when local is false.
     */
    private final String systemName;

    /**
     * Indicates whether to retrieve variables from the local system.
     */
    private final boolean local;

    /**
     * Optional list of variable names.
     */
    private final List<String> variableNames;

    /**
     * Optional variable type filter.
     */
    private final VariableType variableType;

    /**
     * VariableGetInputData constructor.
     * <p>
     * This constructor is package-private.
     *
     * @param builder VariableGetInputData.Builder builder
     */
    VariableGetInputData(final Builder builder) {
        this.local = builder.local;
        if (!builder.local) {
            ValidateUtils.checkIllegalParameter(builder.sysplexName, "sysplexName");
            ValidateUtils.checkIllegalParameter(builder.systemName, "systemName");
        }
        this.sysplexName = builder.sysplexName;
        this.systemName = builder.systemName;
        this.variableNames = builder.variableNames == null ? null : List.copyOf(builder.variableNames);
        this.variableType = builder.variableType;
    }

    /**
     * Returns the target sysplex name.
     *
     * @return sysplex name
     */
    public Optional<String> getSysplexName() {
        return Optional.ofNullable(sysplexName);
    }

    /**
     * Returns the target z/OS system name.
     *
     * @return system name
     */
    public Optional<String> getSystemName() {
        return Optional.ofNullable(systemName);
    }

    /**
     * Determine whether the local system is requested.
     *
     * @return true if retrieving variables from the local system; false otherwise
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * Returns the names of the requested variables, if specified.
     *
     * @return optional list of variable names
     */
    public Optional<List<String>> getVariableNames() {
        return Optional.ofNullable(variableNames);
    }

    /**
     * Returns the requested variable type.
     *
     * @return optional variable type
     */
    public Optional<VariableType> getVariableType() {
        return Optional.ofNullable(variableType);
    }

    /**
     * Builder class for VariableGetInputData.
     */
    static final class Builder {

        /**
         * Sysplex name.
         */
        private String sysplexName;

        /**
         * System name.
         */
        private String systemName;

        /**
         * Indicates whether the local system is requested.
         */
        private boolean local;

        /**
         * Variable names.
         */
        private List<String> variableNames;

        /**
         * Variable type.
         */
        private VariableType variableType;

        /**
         * Set sysplex name.
         *
         * @param sysplexName sysplex name
         * @return VariableGetInputData.Builder
         */
        Builder setSysplexName(final String sysplexName) {
            this.sysplexName = sysplexName;
            return this;
        }

        /**
         * Set the system name.
         *
         * @param systemName system name
         * @return VariableGetInputData.Builder
         */
        Builder setSystemName(final String systemName) {
            this.systemName = systemName;
            return this;
        }

        /**
         * Set a local system flag.
         *
         * @param local true if retrieving variables from the local system
         * @return VariableGetInputData.Builder
         */
        Builder setLocal(final boolean local) {
            this.local = local;
            return this;
        }

        /**
         * Set variable names.
         *
         * @param variableNames variable names
         * @return VariableGetInputData.Builder
         */
        Builder setVariableNames(final List<String> variableNames) {
            this.variableNames = variableNames;
            return this;
        }

        /**
         * Set a variable type.
         *
         * @param variableType variable type
         * @return VariableGetInputData.Builder
         */
        Builder setVariableType(final VariableType variableType) {
            this.variableType = variableType;
            return this;
        }

        /**
         * Build VariableGetInputData object.
         *
         * @return VariableGetInputData
         */
        VariableGetInputData build() {
            return new VariableGetInputData(this);
        }
    }

}
