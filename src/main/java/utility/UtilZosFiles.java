/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

import rest.ZosmfHeaders;
import zosfiles.input.DownloadParams;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility Class for zosFiles related static helper methods.
 *
 * @version 1.0
 */
public class UtilZosFiles {

    /**
     * Data set name qualifier separator
     */
    public static final String DSN_SEP = ".";

    /**
     * Default file extension
     */
    public static final String DEFAULT_FILE_EXTENSION = "txt";

    /**
     * Default Max Member Length
     */
    public static final Integer MAX_MEMBER_LENGTH = 8;

    /**
     * Break up a dataset name of either: USER.WORK.JCL(TEMPLATE) to user/work/jcl/template
     * Or USER.WORK.PS to user/work/ps
     *
     * @param dataSet dataset to break up into folders
     * @return directory string
     */
    public static String getDirsFromDataSet(String dataSet) {
        final String regex = "\\\\$\\{this.DSN_SEP\\}";
        final String string = "";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        String localDirectory = dataSet.replace(matcher.group(0), UtilIO.FILE_DELIM).toLowerCase(Locale.ROOT);
        if (localDirectory.indexOf("(") >= 0 && localDirectory.indexOf(")") >= 0) {
            localDirectory = localDirectory.replace("(", UtilIO.FILE_DELIM);
            localDirectory = localDirectory.substring(0, -1);
        }
        return localDirectory;
    }

    /**
     * Common method to build headers given input options object
     *
     * @param options various options parameters, see DownloadParams object
     * @return HeaderContent
     */
    public static Map<String, String> generateHeadersBasedOnOptions(DownloadParams options) {
        String key, value;
        Map<String, String> headers = new HashMap<>();

        if (options.getBinary().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(1);
            headers.put(key, value);
        } else if (options.getEncoding().isPresent()) {
            key = ZosmfHeaders.X_IBM_TEXT;
            value = ZosmfHeaders.X_IBM_TEXT + ZosmfHeaders.X_IBM_TEXT_ENCODING + options.getEncoding();
            headers.put(key, value);
        }

        key = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(0);
        value = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(1);
        headers.put(key, value);

        if (options.getResponseTimeout().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
            value = ZosmfHeaders.HEADERS.get(options.getResponseTimeout().toString()).get(1);
            headers.put(key, value);
        }

        return headers;
    }

}
