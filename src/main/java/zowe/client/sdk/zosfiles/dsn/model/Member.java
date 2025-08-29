/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.model;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Represents a z/OS partition data set member
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class Member {

    /**
     * The name of the member
     */
    private final String member;

    /**
     * The version of member
     */
    private final Long vers;

    /**
     * The number of modifications of member
     */
    private final Long mod;

    /**
     * The creation date of member
     */
    private final String c4date;

    /**
     * The modification date of member
     */
    private final String m4date;

    /**
     * The cnorc of member
     */
    private final Long cnorc;

    /**
     * The inorc of member
     */
    private final Long inorc;

    /**
     * The mnorc of member
     */
    private final Long mnorc;

    /**
     * The mtime of member
     */
    private final String mtime;

    /**
     * The msec of member
     */
    private final String msec;

    /**
     * The user of member
     */
    private final String user;

    /**
     * The sclm of member
     */
    private final String sclm;

    /**
     * Member constructor
     *
     * @param builder Builder object
     * @author Frank Giordano
     */
    private Member(final Builder builder) {
        this.member = builder.member;
        this.vers = builder.vers;
        this.mod = builder.mod;
        this.c4date = builder.c4date;
        this.m4date = builder.m4date;
        this.cnorc = builder.cnorc;
        this.inorc = builder.inorc;
        this.mnorc = builder.mnorc;
        this.mtime = builder.mtime;
        this.msec = builder.msec;
        this.user = builder.user;
        this.sclm = builder.sclm;
    }

    /**
     * Retrieve the name of the member
     *
     * @return Optional string value
     */
    public Optional<String> getMember() {
        return Optional.ofNullable(member);
    }

    /**
     * Retrieve version number of the member
     *
     * @return Optional long value
     */
    public OptionalLong getVers() {
        return (vers == null) ? OptionalLong.empty() : OptionalLong.of(vers);
    }

    /**
     * Retrieve modification number of the member
     *
     * @return Optional long value
     */
    public OptionalLong getMod() {
        return (mod == null) ? OptionalLong.empty() : OptionalLong.of(mod);
    }

    /**
     * Retrieve creation date of the member
     *
     * @return Optional string value
     */
    public Optional<String> getC4date() {
        return Optional.ofNullable(c4date);
    }

    /**
     * Retrieve modification date of the member
     *
     * @return Optional string value
     */
    public Optional<String> getM4date() {
        return Optional.ofNullable(m4date);
    }

    /**
     * Retrieve cnorc of the member
     *
     * @return Optional long value
     */
    public OptionalLong getCnorc() {
        return (cnorc == null) ? OptionalLong.empty() : OptionalLong.of(cnorc);
    }

    /**
     * Retrieve inorc of the member
     *
     * @return Optional long value
     */
    public OptionalLong getInorc() {
        return (inorc == null) ? OptionalLong.empty() : OptionalLong.of(inorc);
    }

    /**
     * Retrieve mnorc of the member
     *
     * @return Optional long value
     */
    public OptionalLong getMnorc() {
        return (mnorc == null) ? OptionalLong.empty() : OptionalLong.of(mnorc);
    }

    /**
     * Retrieve mtime of the member
     *
     * @return Optional string value
     */
    public Optional<String> getMtime() {
        return Optional.ofNullable(mtime);
    }

    /**
     * Retrieve msec of the member
     *
     * @return Optional string value
     */
    public Optional<String> getMsec() {
        return Optional.ofNullable(msec);
    }

    /**
     * Retrieve user of the member
     *
     * @return Optional string value
     */
    public Optional<String> getUser() {
        return Optional.ofNullable(user);
    }

    /**
     * Retrieve sclm of the member
     *
     * @return Optional string value
     */
    public Optional<String> getSclm() {
        return Optional.ofNullable(sclm);
    }

    /**
     * Return string value representing a Member object
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
         * The creation date of a member
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
         * Set mod value
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

