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

public class ZosmfIssueResponse {

    private Optional<String> cmdResponseUrl = Optional.empty();
    private Optional<String> cmdResponse = Optional.empty();
    private Optional<String> cmdResponseUri = Optional.empty();
    private Optional<String> cmdResponseKey = Optional.empty();
    private Optional<String> solKeyDetected = Optional.empty();

    public Optional<String> getCmdResponseUrl() {
        return cmdResponseUrl;
    }

    public void setCmdResponseUrl(String cmdResponseUrl) {
        this.cmdResponseUrl = Optional.ofNullable(cmdResponseUrl);
    }

    public Optional<String> getCmdResponse() {
        return cmdResponse;
    }

    public void setCmdResponse(String cmdResponse) {
        this.cmdResponse = Optional.ofNullable(cmdResponse);
    }

    public Optional<String> getCmdResponseUri() {
        return cmdResponseUri;
    }

    public void setCmdResponseUri(String cmdResponseUri) {
        this.cmdResponseUri = Optional.ofNullable(cmdResponseUri);
    }

    public Optional<String> getCmdResponseKey() {
        return cmdResponseKey;
    }

    public void setCmdResponseKey(String cmdResponseKey) {
        this.cmdResponseKey = Optional.ofNullable(cmdResponseKey);
    }

    public Optional<String> getSolKeyDetected() {
        return solKeyDetected;
    }

    public void setSolKeyDetected(String solKeyDetected) {
        this.solKeyDetected = Optional.ofNullable(solKeyDetected);
    }

    @Override
    public String toString() {
        return "ZosmfIssueResponse{" +
                "cmdResponseUrl=" + cmdResponseUrl +
                ", cmdResponse=" + cmdResponse +
                ", cmdResponseUri=" + cmdResponseUri +
                ", cmdResponseKey=" + cmdResponseKey +
                ", solKeyDetected=" + solKeyDetected +
                '}';
    }

}
