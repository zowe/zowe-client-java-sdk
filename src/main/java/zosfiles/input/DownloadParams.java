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

public class DownloadParams {
    private Optional<String> file;
    private Optional<String> extension;
    private Optional<String> directory;
    private Optional<String[]> excludePatterns;
    private Optional<HashMap<String, String>> extensionMap;
    private Optional<Integer> maxConcurrentRequests;
    private Optional<Boolean> returnEtag;
    private Optional<Boolean> preserveOriginalLetterCase;
    private Optional<Boolean> failFast;
    private Optional<Boolean> binary;
    private Optional<Integer> encoding;
    private Optional<String> volume;
    private Optional<String> task;
    private Optional<String> responseTimeout;

    public DownloadParams(zosfiles.input.DownloadParams.Builder builder) {
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

    public Optional<String> getFile() {
        return file;
    }

    public Optional<String> getExtension() {
        return extension;
    }

    public Optional<String> getDirectory() {
        return directory;
    }

    public Optional<String[]> getExcludePatterns() {
        return excludePatterns;
    }

    public Optional<HashMap<String, String>> getExtensionMap() {
        return extensionMap;
    }

    public Optional<Integer> getNaxConcurrentRequests() {
        return maxConcurrentRequests;
    }

    public Optional<Boolean> getReturnEtag() {
        return returnEtag;
    }

    public Optional<Boolean> getPreserveOriginalLetterCase() {
        return preserveOriginalLetterCase;
    }

    public Optional<Boolean> getFailFast() {
        return failFast;
    }

    public Optional<Boolean> getBinary() {
        return binary;
    }

    public Optional<Integer> getEncoding() {
        return encoding;
    }

    public Optional<String> getVolume() {
        return volume;
    }

    public Optional<String> getTask() {
        return task;
    }

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
