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

public class ZosmfTsoResponse {

    private Optional<String> servletKey;
    private Optional<String> queueId;
    private Optional<String> ver;
    private Optional<Boolean> reused;
    private Optional<Boolean> timeout;
    private Optional<List<ZosmfMessages>> msgData;
    private Optional<String> sessionId;
    private Optional<List<TsoMessages>> tsoData;
    private Optional<String> appData;

    public ZosmfTsoResponse(ZosmfTsoResponse.Builder builder) {
        if (builder.servletKey != null)
            this.servletKey = Optional.ofNullable(builder.servletKey);
        else this.servletKey = Optional.empty();

        if (builder.queueId != null)
            this.queueId = Optional.ofNullable(builder.queueId);
        else this.queueId = Optional.empty();

        if (builder.ver != null)
            this.ver = Optional.ofNullable(builder.ver);
        else this.ver = Optional.empty();

        if (builder.reused)
            this.reused = Optional.ofNullable(builder.reused);
        else this.reused = Optional.empty();

        if (builder.timeout)
            this.timeout = Optional.ofNullable(builder.timeout);
        else this.timeout = Optional.empty();

        if (builder.msgData != null)
            this.msgData = Optional.ofNullable(builder.msgData);
        else this.msgData = Optional.empty();

        if (builder.sessionId != null)
            this.sessionId = Optional.ofNullable(builder.sessionId);
        else this.sessionId = Optional.empty();

        if (builder.tsoData != null)
            this.tsoData = Optional.ofNullable(builder.tsoData);
        else this.tsoData = Optional.empty();

        if (builder.appData != null)
            this.appData = Optional.ofNullable(builder.appData);
        else this.appData = Optional.empty();
    }

    public Optional<String> getServletKey() {
        return servletKey;
    }

    public Optional<String> getQueueId() {
        return queueId;
    }

    public Optional<String> getVer() {
        return ver;
    }

    public Optional<Boolean> getReused() {
        return reused;
    }

    public Optional<Boolean> getTimeout() {
        return timeout;
    }

    public Optional<List<ZosmfMessages>> getMsgData() {
        return msgData;
    }

    public Optional<String> getSessionId() {
        return sessionId;
    }

    public Optional<List<TsoMessages>> getTsoData() {
        return tsoData;
    }

    public Optional<String> getAppData() {
        return appData;
    }

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
