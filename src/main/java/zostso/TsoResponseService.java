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

import zostso.zosmf.ZosmfMessages;
import zostso.zosmf.ZosmfTsoResponse;

public class TsoResponseService {

    public static StartStopResponse populateStartAndStop(ZosmfTsoResponse zosmfResponse) {
        StartStopResponse startStopResponse = new StartStopResponse(false, zosmfResponse,
                zosmfResponse.getServletKey().orElse(""));

        startStopResponse.setSuccess(zosmfResponse.getServletKey().isPresent() ? true : false);
        if (zosmfResponse.getMsgData().isPresent()) {
            ZosmfMessages zosmfMsg = zosmfResponse.getMsgData().get().get(0);
            String msgText = zosmfMsg.getMessageText().orElse(TsoConstants.ZOSMF_UNKNOWN_ERROR);
            startStopResponse.setFailureResponse(msgText);
        }

        return startStopResponse;
    }

}
