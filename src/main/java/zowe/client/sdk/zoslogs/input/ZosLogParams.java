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

import java.util.Optional;

/**
 * The z/OSMF log API parameters. See the z/OSMF REST API documentation for full details.
 * <pre>
 * @see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a>
 * </pre>
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZosLogParams {

    /**
     * The z/OS log api time param. See "time" attribute from the following link:
     * <pre>
     * @see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a>
     * </pre>
     */
    private final Optional<String> startTime;
    /**
     * The z/OS log type to retrieve log data from. See "hardCopy" attribute from the following link:
     * <pre>
     * @see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a>
     * </pre>
     */
    private final Optional<HardCopyType> hardCopy;
    /**
     * The direction param. See "direction" attribute from the following link:
     * <pre>
     * @see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a>
     * </pre>
     */
    private final Optional<DirectionType> direction;
    /**
     * The timeRange param. See "timeRange" attribute from the following link:
     * <pre>
     * @see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a>
     * </pre>
     */
    private final Optional<String> timeRange;
    /**
     * The z/OSMF Console API returns '\r' or '\r\n' where line-breaks. Can attempt to replace these
     * sequences with '\n', but there may be cases where that is not preferable. Specify false to prevent processing.
     */
    private final boolean processResponses;

    /**
     * Return start time string value.
     *
     * @return String value
     * @author Frank Giordano
     */
    public Optional<String> getStartTime() {
        return startTime;
    }

    /**
     * Return hard copy enum type.
     *
     * @return HardCopyType enum value
     * @author Frank Giordano
     */
    public Optional<HardCopyType> getHardCopy() {
        return hardCopy;
    }

    /**
     * Return direction type enum type.
     *
     * @return DirectionType enum type
     * @author Frank Giordano
     */
    public Optional<DirectionType> getDirection() {
        return direction;
    }

    /**
     * Return time range string value.
     *
     * @return string value
     * @author Frank Giordano
     */
    public Optional<String> getTimeRange() {
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

    private ZosLogParams(ZosLogParams.Builder builder) {
        this.startTime = Optional.ofNullable(builder.startTime);
        this.hardCopy = Optional.ofNullable(builder.hardCopy);
        this.direction = Optional.ofNullable(builder.direction);
        this.timeRange = Optional.ofNullable(builder.timeRange);
        this.processResponses = builder.processResponses;
    }

    public static class Builder {

        private String startTime;
        private HardCopyType hardCopy;
        private DirectionType direction;
        private String timeRange;
        private boolean processResponses;

        public ZosLogParams build() {
            return new ZosLogParams(this);
        }

        /**
         * Set the start time to retrieve log output from.
         * <p>
         * The default value is the current UNIX timestamp on the server.
         *
         * @param startTime A String that represents either a DateTime in this format: YYYY-MM-DDTHH:MM:SSZ.
         *                  <pre>@see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a></pre>
         * @return ZosLogParams.Builder this object
         * @author Frank Giordano
         */
        public ZosLogParams.Builder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        /**
         * Set the z/OS log type (OPERLOG or SYSLOG) to retrieve log data from.
         * <p>
         * If the hardcopy parameter is not specified, the API tries OPERLOG first.
         * If the OPERLOG is not enabled on the system, the API returns the SYSLOG to the user.
         *
         * @param hardCopy HardCopyType enum value.
         *                 <pre>@see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a></pre>
         * @return ZosLogParams.Builder this object
         * @author Frank Giordano
         */
        public ZosLogParams.Builder hardCopy(HardCopyType hardCopy) {
            this.hardCopy = hardCopy;
            return this;
        }

        /**
         * Direction enum representing either forward or backward direction to retrieve log data from.
         * <p>
         * The default is "backward," meaning that messages are retrieved backward from the specified time.
         *
         * @param direction DirectionType enum value.
         *                  <pre>@see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a></pre>
         * @return ZosLogParams.Builder this object
         * @author Frank Giordano
         */
        public ZosLogParams.Builder direction(DirectionType direction) {
            this.direction = direction;
            return this;
        }

        /**
         * Time range of log retrieval.
         * <p>
         * The default is 10m.
         *
         * @param timeRange range of log output to retrieve, the following are valid examples:
         *                  1s (one second), 10m (tem minutes), 24h (24 hours), etc.
         *                  <pre>@see <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-messages-from-hardcopy-log">IBM Reference</a></pre>
         * @return ZosLogParams.Builder this object
         * @author Frank Giordano
         */
        public ZosLogParams.Builder timeRange(String timeRange) {
            this.timeRange = timeRange;
            return this;
        }

        /**
         * The z/OSMF Console API returns '\r' or '\r\n' where line-breaks. Can attempt to replace these
         * sequences with '\n', but there may be cases where that is not preferable. Specify false to prevent processing.
         * <p>
         * Default is false.
         *
         * @param processResponses true of false should message item be parsed for newline characters
         * @return ZosLogParams.Builder this object
         * @author Frank Giordano
         */
        public ZosLogParams.Builder processResponses(boolean processResponses) {
            this.processResponses = processResponses;
            return this;
        }

    }

}