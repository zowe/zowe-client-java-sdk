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

/**
 * Interface for create UNIX files and directories
 * zOSMF REST API information:
 * <a href="">https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-create-unix-file-directory</a>
 *
 * @author James Kostrewski
 */
public class CreateParams {

    /**
     * The request type. This field supports the values: directory or dir to
     * create a directory. file to create a file
     */
    private UssType type;

    /**
     * Specifies the file or directory permission bits to be used in creating
     * the file or directory. The characters used to describe permissions are:
     * r: Permission to read the file
     * w: Permission to write on the file
     * x: Permission to execute the file
     * -: No permission
     */
    private final String mode;

    private CreateParams(UssType type, String mode) {
        this.type = type;
        this.mode = mode;
    }

    /**
     * Retrieve type value
     *
     * @return type value
     * @author James Kostrewski
     */
    public UssType getType() {
        return type;
    }

    /**
     * Retrieve mode value
     *
     * @return mode value
     * @author James Kostrewski
     */
    public String getMode() {
        return mode;
    }


    @Override
    public String toString() {
        return "CreateOptions{" +
                "type=" + type +
                ", mode=" + mode +
                "}";
    }

}
