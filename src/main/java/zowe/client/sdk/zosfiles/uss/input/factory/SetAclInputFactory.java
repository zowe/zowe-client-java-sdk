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
 * This factory provides controlled creation methods for supported USS setfacl operations.
 * The setfacl API requires at least one ACL operation keyword to be specified:
 * delete-type, set, modify, or delete.
 * <p>
 * The modify and delete keywords may be used together. The delete-type and set keywords
 * cannot be combined with any other ACL operation keyword.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class SetAclInputFactory {

    /**
     * Private constructor defined to avoid instantiation of a static factory class
     */
    private SetAclInputFactory() {
        throw new IllegalStateException("Factory class");
    }

    /**
     * Create delete-type input data using default abort and links values.
     *
     * @param deleteType ACL type whose extended ACL entries are deleted
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createDeleteTypeInput(final DeleteAclType deleteType) {
        return createDeleteTypeInput(deleteType, false, LinkType.FOLLOW);
    }

    /**
     * Create delete-type input data using the default links value.
     *
     * @param deleteType ACL type whose extended ACL entries are deleted
     * @param abort      true to abort processing when an error or warning occurs; false otherwise
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createDeleteTypeInput(final DeleteAclType deleteType,
                                                           final boolean abort) {
        return createDeleteTypeInput(deleteType, abort, LinkType.FOLLOW);
    }

    /**
     * Create delete-type input data.
     *
     * @param deleteType ACL type whose extended ACL entries are deleted
     * @param abort      true to abort processing when an error or warning occurs; false otherwise
     * @param links      symbolic link handling option for the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createDeleteTypeInput(final DeleteAclType deleteType,
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
     * @param delete ACL entries to remove from the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createDeleteInput(final String delete) {
        return createDeleteInput(delete, false, LinkType.FOLLOW);
    }

    /**
     * Create delete input data using the default links value.
     *
     * @param delete ACL entries to remove from the target path
     * @param abort  true to abort processing when an error or warning occurs; false otherwise
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createDeleteInput(final String delete,
                                                       final boolean abort) {
        return createDeleteInput(delete, abort, LinkType.FOLLOW);
    }

    /**
     * Create delete input data.
     *
     * @param delete ACL entries to remove from the target path
     * @param abort  true to abort processing when an error or warning occurs; false otherwise
     * @param links  symbolic link handling option for the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createDeleteInput(final String delete,
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
     * @param modify ACL entries to add or replace on the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createModifyInput(final String modify) {
        return createModifyInput(modify, false, LinkType.FOLLOW);
    }

    /**
     * Create modify input data using default links value.
     *
     * @param modify ACL entries to add or replace on the target path
     * @param abort  true to abort processing when an error or warning occurs; false otherwise
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createModifyInput(final String modify,
                                                       final boolean abort) {
        return createModifyInput(modify, abort, LinkType.FOLLOW);
    }

    /**
     * Create modify input data.
     *
     * @param modify ACL entries to add or replace on the target path
     * @param abort  true to abort processing when an error or warning occurs; false otherwise
     * @param links  symbolic link handling option for the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createModifyInput(final String modify,
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
     * @param set ACL entries used to replace all existing ACLs on the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createSetInput(final String set) {
        return createSetInput(set, false, LinkType.FOLLOW);
    }

    /**
     * Create set input data using the default links value.
     *
     * @param set   ACL entries used to replace all existing ACLs on the target path
     * @param abort true to abort processing when an error or warning occurs; false otherwise
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createSetInput(final String set,
                                                    final boolean abort) {
        return createSetInput(set, abort, LinkType.FOLLOW);
    }

    /**
     * Create set input data.
     *
     * @param set   ACL entries used to replace all existing ACLs on the target path
     * @param abort true to abort processing when an error or warning occurs; false otherwise
     * @param links symbolic link handling option for the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createSetInput(final String set,
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
     * @param modify ACL entries to add or replace on the target path
     * @param delete ACL entries to remove from the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createModifyDeleteInput(final String modify,
                                                             final String delete) {
        return createModifyDeleteInput(modify, delete, false, LinkType.FOLLOW);
    }

    /**
     * Create modify and delete input data using default links value.
     *
     * @param modify ACL entries to add or replace on the target path
     * @param delete ACL entries to remove from the target path
     * @param abort  true to abort processing when an error or warning occurs; false otherwise
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createModifyDeleteInput(final String modify,
                                                             final String delete,
                                                             final boolean abort) {
        return createModifyDeleteInput(modify, delete, abort, LinkType.FOLLOW);
    }

    /**
     * Create modify and delete input data.
     *
     * @param modify ACL entries to add or replace on the target path
     * @param delete ACL entries to remove from the target path
     * @param abort  true to abort processing when an error or warning occurs; false otherwise
     * @param links  symbolic link handling option for the target path
     * @return UssSetAclInputData
     */
    public static UssSetAclInputData createModifyDeleteInput(final String modify,
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
