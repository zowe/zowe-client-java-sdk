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
 * Parameter container class for Unix System Services (USS) write to file object
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-write-data-zos-unix-file">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class WriteInputData {

    /**
     * Text content for file write
     */
    private final String textContent;

    /**
     * Binary byte array content for file write
     */
    private final byte[] binaryContent;

    /**
     * Can be used to specify an alternate EBCDIC code page. The default code page is IBM-1047.
     */
    private final String fileEncoding;

    /**
     * Can be used to control whether each input text line is terminated with a carriage return line feed (CRLF),
     * rather than a line feed (LF), which is the default. Set to true to turn off default.
     */
    private final boolean crlf;

    /**
     * If true performs binary write instead of text.
     */
    private final boolean binary;

    /**
     * WriteParams constructor
     *
     * @param builder WriteParams.Builder builder
     * @author Frank Giordano
     */
    public WriteInputData(final WriteInputData.Builder builder) {
        this.textContent = builder.textContent;
        this.binaryContent = builder.binaryContent;
        this.fileEncoding = builder.fileEncoding;
        this.crlf = builder.crlf;
        this.binary = builder.binary;
    }

    /**
     * Retrieve textContent value
     *
     * @return textContent value
     */
    public Optional<String> getTextContent() {
        return Optional.ofNullable(textContent);
    }

    /**
     * Retrieve binaryContent value
     *
     * @return binaryContent value
     */
    public Optional<byte[]> getBinaryContent() {
        return Optional.ofNullable(binaryContent);
    }

    /**
     * Retrieve fileEncoding value
     *
     * @return fileEncoding value
     */
    public Optional<String> getFileEncoding() {
        return Optional.ofNullable(fileEncoding);
    }

    /**
     * Retrieve is crlf specified
     *
     * @return boolean true or false
     */
    public boolean isCrlf() {
        return crlf;
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
     * Return string value representing WriteParams object
     *
     * @return string representation of WriteParams
     */
    @Override
    public String toString() {
        return "WriteParams{" +
                "textContent=" + textContent +
                ", binaryContent=" + binaryContent +
                ", fileEncoding=" + fileEncoding +
                ", crlf=" + crlf +
                ", binary=" + binary +
                '}';
    }

    /**
     * Builder class for WriteParams
     */
    public static class Builder {

        /**
         * Text content for file write
         */
        private String textContent;

        /**
         * Binary byte array content for file write
         */
        private byte[] binaryContent;

        /**
         * Can be used to specify an alternate EBCDIC code page. The default code page is IBM-1047.
         */
        private String fileEncoding;

        /**
         * Can be used to control whether each input text line is terminated with a carriage return line feed (CRLF),
         * rather than a line feed (LF), which is the default. Set to true to turn off default.
         */
        private boolean crlf = false;

        /**
         * If true performs binary write instead of text.
         */
        private boolean binary = false;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set textContent string value
         *
         * @param textContent string value
         * @return Builder this object
         */
        public Builder textContent(final String textContent) {
            this.textContent = textContent;
            return this;
        }

        /**
         * Set binaryContent byte array value
         *
         * @param binaryContent byte array value
         * @return Builder this object
         */
        public Builder binaryContent(final byte[] binaryContent) {
            this.binaryContent = binaryContent;
            return this;
        }

        /**
         * Set fileEncoding string value
         *
         * @param fileEncoding string value
         * @return Builder this object
         */
        public Builder fileEncoding(final String fileEncoding) {
            this.fileEncoding = fileEncoding;
            return this;
        }

        /**
         * Set crlf boolean value
         *
         * @param crlf boolean true or false value
         * @return Builder this object
         */
        public Builder crlf(final boolean crlf) {
            this.crlf = crlf;
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
         * Return WriteParams object based on Builder this object
         *
         * @return WriteParams object
         */
        public WriteInputData build() {
            return new WriteInputData(this);
        }

    }

}
