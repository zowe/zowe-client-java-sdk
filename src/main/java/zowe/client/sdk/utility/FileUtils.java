/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for UNIX (USS) related static helper methods.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public final class FileUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Validate permission
     * <p>
     * A valid permission is nine characters in three groups of three;
     * they describe the permissions on the file or directory. The first group
     * of 3 describes owner permissions; the second describes group permissions;
     * the third describes other (or world) permissions.
     * <p>
     * Each group contains a value of either r w x or -.
     *
     * @param permission permission string
     * @return same value string back to caller if valid
     * @author Frank Giordano
     */
    public static String validatePermission(final String permission) {
        ValidateUtils.checkNullParameter(permission, "permission value");
        ValidateUtils.checkIllegalParameter(permission.length() != 9, "specify 9 character permission");
        Pattern p = Pattern.compile("(rwx|rw-|r--|r-x|--x|-wx|-w-)+");
        Matcher m = p.matcher(permission);
        if (!m.matches()) {
            throw new IllegalStateException("specify valid permission value");
        }
        return permission;
    }

    /**
     * Validate permission i.e. /xxx/xx/x
     *
     * @param path path string
     * @return same value string back to caller if valid
     * @author Frank Giordano
     */
    public static String validatePath(final String path) {
        ValidateUtils.checkNullParameter(path, "path value");
        Pattern p = Pattern.compile("/.*");
        Matcher m = p.matcher(path);
        if (!m.matches()) {
            throw new IllegalStateException("specify valid path value");
        }
        return path;
    }

}
