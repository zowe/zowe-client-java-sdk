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
package zowe.client.sdk.zoslogs.input;

import java.sql.Timestamp;

/**
 * The z/OSMF log API parameters. See the z/OSMF REST API documentation for full details.
 */
public class ZosLogParams {

    /**
     * The z/OS log api time param.
     */
    private final String startTime;
    /**
     * The direction param.
     */
    private final DirectionType direction;
    /**
     * The timeRange param.
     */
    private final String timeRange;
    /**
     * The z/OSMF Console API returns '\r' or '\r\n' where line-breaks. Can attempt to replace these
     * sequences with '\n', but there may be cases where that is not preferable. Specify false to prevent processing.
     */
    private final boolean processResponses;

    public ZosLogParams(String startTime, DirectionType direction, String timeRange, boolean processResponses) {
        this.startTime = startTime;
        this.direction = direction;
        this.timeRange = timeRange;
        this.processResponses = processResponses;
    }

    public ZosLogParams(String startTime, DirectionType direction, String timeRange) {
        this.startTime = startTime;
        this.direction = direction;
        this.timeRange = timeRange;
        this.processResponses = false;
    }

    public String getStartTime() {
        return startTime;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public boolean isProcessResponses() {
        return processResponses;
    }

}
