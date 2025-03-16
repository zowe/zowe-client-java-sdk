/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.response;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Represents a z/OS partition data set member
 *
 * @author Frank Giordano
 * @version 3.0
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

    /**
     * Member constructor
     *
     * @param builder Builder object
     * @author Frank Giordano
     */
    private Member(final Builder builder) {
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
     *
     * @return Optional string value
     */
    public Optional<String> getMember() {
        return member;
    }

    /**
     * Retrieve version number of the member
     *
     * @return Optional long value
     */
    public OptionalLong getVers() {
        return vers;
    }

    /**
     * Retrieve modification number of the member
     *
     * @return Optional long value
     */
    public OptionalLong getMod() {
        return mod;
    }

    /**
     * Retrieve creation date of the member
     *
     * @return Optional string value
     */
    public Optional<String> getC4date() {
        return c4date;
    }

    /**
     * Retrieve modification date of the member
     *
     * @return Optional string value
     */
    public Optional<String> getM4date() {
        return m4date;
    }

    /**
     * Retrieve cnorc of the member
     *
     * @return Optional long value
     */
    public OptionalLong getCnorc() {
        return cnorc;
    }

    /**
     * Retrieve inorc of the member
     *
     * @return Optional long value
     */
    public OptionalLong getInorc() {
        return inorc;
    }

    /**
     * Retrieve mnorc of the member
     *
     * @return Optional long value
     */
    public OptionalLong getMnorc() {
        return mnorc;
    }

    /**
     * Retrieve mtime of the member
     *
     * @return Optional string value
     */
    public Optional<String> getMtime() {
        return mtime;
    }

    /**
     * Retrieve msec of the member
     *
     * @return Optional string value
     */
    public Optional<String> getMsec() {
        return msec;
    }

    /**
     * Retrieve user of the member
     *
     * @return Optional string value
     */
    public Optional<String> getUser() {
        return user;
    }

    /**
     * Retrieve sclm of the member
     *
     * @return Optional string value
     */
    public Optional<String> getSclm() {
        return sclm;
    }

    /**
     * Return string value representing Member object
     *
     * @return string representation of Member
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

    /**
     * Builder class for Member
     */
    public static class Builder {

        /**
         * The name of the member
         */
        private String member;

        /**
         * The version of member
         */
        private Long vers;

        /**
         * The number of modifications of member
         */
        private Long mod;

        /**
         * The creation date of member
         */
        private String c4date;

        /**
         * The modification date of member
         */
        private String m4date;

        /**
         * The cnorc of member
         */
        private Long cnorc;

        /**
         * The inorc of member
         */
        private Long inorc;

        /**
         * The mnorc of member
         */
        private Long mnorc;

        /**
         * The mtime of member
         */
        private String mtime;

        /**
         * The msec of member
         */
        private String msec;

        /**
         * The user of member
         */
        private String user;

        /**
         * The sclm of member
         */
        private String sclm;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set member string value
         *
         * @param member string value
         * @return Builder this object
         */
        public Builder member(final String member) {
            this.member = member;
            return this;
        }

        /**
         * Set vers long value
         *
         * @param vers long value
         * @return Builder this object
         */
        public Builder vers(final Long vers) {
            this.vers = vers;
            return this;
        }

        /**
         * Set mod long value
         *
         * @param mod long value
         * @return Builder this object
         */
        public Builder mod(final Long mod) {
            this.mod = mod;
            return this;
        }

        /**
         * Set c4date string value
         *
         * @param c4date string value
         * @return Builder this object
         */
        public Builder c4date(final String c4date) {
            this.c4date = c4date;
            return this;
        }

        /**
         * Set m4date string value
         *
         * @param m4date string value
         * @return Builder this object
         */
        public Builder m4date(final String m4date) {
            this.m4date = m4date;
            return this;
        }

        /**
         * Set cnorc long value
         *
         * @param cnorc long value
         * @return Builder this object
         */
        public Builder cnorc(final Long cnorc) {
            this.cnorc = cnorc;
            return this;
        }

        /**
         * Set inorc long value
         *
         * @param inorc long value
         * @return Builder this object
         */
        public Builder inorc(final Long inorc) {
            this.inorc = inorc;
            return this;
        }

        /**
         * Set mnorc long value
         *
         * @param mnorc long value
         * @return Builder this object
         */
        public Builder mnorc(final Long mnorc) {
            this.mnorc = mnorc;
            return this;
        }

        /**
         * Set mtime string value
         *
         * @param mtime string value
         * @return Builder this object
         */
        public Builder mtime(final String mtime) {
            this.mtime = mtime;
            return this;
        }

        /**
         * Set msec string value
         *
         * @param msec string value
         * @return Builder this object
         */
        public Builder msec(final String msec) {
            this.msec = msec;
            return this;
        }

        /**
         * Set user string value
         *
         * @param user string value
         * @return Builder this object
         */
        public Builder user(final String user) {
            this.user = user;
            return this;
        }

        /**
         * Set sclm string value
         *
         * @param sclm string value
         * @return Builder this object
         */
        public Builder sclm(final String sclm) {
            this.sclm = sclm;
            return this;
        }

        /**
         * Return Member object based on Builder this object
         *
         * @return Member object
         */
        public Member build() {
            return new Member(this);
        }

    }

}

