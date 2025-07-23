/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.input;

import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.uss.types.ChangeTagAction;
import zowe.client.sdk.zosfiles.uss.types.ChangeTagType;
import zowe.client.sdk.zosfiles.uss.types.LinkType;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) change tag operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public class ChangeTagParams {

    /**
     * The file tag action.
     * If "set", the file is tagged as specified in the "type" keyword.
     * If "remove", any existing file tag is removed.
     * If "list", the existing tag information will be returned in a JSON response document.
     */
    private final Optional<ChangeTagAction> action;

    /**
     * The default is "mixed".
     * This option can be specified only when the action is "set".
     */
    private final Optional<ChangeTagType> type;

    /**
     * Specifies the coded character set in which text data is encoded, such as ASCII or EBCDIC.
     * For example, the code set for ASCII is ISO8859-1; the code set for EBCDIC is IBM-1047.
     */
    private final Optional<String> codeset;

    /**
     * The default is false.
     * When 'true', tags all the files and subdirectorires in that directory (chtag -R).
     */
    private final boolean recursive;

    /**
     * The default is 'change' encountered links, applying a tag action to the file or directory pointed
     * to by any encountered links.
     * <p>
     * 'suppress' a tag action for the file or directory pointed to by any encountered
     */
    private final Optional<LinkType> links;

    /**
     * ChangeTagParams constructor
     *
     * @param builder Builder builder
     * @author James Kostrewski
     */
    public ChangeTagParams(final Builder builder) {
        this.action = Optional.ofNullable(builder.action);
        this.type = Optional.ofNullable(builder.type);
        this.codeset = Optional.ofNullable(builder.codeset);
        this.recursive = builder.recursive;
        this.links = Optional.ofNullable(builder.links);
    }

    /**
     * Retrieve action value
     *
     * @return action value
     */
    public Optional<ChangeTagAction> getAction() {
        return action;
    }

    /**
     * Retrieve type value
     *
     * @return type value
     */
    public Optional<ChangeTagType> getType() {
        return type;
    }

    /**
     * Retrieve codeset value
     *
     * @return codeset value
     */
    public Optional<String> getCodeset() {
        return codeset;
    }

    /**
     * Retrieve is recursive specified
     *
     * @return boolean true or false
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * Retrieve links value
     *
     * @return links value
     */
    public Optional<LinkType> getLinks() {
        return links;
    }

    /**
     * Return string value representing ChangeTagParams object
     *
     * @return string representation of ChangeTagParams
     */
    @Override
    public String toString() {
        return "ChangeTagParams{" +
                "action='" + action + '\'' +
                ", type='" + type + '\'' +
                ", codeset='" + codeset + '\'' +
                ", recursive='" + recursive + '\'' +
                ", links='" + links + '\'' +
                '}';
    }

    /**
     * Builder class for ChangeTagParams
     */
    public static class Builder {

        /**
         * The file tag action.
         * If "set", the file is tagged as specified in the "type" keyword.
         * If "remove", any existing file tag is removed.
         * If "list", the existing tag information will be returned in a JSON response document.
         */
        private ChangeTagAction action;

        /**
         * The default is "mixed".
         * This option can be specified only when the action is "set".
         */
        private ChangeTagType type;

        /**
         * Specifies the coded character set in which text data is encoded, such as ASCII or EBCDIC.
         * For example, the code set for ASCII is ISO8859-1; the code set for EBCDIC is IBM-1047.
         */
        private String codeset;

        /**
         * The default is false.
         * When 'true', tags all the files and subdirectorires in that directory (chtag -R).
         */
        private boolean recursive = false;

        /**
         * The default is 'change' encountered links, applying a tag action to the file or directory pointed
         * to by any encountered links.
         * <p>
         * 'suppress' a tag action for the file or directory pointed to by any encountered
         */
        private LinkType links;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set ChangeTagAction type value
         *
         * @param action ChangeTagAction type value
         * @return Builder this object
         */
        public Builder action(final ChangeTagAction action) {
            ValidateUtils.checkNullParameter(action == null, "action is null");
            this.action = action;
            return this;
        }

        /**
         * Set ChangeTagType type value
         *
         * @param type ChangeTagType enum value
         * @return Builder this object
         */
        public Builder type(final ChangeTagType type) {
            this.type = type;
            return this;
        }

        /**
         * Set codeset string value
         *
         * @param codeset string value
         * @return Builder this object
         */
        public Builder codeset(final String codeset) {
            this.codeset = codeset;
            return this;
        }

        /**
         * Set recursive boolean value
         *
         * @param recursive boolean true or false value
         * @return Builder this object
         */
        public Builder recursive(final boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        /**
         * Set links LinkType value
         *
         * @param links LinkType type object
         * @return Builder this object
         */
        public Builder links(final LinkType links) {
            this.links = links;
            return this;
        }

        /**
         * Return ChangeTagParams object based on Builder this object
         *
         * @return ChangeTagParams this object
         */
        public ChangeTagParams build() {
            return new ChangeTagParams(this);
        }

    }

}
