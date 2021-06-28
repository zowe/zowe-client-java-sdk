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

public class IssueResponse {

    public boolean success;
    public StartStopResponses startResponse;
    public boolean startReady;
    public StartStopResponse stopResponses;
    public ZosmfTsoResponse zosmfResponse;
    public String commandResponses;

}
