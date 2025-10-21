/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zoslogs.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import zowe.client.sdk.zoslogs.model.ZosLogItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard log response document. Represents the details about the messages and logs.
 *
 * <p>Refactored as an immutable Jackson POJO without Optional or builder.
 * Uses primitive {@code long} defaults for numeric fields when null.</p>
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ZosLogResponse {

    /**
     * Specify the timezone of the z/OS system. Valid values for the timezone range from -12 to 12.
     * For example, "-3" means UTC-3 timezone.
     */
    private final long timeZone;

    /**
     * The UNIX timestamp. This value could be used in a later request to specify a starting timestamp.
     * Logs in the "nextTimestamp" are not returned to the current response.
     */
    private final long nextTimeStamp;

    /**
     * Indicates the source of the log.
     * Value "OPERLOG" indicates the operation log.
     */
    private final String source;

    /**
     * Total number of messages returned to the response.
     */
    private final long totalItems;

    /**
     * JSON array of messages.
     */
    private final List<ZosLogItem> items;

    /**
     * Jackson constructor for ZosLogResponse.
     *
     * @param timeZone      Timezone value returned from response
     * @param nextTimeStamp Next timestamp value returned from response
     * @param source        Source string value returned from response
     * @param totalItems    Total items count returned from response
     * @param items         List of ZosLogItem messages returned from response
     */
    @JsonCreator
    public ZosLogResponse(
            @JsonProperty("timeZone") final Long timeZone,
            @JsonProperty("nextTimeStamp") final Long nextTimeStamp,
            @JsonProperty("source") final String source,
            @JsonProperty("totalItems") final Long totalItems,
            @JsonProperty("items") final List<ZosLogItem> items) {
        this.timeZone = timeZone == null ? 0L : timeZone;
        this.nextTimeStamp = nextTimeStamp == null ? 0L : nextTimeStamp;
        this.source = source == null ? "" : source;
        this.totalItems = totalItems == null ? 0L : totalItems;
        this.items = items == null ? new ArrayList<>() : items;
    }

    /**
     * Retrieve the timezone.
     *
     * @return long value representing the timezone (0 if absent)
     */
    public long getTimeZone() {
        return timeZone;
    }

    /**
     * Retrieve the next timestamp.
     *
     * @return long value representing the next timestamp (0 if absent)
     */
    public long getNextTimeStamp() {
        return nextTimeStamp;
    }

    /**
     * Retrieve the source of the log.
     *
     * @return source string
     */
    public String getSource() {
        return source;
    }

    /**
     * Retrieve the total number of items.
     *
     * @return long total number of messages (0 if absent)
     */
    public long getTotalItems() {
        return totalItems;
    }

    /**
     * Retrieve the list of log items.
     *
     * @return list of {@link ZosLogItem}
     */
    public List<ZosLogItem> getItems() {
        return items;
    }

    /**
     * Return string value representing ZosLogResponse object.
     *
     * @return string representation of ZosLogResponse
     */
    @Override
    public String toString() {
        return "ZosLogResponse{" +
                "timeZone=" + timeZone +
                ", nextTimeStamp=" + nextTimeStamp +
                ", source='" + source + '\'' +
                ", totalItems=" + totalItems +
                ", items=" + items +
                '}';
    }

}
