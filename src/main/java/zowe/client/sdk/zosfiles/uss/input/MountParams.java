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

import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.uss.types.MountActionType;
import zowe.client.sdk.zosfiles.uss.types.MountModeType;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) mount and unmount of a file system name
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-mount-zos-unix-file-system">z/OSMF REST MOUNT API</a>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-unmount-unix-file-system">z/OSMF REST UNMOUNT API</a>
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class MountParams {

    /**
     * Specifies the action mount to mount an UNIX file system.
     */
    private final Optional<MountActionType> action;

    /**
     * Specifies the mount point to be used for mounting the UNIX file system.
     */
    private final Optional<String> mountPoint;

    /**
     * Specifies the type of file system to be mounted. This value must match the TYPE operand on a FILESYSTYPE
     * statement in the BPXPRMxx parmlib member for your system.
     */
    private final Optional<String> fsType;

    /**
     * Specifies the mode in which the file system is mounted.
     * If not specified, this value defaults to rdonly.
     */
    private final Optional<MountModeType> mode;


    public MountParams(MountParams.Builder builder) {
        this.action = Optional.ofNullable(builder.action);
        this.mountPoint = Optional.ofNullable(builder.mountPoint);
        this.fsType = Optional.ofNullable(builder.fsType);
        this.mode = Optional.ofNullable(builder.mode);
    }

    public Optional<MountActionType> getAction() {
        return action;
    }

    public Optional<String> getMountPoint() {
        return mountPoint;
    }

    public Optional<String> getFsType() {
        return fsType;
    }

    public Optional<MountModeType> getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "MountParams{" +
                "action=" + action +
                ", mountPoint=" + mountPoint +
                ", fsType=" + fsType +
                ", mode=" + mode +
                '}';
    }

    public static class Builder {

        private MountActionType action;
        private String mountPoint;
        private String fsType;
        private MountModeType mode;

        public MountParams build() {
            return new MountParams(this);
        }

        public MountParams.Builder action(MountActionType action) {
            this.action = action;
            return this;
        }

        public MountParams.Builder mountPoint(String mountPoint) {
            ValidateUtils.checkNullParameter(mountPoint == null, "mountPoint is null");
            ValidateUtils.checkIllegalParameter(mountPoint.isEmpty(), "mountPoint not specified");
            this.mountPoint = mountPoint;
            return this;
        }

        public MountParams.Builder fsType(String fsType) {
            ValidateUtils.checkNullParameter(fsType == null, "fsType is null");
            ValidateUtils.checkIllegalParameter(fsType.isEmpty(), "fsType not specified");
            this.fsType = fsType;
            return this;
        }

        public MountParams.Builder mode(MountModeType mode) {
            this.mode = mode;
            return this;
        }

    }

}
