/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosconsole.zosmf;

import java.util.Optional;

public class ConsoleParms {

    private Optional<String> consoleName = Optional.empty();
    private Optional<Boolean> processResponses = Optional.empty();

    public Optional<String> getConsoleName() {
        return consoleName;
    }

    public void setConsoleName(String consoleName) {
        this.consoleName = Optional.ofNullable(consoleName);
    }

    public Optional<Boolean> getProcessResponses() {
        return processResponses;
    }

    public void setProcessResponses(Boolean processResponses) {
        this.processResponses = Optional.ofNullable(processResponses);;
    }

    @Override
    public String toString() {
        return "ConsoleParms{" +
                "consoleName=" + consoleName +
                ", processResponses=" + processResponses +
                '}';
    }

}
