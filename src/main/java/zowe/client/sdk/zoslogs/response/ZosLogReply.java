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
package zowe.client.sdk.zoslogs.response;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Standard log response document. Represents the details about the messages and logs.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosLogReply {

    /**
     * Specify the timezone of the z/OS system. Valid values for the timezone range from -12 to 12.
     * For example, "-3" means UTC-3 timezone.
     */
    private final OptionalLong timeZone;

    /**
     * The UNIX timestamp. This value could be used in a later request to specify a starting timestamp.
     * Logs in the "nextTimestamp" are not returned to the current response.
     */
    private final OptionalLong nextTimeStamp;

    /**
     * Indicates the source of the log.
     * Value "OPERLOG" indicates the operation log.
     */
    private final Optional<String> source;

    /**
     * Total number of messages returned to the response.
     */
    private final OptionalLong totalItems;

    /**
     * JSON array of messages
     */
    private final List<ZosLogItem> items;

    /**
     * ZosLogReply constructor
     *
     * @param timeZone      long timezone value returned from response
     * @param nextTimeStamp long nextTimestamp value returned from response
     * @param source        string source value returned from response
     * @param totalItems    long totalitems value returned from response
     * @param items         ZosLogItem object items returned from response
     * @author Frank Giordano
     */
    public ZosLogReply(final Long timeZone, final Long nextTimeStamp, final String source, final Long totalItems,
                       final List<ZosLogItem> items) {
        if (timeZone == null) {
            this.timeZone = OptionalLong.empty();
        } else {
            this.timeZone = OptionalLong.of(timeZone);
        }
        if (nextTimeStamp == 0) {
            this.nextTimeStamp = OptionalLong.empty();
        } else {
            this.nextTimeStamp = OptionalLong.of(nextTimeStamp);
        }
        this.source = Optional.ofNullable(source);
        if (totalItems == 0) {
            this.totalItems = OptionalLong.empty();
        } else {
            this.totalItems = OptionalLong.of(totalItems);
        }
        this.items = items;
    }

    /**
     * Return timeZone OptionalLong value.
     *
     * @return OptionalLong value
     */
    public OptionalLong getTimeZone() {
        return timeZone;
    }

    /**
     * Return nextTimeStamp OptionalLong value.
     *
     * @return OptionalLong value
     */
    public OptionalLong getNextTimeStamp() {
        return nextTimeStamp;
    }

    /**
     * Return source Optional value.
     *
     * @return OptionalLong value
     */
    public Optional<String> getSource() {
        return source;
    }

    /**
     * Return totalItems OptionalLong value.
     *
     * @return OptionalLong value
     */
    public OptionalLong getTotalItems() {
        return totalItems;
    }

    /**
     * Return items value.
     *
     * @return OptionalLong value
     */
    public List<ZosLogItem> getItemLst() {
        return items;
    }

}