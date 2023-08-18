/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.response;

import zowe.client.sdk.zostso.message.ZosmfTsoResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Tso collected Responses
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class CollectedResponses {

    /**
     * z/OSMF synchronous most tso command response messages.
     */
    private final List<ZosmfTsoResponse> tsos;

    /**
     * Appended collected messages including READY prompt at the end.
     */
    private final Optional<String> messages;

    /**
     * CollectedResponses constructor
     *
     * @param tsoLst   tso list of ZosmfTsoResponse objects
     * @param messages tso messages
     * @author Frank Giordano
     */
    public CollectedResponses(final List<ZosmfTsoResponse> tsoLst, final String messages) {
        this.tsos = Objects.requireNonNullElse(tsoLst, Collections.emptyList());
        this.messages = Optional.ofNullable(messages);
    }

    /**
     * Retrieve messages specified
     *
     * @return messages
     */
    public Optional<String> getMessages() {
        return messages;
    }

    /**
     * Retrieve tsos specified
     *
     * @return list of ZosmfTsoResponse objects
     */
    public List<ZosmfTsoResponse> getTsos() {
        return tsos;
    }

    @Override
    public String toString() {
        return "CollectedResponses{" +
                "tsos=" + tsos +
                ", messages=" + messages +
                '}';
    }

}
