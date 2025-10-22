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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a z/OS partition data set member
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
     * Member constructor for Jackson JSON parsing
     *
     * @param member the member name
     * @param vers   version number
     * @param mod    modification count
     * @param c4date creation date
     * @param m4date modification date
     * @param cnorc  cnorc value
     * @param inorc  inorc value
     * @param mnorc  mnorc value
     * @param mtime  mtime value
     * @param msec   msec value
     * @param user   user of member
     * @param sclm   sclm value
     * @author Frank Giordano
     */
    @JsonCreator
    public Member(
            @JsonProperty("member") String member,
            @JsonProperty("vers") Long vers,
            @JsonProperty("mod") Long mod,
            @JsonProperty("c4date") String c4date,
            @JsonProperty("m4date") String m4date,
            @JsonProperty("cnorc") Long cnorc,
            @JsonProperty("inorc") Long inorc,
            @JsonProperty("mnorc") Long mnorc,
            @JsonProperty("mtime") String mtime,
            @JsonProperty("msec") String msec,
            @JsonProperty("user") String user,
            @JsonProperty("sclm") String sclm
    ) {
        this.member = member;
        this.vers = vers;
        this.mod = mod;
        this.c4date = c4date;
        this.m4date = m4date;
        this.cnorc = cnorc;
        this.inorc = inorc;
        this.mnorc = mnorc;
        this.mtime = mtime;
        this.msec = msec;
        this.user = user;
        this.sclm = sclm;
    }

    /**
     * Retrieve the name of the member
     *
     * @return string value
     */
    public String getMember() {
        return member;
    }

    /**
     * Retrieve version number of the member
     *
     * @return long value
     */
    public Long getVers() {
        return vers;
    }

    /**
     * Retrieve modification number of the member
     *
     * @return long value
     */
    public Long getMod() {
        return mod;
    }

    /**
     * Retrieve creation date of the member
     *
     * @return string value
     */
    public String getC4date() {
        return c4date;
    }

    /**
     * Retrieve modification date of the member
     *
     * @return string value
     */
    public String getM4date() {
        return m4date;
    }

    /**
     * Retrieve cnorc of the member
     *
     * @return long value
     */
    public Long getCnorc() {
        return cnorc;
    }

    /**
     * Retrieve inorc of the member
     *
     * @return long value
     */
    public Long getInorc() {
        return inorc;
    }

    /**
     * Retrieve mnorc of the member
     *
     * @return long value
     */
    public Long getMnorc() {
        return mnorc;
    }

    /**
     * Retrieve mtime of the member
     *
     * @return string value
     */
    public String getMtime() {
        return mtime;
    }

    /**
     * Retrieve msec of the member
     *
     * @return string value
     */
    public String getMsec() {
        return msec;
    }

    /**
     * Retrieve user of the member
     *
     * @return string value
     */
    public String getUser() {
        return user;
    }

    /**
     * Retrieve sclm of the member
     *
     * @return string value
     */
    public String getSclm() {
        return sclm;
    }

    /**
     * Return string value representing a Member object
     *
     * @return string representation of Member
     */
    @Override
    public String toString() {
        return "Member{" +
                "member='" + member + '\'' +
                ", vers=" + vers +
                ", mod=" + mod +
                ", c4date='" + c4date + '\'' +
                ", m4date='" + m4date + '\'' +
                ", cnorc=" + cnorc +
                ", inorc=" + inorc +
                ", mnorc=" + mnorc +
                ", mtime='" + mtime + '\'' +
                ", msec='" + msec + '\'' +
                ", user='" + user + '\'' +
                ", sclm='" + sclm + '\'' +
                '}';
    }

}
