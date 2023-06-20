/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zowe.client.sdk.zosfiles.dsn.input;

import java.util.Optional;

public class RenameParams {

    /**
     * Current name of the dataset
     */
    private final Optional<String> dataSetName;

    /**
     * New name of the dataset
     */
    private final Optional<String> afterDataSetName;

    /**
     * Current name of the member
     */
    private final Optional<String> memberName;

    /**
     * New name of the member
     */
    private final Optional<String> afterMemberName;

    private RenameParams(RenameParams.Builder builder) {
        this.dataSetName = Optional.ofNullable(builder.dataSetName);
        this.afterDataSetName = Optional.ofNullable(builder.afterDataSetName);
        this.memberName = Optional.ofNullable(builder.memberName);
        this.afterMemberName = Optional.ofNullable(builder.afterMemberName);
    }

    public Optional<String> getDataSetName() {
        return dataSetName;
    }

    public Optional<String> getAfterDataSetName() {
        return afterDataSetName;
    }

    public Optional<String> getMemberName() {
        return memberName;
    }

    public Optional<String> getAfterMemberName() {
        return afterMemberName;
    }

    @Override
    public String toString() {
        return "RenameParams{" +
                "dataSetName=" + dataSetName +
                ", afterDataSetName=" + afterDataSetName +
                ", memberName=" + memberName +
                ", afterMemberName=" + afterMemberName +
                '}';
    }

    public static class Builder {

        private String dataSetName;
        private String afterDataSetName;
        private String memberName;
        private String afterMemberName;

        public RenameParams build() {
            return new RenameParams(this);
        }

        public RenameParams.Builder dataSetName(String dataSetName) {
            this.dataSetName = dataSetName;
            return this;
        }

        public RenameParams.Builder afterDataSetName(String afterDataSetName) {
            this.afterDataSetName = afterDataSetName;
            return this;
        }

        public RenameParams.Builder memberName(String memberName) {
            this.memberName = memberName;
            return this;
        }

        public RenameParams.Builder afterMemberName(String afterMemberName) {
            this.afterMemberName = afterMemberName;
            return this;
        }

    }

}
