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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Tso collected Responses
 *
 * @author Frank Giordano
 * @version 1.0
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
     * @param tsos     tso list of ZosmfTsoResponse objects
     * @param messages tso messages
     * @author Frank Giordano
     */
    public CollectedResponses(List<ZosmfTsoResponse> tsos, String messages) {
        this.tsos = Objects.requireNonNullElse(tsos, Collections.emptyList());
        this.messages = Optional.ofNullable(messages);
    }

    /**
     * Retrieve tsos specified
     *
     * @return list of ZosmfTsoResponse objects
     * @author Frank Giordano
     */
    public List<ZosmfTsoResponse> getTsos() {
        return tsos;
    }

    /**
     * Retrieve messages specified
     *
     * @return messages
     * @author Frank Giordano
     */
    public Optional<String> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "CollectedResponses{" +
                "tsos=" + tsos +
                ", messages=" + messages +
                '}';
    }

}
