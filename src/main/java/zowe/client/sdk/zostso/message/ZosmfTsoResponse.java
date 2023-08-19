/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.message;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * z/OSMF synchronous most tso command response messages. See the z/OSMF REST API publication for complete details.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfTsoResponse {

    /**
     * Unique identifier for the servlet entry for tso session
     */
    private final Optional<String> servletKey;

    /**
     * Message queue ID
     */
    private final Optional<String> queueId;

    /**
     * Structure version
     */
    private final Optional<String> ver;

    /**
     * Reconnected indicator
     */
    private final boolean reused;

    /**
     * Timeout indicator
     */
    private final boolean timeout;

    /**
     * z/OSMF messages
     */
    private final List<ZosmfMessages> msgData;

    /**
     * The id of the tso session
     */
    private final Optional<String> sessionId;

    /**
     * TSO/E messages that were received during the request
     */
    private List<TsoMessages> tsoData;

    /**
     * Application messages
     */
    private final Optional<String> appData;

    private ZosmfTsoResponse(final Builder builder) {
        this.servletKey = Optional.ofNullable(builder.servletKey);
        this.queueId = Optional.ofNullable(builder.queueId);
        this.ver = Optional.ofNullable(builder.ver);
        this.reused = builder.reused;
        this.timeout = builder.timeout;
        this.msgData = Objects.requireNonNullElse(builder.msgData, Collections.emptyList());
        this.sessionId = Optional.ofNullable(builder.sessionId);
        this.tsoData = Objects.requireNonNullElse(builder.tsoData, Collections.emptyList());
        this.appData = Optional.ofNullable(builder.appData);
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey value
     */
    public Optional<String> getServletKey() {
        return servletKey;
    }

    /**
     * Retrieve queueId specified
     *
     * @return queueId value
     */
    public Optional<String> getQueueId() {
        return queueId;
    }

    /**
     * Retrieve ver specified
     *
     * @return ver value
     */
    public Optional<String> getVer() {
        return ver;
    }

    /**
     * Retrieve is reused specified
     *
     * @return boolean true or false
     */
    public boolean isReused() {
        return reused;
    }

    /**
     * Retrieve is timeout specified
     *
     * @return boolean true or false
     */
    public boolean isTimeout() {
        return timeout;
    }

    /**
     * Retrieve msgData specified
     *
     * @return msgData value
     */
    public List<ZosmfMessages> getMsgData() {
        return msgData;
    }

    /**
     * Retrieve sessionId specified
     *
     * @return sessionId value
     */
    public Optional<String> getSessionId() {
        return sessionId;
    }

    /**
     * Retrieve tsoData specified
     *
     * @return tsoData value
     */
    public List<TsoMessages> getTsoData() {
        return tsoData;
    }

    /**
     * Retrieve appData specified
     *
     * @return appData value
     */
    public Optional<String> getAppData() {
        return appData;
    }

    /**
     * Assign tsoData message list
     *
     * @param tsoData message list
     */
    public void setTsoData(final List<TsoMessages> tsoData) {
        this.tsoData = Objects.requireNonNullElse(tsoData, Collections.emptyList());
    }

    /**
     * Return string value representing ZosmfTsoResponse object
     *
     * @return string representation of ZosmfTsoResponse
     */
    @Override
    public String toString() {
        return "ZosmfTsoResponse{" +
                "servletKey=" + servletKey +
                ", queueId=" + queueId +
                ", ver=" + ver +
                ", reused=" + reused +
                ", timeout=" + timeout +
                ", msgData=" + msgData +
                ", sessionId=" + sessionId +
                ", tsoData=" + tsoData +
                ", appData=" + appData +
                '}';
    }

    /**
     * Builder class for ZosmfTsoResponse
     */
    public static class Builder {

        /**
         * Unique identifier for the servlet entry for tso session
         */
        private String servletKey;

        /**
         * Message queue ID
         */
        private String queueId;

        /**
         * Structure version
         */
        private String ver;

        /**
         * Reconnected indicator
         */
        private boolean reused;

        /**
         * Timeout indicator
         */
        private boolean timeout;

        /**
         * z/OSMF messages
         */
        private List<ZosmfMessages> msgData;

        /**
         * The id of the tso session
         */
        private String sessionId;

        /**
         * TSO/E messages that were received during the request
         */
        private List<TsoMessages> tsoData;

        /**
         * Application messages
         */
        private String appData;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        public Builder servletKey(final String servletKey) {
            this.servletKey = servletKey;
            return this;
        }

        /**
         * Set queueId string value
         *
         * @param queueId string value
         * @return Builder this object
         */
        public Builder queueId(final String queueId) {
            this.queueId = queueId;
            return this;
        }

        /**
         * Set ver string value
         *
         * @param ver string value
         * @return Builder this object
         */
        public Builder ver(final String ver) {
            this.ver = ver;
            return this;
        }

        /**
         * Set reused boolean value
         *
         * @param reused boolean true or false value
         * @return Builder this object
         */
        public Builder reused(final boolean reused) {
            this.reused = reused;
            return this;
        }

        /**
         * Set timeout boolean value
         *
         * @param timeout boolean true or false value
         * @return Builder this object
         */
        public Builder timeout(final boolean timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Set msgData list of ZosmfMessages
         *
         * @param msgData list of ZosmfMessages
         * @return Builder this object
         */
        public Builder msgData(final List<ZosmfMessages> msgData) {
            this.msgData = msgData;
            return this;
        }

        /**
         * Set sessionId string value
         *
         * @param sessionId string value
         * @return Builder this object
         */
        public Builder sessionId(final String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        /**
         * Set tsoData list of TsoMessages
         *
         * @param tsoData list of TsoMessages
         * @return Builder this object
         */
        public Builder tsoData(final List<TsoMessages> tsoData) {
            this.tsoData = tsoData;
            return this;
        }

        /**
         * Set appData string value
         *
         * @param appData string value
         * @return Builder this object
         */
        public Builder tsoData(final String appData) {
            this.appData = appData;
            return this;
        }

        /**
         * Return ZosmfTsoResponse object based on Builder this object
         *
         * @return ZosmfTsoResponse this object
         */
        public ZosmfTsoResponse build() {
            return new ZosmfTsoResponse(this);
        }

    }

}
