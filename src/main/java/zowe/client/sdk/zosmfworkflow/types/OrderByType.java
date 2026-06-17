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
 * Enum class representing the sort order for archived workflow instances.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-list-archived-workflows-system">z/OSMF REST API</a>
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public enum OrderByType {

    /**
     * Sort from newest to oldest.
     */
    DESC("desc"),

    /**
     * Sort from oldest to newest.
     */
    ASC("asc");

    private final String value;

    OrderByType(final String value) {
        this.value = value;
    }

    /**
     * Returns the string value of the order by type.
     *
     * @return string value of the order by type
     */
    public String getValue() {
        return value;
    }

}
