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

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) getfacl operation
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class GetAclParams {

    /**
     * The default is 'access', displays the access ACL entries for a file or directory (getfacl -a).
     * 'dir' displays the directory default ACL entries (getfacl -d).
     * If the target is not a directory, a warning is issued.
     */
    private final Optional<String> type;

    /**
     * The user ID or UID (as a JSON string),
     * displays only the ACL entries for the specified types of access control lists (getfacl -a, -d, -f)
     * which affects the specified user's access (getfacl -e user).
     */
    private final Optional<String> user;

    /**
     * 	The default is 'false'.
     * 	When true, displays each ACL entry, using commas to separate the ACL entries instead of newlines.
     */
    private final boolean useCommas;

    /**
     * The default is 'false'.
     * When true, the comment header (the first three lines of each file's output) is not to be displayed (getfacl -m)
     */
    private final boolean suppressHeader;

    /**
     * The default is 'false'.
     * When true, displays only the extended ACL entries. Does not display the base ACL entries (getfacl -o).
     */
    private final boolean suppressBaseAcl;

    public GetAclParams(GetAclParams.Builder builder) {
        this.type = Optional.ofNullable(builder.type);
        this.user = Optional.ofNullable(builder.user);
        this.useCommas = builder.useCommas;
        this.suppressHeader = builder.suppressHeader;
        this.suppressBaseAcl = builder.suppressBaseAcl;
    }

    public Optional<String> getType() {
        return type;
    }

    public Optional<String> getUser() {
        return user;
    }

    public boolean getUseCommas() {
        return useCommas;
    }

    public boolean getSuppressHeader() {
        return suppressHeader;
    }

    public boolean getSuppressBaseAcl() {
        return suppressBaseAcl;
    }

    @Override
    public String toString() {
        return "GetAclParams{" +
                "type=" + type +
                ", user=" + user +
                ", useCommas=" + useCommas +
                ", suppressHeader=" + suppressHeader +
                ", suppressBaseAcl=" + suppressBaseAcl +
                '}';
    }

    public static class Builder {
        private String type;
        private String user;
        private boolean useCommas;
        private boolean suppressHeader;
        private boolean suppressBaseAcl;

        public Builder() {
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder useCommas(boolean useCommas) {
            this.useCommas = useCommas;
            return this;
        }

        public Builder suppressHeader(boolean suppressHeader) {
            this.suppressHeader = suppressHeader;
            return this;
        }

        public Builder suppressBaseAcl(boolean suppressBaseAcl) {
            this.suppressBaseAcl = suppressBaseAcl;
            return this;
        }

        public GetAclParams build() {
            return new GetAclParams(this);
        }
    }

}
