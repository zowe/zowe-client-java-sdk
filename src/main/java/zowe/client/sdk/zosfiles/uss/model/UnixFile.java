/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UnixFile object representing an item from Unix System Services (USS) file list operation.
 * Immutable class using Jackson for JSON parsing.
 *
 * @author Frank Giordano
 * @version 6.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UnixFile {

    /**
     * File, symbolic file or directory name
     */
    private final String name;

    /**
     * Permission (mode) of returned name item
     */
    private final String mode;

    /**
     * size of returned name item
     */
    private final Long size;

    /**
     * uid of returned name item
     */
    private final Long uid;

    /**
     * user of returned name item
     */
    private final String user;

    /**
     * gid of returned name item
     */
    private final Long gid;

    /**
     * group of returned name items
     */
    private final String group;

    /**
     * mtime of returned name item
     */
    private final String mtime;

    /**
     * target of returned name item
     */
    private final String target;

    /**
     * Jackson constructor for UnixFile
     *
     * @param name   File, symbolic file or directory name
     * @param mode   Permission (mode) of returned name item
     * @param size   size of returned name item
     * @param uid    uid of returned name item
     * @param user   user of returned name item
     * @param gid    gid of returned name item
     * @param group  group of returned name items
     * @param mtime  mtime of returned name item
     * @param target target of returned name item
     */
    @JsonCreator
    public UnixFile(
            @JsonProperty("name") String name,
            @JsonProperty("mode") String mode,
            @JsonProperty("size") Long size,
            @JsonProperty("uid") Long uid,
            @JsonProperty("user") String user,
            @JsonProperty("gid") Long gid,
            @JsonProperty("group") String group,
            @JsonProperty("mtime") String mtime,
            @JsonProperty("target") String target
    ) {
        this.name = name == null ? "" : name;
        this.mode = mode == null ? "" : mode;
        this.size = (size == null) ? 0L : size;
        this.uid = (uid == null) ? 0L : uid;
        this.gid = (gid == null) ? 0L : gid;
        this.user = user == null ? "" : user;
        this.group = group == null ? "" : group;
        this.mtime = mtime == null ? "" : mtime;
        this.target = target == null ? "" : target;
    }

    /**
     * Retrieve name value
     *
     * @return name value
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve mode value
     *
     * @return mode value
     */
    public String getMode() {
        return mode;
    }

    /**
     * Retrieve size value
     *
     * @return size value
     */
    public Long getSize() {
        return size;
    }

    /**
     * Retrieve uid value
     *
     * @return uid value
     */
    public Long getUid() {
        return uid;
    }

    /**
     * Retrieve user value
     *
     * @return user value
     */
    public String getUser() {
        return user;
    }

    /**
     * Retrieve gid value
     *
     * @return gid value
     */
    public Long getGid() {
        return gid;
    }

    /**
     * Retrieve group value
     *
     * @return group value
     */
    public String getGroup() {
        return group;
    }

    /**
     * Retrieve mtime value
     *
     * @return mtime value
     */
    public String getMtime() {
        return mtime;
    }

    /**
     * Retrieve target value
     *
     * @return target value
     */
    public String getTarget() {
        return target;
    }

    /**
     * Return string value representing UnixFile object
     *
     * @return string representation of UnixFile
     */
    @Override
    public String toString() {
        return "UnixFile{" +
                "name=" + name +
                ", mode=" + mode +
                ", size=" + size +
                ", uid=" + uid +
                ", user=" + user +
                ", gid=" + gid +
                ", group=" + group +
                ", mtime=" + mtime +
                ", target=" + target +
                '}';
    }

}
