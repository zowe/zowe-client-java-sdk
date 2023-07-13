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
    private Optional<String> search;

    /**
     * The file is searched for the first line that matches the given extended regular expression.
     * This parameter may not be specified with the search parameter.
     */
    private Optional<String> research;

    /**
     * The default is 'true'. When 'true', searches (search and research) are case-insensitive.
     * For case-sensitive searches, specify 'false'.
     */
    private boolean insensitive;

    /**
     * This parameter may be specified only with search or research parameters.
     * The value given is the maximum number of lines to return.
     * The default, if not specified, is 100.
     */
    private OptionalInt maxreturnsize;

    /**
     * Internal use to count number of query parameters specified
     */
    private int queryCount;

    /**
     * If true perform binary read instead of text.
     */
    private boolean binary;

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
    }

    public Optional<String> getSearch() {
        return search;
    }

    public Optional<String> getResearch() {
        return research;
    }

    public boolean isInsensitive() {
        return insensitive;
    }

    public OptionalInt getMaxReturnSize() {
        return maxreturnsize;
    }

    public int getQueryCount() {
        return queryCount;
    }

    public boolean isBinary() {
        return binary;
    }

    @Override
    public String toString() {
        return "GetParams{" +
                "search=" + search +
                ", research=" + research +
                ", insensitive=" + insensitive +
                ", maxreturnsize=" + maxreturnsize +
                ", binary=" + binary +
                '}';
    }

    public static class Builder {
        private String search;
        private String research;
        private boolean insensitive = true;
        private Integer maxreturnsize;
        private boolean binary = false;
        private int queryCount = 0;

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

    }

}
