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

/**
 * Parameter container class for unix system services write to file object
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-write-data-zos-unix-file">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class WriteParams {

    /**
     * Text content for file write
     */
    public final String textContent;
    /**
     * Binary byte array content for file write
     */
    public final String textHeader;
    public final Optional<byte[]> binaryContent;
    /**
     * If true perform binary write instead of text.
     */
    public final boolean binary;

    public WriteParams(WriteParams.Builder builder) {
        this.textContent = builder.textContent;
        this.textHeader = builder.textHeader;
        this.binaryContent = Optional.ofNullable(builder.binaryContent);
        this.binary = builder.binary;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getTextHeader() { return textHeader; }

    public Optional<byte[]> getBinaryContent() {
        return binaryContent;
    }

    public boolean isBinary() {
        return binary;
    }

    @Override
    public String toString() {
        return "WriteParams{" +
                "textContent=" + textContent +
                ", binaryContent=" + binaryContent +

                ", binary=" + binary +
                '}';
    }

    public static class Builder {
        private String textContent;
        private String textHeader;
        private byte[] binaryContent;
        private boolean binary = false;

        public WriteParams build() {
            return new WriteParams(this);
        }

        public WriteParams.Builder textContent(String textContent) {
            this.textContent = textContent;
            return this;
        }

        public WriteParams.Builder textHeader(String textHeader) {
            this.textHeader = textHeader;
            return this;
        }
        public WriteParams.Builder binaryContent(byte[] binaryContent) {
            this.binaryContent = binaryContent;
            return this;
        }

        public WriteParams.Builder binary(boolean binary) {
            this.binary = binary;
            return this;
        }

    }

}
