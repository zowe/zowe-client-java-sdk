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

public class CollectedResponses {

    private Optional<List<ZosmfTsoResponse>> tsos;
    private Optional<String> messages;

    public CollectedResponses(List<ZosmfTsoResponse> tsos, String messages) {
        this.tsos = Optional.ofNullable(tsos);
        this.messages = Optional.ofNullable(messages);
    }

    public Optional<List<ZosmfTsoResponse>> getTsos() {
        return tsos;
    }

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
