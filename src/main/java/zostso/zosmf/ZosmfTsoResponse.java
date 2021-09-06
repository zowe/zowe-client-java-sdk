/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso.zosmf;

import java.util.List;
import java.util.Optional;

/**
 * z/OSMF synchronous most tso command response messages. See the z/OSMF REST API publication for complete details.
 *
 * @author Frank Giordano
 * @version 1.0
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
    private final Optional<Boolean> reused;

    /**
     * Timeout indicator
     */
    private final Optional<Boolean> timeout;

    /**
     * z/OSMF messages
     */
    private final Optional<List<ZosmfMessages>> msgData;

    /**
     * Id of the tso session
     */
    private final Optional<String> sessionId;

    /**
     * TSO/E messages that were received during the request
     */
    private Optional<List<TsoMessages>> tsoData;

    /**
     * Application messages
     */
    private final Optional<String> appData;

    private ZosmfTsoResponse(ZosmfTsoResponse.Builder builder) {
        this.servletKey = Optional.ofNullable(builder.servletKey);
        this.queueId = Optional.ofNullable(builder.queueId);
        this.ver = Optional.ofNullable(builder.ver);
        this.reused = Optional.ofNullable(builder.reused);
        this.timeout = Optional.ofNullable(builder.timeout);
        this.msgData = Optional.ofNullable(builder.msgData);
        this.sessionId = Optional.ofNullable(builder.sessionId);
        this.tsoData = Optional.ofNullable(builder.tsoData);
        this.appData = Optional.ofNullable(builder.appData);
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey value
     * @author Frank Giordano
     */
    public Optional<String> getServletKey() {
        return servletKey;
    }

    /**
     * Retrieve queueId specified
     *
     * @return queueId value
     * @author Frank Giordano
     */
    public Optional<String> getQueueId() {
        return queueId;
    }

    /**
     * Retrieve ver specified
     *
     * @return ver value
     * @author Frank Giordano
     */
    public Optional<String> getVer() {
        return ver;
    }

    /**
     * Retrieve reused specified
     *
     * @return reused value
     * @author Frank Giordano
     */
    public Optional<Boolean> getReused() {
        return reused;
    }

    /**
     * Retrieve timeout specified
     *
     * @return timeout value
     * @author Frank Giordano
     */
    public Optional<Boolean> getTimeout() {
        return timeout;
    }

    /**
     * Retrieve msgData specified
     *
     * @return msgData value
     * @author Frank Giordano
     */
    public Optional<List<ZosmfMessages>> getMsgData() {
        return msgData;
    }

    /**
     * Retrieve sessionId specified
     *
     * @return sessionId value
     * @author Frank Giordano
     */
    public Optional<String> getSessionId() {
        return sessionId;
    }

    /**
     * Retrieve tsoData specified
     *
     * @return tsoData value
     * @author Frank Giordano
     */
    public Optional<List<TsoMessages>> getTsoData() {
        return tsoData;
    }

    /**
     * Retrieve appData specified
     *
     * @return appData value
     * @author Frank Giordano
     */
    public Optional<String> getAppData() {
        return appData;
    }

    /**
     * Assign tsoData message list
     *
     * @param tsoData message list
     * @author Frank Giordano
     */
    public void setTsoData(List<TsoMessages> tsoData) {
        this.tsoData = Optional.ofNullable(tsoData);
    }

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

        public ZosmfTsoResponse.Builder servletKey(String servletKey) {
            this.servletKey = servletKey;
            return this;
        }

        public ZosmfTsoResponse.Builder queueId(String queueId) {
            this.queueId = queueId;
            return this;
        }

        public ZosmfTsoResponse.Builder ver(String ver) {
            this.ver = ver;
            return this;
        }

        public ZosmfTsoResponse.Builder reused(boolean reused) {
            this.reused = reused;
            return this;
        }

        public ZosmfTsoResponse.Builder timeout(boolean timeout) {
            this.timeout = timeout;
            return this;
        }

        public ZosmfTsoResponse.Builder msgData(List<ZosmfMessages> msgData) {
            this.msgData = msgData;
            return this;
        }

        public ZosmfTsoResponse.Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public ZosmfTsoResponse.Builder tsoData(List<TsoMessages> tsoData) {
            this.tsoData = tsoData;
            return this;
        }

        public ZosmfTsoResponse.Builder tsoData(String appData) {
            this.appData = appData;
            return this;
        }

        public ZosmfTsoResponse build() {
            return new ZosmfTsoResponse(this);
        }

    }

}
