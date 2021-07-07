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

public class TsoResponseService {

    public static StartStopResponse populateStartAndStop(ZosmfTsoResponse zosmfResponse) {
        StartStopResponse startStopResponse = new StartStopResponse(false, zosmfResponse,
                zosmfResponse.getServletKey().isPresent() ? zosmfResponse.getServletKey().get() : "");

        if (zosmfResponse.getServletKey().isPresent()) {
            startStopResponse.setSuccess(true);
        } else if (zosmfResponse.getMsgData().isPresent()) {
            startStopResponse.setFailureResponse(zosmfResponse.getMsgData().isPresent() ?
                             zosmfResponse.getMsgData().get().get(0).getMessageText() : "zosmf error response");
        }

        return startStopResponse;
    }

}
