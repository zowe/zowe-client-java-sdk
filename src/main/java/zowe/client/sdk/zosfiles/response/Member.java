/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.response;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Represents a z/OS partition data set member
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class Member {

    /**
     * The name of the member
     */
    private final Optional<String> member;

    /**
     * The version of member
     */
    private final OptionalLong vers;

    /**
     * The number of modifications of member
     */
    private final OptionalLong mod;

    /**
     * The creation date of member
     */
    private final Optional<String> c4date;

    /**
     * The modification date of member
     */
    private final Optional<String> m4date;

    /**
     * The cnorc of member
     */
    private final OptionalLong cnorc;

    /**
     * The inorc of member
     */
    private final OptionalLong inorc;

    /**
     * The mnorc of member
     */
    private final OptionalLong mnorc;

    /**
     * The mtime of member
     */
    private final Optional<String> mtime;

    /**
     * The msec of member
     */
    private final Optional<String> msec;

    /**
     * The user of member
     */
    private final Optional<String> user;

    /**
     * The sclm of member
     */
    private final Optional<String> sclm;

    private Member(Member.Builder builder) {
        this.member = Optional.ofNullable(builder.member);
        if (builder.vers == null) {
            this.vers = OptionalLong.empty();
        } else {
            this.vers = OptionalLong.of(builder.vers);
        }
        if (builder.mod == null) {
            this.mod = OptionalLong.empty();
        } else {
            this.mod = OptionalLong.of(builder.mod);
        }
        this.c4date = Optional.ofNullable(builder.c4date);
        this.m4date = Optional.ofNullable(builder.m4date);
        if (builder.cnorc == null) {
            this.cnorc = OptionalLong.empty();
        } else {
            this.cnorc = OptionalLong.of(builder.cnorc);
        }
        if (builder.inorc == null) {
            this.inorc = OptionalLong.empty();
        } else {
            this.inorc = OptionalLong.of(builder.inorc);
        }
        if (builder.mnorc == null) {
            this.mnorc = OptionalLong.empty();
        } else {
            this.mnorc = OptionalLong.of(builder.mnorc);
        }
        this.mtime = Optional.ofNullable(builder.mtime);
        this.msec = Optional.ofNullable(builder.msec);
        this.user = Optional.ofNullable(builder.user);
        this.sclm = Optional.ofNullable(builder.sclm);
    }

    /**
     * Retrieve name of the member
     */
    public Optional<String> getMember() {
        return member;
    }

    /**
     * Retrieve version number of the member
     */
    public OptionalLong getVers() {
        return vers;
    }

    /**
     * Retrieve modification number of the member
     */
    public OptionalLong getMod() {
        return mod;
    }

    /**
     * Retrieve creation date of the member
     */
    public Optional<String> getC4date() {
        return c4date;
    }

    /**
     * Retrieve modification date of the member
     */
    public Optional<String> getM4date() {
        return m4date;
    }

    /**
     * Retrieve cnorc of the member
     */
    public OptionalLong getCnorc() {
        return cnorc;
    }

    /**
     * Retrieve inorc of the member
     */
    public OptionalLong getInorc() {
        return inorc;
    }

    /**
     * Retrieve mnorc of the member
     */
    public OptionalLong getMnorc() {
        return mnorc;
    }

    /**
     * Retrieve mtime of the member
     */
    public Optional<String> getMtime() {
        return mtime;
    }

    /**
     * Retrieve msec of the member
     */
    public Optional<String> getMsec() {
        return msec;
    }

    /**
     * Retrieve user of the member
     */
    public Optional<String> getUser() {
        return user;
    }

    /**
     * Retrieve sclm of the member
     */
    public Optional<String> getSclm() {
        return sclm;
    }

    /**
     * String representation of the member object
     */
    @Override
    public String toString() {
        return "Member{" +
                "member=" + member +
                ", vers=" + vers +
                ", mod=" + mod +
                ", c4date=" + c4date +
                ", m4date=" + m4date +
                ", cnorc=" + cnorc +
                ", inorc=" + inorc +
                ", mnorc=" + mnorc +
                ", mtime=" + mtime +
                ", msec=" + msec +
                ", user=" + user +
                ", sclm=" + sclm +
                '}';
    }

    public static class Builder {

        private String member;
        private Long vers;
        private Long mod;
        private String c4date;
        private String m4date;
        private Long cnorc;
        private Long inorc;
        private Long mnorc;
        private String mtime;
        private String msec;
        private String user;
        private String sclm;

        public Member.Builder member(String member) {
            this.member = member;
            return this;
        }

        public Member.Builder vers(Long vers) {
            this.vers = vers;
            return this;
        }

        public Member.Builder mod(Long mod) {
            this.mod = mod;
            return this;
        }

        public Member.Builder c4date(String c4date) {
            this.c4date = c4date;
            return this;
        }

        public Member.Builder m4date(String m4date) {
            this.m4date = m4date;
            return this;
        }

        public Member.Builder cnorc(Long cnorc) {
            this.cnorc = cnorc;
            return this;
        }

        public Member.Builder inorc(Long inorc) {
            this.inorc = inorc;
            return this;
        }

        public Member.Builder mnorc(Long mnorc) {
            this.mnorc = mnorc;
            return this;
        }

        public Member.Builder mtime(String mtime) {
            this.mtime = mtime;
            return this;
        }

        public Member.Builder msec(String msec) {
            this.msec = msec;
            return this;
        }

        public Member.Builder user(String user) {
            this.user = user;
            return this;
        }

        public Member.Builder sclm(String sclm) {
            this.sclm = sclm;
            return this;
        }

        public Member build() {
            return new Member(this);
        }

    }

}

