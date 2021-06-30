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

import core.ZOSConnection;
import org.json.simple.JSONObject;
import zosjobs.response.Job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Util {

    public static String getAuthEncoding(ZOSConnection connection) {
        return Base64.getEncoder().encodeToString((connection.getUser() + ":" + connection.getPassword())
                .getBytes(StandardCharsets.UTF_8));
    }

    public static void checkConnection(ZOSConnection connection) {
        if (connection == null || connection.getPort() == null || connection.getHost() == null ||
                connection.getPassword() == null || connection.getUser() == null || connection.getPort().isEmpty() ||
                connection.getHost().isEmpty() || connection.getPassword().isEmpty() || connection.getUser().isEmpty())
            throw new IllegalStateException("Connection data not setup properly");
    }

    public static void checkNullParameter(boolean check, String msg) {
        if (check)
            throw new IllegalArgumentException(msg);
    }

    public static void checkStateParameter(boolean check, String msg) {
        if (check)
            throw new IllegalStateException(msg);
    }

    public static Job createJobObjFromJson(JSONObject json) {
        return new Job.Builder().jobId((String) json.get("jobid"))
                .jobName((String) json.get("jobname"))
                .subSystem((String) json.get("subsystem"))
                .owner((String) json.get("owner"))
                .type((String) json.get("type"))
                .status((String) json.get("status"))
                .url((String) json.get("url"))
                .classs((String) json.get("class"))
                .filesUrl((String) json.get("files-url"))
                .retCode((String) json.get("retCode"))
                .jobCorrelator((String) json.get("job-correlator"))
                .phaseName((String) json.get("phase-name"))
                .build();
    }

    /**
     * Encodes the passed String as UTF-8 using an algorithm that's compatible
     * with JavaScript's <code>encodeURIComponent</code> function. Returns
     * <code>null</code> if the String is <code>null</code>.
     *
     * @param str The String to be encoded
     * @return the encoded String
     */
    public static String encodeURIComponent(String str)
    {
        String result = null;

        try
        {
            result = URLEncoder.encode(str, "UTF-8")
                               .replaceAll("\\+", "%20")
                               .replaceAll("\\%21", "!")
                               .replaceAll("\\%27", "'")
                               .replaceAll("\\%28", "(")
                               .replaceAll("\\%29", ")")
                               .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = str;
        }

        return result;
    }

}
