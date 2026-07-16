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
 * Enum class representing the status of a workflow.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-list-workflows-system-sysplex">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public enum StatusNameType {

    /**
     * The workflow is in progress.
     */
    IN_PROGRESS("in-progress"),

    /**
     * The workflow is complete.
     */
    COMPLETE("complete"),

    /**
     * The workflow automation is in progress.
     */
    AUTOMATION_IN_PROGRESS("automation-in-progress"),

    /**
     * The workflow is canceled.
     */
    CANCELED("canceled");

    private final String value;

    StatusNameType(final String value) {
        this.value = value;
    }

    /**
     * Returns the string value of the status name type.
     *
     * @return string value of the status name type
     */
    public String getValue() {
        return value;
    }

}
