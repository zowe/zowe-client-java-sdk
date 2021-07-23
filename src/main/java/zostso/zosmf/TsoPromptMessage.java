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

import java.util.Optional;

public class TsoPromptMessage {

    private Optional<String> version;
    private Optional<String> hidden;

    public TsoPromptMessage() {
    }

    public TsoPromptMessage(Optional<String> version, Optional<String> hidden) {
        this.version = version;
        this.hidden = hidden;
    }

    public Optional<String> getVersion() {
        return version;
    }

    public void setVersion(Optional<String> version) {
        this.version = version;
    }

    public Optional<String> getHidden() {
        return hidden;
    }

    public void setHidden(Optional<String> hidden) {
        this.hidden = hidden;
    }

}
