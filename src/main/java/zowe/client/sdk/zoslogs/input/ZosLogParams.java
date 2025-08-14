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

import zowe.client.sdk.zoslogs.types.DirectionType;
import zowe.client.sdk.zoslogs.types.HardCopyType;

import java.util.Optional;

/**
 * The z/OSMF log API parameters. See the z/OSMF REST API documentation for full details.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a>
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosLogParams {

    /**
     * The z/OS log api time parameter. This field is optional.
     * <p>
     * Specifies when z/OSMF starts to retrieve messages in the ISO 8601 JSON date and time format.
     * For example, 2021-01-26T03:33Z.
     * <p>
     * The default value is the current UNIX timestamp on the server.
     * This value is used if the timestamp parameter is not specified.
     */
    private final Optional<String> startTime;

    /**
     * Specify the source where the logs come from. This field is optional.
     * <p>
     * If the hardcopy parameter is not specified, the API tries OPERLOG first.
     * If the OPERLOG is not enabled on the system, the API returns the SYSLOG to the user.
     */
    private final Optional<HardCopyType> hardCopy;

    /**
     * Specifies the direction (from a specified time) in which messages are retrieved. This field is optional.
     * The options are 'forward' or 'backward'. These strings are case-insensitive.
     * <p>
     * The default is 'backward', meaning that messages are retrieved backward from the specified time.
     */
    private final Optional<DirectionType> direction;

    /**
     * Specifies the time range for which the log is to be retrieved. This field is optional.
     * <p>
     * Supported time units include s, m, and h for seconds, minutes, and hours.
     * For example, 10s, 10m, 10h.
     * The format is nnnu, where nnn is a number 1-999 and u is one of the time units "s", "m", or "h".
     * For example, 999s of 20m.
     * <p>
     * The default is 10m.
     */
    private final Optional<String> timeRange;

    /**
     * The z/OSMF Console API returns '\r' or '\r\n' where line-breaks. Can attempt to replace these
     * sequences with '\n', but there may be cases where that are not preferable. Specify false to prevent processing.
     */
    private final boolean processResponses;

    /**
     * Internal use to count the number of query parameters specified
     */
    private final int queryCount;

    /**
     * ZosLogParams constructor
     *
     * @param builder Builder object
     * @author Frank Giordano
     */
    private ZosLogParams(final Builder builder) {
        this.startTime = Optional.ofNullable(builder.startTime);
        this.hardCopy = Optional.ofNullable(builder.hardCopy);
        this.direction = Optional.ofNullable(builder.direction);
        this.timeRange = Optional.ofNullable(builder.timeRange);
        this.processResponses = builder.processResponses;
        this.queryCount = builder.queryCount;
    }

    /**
     * Return start time string value.
     *
     * @return String value
     */
    public Optional<String> getStartTime() {
        return startTime;
    }

    /**
     * Return hard copy enum type.
     *
     * @return HardCopyType enum value
     */
    public Optional<HardCopyType> getHardCopy() {
        return hardCopy;
    }

    /**
     * Return direction type enum type.
     *
     * @return DirectionType enum type
     */
    public Optional<DirectionType> getDirection() {
        return direction;
    }

    /**
     * Return time range string value.
     *
     * @return string value
     */
    public Optional<String> getTimeRange() {
        return timeRange;
    }

    /**
     * Is process response specified if so, handle JSON data differently?
     *
     * @return boolean true or false
     */
    public boolean isProcessResponses() {
        return processResponses;
    }

    /**
     * Retrieve queryCount value
     *
     * @return queryCount value
     */
    public int getQueryCount() {
        return queryCount;
    }

    /**
     * Return string value representing ZosLogParams object
     *
     * @return string representation of ZosLogParams
     */
    @Override
    public String toString() {
        return "ZosLogParams{" +
                "startTime=" + startTime +
                ", hardCopy=" + hardCopy +
                ", direction=" + direction +
                ", timeRange=" + timeRange +
                ", processResponses=" + processResponses +
                '}';
    }

    /**
     * Builder class for ZosLogParams
     */
    public static class Builder {

        /**
         * The z/OS log api time parameter. This field is optional.
         * <p>
         * Specifies when z/OSMF starts to retrieve messages in the ISO 8601 JSON date and time format.
         * For example, 2021-01-26T03:33:18.065Z.
         * <p>
         * The default value is the current UNIX timestamp on the server.
         * This value is used if the timestamp parameter is not specified.
         */
        private String startTime;

        /**
         * Specify the source where the logs come from. This field is optional.
         * <p>
         * If the hardcopy parameter is not specified, the API tries OPERLOG first.
         * If the OPERLOG is not enabled on the system, the API returns the SYSLOG to the user.
         */
        private HardCopyType hardCopy;

        /**
         * Specifies the direction (from a specified time) in which messages are retrieved. This field is optional.
         * The options are 'forward' or 'backward'. These strings are case-insensitive.
         * <p>
         * The default is 'backward', meaning that messages are retrieved backward from the specified time.
         */
        private DirectionType direction;

        /**
         * Specifies the time range for which the log is to be retrieved. This field is optional.
         * <p>
         * Supported time units include s, m, and h for seconds, minutes, and hours.
         * For example, 10s, 10m, 10h.
         * The format is nnnu, where nnn is a number 1-999 and u is one of the time units "s", "m", or "h".
         * For example, 999s of 20m.
         * <p>
         * The default is 10m.
         */
        private String timeRange;

        /**
         * The z/OSMF Console API returns '\r' or '\r\n' where line-breaks. Can attempt to replace these
         * sequences with '\n', but there may be cases where that are not preferable. Specify false to prevent processing.
         */
        private boolean processResponses;

        /**
         * Internal use to count the number of query parameters specified
         */
        private int queryCount = 0;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set the start time to retrieve log output from.
         * <p>
         * The default value is the current UNIX timestamp on the server.
         *
         * @param startTime A String that represents either a DateTime in this format: YYYY-MM-DDTHH:MM:SSZ.
         * @return Builder this object
         */
        public Builder startTime(final String startTime) {
            this.startTime = startTime;
            queryCount++;
            return this;
        }

        /**
         * Set the z/OS log type (OPERLOG or SYSLOG) to retrieve log data from.
         * <p>
         * If the hardcopy parameter is not specified, the API tries OPERLOG first.
         * If the OPERLOG is not enabled on the system, the API returns the SYSLOG to the user.
         *
         * @param hardCopy HardCopyType enum value.
         * @return Builder this object
         */
        public Builder hardCopy(final HardCopyType hardCopy) {
            this.hardCopy = hardCopy;
            queryCount++;
            return this;
        }

        /**
         * Direction enum representing either 'forward' or 'backward' direction to retrieve log data from.
         * <p>
         * The default is 'backward', meaning that messages are retrieved backward from the specified time.
         *
         * @param direction DirectionType enum value.
         * @return Builder this object
         */
        public Builder direction(final DirectionType direction) {
            this.direction = direction;
            queryCount++;
            return this;
        }

        /**
         * Time range of log retrieval.
         * <p>
         * The default is 10m.
         *
         * @param timeRange range of log output to retrieve, the following are valid examples:
         *                  1s (one second), 10m (tem minutes), 24h (24 hours), etc.
         * @return Builder this object
         */
        public Builder timeRange(final String timeRange) {
            this.timeRange = timeRange;
            queryCount++;
            return this;
        }

        /**
         * The z/OSMF Console API returns '\r' or '\r\n' where line-breaks. Can attempt to replace these
         * sequences with '\n', but there may be cases where that are not preferable. Specify false to prevent processing.
         * <p>
         * Default is false.
         *
         * @param processResponses true of false should message item be parsed for newline characters
         * @return Builder this object
         */
        public Builder processResponses(final boolean processResponses) {
            this.processResponses = processResponses;
            queryCount++;
            return this;
        }

        /**
         * Return ZosLogParams object based on Builder this object
         *
         * @return ZosLogParams this object
         */
        public ZosLogParams build() {
            return new ZosLogParams(this);
        }

    }

}