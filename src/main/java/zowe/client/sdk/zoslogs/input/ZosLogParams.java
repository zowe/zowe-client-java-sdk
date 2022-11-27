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

/**
 * The z/OSMF log API parameters. See the z/OSMF REST API documentation for full details.
 *
 * @author Frank Giordano
 * @version 1.0
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

    /**
     * ZosLogParams constructor
     *
     * @param startTime        A String that represents either a Date in this format: YYYY-MM-DD
     *                         or a DataTime format: YYYY-MM-DDTHH:MM:SSZ
     * @param direction        a DirectionType enum value
     * @param timeRange        range of log output to retrieve, the following are valid examples:
     *                         1s (one second), 10m (tem minutes), 24h (24 hours), etc.
     * @param processResponses true of false should message item be parsed for newline characters
     * @author Frank Giordano
     */
    public ZosLogParams(String startTime, DirectionType direction, String timeRange, boolean processResponses) {
        this.startTime = startTime;
        this.direction = direction;
        this.timeRange = timeRange;
        this.processResponses = processResponses;
    }

    /**
     * Return start time string value.
     *
     * @return String value
     * @author Frank Giordano
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Return direction type enum type.
     *
     * @return DirectionType enum type
     * @author Frank Giordano
     */
    public DirectionType getDirection() {
        return direction;
    }

    /**
     * Return time range string value.
     *
     * @return string value
     * @author Frank Giordano
     */
    public String getTimeRange() {
        return timeRange;
    }

    /**
     * Return process response boolean value.
     *
     * @return boolean value
     * @author Frank Giordano
     */
    public boolean isProcessResponses() {
        return processResponses;
    }

}
