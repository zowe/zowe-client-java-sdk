/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.timer;

/**
 * Global Utility Class with static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class WaitUtil {

    /**
     * Wait by time specified.
     *
     * @param time in milliseconds
     * @author Frank Giordano
     */
    public static void wait(int time) {
        Timer timer = new Timer(time).initialize();
        while (!timer.isEnded()) {
        }
    }

}