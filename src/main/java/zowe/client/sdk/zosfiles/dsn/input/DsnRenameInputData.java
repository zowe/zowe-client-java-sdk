/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.input;

import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.dsn.types.RenameType;

/**
 * Immutable input for dataset rename operations.
 * <p>
 * Supports renaming either a dataset or a member within a partitioned dataset (PDS).
 *
 * @author Frank Giordano
 * @version 7.0
 */
public final class DsnRenameInputData {

    private final RenameType type;
    private final String sourceDatasetName;
    private final String destinationDatasetName;
    private final String sourceMemberName;
    private final String destinationMemberName;

    /**
     * Private constructor.
     */
    private DsnRenameInputData(
            final RenameType type,
            final String sourceDatasetName,
            final String destinationDatasetName,
            final String sourceMemberName,
            final String destinationMemberName) {

        this.type = type;
        this.sourceDatasetName = sourceDatasetName;
        this.destinationDatasetName = destinationDatasetName;
        this.sourceMemberName = sourceMemberName;
        this.destinationMemberName = destinationMemberName;
    }

    /**
     * Creates input for a dataset rename operation.
     *
     * @param sourceDatasetName      existing dataset name
     * @param destinationDatasetName new dataset name
     * @return RenameInputData
     */
    public static DsnRenameInputData forDataset(
            final String sourceDatasetName,
            final String destinationDatasetName) {

        ValidateUtils.checkIllegalParameter(sourceDatasetName, "sourceDatasetName");
        ValidateUtils.checkIllegalParameter(destinationDatasetName, "destinationDatasetName");

        return new DsnRenameInputData(
                RenameType.DATASET,
                sourceDatasetName,
                destinationDatasetName,
                null,
                null);
    }

    /**
     * Creates input for a member rename operation.
     *
     * @param datasetName           partitioned dataset containing the member
     * @param sourceMemberName      existing member name
     * @param destinationMemberName new member name
     * @return RenameInputData
     */
    public static DsnRenameInputData forMember(
            final String datasetName,
            final String sourceMemberName,
            final String destinationMemberName) {

        ValidateUtils.checkIllegalParameter(datasetName, "datasetName");
        ValidateUtils.checkIllegalParameter(sourceMemberName, "sourceMemberName");
        ValidateUtils.checkIllegalParameter(destinationMemberName, "destinationMemberName");

        return new DsnRenameInputData(
                RenameType.MEMBER,
                datasetName,
                null,
                sourceMemberName,
                destinationMemberName);
    }

    /**
     * Returns the rename operation type.
     *
     * @return rename type
     */
    public RenameType getType() {
        return type;
    }

    /**
     * Returns the source dataset name.
     *
     * @return source dataset name
     */
    public String getSourceDatasetName() {
        return sourceDatasetName;
    }

    /**
     * Returns the destination dataset name.
     * <p>
     * This value is only applicable for dataset rename operations.
     *
     * @return destination dataset name or {@code null}
     */
    public String getDestinationDatasetName() {
        return destinationDatasetName;
    }

    /**
     * Returns the source member name.
     * <p>
     * This value is only applicable for member rename operations.
     *
     * @return source member name or {@code null}
     */
    public String getSourceMemberName() {
        return sourceMemberName;
    }

    /**
     * Returns the destination member name.
     * <p>
     * This value is only applicable for member rename operations.
     *
     * @return destination member name or {@code null}
     */
    public String getDestinationMemberName() {
        return destinationMemberName;
    }

}
