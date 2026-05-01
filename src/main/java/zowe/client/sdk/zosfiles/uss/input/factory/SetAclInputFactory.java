/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.input.factory;

import zowe.client.sdk.zosfiles.uss.types.DeleteAclType;
import zowe.client.sdk.zosfiles.uss.types.LinkType;

/**
 * Factory class for creating UssSetAclInputData instances.
 * <p>
 * This factory provides controlled creation methods for the supported USS setfacl operations.
 * The setfacl API requires at least one of the following keywords to be specified:
 * delete-type, set, modify, or delete.
 * <p>
 * The modify and delete keywords may be used together. The delete-type and set keywords
 * cannot be combined with any other ACL operation keyword.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class SetAclInputFactory {

    /**
     * Create delete-type input data using default abort and links values.
     *
     * @param deleteType deleteType value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createDeleteTypeInput(final DeleteAclType deleteType) {
        return this.createDeleteTypeInput(deleteType, false, LinkType.FOLLOW);
    }

    /**
     * Create delete-type input data using default links value.
     *
     * @param deleteType deleteType value
     * @param abort abort value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createDeleteTypeInput(final DeleteAclType deleteType,
                                                    final boolean abort) {
        return this.createDeleteTypeInput(deleteType, abort, LinkType.FOLLOW);
    }

    /**
     * Create delete-type input data.
     *
     * @param deleteType deleteType value
     * @param abort abort value
     * @param links links value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createDeleteTypeInput(final DeleteAclType deleteType,
                                                    final boolean abort,
                                                    final LinkType links) {
        return new UssSetAclInputData.Builder()
                .setDeleteType(deleteType)
                .setAbort(abort)
                .setLinks(links)
                .build();
    }

    /**
     * Create delete input data using default abort and links values.
     *
     * @param delete delete value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createDeleteInput(final String delete) {
        return this.createDeleteInput(delete, false, LinkType.FOLLOW);
    }

    /**
     * Create delete input data using default links value.
     *
     * @param delete delete value
     * @param abort abort value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createDeleteInput(final String delete,
                                                final boolean abort) {
        return this.createDeleteInput(delete, abort, LinkType.FOLLOW);
    }

    /**
     * Create delete input data.
     *
     * @param delete delete value
     * @param abort abort value
     * @param links links value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createDeleteInput(final String delete,
                                                final boolean abort,
                                                final LinkType links) {
        return new UssSetAclInputData.Builder()
                .setDelete(delete)
                .setAbort(abort)
                .setLinks(links)
                .build();
    }

    /**
     * Create modify input data using default abort and links values.
     *
     * @param modify modify value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createModifyInput(final String modify) {
        return this.createModifyInput(modify, false, LinkType.FOLLOW);
    }

    /**
     * Create modify input data using default links value.
     *
     * @param modify modify value
     * @param abort abort value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createModifyInput(final String modify,
                                                final boolean abort) {
        return this.createModifyInput(modify, abort, LinkType.FOLLOW);
    }

    /**
     * Create modify input data.
     *
     * @param modify modify value
     * @param abort abort value
     * @param links links value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createModifyInput(final String modify,
                                                final boolean abort,
                                                final LinkType links) {
        return new UssSetAclInputData.Builder()
                .setModify(modify)
                .setAbort(abort)
                .setLinks(links)
                .build();
    }

    /**
     * Create set input data using default abort and links values.
     *
     * @param set set value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createSetInput(final String set) {
        return this.createSetInput(set, false, LinkType.FOLLOW);
    }

    /**
     * Create set input data using default links value.
     *
     * @param set set value
     * @param abort abort value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createSetInput(final String set,
                                             final boolean abort) {
        return this.createSetInput(set, abort, LinkType.FOLLOW);
    }

    /**
     * Create set input data.
     *
     * @param set set value
     * @param abort abort value
     * @param links links value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createSetInput(final String set,
                                             final boolean abort,
                                             final LinkType links) {
        return new UssSetAclInputData.Builder()
                .setSet(set)
                .setAbort(abort)
                .setLinks(links)
                .build();
    }

    /**
     * Create modify and delete input data using default abort and links values.
     *
     * @param modify modify value
     * @param delete delete value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createModifyDeleteInput(final String modify,
                                                      final String delete) {
        return this.createModifyDeleteInput(modify, delete, false, LinkType.FOLLOW);
    }

    /**
     * Create modify and delete input data using default links value.
     *
     * @param modify modify value
     * @param delete delete value
     * @param abort abort value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createModifyDeleteInput(final String modify,
                                                      final String delete,
                                                      final boolean abort) {
        return this.createModifyDeleteInput(modify, delete, abort, LinkType.FOLLOW);
    }

    /**
     * Create modify and delete input data.
     *
     * @param modify modify value
     * @param delete delete value
     * @param abort abort value
     * @param links links value
     * @return UssSetAclInputData
     */
    public UssSetAclInputData createModifyDeleteInput(final String modify,
                                                      final String delete,
                                                      final boolean abort,
                                                      final LinkType links) {
        return new UssSetAclInputData.Builder()
                .setModify(modify)
                .setDelete(delete)
                .setAbort(abort)
                .setLinks(links)
                .build();
    }

}
