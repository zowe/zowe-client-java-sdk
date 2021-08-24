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

public class TsoResponseMessage {

    private Optional<String> version;
    private Optional<String> data;

    public TsoResponseMessage() {
    }

    public TsoResponseMessage(Optional<String> version, Optional<String> data) {
        this.version = version;
        this.data = data;
    }

    public Optional<String> getVersion() {
        return version;
    }

    public void setVersion(Optional<String> version) {
        this.version = version;
    }

    public Optional<String> getData() {
        return data;
    }

    public void setData(Optional<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TsoResponseMessage{" +
                "version=" + version +
                ", data=" + data +
                '}';
    }

}
