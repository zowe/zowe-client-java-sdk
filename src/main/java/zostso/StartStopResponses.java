/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso;

import zostso.zosmf.ZosmfTsoResponse;

import java.util.List;
import java.util.Optional;

public class StartStopResponses {

    private Optional<ZosmfTsoResponse> zosmfTsoResponse;
    private Optional<List<ZosmfTsoResponse>> collectedResponses;
    private Optional<String> failureResponse;
    private Optional<String> servletKey;
    private Optional<String> messages;
    private boolean success;

    public StartStopResponses(ZosmfTsoResponse zosmfTsoResponse) {
        this.zosmfTsoResponse = Optional.ofNullable(zosmfTsoResponse);
        if (zosmfTsoResponse.getMsgData().isPresent()) {
            this.success = false;
            if (zosmfTsoResponse.getMsgData().isPresent())
                this.failureResponse =
                        Optional.ofNullable(zosmfTsoResponse.getMsgData().get().get(0).getMessageText());
            else
                this.failureResponse = Optional.of("Unknown error");
        }
        else this.success = true;
        if (zosmfTsoResponse.getServletKey().isPresent()) {
            this.servletKey = Optional.of(zosmfTsoResponse.getServletKey().get());
        }

        // TODO
        // do we put all tsoData tso messages into this.messages?
    }

    public Optional<ZosmfTsoResponse> getZosmfTsoResponse() {
        return zosmfTsoResponse;
    }

    public Optional<List<ZosmfTsoResponse>> getCollectedResponses() {
        return collectedResponses;
    }

    public void setCollectedResponses(List<ZosmfTsoResponse> collectedResponses) {
        this.collectedResponses = Optional.ofNullable(collectedResponses);
    }

    public Optional<String> getFailureResponse() {
        return failureResponse;
    }

    public Optional<String> getServletKey() {
        return servletKey;
    }

    public void setServletKey(String servletKey) {
        this.servletKey = Optional.ofNullable(servletKey);
    }

    public Optional<String> getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = Optional.ofNullable(messages);
    }

    public boolean isSuccess() {
        return success;
    }

}
