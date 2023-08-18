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

    private ZosmfTsoResponse(final ZosmfTsoResponse.Builder builder) {
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

    public static class Builder {

        private String servletKey;
        private String queueId;
        private String ver;
        private boolean reused;
        private boolean timeout;
        private List<ZosmfMessages> msgData;
        private String sessionId;
        private List<TsoMessages> tsoData;
        private String appData;

        public ZosmfTsoResponse.Builder servletKey(final String servletKey) {
            this.servletKey = servletKey;
            return this;
        }

        public ZosmfTsoResponse.Builder queueId(final String queueId) {
            this.queueId = queueId;
            return this;
        }

        public ZosmfTsoResponse.Builder ver(final String ver) {
            this.ver = ver;
            return this;
        }

        public ZosmfTsoResponse.Builder reused(final boolean reused) {
            this.reused = reused;
            return this;
        }

        public ZosmfTsoResponse.Builder timeout(final boolean timeout) {
            this.timeout = timeout;
            return this;
        }

        public ZosmfTsoResponse.Builder msgData(final List<ZosmfMessages> msgData) {
            this.msgData = msgData;
            return this;
        }

        public ZosmfTsoResponse.Builder sessionId(final String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public ZosmfTsoResponse.Builder tsoData(final List<TsoMessages> tsoData) {
            this.tsoData = tsoData;
            return this;
        }

        public ZosmfTsoResponse.Builder tsoData(final String appData) {
            this.appData = appData;
            return this;
        }

        public ZosmfTsoResponse build() {
            return new ZosmfTsoResponse(this);
        }

    }

}
