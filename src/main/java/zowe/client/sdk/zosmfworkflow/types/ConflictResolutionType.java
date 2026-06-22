/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.types;

/**
 * Enum class representing conflict resolution strategies for workflow variables.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-start-workflow">z/OSMF REST API</a>
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public enum ConflictResolutionType {

    /**
     * Use the value from the output file when a variable conflict occurs.
     */
    OUTPUT_FILE_VALUE("outputFileValue"),

    /**
     * Keep the existing value when a variable conflict occurs.
     */
    EXISTING_VALUE("existingValue"),

    /**
     * Leave the conflict unresolved for manual resolution.
     */
    LEAVE_CONFLICT("leaveConflict");

    private final String value;

    ConflictResolutionType(final String value) {
        this.value = value;
    }

    /**
     * Returns the string value of the conflict resolution type.
     *
     * @return string value of the conflict resolution type
     */
    public String getValue() {
        return value;
    }

}
