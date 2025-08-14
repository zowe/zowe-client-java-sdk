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
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-retrieve-contents-zos-unix-file">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 4.0
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
     * If true, performs binary read instead of text.
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
     * file are retrieved. When SSS is omitted (i.e., EEE), the final EEE records of the file are retrieved.
     * <p>
     * SSS,NNN
     * <p>
     * Where SSS identifies the start record and NNN identifies the number of records to be retrieved.
     * <p>
     * Usage note: If zero bytes returned due to the range specified, status code 500 is returned.
     */
    private final Optional<String> recordsRange;

    /**
     * GetParams constructor
     *
     * @param builder GetParams.Builder builder
     * @author James Kostrewski
     */
    public GetParams(final GetParams.Builder builder) {
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
     */
    public Optional<String> getSearch() {
        return search;
    }

    /**
     * Retrieve research value
     *
     * @return research value
     */
    public Optional<String> getResearch() {
        return research;
    }

    /**
     * Retrieve insensitive boolean value
     *
     * @return boolean true or false
     */
    public boolean isInsensitive() {
        return insensitive;
    }

    /**
     * Retrieve maxreturnsize value
     *
     * @return maxreturnsize value
     */
    public OptionalInt getMaxReturnSize() {
        return maxreturnsize;
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
     * Retrieve is binary specified
     *
     * @return boolean true or false
     */
    public boolean isBinary() {
        return binary;
    }

    /**
     * Retrieve is recordsRange specified
     *
     * @return boolean true or false
     */
    public Optional<String> getRecordsRange() {
        return recordsRange;
    }

    /**
     * Return string value representing GetParams object
     *
     * @return string representation of GetParams
     */
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

    /**
     * Builder class for GetParams
     */
    public static class Builder {

        /**
         * The file is searched for the first line that contains the string, without respect to case (by default).
         * Optionally, insensitive=false may be specified for case-sensitive matching.
         * This parameter may not be specified with the research parameter.
         */
        private String search;

        /**
         * The file is searched for the first line that matches the given extended regular expression.
         * This parameter may not be specified with the search parameter.
         */
        private String research;

        /**
         * The default is 'true'. When 'true', searches (search and research) are case-insensitive.
         * For case-sensitive searches, specify 'false'.
         */
        private boolean insensitive = true;

        /**
         * This parameter may be specified only with search or research parameters.
         * The value given is the maximum number of lines to return.
         * The default, if not specified, is 100.
         */
        private Integer maxreturnsize;

        /**
         * Internal use to count number of query parameters specified
         */
        private int queryCount = 0;

        /**
         * If true, performs binary read instead of text.
         */
        private boolean binary = false;

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
        private String recordsRange;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set search string value
         *
         * @param search string value
         * @return Builder this object
         */
        public Builder search(final String search) {
            if (this.research != null) {
                throw new IllegalStateException("cannot specify both search and research parameters");
            }
            this.search = search;
            queryCount++;
            return this;
        }

        /**
         * Set research value
         *
         * @param research string value
         * @return GetParams.Builder this object
         */
        public Builder research(final String research) {
            if (this.search != null) {
                throw new IllegalStateException("cannot specify both search and research parameters");
            }
            this.research = research;
            queryCount++;
            return this;
        }

        /**
         * Set insensitive boolean value
         *
         * @param insensitive boolean true or false value
         * @return Builder this object
         */
        public Builder insensitive(final boolean insensitive) {
            this.insensitive = insensitive;
            queryCount++;
            return this;
        }

        /**
         * Set binary int value
         *
         * @param maxreturnsize int value
         * @return Builder this object
         */
        public Builder maxreturnsize(final int maxreturnsize) {
            this.maxreturnsize = maxreturnsize;
            queryCount++;
            return this;
        }

        /**
         * Set binary boolean value
         *
         * @param binary boolean true or false value
         * @return Builder this object
         */
        public Builder binary(final boolean binary) {
            this.binary = binary;
            return this;
        }

        /**
         * Set recordsRange value
         *
         * @param recordsRange string value
         * @return Builder this object
         */
        public Builder recordsRange(final String recordsRange) {
            this.recordsRange = recordsRange;
            return this;
        }

        /**
         * Return GetParams object based on Builder this object
         *
         * @return GetParams object
         */
        public GetParams build() {
            return new GetParams(this);
        }

    }

}
