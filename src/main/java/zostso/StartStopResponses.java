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

public class StartStopResponses {

    public boolean success;
    public ZosmfTsoResponse zosmfTsoResponse;
    public List<ZosmfTsoResponse> collectedResponses;
    public String failureResponse;
    public String servletKey;
    public String messages;

    public StartStopResponses(ZosmfTsoResponse zosmfTsoResponse) {
        this.zosmfTsoResponse = zosmfTsoResponse;
    }

}
