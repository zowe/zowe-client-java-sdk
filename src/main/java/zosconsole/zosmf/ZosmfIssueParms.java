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

public class ZosmfIssueParms {

    private Optional<String> cmd = Optional.empty();
    private Optional<String> solKey = Optional.empty();
    private Optional<String> system = Optional.empty();
    private Optional<String> async = Optional.empty();

    public Optional<String> getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = Optional.ofNullable(cmd);
    }

    public Optional<String> getSolKey() {
        return solKey;
    }

    public void setSolKey(String solKey) {
        this.solKey = Optional.ofNullable(solKey);
    }

    public Optional<String> getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = Optional.ofNullable(system);
    }

    public Optional<String> getAsync() {
        return async;
    }

    public void setAsync(String async) {
        this.async = Optional.ofNullable(async);
    }

    @Override
    public String toString() {
        return "ZosmfIssueParms{" +
                "cmd=" + cmd +
                ", solKey=" + solKey +
                ", system=" + system +
                ", async=" + async +
                '}';
    }

}
