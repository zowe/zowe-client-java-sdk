/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.input;

/**
 * Parameters for the z/OSMF list archived workflows API input data.
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class WorkflowListArchivedInputData {

    /**
     * Sort order for archived workflow instances.
     */
    private final String orderBy;

    /**
     * View type for archived workflow instances.
     */
    private final String view;

    /**
     * WorkflowListArchivedInputData constructor.
     *
     * @param builder builder instance
     */
    private WorkflowListArchivedInputData(final Builder builder) {
        this.orderBy = builder.orderBy;
        this.view = builder.view;
    }

    /**
     * Retrieve orderBy value.
     *
     * @return orderBy value
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Retrieve view value.
     *
     * @return view value
     */
    public String getView() {
        return view;
    }

    /**
     * Create a new builder for workflow list archived input data.
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Return string value representing WorkflowListArchivedInputData object.
     *
     * @return string representation of WorkflowListArchivedInputData
     */
    @Override
    public String toString() {
        return "WorkflowListArchivedInputData{" +
                "orderBy='" + orderBy + '\'' +
                ", view='" + view + '\'' +
                '}';
    }

    /**
     * Builder for workflow list archived input data.
     */
    public static final class Builder {

        private String orderBy;
        private String view;

        private Builder() {
        }

        /**
         * Set the order by value.
         *
         * @param orderBy order by value
         * @return this builder instance
         */
        public Builder orderBy(final String orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        /**
         * Set the view value.
         *
         * @param view view value
         * @return this builder instance
         */
        public Builder view(final String view) {
            this.view = view;
            return this;
        }

        /**
         * Build WorkflowListArchivedInputData instance.
         *
         * @return WorkflowListArchivedInputData instance
         */
        public WorkflowListArchivedInputData build() {
            return new WorkflowListArchivedInputData(this);
        }

    }

}