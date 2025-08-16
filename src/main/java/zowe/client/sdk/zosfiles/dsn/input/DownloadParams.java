/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.input;

import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * This interface defines the options that can be sent into the download data set function
 *
 * @author Nikunj Goyal
 * @version 4.0
 */
public class DownloadParams {

    /**
     * The local file to download the data set to, e.g. "./path/to/file.txt"
     */
    private final String file;

    /**
     * The extension you want to use for the file, e.g., extensions: .txt, .c
     */
    private final String extension;

    /**
     * The local directory to download all members from a pds. e.g. "./path/to/dir"
     */
    private final String directory;

    /**
     * Exclude data sets that match these DSLEVEL patterns. Any data sets that match
     * this pattern will not be downloaded, e.g. "ibmuser.**.jcl, ibmuser.rexa.*"
     */
    private final String[] excludePatterns;

    /**
     * Map data set names that match your pattern to the desired extension. e.g., cpgm=c,asmpgm=asm
     */
    private final HashMap<String, String> extensionMap;

    /**
     * The maximum REST requests to perform at once
     * Increasing this value result in faster downloads but increase resource consumption
     * on z/OS and risks encountering an error caused
     * by making too many requests at once.
     * Default: 1
     */
    private final Long maxConcurrentRequests;

    /**
     * The indicator to force the return of ETag.
     * If set to 'true' it forces the response to include an "ETag" header, regardless of the size of the response data.
     * If it is not present, the default is to only send an Etag for data sets smaller than a system-determined length,
     * which is at least 8 MB.
     */
    private final boolean returnEtag;

    /**
     * Indicates if the created directories and files use the original letter case, which is for data sets always uppercase.
     * The default value is false for backward compatibility.
     * If the option "directory" or "file" is provided, this option doesn't have any effect.
     * This option has only an effect on automatically generated directories and files.
     */
    private final boolean preserveOriginalLetterCase;

    /**
     * Indicates if a download operation for multiple files/data sets should fail as soon as the first failure happens.
     * If set to true, the first failure will throw an error and abort the download operation.
     * If set to false, individual download failures will be reported after all other downloads have been completed.
     * The default value is true for backward compatibility.
     */
    private final boolean failFast;

    /**
     * The indicator to view the data set or USS file in binary mode
     */
    private final boolean binary;

    /**
     * Code page encoding
     */
    private final Long encoding;

    /**
     * The volume on which the data set is stored
     */
    private final String volume;

    /**
     * Task status object used by CLI handlers to create progress bars
     */
    private final String task;

    /**
     * Request time out value
     */
    private final String responseTimeout;

    /**
     * DownloadParams constructor
     *
     * @param builder Builder object
     * @author Nikunj Goyal
     */
    private DownloadParams(final Builder builder) {
        this.file = builder.file;
        this.extension = builder.extension;
        this.directory = builder.directory;
        this.excludePatterns = builder.excludePatterns;
        this.extensionMap = builder.extensionMap;
        this.maxConcurrentRequests = builder.maxConcurrentRequests;
        this.returnEtag = builder.returnEtag;
        this.preserveOriginalLetterCase = builder.preserveOriginalLetterCase;
        this.failFast = builder.failFast;
        this.binary = builder.binary;
        this.encoding = builder.encoding;
        this.volume = builder.volume;
        this.task = builder.task;
        this.responseTimeout = builder.responseTimeout;
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
     * Retrieve directory value
     *
     * @return directory value
     */
    public Optional<String> getDirectory() {
        return Optional.ofNullable(directory);
    }

    /**
     * Retrieve encoding value
     *
     * @return encoding value
     */
    public OptionalLong getEncoding() {
        return (encoding == null) ? OptionalLong.empty() : OptionalLong.of(encoding);
    }

    /**
     * Retrieve excludePatterns value
     *
     * @return excludePatterns value
     */
    public Optional<String[]> getExcludePatterns() {
        return Optional.ofNullable(excludePatterns);
    }

    /**
     * Retrieve extension value
     *
     * @return extension value
     */
    public Optional<String> getExtension() {
        return Optional.ofNullable(extension);
    }

    /**
     * Retrieve extensionMap value
     *
     * @return extensionMap value
     */
    public Optional<HashMap<String, String>> getExtensionMap() {
        return Optional.ofNullable(extensionMap);
    }

    /**
     * Retrieve is failFast specified
     *
     * @return boolean true or false
     */
    public boolean isFailFast() {
        return failFast;
    }

    /**
     * Retrieve file value
     *
     * @return file value
     */
    public Optional<String> getFile() {
        return Optional.ofNullable(file);
    }

    /**
     * Retrieve maxConcurrentRequests value
     *
     * @return maxConcurrentRequests value
     */
    public OptionalLong getNaxConcurrentRequests() {
        return (maxConcurrentRequests == null) ? OptionalLong.empty() : OptionalLong.of(maxConcurrentRequests);
    }

    /**
     * Retrieve is preserveOriginalLetterCase specified
     *
     * @return boolean true or false
     */
    public boolean isPreserveOriginalLetterCase() {
        return preserveOriginalLetterCase;
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     */
    public Optional<String> getResponseTimeout() {
        return Optional.ofNullable(responseTimeout);
    }

    /**
     * Retrieve is returnEtag specified
     *
     * @return true or false
     */
    public boolean isReturnEtag() {
        return returnEtag;
    }

    /**
     * Retrieve task value
     *
     * @return task value
     */
    public Optional<String> getTask() {
        return Optional.ofNullable(task);
    }

    /**
     * Retrieve volume value
     *
     * @return volume value
     */
    public Optional<String> getVolume() {
        return Optional.ofNullable(volume);
    }

    /**
     * Return string value representing DownloadOptions object
     *
     * @return string representation of DownloadOptions
     */
    @Override
    public String toString() {
        return "DownloadOptions{" +
                "file=" + file +
                ", extension=" + extension +
                ", directory=" + directory +
                ", excludePatterns=" + excludePatterns +
                ", extensionMap=" + extensionMap +
                ", maxConcurrentRequests=" + maxConcurrentRequests +
                ", returnEtag=" + returnEtag +
                ", preserveOriginalLetterCase=" + preserveOriginalLetterCase +
                ", failFast=" + failFast +
                ", binary=" + binary +
                ", encoding=" + encoding +
                ", volume=" + volume +
                ", task=" + task +
                ", responseTimeout=" + responseTimeout +
                '}';
    }

    /**
     * Builder class for DownloadParams
     */
    public static class Builder {

        /**
         * The local file to download the data set to, e.g. "./path/to/file.txt"
         */
        private String file;

        /**
         * The extension you want to use for the file, e.g., extensions: .txt, .c
         */
        private String extension;

        /**
         * The local directory to download all members from a pds. e.g. "./path/to/dir"
         */
        private String directory;

        /**
         * Exclude data sets that match these DSLEVEL patterns. Any data sets that match
         * this pattern will not be downloaded, e.g. "ibmuser.**.jcl, ibmuser.rexa.*"
         */
        private String[] excludePatterns;

        /**
         * Map data set names that match your pattern to the desired extension. e.g., cpgm=c,asmpgm=asm
         */
        private HashMap<String, String> extensionMap;

        /**
         * The maximum REST requests to perform at once
         * Increasing this value result in faster downloads but increase resource consumption
         * on z/OS and risks encountering an error caused
         * by making too many requests at once.
         * Default: 1
         */
        private Long maxConcurrentRequests;

        /**
         * The indicator to force the return of ETag.
         * If set to 'true' it forces the response to include an "ETag" header, regardless of the size of the response data.
         * If it is not present, the default is to only send an Etag for data sets smaller than a system determined length,
         * which is at least 8 MB.
         */
        private boolean returnEtag;

        /**
         * Indicates if the created directories and files use the original letter case, which is for data sets always uppercase.
         * The default value is false for backward compatibility.
         * If the option "directory" or "file" is provided, this option doesn't have any effect.
         * This option has only an effect on automatically generated directories and files.
         */
        private boolean preserveOriginalLetterCase;

        /**
         * Indicates if a download operation for multiple files/data sets should fail as soon as the first failure happens.
         * If set to true, the first failure will throw an error and abort the download operation.
         * If set to false, individual download failures will be reported after all other downloads have been completed.
         * The default value is true for backward compatibility.
         */
        private boolean failFast;

        /**
         * The indicator to view the data set or USS file in binary mode
         */
        private boolean binary;

        /**
         * Code page encoding
         */
        private Long encoding;

        /**
         * The volume on which the data set is stored
         */
        private String volume;

        /**
         * Task status object used by CLI handlers to create progress bars
         */
        private String task;

        /**
         * Request time out value
         */
        private String responseTimeout;

        /**
         * Builder constructor
         */
        public Builder() {
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
         * Set directory string value
         *
         * @param directory string value
         * @return Builder this object
         */
        public Builder directory(final String directory) {
            this.directory = directory;
            return this;
        }

        /**
         * Set encoding long value
         *
         * @param encoding long value
         * @return Builder this object
         */
        public Builder encoding(final Long encoding) {
            this.encoding = encoding;
            return this;
        }

        /**
         * Set excludePatterns string array value
         *
         * @param excludePatterns string array value
         * @return Builder this object
         */
        public Builder excludePatterns(final String[] excludePatterns) {
            this.excludePatterns = excludePatterns;
            return this;
        }

        /**
         * Set extension string value
         *
         * @param extension string value
         * @return Builder this object
         */
        public Builder extension(final String extension) {
            this.extension = extension;
            return this;
        }

        /**
         * Set extensionMap HashMap value
         *
         * @param extensionMap HashMap value
         * @return Builder this object
         */
        public Builder extensionMap(final HashMap<String, String> extensionMap) {
            this.extensionMap = extensionMap;
            return this;
        }

        /**
         * Set failFast boolean value
         *
         * @param failFast boolean true or false value
         * @return Builder this object
         */
        public Builder failFast(final boolean failFast) {
            this.failFast = failFast;
            return this;
        }

        /**
         * Set file string value
         *
         * @param file string value
         * @return Builder this object
         */
        public Builder file(final String file) {
            this.file = file;
            return this;
        }

        /**
         * Set maxConcurrentRequests long value
         *
         * @param maxConcurrentRequests long value
         * @return Builder this object
         */
        public Builder maxConcurrentRequests(final Long maxConcurrentRequests) {
            this.maxConcurrentRequests = maxConcurrentRequests;
            return this;
        }

        /**
         * Set preserveOriginalLetterCase boolean value
         *
         * @param preserveOriginalLetterCase boolean true or false value
         * @return Builder this object
         */
        public Builder preserveOriginalLetterCase(final boolean preserveOriginalLetterCase) {
            this.preserveOriginalLetterCase = preserveOriginalLetterCase;
            return this;
        }

        /**
         * Set responseTimeout string value
         *
         * @param responseTimeout string value
         * @return Builder this object
         */
        public Builder responseTimeout(final String responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        /**
         * Set returnEtag boolean value
         *
         * @param returnEtag boolean true or false value
         * @return Builder this object
         */
        public Builder returnEtag(final boolean returnEtag) {
            this.returnEtag = returnEtag;
            return this;
        }

        /**
         * Set task string value
         *
         * @param task string value
         * @return Builder this object
         */
        public Builder task(final String task) {
            this.task = task;
            return this;
        }

        /**
         * Set volume string value
         *
         * @param volume string value
         * @return Builder this object
         */
        public Builder volume(final String volume) {
            this.volume = volume;
            return this;
        }

        /**
         * Return DownloadParams object based on Builder this object
         *
         * @return DownloadParams this object
         */
        public DownloadParams build() {
            return new DownloadParams(this);
        }

    }

}
