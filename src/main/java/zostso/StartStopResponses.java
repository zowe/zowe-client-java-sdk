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

import zostso.zosmf.TsoMessages;
import zostso.zosmf.ZosmfMessages;
import zostso.zosmf.ZosmfTsoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The TsoStartStop API responses
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class StartStopResponses {

    /**
     * Response from z/OSMF to start rest call
     */
    private Optional<ZosmfTsoResponse> zosmfTsoResponse;

    /**
     * Collected responses from z/OSMF
     */
    private Optional<List<ZosmfTsoResponse>> collectedResponses;

    /**
     * If an error occurs, returns the error which contains cause error.
     */
    private Optional<String> failureResponse;

    /**
     * Servlet key from IZosmfTsoResponse
     */
    private Optional<String> servletKey;

    /**
     * Appended collected messages including READY prompt at the end.
     */
    private Optional<String> messages;

    /**
     * True if the command was issued and the responses were collected.
     */
    private boolean success;

    /**
     * StartStopResponses constructor
     *
     * @param zosmfTsoResponse tso response
     * @author Frank Giordano
     */
    public StartStopResponses(ZosmfTsoResponse zosmfTsoResponse) {
        this.zosmfTsoResponse = Optional.ofNullable(zosmfTsoResponse);
        if (zosmfTsoResponse.getMsgData().isPresent()) {
            this.success = false;
            ZosmfMessages zOSMFMsg = zosmfTsoResponse.getMsgData().get().get(0);
            this.failureResponse = Optional.of(zOSMFMsg.getMessageText().orElse(TsoConstants.ZOSMF_UNKNOWN_ERROR));
        } else {
            this.success = true;
            this.failureResponse = Optional.empty();
        }
        if (zosmfTsoResponse.getServletKey().isPresent()) {
            this.servletKey = Optional.of(zosmfTsoResponse.getServletKey().get());
        }

        StringBuilder buildMessage = new StringBuilder();
        List<TsoMessages> tsoMsgLst = zosmfTsoResponse.getTsoData().orElse(new ArrayList<>());
        tsoMsgLst.forEach(msg -> buildMessage.append(msg));
        this.messages = Optional.of(buildMessage.toString());
    }

    /**
     * Retrieve zosmfTsoResponse specified
     *
     * @return ZosmfTsoResponse value
     * @author Frank Giordano
     */
    public Optional<ZosmfTsoResponse> getZosmfTsoResponse() {
        return zosmfTsoResponse;
    }

    /**
     * Retrieve collectedResponses specified
     *
     * @return ZosmfTsoResponse value
     * @author Frank Giordano
     */
    public Optional<List<ZosmfTsoResponse>> getCollectedResponses() {
        return collectedResponses;
    }

    /**
     * Assign collectedResponses value
     *
     * @param collectedResponses list of ZosmfTsoResponse objects
     * @author Frank Giordano
     */
    public void setCollectedResponses(List<ZosmfTsoResponse> collectedResponses) {
        this.collectedResponses = Optional.ofNullable(collectedResponses);
    }

    /**
     * Retrieve failureResponse specified
     *
     * @return failureResponse value
     * @author Frank Giordano
     */
    public Optional<String> getFailureResponse() {
        return failureResponse;
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
     * Assign servletKey value
     *
     * @param servletKey key of tso address space
     * @author Frank Giordano
     */
    public void setServletKey(String servletKey) {
        this.servletKey = Optional.ofNullable(servletKey);
    }

    /**
     * Retrieve messages specified
     *
     * @return messages value
     * @author Frank Giordano
     */
    public Optional<String> getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = Optional.ofNullable(messages);
    }

    /**
     * Retrieve success specified
     *
     * @return success value
     * @author Frank Giordano
     */
    public boolean isSuccess() {
        return success;
    }

}
