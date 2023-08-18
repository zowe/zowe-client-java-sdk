/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.response;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * ListItem object representing an item from Unix System Services (USS) list operation
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UnixFile {

    /**
     * File, symbolic file or directory name
     */
    private final Optional<String> name;

    /**
     * Permission (mode) of returned name item
     */
    private final Optional<String> mode;

    /**
     * size of returned name item
     */
    private final OptionalLong size;

    /**
     * uid of returned name item
     */
    private final OptionalLong uid;

    /**
     * user of returned name item
     */
    private final Optional<String> user;

    /**
     * gid of returned name item
     */
    private final OptionalLong gid;

    /**
     * group of returned name item
     */
    private final Optional<String> group;

    /**
     * mtime of returned name item
     */
    private final Optional<String> mtime;

    /**
     * UnixFile constructor
     *
     * @param builder UnixFile.Builder object
     * @author Frank Giordano
     */
    public UnixFile(final UnixFile.Builder builder) {
        this.name = Optional.ofNullable(builder.name);
        this.mode = Optional.ofNullable(builder.mode);
        if (builder.size == null) {
            this.size = OptionalLong.empty();
        } else {
            this.size = OptionalLong.of(builder.size);
        }
        if (builder.uid == null) {
            this.uid = OptionalLong.empty();
        } else {
            this.uid = OptionalLong.of(builder.uid);
        }
        this.user = Optional.ofNullable(builder.user);
        if (builder.gid == null) {
            this.gid = OptionalLong.empty();
        } else {
            this.gid = OptionalLong.of(builder.gid);
        }
        this.group = Optional.ofNullable(builder.group);
        this.mtime = Optional.ofNullable(builder.mtime);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getMode() {
        return mode;
    }

    public OptionalLong getSize() {
        return size;
    }

    public OptionalLong getUid() {
        return uid;
    }

    public Optional<String> getUser() {
        return user;
    }

    public OptionalLong getGid() {
        return gid;
    }

    public Optional<String> getGroup() {
        return group;
    }

    public Optional<String> getMtime() {
        return mtime;
    }

    @Override
    public String toString() {
        return "UssItem{" +
                "name=" + name +
                ", mode=" + mode +
                ", size=" + size +
                ", uid=" + uid +
                ", user=" + user +
                ", gid=" + gid +
                ", group=" + group +
                ", mtime=" + mtime +
                '}';
    }

    public static class Builder {

        private String name;
        private String mode;
        private Long size;
        private Long uid;
        private String user;
        private Long gid;
        private String group;
        private String mtime;

        public UnixFile build() {
            return new UnixFile(this);
        }

        public UnixFile.Builder name(final String name) {
            this.name = name;
            return this;
        }

        public UnixFile.Builder mode(final String mode) {
            this.mode = mode;
            return this;
        }

        public UnixFile.Builder size(final Long size) {
            this.size = size;
            return this;
        }

        public UnixFile.Builder uid(final Long uid) {
            this.uid = uid;
            return this;
        }

        public UnixFile.Builder user(final String user) {
            this.user = user;
            return this;
        }

        public UnixFile.Builder gid(final Long gid) {
            this.gid = gid;
            return this;
        }

        public UnixFile.Builder group(final String group) {
            this.group = group;
            return this;
        }

        public UnixFile.Builder mtime(final String mtime) {
            this.mtime = mtime;
            return this;
        }

    }

}
