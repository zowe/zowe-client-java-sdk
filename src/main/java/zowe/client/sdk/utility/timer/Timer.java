/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zowe.client.sdk.utility.timer;

/**
 * Timer class to help support wait time operations.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class Timer {

    /**
     * Wait by the time specified in milliseconds.
     */
    private final int waitTime;

    /**
     * End by time specified in milliseconds.
     */
    private long endTime;

    /**
     * Timer constructor
     *
     * @param waitTime in milliseconds
     * @author Frank Giordano
     */
    public Timer(final int waitTime) {
        this.waitTime = waitTime;
        this.endTime = System.currentTimeMillis();
    }

    /**
     * Initialize time construct before calling isEnded
     *
     * @return timer object
     * @author Frank Giordano
     */
    public Timer initialize() {
        final long currentTime = System.currentTimeMillis();
        endTime = currentTime + waitTime;
        return this;
    }

    /**
     * Has the current time range ended yet?
     *
     * @return boolean true if time range reached
     * @author Frank Giordano
     */
    public boolean isEnded() {
        return System.currentTimeMillis() >= endTime;
    }

}