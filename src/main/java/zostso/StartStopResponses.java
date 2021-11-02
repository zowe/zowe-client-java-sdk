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
    private final ZosmfTsoResponse zosmfTsoResponse;

    /**
     * Collected responses from z/OSMF
     */
    private List<ZosmfTsoResponse> collectedResponses;

    /**
     * If an error occurs, returns the error which contains cause error.
     */
    private final String failureResponse;

    /**
     * Servlet key from IZosmfTsoResponse
     */
    private String servletKey;

    /**
     * Appended collected messages including READY prompt at the end.
     */
    private final String messages;

    /**
     * True if the command was issued and the responses were collected.
     */
    private final boolean success;

    /**
     * StartStopResponses constructor
     *
     * @param zosmfTsoResponse tso response
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public StartStopResponses(ZosmfTsoResponse zosmfTsoResponse, CollectedResponses collectedResponses) throws Exception {
        if (zosmfTsoResponse == null) throw new Exception("zosmfTsoResponse is null");

        this.zosmfTsoResponse = zosmfTsoResponse;
        if (!zosmfTsoResponse.getMsgData().isEmpty()) {
            // more data means more tso responses to come and as such tso command request has not ended in success yet
            this.success = false;
            ZosmfMessages zosmfMsg = zosmfTsoResponse.getMsgData().get(0);
            this.failureResponse = zosmfMsg.getMessageText().orElse(TsoConstants.ZOSMF_UNKNOWN_ERROR);
        } else {
            // no data means no more tso responses to come and as such tso command request has ended successfully 
            this.success = true;
            this.failureResponse = null;
        }

        this.servletKey = zosmfTsoResponse.getServletKey().orElseThrow(() -> new Exception("servletKey is missing"));

        StringBuilder buildMessage = new StringBuilder();
        List<TsoMessages> tsoMsgLst = zosmfTsoResponse.getTsoData();
        tsoMsgLst.forEach(buildMessage::append);
        this.messages = buildMessage.toString();
        this.collectedResponses = collectedResponses.getTsos();
    }

    /**
     * Retrieve zosmfTsoResponse specified
     *
     * @return ZosmfTsoResponse value
     * @author Frank Giordano
     */
    public Optional<ZosmfTsoResponse> getZosmfTsoResponse() {
        return Optional.of(zosmfTsoResponse);
    }

    /**
     * Retrieve collectedResponses specified
     *
     * @return ZosmfTsoResponse value
     * @author Frank Giordano
     */
    public List<ZosmfTsoResponse> getCollectedResponses() {
        return collectedResponses;
    }

    /**
     * Assign collectedResponses value
     *
     * @param collectedResponses list of ZosmfTsoResponse objects
     * @author Frank Giordano
     */
    public void setCollectedResponses(List<ZosmfTsoResponse> collectedResponses) {
        this.collectedResponses = collectedResponses;
    }

    /**
     * Retrieve failureResponse specified
     *
     * @return failureResponse value
     * @author Frank Giordano
     */
    public Optional<String> getFailureResponse() {
        return Optional.of(failureResponse);
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey value
     * @author Frank Giordano
     */
    public Optional<String> getServletKey() {
        return Optional.ofNullable(servletKey);
    }

    /**
     * Assign servletKey value
     *
     * @param servletKey key of tso address space
     * @author Frank Giordano
     */
    public void setServletKey(String servletKey) {
        this.servletKey = servletKey;
    }

    /**
     * Retrieve messages specified
     *
     * @return messages value
     * @author Frank Giordano
     */
    public Optional<String> getMessages() {
        return Optional.of(messages);
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
