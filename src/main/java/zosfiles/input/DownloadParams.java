/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles.input;

import java.util.Optional;
import java.util.HashMap;

/**
 * This interface defines the options that can be sent into the download data set function
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class DownloadParams {

    /**
     * The local file to download the data set to, i.e. "./path/to/file.txt"
     */
    private final Optional<String> file;

    /**
     * The extension you want to use for the file, i.e. .txt, .c
     */
    private final Optional<String> extension;

    /**
     * The local directory to download all members from a pds. i.e. "./path/to/dir"
     */
    private final Optional<String> directory;

    /**
     * Exclude data sets that match these DSLEVEL patterns. Any data sets that match
     * this pattern will not be downloaded, i.e. "ibmuser.**.jcl, ibmuser.rexa.*"
     */
    private final Optional<String[]> excludePatterns;

    /**
     * Map data set names that match your pattern to the desired extension. i.e. cpgm=c,asmpgm=asm
     */
    private final Optional<HashMap<String, String>> extensionMap;

    /**
     * The maximum REST requests to perform at once
     * Increasing this value results in faster downloads but increases resource consumption
     * on z/OS and risks encountering an error caused
     * by making too many requests at once.
     * Default: 1
     */
    private final Optional<Integer> maxConcurrentRequests;

    /**
     * The indicator to force return of ETag.
     * If set to 'true' it forces the response to include an "ETag" header, regardless of the size of the response data.
     * If it is not present, the the default is to only send an Etag for data sets smaller than a system determined length,
     * which is at least 8MB.
     */
    private final Optional<Boolean> returnEtag;

    /**
     * Indicates if the created directories and files use the original letter case, which is for data sets always uppercase.
     * The default value is false for backward compability.
     * If the option "directory" or "file" is provided, this option doesn't have any effect.
     * This option has only effect on automatically generated directories and files.
     */
    private final Optional<Boolean> preserveOriginalLetterCase;

    /**
     * Indicates if a download operation for multiple files/data sets should fail as soon as the first failure happens.
     * If set to true, the first failure will throw an error and abort the download operation.
     * If set to false, individual download failures will be reported after all other downloads have completed.
     * The default value is true for backward compatibility.
     */
    private final Optional<Boolean> failFast;

    /**
     * The indicator to view the data set or USS file in binary mode
     */
    private final Optional<Boolean> binary;

    /**
     * Code page encoding
     */
    private final Optional<Integer> encoding;

    /**
     * The volume on which the data set is stored
     */
    private final Optional<String> volume;

    /**
     * Task status object used by CLI handlers to create progress bars
     */
    private final Optional<String> task;

    /**
     * Request time out value
     */
    private final Optional<String> responseTimeout;

    private DownloadParams(zosfiles.input.DownloadParams.Builder builder) {
        this.file = Optional.ofNullable(builder.file);
        this.extension = Optional.ofNullable(builder.extension);
        this.directory = Optional.ofNullable(builder.directory);
        this.excludePatterns = Optional.ofNullable(builder.excludePatterns);
        this.extensionMap = Optional.ofNullable(builder.extensionMap);
        this.maxConcurrentRequests = Optional.ofNullable(builder.maxConcurrentRequests);
        this.returnEtag = Optional.ofNullable(builder.returnEtag);
        this.preserveOriginalLetterCase = Optional.ofNullable(builder.preserveOriginalLetterCase);
        this.failFast = Optional.ofNullable(builder.failFast);
        this.binary = Optional.ofNullable(builder.binary);
        this.encoding = Optional.ofNullable(builder.encoding);
        this.volume = Optional.ofNullable(builder.volume);
        this.task = Optional.ofNullable(builder.task);
        this.responseTimeout = Optional.ofNullable(builder.responseTimeout);
    }

    /**
     * Retrieve file value
     *
     * @return file value
     * @author Nikunj Goyal
     */
    public Optional<String> getFile() {
        return file;
    }

    /**
     * Retrieve extension value
     *
     * @return extension value
     * @author Nikunj Goyal
     */
    public Optional<String> getExtension() {
        return extension;
    }

    /**
     * Retrieve directory value
     *
     * @return directory value
     * @author Nikunj Goyal
     */
    public Optional<String> getDirectory() {
        return directory;
    }

    /**
     * Retrieve excludePatterns value
     *
     * @return excludePatterns value
     * @author Nikunj Goyal
     */
    public Optional<String[]> getExcludePatterns() {
        return excludePatterns;
    }

    /**
     * Retrieve extensionMap value
     *
     * @return extensionMap value
     * @author Nikunj Goyal
     */
    public Optional<HashMap<String, String>> getExtensionMap() {
        return extensionMap;
    }

    /**
     * Retrieve maxConcurrentRequests value
     *
     * @return maxConcurrentRequests value
     * @author Nikunj Goyal
     */
    public Optional<Integer> getNaxConcurrentRequests() {
        return maxConcurrentRequests;
    }

    /**
     * Retrieve returnEtag value
     *
     * @return returnEtag value
     * @author Nikunj Goyal
     */
    public Optional<Boolean> getReturnEtag() {
        return returnEtag;
    }

    /**
     * Retrieve preserveOriginalLetterCase value
     *
     * @return preserveOriginalLetterCase value
     * @author Nikunj Goyal
     */
    public Optional<Boolean> getPreserveOriginalLetterCase() {
        return preserveOriginalLetterCase;
    }

    /**
     * Retrieve failFast value
     *
     * @return failFast value
     * @author Nikunj Goyal
     */
    public Optional<Boolean> getFailFast() {
        return failFast;
    }

    /**
     * Retrieve binary value
     *
     * @return binary value
     * @author Nikunj Goyal
     */
    public Optional<Boolean> getBinary() {
        return binary;
    }

    /**
     * Retrieve encoding value
     *
     * @return encoding value
     * @author Nikunj Goyal
     */
    public Optional<Integer> getEncoding() {
        return encoding;
    }

    /**
     * Retrieve volume value
     *
     * @return volume value
     * @author Nikunj Goyal
     */
    public Optional<String> getVolume() {
        return volume;
    }

    /**
     * Retrieve task value
     *
     * @return task value
     * @author Nikunj Goyal
     */
    public Optional<String> getTask() {
        return task;
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     * @author Nikunj Goyal
     */
    public Optional<String> getResponseTimeout() {
        return responseTimeout;
    }

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

    public static class Builder {

        private String file;
        private String extension;
        private String directory;
        private String[] excludePatterns;
        private HashMap<String, String> extensionMap;
        private Integer maxConcurrentRequests;
        private Boolean returnEtag;
        private Boolean preserveOriginalLetterCase;
        private Boolean failFast;
        private Boolean binary;
        private Integer encoding;
        private String volume;
        private String task;
        private String responseTimeout;

        public zosfiles.input.DownloadParams.Builder file(String file) {
            this.file = file;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder extension(String extension) {
            this.extension = extension;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder directory(String directory) {
            this.directory = directory;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder excludePatterns(String[] excludePatterns) {
            this.excludePatterns = excludePatterns;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder extensionMap(HashMap<String, String> extensionMap) {
            this.extensionMap = extensionMap;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder maxConcurrentRequests(Integer maxConcurrentRequests) {
            this.maxConcurrentRequests = maxConcurrentRequests;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder returnEtag(Boolean returnEtag) {
            this.returnEtag = returnEtag;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder preserveOriginalLetterCase(Boolean preserveOriginalLetterCase) {
            this.preserveOriginalLetterCase = preserveOriginalLetterCase;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder failFast(Boolean failFast) {
            this.failFast = failFast;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder binary(Boolean binary) {
            this.binary = binary;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder encoding(Integer encoding) {
            this.encoding = encoding;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder volume(String volume) {
            this.volume = volume;
            return this;
        }

        public zosfiles.input.DownloadParams.Builder task(String task) {
            this.task = task;
            return this;
        }

        public DownloadParams.Builder responseTimeout(String responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        public zosfiles.input.DownloadParams build() {
            return new zosfiles.input.DownloadParams(this);
        }
    }

}
