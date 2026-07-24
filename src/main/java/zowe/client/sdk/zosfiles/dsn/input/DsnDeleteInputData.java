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
import zowe.client.sdk.zosfiles.dsn.types.DeleteType;

/**
 * Immutable input for dataset delete operations.
 * <p>
 * Supports deleting a cataloged dataset, a member within a partitioned dataset (PDS),
 * or an uncataloged dataset residing on a specific volume.
 *
 * @author Jorge Samaniego
 * @version 7.0
 */
public final class DsnDeleteInputData {

    private final DeleteType type;
    private final String datasetName;
    private final String memberName;
    private final String volume;

    /**
     * Private constructor.
     */
    private DsnDeleteInputData(
            final DeleteType type,
            final String datasetName,
            final String memberName,
            final String volume) {

        this.type = type;
        this.datasetName = datasetName;
        this.memberName = memberName;
        this.volume = volume;
    }

    /**
     * Creates input for a dataset delete operation.
     *
     * @param datasetName name of a dataset (e.g. 'DATASET.LIB')
     * @return DsnDeleteInputData
     */
    public static DsnDeleteInputData forDataset(final String datasetName) {
        ValidateUtils.checkIllegalParameter(datasetName, "datasetName");

        return new DsnDeleteInputData(
                DeleteType.DATASET,
                datasetName,
                null,
                null);
    }

    /**
     * Creates input for a member delete operation.
     *
     * @param datasetName partitioned dataset containing the member
     * @param memberName  name of member to delete
     * @return DsnDeleteInputData
     */
    public static DsnDeleteInputData forMember(final String datasetName, final String memberName) {
        ValidateUtils.checkIllegalParameter(datasetName, "datasetName");
        ValidateUtils.checkIllegalParameter(memberName, "memberName");

        return new DsnDeleteInputData(
                DeleteType.MEMBER,
                datasetName,
                memberName,
                null);
    }

    /**
     * Creates input for an uncataloged dataset delete operation.
     *
     * @param datasetName name of a dataset (e.g. 'DATASET.LIB')
     * @param volume      volume serial the dataset resides on (e.g. 'VOL001')
     * @return DsnDeleteInputData
     */
    public static DsnDeleteInputData forUncataloged(final String datasetName, final String volume) {
        ValidateUtils.checkIllegalParameter(datasetName, "datasetName");
        ValidateUtils.checkIllegalParameter(volume, "volume");

        return new DsnDeleteInputData(
                DeleteType.UNCATALOGED,
                datasetName,
                null,
                volume);
    }

    /**
     * Returns the delete operation type.
     *
     * @return delete type
     */
    public DeleteType getType() {
        return type;
    }

    /**
     * Returns the dataset name.
     *
     * @return dataset name
     */
    public String getDatasetName() {
        return datasetName;
    }

    /**
     * Returns the member name.
     * <p>
     * This value is only applicable for member delete operations.
     *
     * @return member name or {@code null}
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     * Returns the volume serial.
     * <p>
     * This value is only applicable for uncataloged dataset delete operations.
     *
     * @return volume serial or {@code null}
     */
    public String getVolume() {
        return volume;
    }

}