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
 * Utility class for USS methods
 * 
 * @author James Kostrewski
 */
public class CreateParams {

    /**
     * The request type. This field supports the values: directory or dir to
     * create a directory. file to create a file
     */
    private final Optional<String> type;
    
    /**
     * Specifies the file or directory permission bits to be used in creating
     * the file or directory. The characters used to describe permissions are:
     * r: Permission to read the file
     * w: Permission to write on the file
     * x: Permission to execute the file
     * -: No permission
     */
    private final Optional<String> mode;
    
    private CreateParams(CreateParams.Builder builder) {
        this.type = Optional.ofNullable(builder.type);
        this.mode = Optional.ofNullable(builder.mode);
    }

    /**
     * Retrieve type value
     * @return type value
     * @author James Kostrewski
     */
    public Optional<String> getType() {
        return type;
    }
    
    /**
     * Retrieve mode value
     * @return mode value
     * @author James Kostrewski
     */
    public Optional<String> getMode() {
        return mode;
    }
    
    
    @Override
    public String toString() {
        return "CreateOptions{" +
                "type=" + type +
                ", mode=" + mode +
                "}";
    }
    
    public static class Builder {
        private String type;
        private String mode;
        
        public CreateParams.Builder type(String type) {
            this.type = type;
            return this;
        }
        
        public CreateParams.Builder mode(String mode) {
            this.mode = mode;
            return this;
        }
        
        public CreateParams build() {
            return new CreateParams(this);
        }
    }
   
}
