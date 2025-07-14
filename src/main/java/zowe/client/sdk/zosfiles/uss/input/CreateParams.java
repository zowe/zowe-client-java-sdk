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

import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

/**
 * Parameter container class for Unix System Services (USS) create a file or directory object
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-create-unix-file-directory">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public class CreateParams {

    /**
     * The request type defined as a CreateType enum value.
     * <p>
     * This field supports the values:
     * dir to create a directory
     * file to create a file
     */
    private final CreateType type;

    /**
     * Specifies the file or directory permission bits to be used in creating the file or directory.
     * <p>
     * The characters used to describe permissions are:
     * r: Permission to read the file
     * w: Permission to write on the file
     * x: Permission to execute the file
     * -: No permission
     * <p>
     * An example would be: rwxrwxrwx
     * <p>
     * The nine characters are in three groups of three; they describe the permissions on the file or directory.
     * The first group of 3 describes owner permissions; the second describes group permissions;
     * the third describes other (or world) permissions.
     */
    private final String mode;

    /**
     * CreateParams constructor
     *
     * @param type CreateType enum value
     * @param mode permission string value
     * @author James Kostrewski
     */
    public CreateParams(final CreateType type, final String mode) {
        ValidateUtils.checkNullParameter(type == null, "type is null");
        ValidateUtils.checkNullParameter(mode == null, "mode is null");
        this.type = type;
        this.mode = FileUtils.validatePermission(mode);
    }

    /**
     * Retrieve CreateType enum type value
     *
     * @return type value
     */
    public CreateType getType() {
        return type;
    }

    /**
     * Retrieve mode value
     *
     * @return mode value
     */
    public String getMode() {
        return mode;
    }

    /**
     * Return string value representing CreateOptions object
     *
     * @return string representation of CreateOptions
     */
    @Override
    public String toString() {
        return "CreateOptions{" +
                "type=" + type +
                ", mode=" + mode +
                "}";
    }

}
