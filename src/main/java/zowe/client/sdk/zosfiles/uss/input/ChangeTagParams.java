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

import zowe.client.sdk.zosfiles.uss.types.ChangeTagAction;
import zowe.client.sdk.zosfiles.uss.types.ChangeTagType;
import zowe.client.sdk.zosfiles.uss.types.LinkType;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) change tag operation
 *
 * @author James Kostrewski
 * @version 2.0
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
     * The default is "mixed"
     * This option can be specified only when the action is "set".
     */
    private final Optional<ChangeTagType> type;

    /**
     * Specifies the coded character set in which text data is encoded such as ASCII or EBCDIC.
     * For example, the code set for ASCII is ISO8859-1; the code set for EBCDIC is IBM-1047.
     */
    private final Optional<String> codeset;

    /**
     * The default is false.
     * When 'true', tags all the files and subdirectorires in that directory (chtag -R).
     */
    private final boolean recursive;

    /**
     * The default is none
     * sets the modification time of the destination file to the present.
     * When 'modtime', sets the modification and access time of each destination file to that of the corresponding
     * source file. (cp -m).
     * When 'all', preserves the modification and access times as well as the file mode,
     * file format, owner, and group owner (cp -p).
     */
    private final Optional<LinkType> links;

    /**
     * ChangeTagParams constructor
     *
     * @params CopyParams.Builder builder
     */
    public ChangeTagParams(ChangeTagParams.Builder builder) {
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
     * Retrieve recursive value
     *
     * @return recursive value
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
     * Builder class for CopyParams
     */
    public static class Builder {

        private ChangeTagAction action;
        private ChangeTagType type;
        private String codeset;
        private boolean recursive = false;
        private LinkType links;

        /**
         * Set action value
         *
         * @param action ChangeTagAction enum value
         * @return Builder object
         */
        public ChangeTagParams.Builder action(ChangeTagAction action) {
            this.action = action;
            return this;
        }

        /**
         * Set type value
         *
         * @param type ChangeTagType enum value
         * @return Builder object
         */
        public ChangeTagParams.Builder type(ChangeTagType type) {
            this.type = type;
            return this;
        }

        /**
         * Set codeset value
         *
         * @param codeset String value
         * @return Builder object
         */
        public ChangeTagParams.Builder codeset(String codeset) {
            this.codeset = codeset;
            return this;
        }

        /**
         * Set recursive value
         *
         * @param recursive boolean value
         * @return Builder object
         */
        public ChangeTagParams.Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        /**
         * Set links value
         *
         * @param links LinkType enum value
         * @return Builder object
         */
        public ChangeTagParams.Builder links(LinkType links) {
            this.links = links;
            return this;
        }

        /**
         * Build CopyParams object
         *
         * @return CopyParams object
         */
        public ChangeTagParams build() {
            return new ChangeTagParams(this);
        }

    }

}
