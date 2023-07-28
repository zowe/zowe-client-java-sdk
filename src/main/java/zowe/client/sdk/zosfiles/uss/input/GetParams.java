/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.input;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Parameter container class forUnix System Services (USS) get operation
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 2.0
 */
public class GetParams {

    /**
     * The file is searched for the first line that contains the string, without respect to case (by default).
     * Optionally, insensitive=false may be specified for case-sensitive matching.
     * This parameter may not be specified with the research parameter.
     */
    private final Optional<String> search;

    /**
     * The file is searched for the first line that matches the given extended regular expression.
     * This parameter may not be specified with the search parameter.
     */
    private final Optional<String> research;

    /**
     * The default is 'true'. When 'true', searches (search and research) are case-insensitive.
     * For case-sensitive searches, specify 'false'.
     */
    private final boolean insensitive;

    /**
     * This parameter may be specified only with search or research parameters.
     * The value given is the maximum number of lines to return.
     * The default, if not specified, is 100.
     */
    private final OptionalInt maxreturnsize;

    /**
     * Internal use to count number of query parameters specified
     */
    private final int queryCount;

    /**
     * If true perform binary read instead of text.
     */
    private final boolean binary;

    /**
     * Specify a range of records (lines delimited by '\n') to retrieve from a file.
     * <p>
     * Specify the range as a string in the following format:
     * <p>
     * SSS-EEE
     * <p>
     * Where SSS identifies the start record and EEE identifies the end record to be retrieved.
     * Both values are relative offsets (0-based). When EEE is set to 0, records through the end of the
     * file are retrieved. When SSS is omitted (i.e. -EEE), the final EEE records of the file are retrieved.
     * <p>
     * SSS,NNN
     * <p>
     * Where SSS identifies the start record and NNN identifies the number of records to be retrieved.
     * <p>
     * Usage note: If zero bytes returned due to the range specified, status code 500 is returned.
     */
    private final Optional<String> recordsRange;

    public GetParams(GetParams.Builder builder) {
        this.search = Optional.ofNullable(builder.search);
        this.research = Optional.ofNullable(builder.research);
        this.insensitive = builder.insensitive;
        if (builder.maxreturnsize == null) {
            this.maxreturnsize = OptionalInt.empty();
        } else {
            this.maxreturnsize = OptionalInt.of(builder.maxreturnsize);
        }
        this.queryCount = builder.queryCount;
        this.binary = builder.binary;
        this.recordsRange = Optional.ofNullable(builder.recordsRange);
    }

    /**
     * Retrieve search value
     *
     * @return search value
     * @author James Kostrewski
     */
    public Optional<String> getSearch() {
        return search;
    }

    /**
     * Retrieve research value
     *
     * @return research value
     * @author James Kostrewski
     */
    public Optional<String> getResearch() {
        return research;
    }

    /**
     * Retrieve insensitive value
     *
     * @return insensitive value
     * @author James Kostrewski
     */
    public boolean isInsensitive() {
        return insensitive;
    }

    /**
     * Retrieve maxreturnsize value
     *
     * @return maxreturnsize value
     * @author James Kostrewski
     */
    public OptionalInt getMaxReturnSize() {
        return maxreturnsize;
    }

    /**
     * Retrieve queryCount value
     *
     * @return queryCount value
     * @author James Kostrewski
     */
    public int getQueryCount() {
        return queryCount;
    }

    /**
     * Retrieve binary value
     *
     * @return binary value
     * @author James Kostrewski
     */
    public boolean isBinary() {
        return binary;
    }

    /**
     * Retrieve recordsRange value
     *
     * @return recordsRange value
     * @author James Kostrewski
     */
    public Optional<String> getRecordsRange() {
        return recordsRange;
    }

    @Override
    public String toString() {
        return "GetParams{" +
                "search=" + search +
                ", research=" + research +
                ", insensitive=" + insensitive +
                ", maxreturnsize=" + maxreturnsize +
                ", queryCount=" + queryCount +
                ", binary=" + binary +
                ", queryCount=" + queryCount +
                ", recordsRange=" + recordsRange +
                '}';
    }

    public static class Builder {

        private String search;
        private String research;
        private boolean insensitive = true;
        private Integer maxreturnsize;
        private boolean binary = false;
        private int queryCount = 0;
        private String recordsRange;

        public GetParams build() {
            return new GetParams(this);
        }

        public GetParams.Builder search(String search) throws Exception {
            if (this.research != null) {
                throw new Exception("cannot specify both search and research parameters");
            }
            this.search = search;
            queryCount++;
            return this;
        }

        public GetParams.Builder research(String research) throws Exception {
            if (this.search != null) {
                throw new Exception("cannot specify both search and research parameters");
            }
            this.research = research;
            queryCount++;
            return this;
        }

        public GetParams.Builder insensitive(boolean insensitive) {
            this.insensitive = insensitive;
            queryCount++;
            return this;
        }

        public GetParams.Builder maxreturnsize(int maxreturnsize) {
            this.maxreturnsize = maxreturnsize;
            queryCount++;
            return this;
        }

        public GetParams.Builder binary(boolean binary) {
            this.binary = binary;
            return this;
        }

        public GetParams.Builder recordsRange(String recordsRange) {
            this.recordsRange = recordsRange;
            return this;
        }

    }

}
