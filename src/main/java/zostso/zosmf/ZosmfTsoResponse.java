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

public class ZosmfTsoResponse {

    public String servletKey;
    public String queueId;
    public String ver;
    public boolean reused;
    public boolean timeout;
    public List<ZosmfMessages> msgData;
    public String sessionId;
    public List<TsoMessages> tsoData;
    public String appData;

    public String getServletKey() {
        return servletKey;
    }

    public void setServletKey(String servletKey) {
        this.servletKey = servletKey;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public boolean isReused() {
        return reused;
    }

    public void setReused(boolean reused) {
        this.reused = reused;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public List<ZosmfMessages> getMsgData() {
        return msgData;
    }

    public void setMsgData(List<ZosmfMessages> msgData) {
        this.msgData = msgData;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<TsoMessages> getTsoData() {
        return tsoData;
    }

    public void setTsoData(List<TsoMessages> tsoData) {
        this.tsoData = tsoData;
    }

    public String getAppData() {
        return appData;
    }

    public void setAppData(String appData) {
        this.appData = appData;
    }

}
