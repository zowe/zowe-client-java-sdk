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
 * ListItem object representing an item from unix system services list operation
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssItem {

    /**
     * File, symbolic file or directory name
     */
    public final Optional<String> name;
    /**
     * Permission (mode) of returned name item
     */
    public final Optional<String> mode;
    /**
     * size of returned name item
     */
    public final OptionalLong size;
    /**
     * uid of returned name item
     */
    public final OptionalLong uid;
    /**
     * user of returned name item
     */
    public final Optional<String> user;
    /**
     * gid of returned name item
     */
    public final OptionalLong gid;
    /**
     * group of returned name item
     */
    public final Optional<String> group;
    /**
     * mtime of returned name item
     */
    public final Optional<String> mtime;

    public UssItem(UssItem.Builder builder) {
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

    public static class Builder {

        private String name;
        private String mode;
        private Long size;
        private Long uid;
        private String user;
        private Long gid;
        private String group;
        private String mtime;

        public UssItem build() {
            return new UssItem(this);
        }

        public UssItem.Builder name(String name) {
            this.name = name;
            return this;
        }

        public UssItem.Builder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public UssItem.Builder size(Long size) {
            this.size = size;
            return this;
        }

        public UssItem.Builder uid(Long uid) {
            this.uid = uid;
            return this;
        }

        public UssItem.Builder user(String user) {
            this.user = user;
            return this;
        }

        public UssItem.Builder gid(Long gid) {
            this.gid = gid;
            return this;
        }

        public UssItem.Builder group(String group) {
            this.group = group;
            return this;
        }

        public UssItem.Builder mtime(String mtime) {
            this.mtime = mtime;
            return this;
        }

    }

}
