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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Parameter container class for Unix System Services (USS) create Zfs file system
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-create-zos-unix-zfs-filesystem">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class CreateZfsParams {

    /**
     * The z/OS user ID or UID for the owner of the ZFS root directory.
     * <p>
     * Defaults to 755. This property is not required.
     */
    private OptionalInt owner;

    /**
     * The z/OS group ID or GID for the group of the ZFS root directory.
     * <p>
     * Defaults to 755. This property is not required.
     */
    private OptionalInt group;

    /**
     * The permissions code for the ZFS root directory.
     * <p>
     * Defaults to 755. This property is not required.
     */
    private OptionalInt perms;

    /**
     * The number of primary cylinders to allocate for the ZFS.
     * <p>
     * Defaults to 0. This property is required.
     */
    private OptionalInt cylsPri;

    /**
     * The number of secondary cylinders to allocate for the ZFS.
     * <p>
     * Defaults to 0. This property is not required.
     */
    private OptionalInt cylsSec;

    /**
     * The SMS storage class to use for the allocation is a collection of performance goals
     * and device availability requirements that the storage administrator defines.
     * <p>
     * This property is not required.
     */
    private Optional<String> storageClass;

    /**
     * The SMS management class to use for the allocation is a list of data set migration,
     * backup, class transition and retention attribute values.
     * <p>
     * This property is not required.
     */
    private Optional<String> managementClass;

    /**
     * The SMS data class to use for the allocation attributes and their values.
     * <p>
     * This property is not required.
     */
    private Optional<String> dataClass;

    /**
     * List of volumes. This property is not required.
     */
    private List<String> volumes;

    /**
     * The number of seconds to wait for the underlying "zfsadm format" command to complete.
     * If this command times out, the ZFS may have been created but not formatted correctly.
     * <p>
     * Default value: 20. This property is not required.
     */
    private OptionalInt timeout;

    /**
     * Indicates the target system name (nick name) for this request, where the system name (nick name)
     * is defined in the local system Systems table. The target host system must support single-sign-on by
     * using either an LTPA token or a valid X-IBM-Target-System-User and X-IBM-Target-System-Password is
     * provided for the target system. If the target system is the local system, this is ignored
     * and has no effect.
     */
    private Optional<String> system;

    /**
     * CreateZfsParams constructor
     *
     * @param builder CreateZfsParams.Builder object
     * @author Frank Giordano
     */
    private CreateZfsParams(final CreateZfsParams.Builder builder) {
        if (builder.owner == null) {
            this.owner = OptionalInt.empty();
        } else {
            this.owner = OptionalInt.of(builder.owner);
        }
        if (builder.group == null) {
            this.group = OptionalInt.empty();
        } else {
            this.group = OptionalInt.of(builder.group);
        }
        if (builder.perms == null) {
            this.perms = OptionalInt.empty();
        } else {
            this.perms = OptionalInt.of(builder.perms);
        }
        if (builder.cylsPri == null) {
            this.cylsPri = OptionalInt.empty();
        } else {
            this.cylsPri = OptionalInt.of(builder.cylsPri);
        }
        if (builder.cylsSec == null) {
            this.cylsSec = OptionalInt.empty();
        } else {
            this.cylsSec = OptionalInt.of(builder.cylsSec);
        }
        if (builder.cylsPri == null) {
            this.cylsPri = OptionalInt.empty();
        } else {
            this.cylsPri = OptionalInt.of(builder.cylsPri);
        }
        this.storageClass = Optional.ofNullable(builder.storageClass);
        this.managementClass = Optional.ofNullable(builder.managementClass);
        this.dataClass = Optional.ofNullable(builder.dataClass);
        this.volumes = builder.volumes;
        if (builder.timeout == null) {
            this.timeout = OptionalInt.empty();
        } else {
            this.timeout = OptionalInt.of(builder.timeout);
        }
        this.system = Optional.ofNullable(builder.system);
    }

    /**
     * Retrieve owner specified
     *
     * @return owner value
     */
    public OptionalInt getOwner() {
        return owner;
    }

    /**
     * Retrieve group specified
     *
     * @return group value
     */
    public OptionalInt getGroup() {
        return group;
    }

    /**
     * Retrieve perms specified
     *
     * @return perms value
     */
    public OptionalInt getPerms() {
        return perms;
    }

    /**
     * Retrieve cylsPri specified
     *
     * @return cylsPri value
     */
    public OptionalInt getCylsPri() {
        return cylsPri;
    }

    /**
     * Retrieve cylsSec specified
     *
     * @return cylsSec value
     */
    public OptionalInt getCylsSec() {
        return cylsSec;
    }

    /**
     * Retrieve storageClass specified
     *
     * @return storageClass value
     */
    public Optional<String> getStorageClass() {
        return storageClass;
    }

    /**
     * Retrieve managementClass specified
     *
     * @return managementClass value
     */
    public Optional<String> getManagementClass() {
        return managementClass;
    }

    /**
     * Retrieve dataClass specified
     *
     * @return dataClass value
     */
    public Optional<String> getDataClass() {
        return dataClass;
    }

    /**
     * Retrieve volumes specified
     *
     * @return volumes value
     */
    public List<String> getVolumes() {
        return volumes;
    }

    /**
     * Retrieve timeout specified
     *
     * @return timeout value
     */
    public OptionalInt getTimeout() {
        return timeout;
    }

    /**
     * Retrieve system specified
     *
     * @return system value
     */
    public Optional<String> getSystem() {
        return system;
    }

    /**
     * Return string value representing CreateZfsParams object
     *
     * @return string representation of CreateZfsParams
     */
    @Override
    public String toString() {
        return "CreateZfsParams{" +
                "owner=" + owner +
                ", group=" + group +
                ", perms=" + perms +
                ", cylsPri=" + cylsPri +
                ", cylsSec=" + cylsSec +
                ", storageClass=" + storageClass +
                ", managementClass=" + managementClass +
                ", dataClass=" + dataClass +
                ", volumes=" + volumes +
                ", timeout=" + timeout +
                ", system=" + system +
                '}';
    }

    /**
     * Builder class for CreateZfsParams
     */
    public static class Builder {

        /**
         * The z/OS user ID or UID for the owner of the ZFS root directory.
         * <p>
         * Defaults to 755. This property is not required.
         */
        private Integer owner;

        /**
         * The z/OS group ID or GID for the group of the ZFS root directory.
         * <p>
         * Defaults to 755. This property is not required.
         */
        private Integer group;

        /**
         * The permissions code for the ZFS root directory.
         * <p>
         * Defaults to 755. This property is not required.
         */
        private Integer perms;

        /**
         * The number of primary cylinders to allocate for the ZFS.
         * <p>
         * Defaults to 0. This property is required.
         */
        private Integer cylsPri;

        /**
         * The number of secondary cylinders to allocate for the ZFS.
         * <p>
         * Defaults to 0. This property is not required.
         */
        private Integer cylsSec;

        /**
         * The SMS storage class to use for the allocation is a collection of performance goals
         * and device availability requirements that the storage administrator defines.
         * <p>
         * This property is not required.
         */
        private String storageClass;

        /**
         * The SMS management class to use for the allocation is a list of data set migration,
         * backup, class transition and retention attribute values.
         * <p>
         * This property is not required.
         */
        private String managementClass;

        /**
         * The SMS data class to use for the allocation attributes and their values.
         * <p>
         * This property is not required.
         */
        private String dataClass;

        /**
         * List of volumes. This property is not required.
         */
        private List<String> volumes = new ArrayList<>();

        /**
         * The number of seconds to wait for the underlying "zfsadm format" command to complete.
         * If this command times out, the ZFS may have been created but not formatted correctly.
         * <p>
         * Default value: 20. This property is not required.
         */
        private Integer timeout;

        /**
         * Indicates the target system name (nick name) for this request, where the system name (nick name)
         * is defined in the local system Systems table. The target host system must support single-sign-on by
         * using either an LTPA token or a valid X-IBM-Target-System-User and X-IBM-Target-System-Password is
         * provided for the target system. If the target system is the local system, this is ignored
         * and has no effect.
         */
        private String system;

        /**
         * Builder constructor
         *
         * @param cylsPri value of required primary cylinders
         */
        public Builder(final Integer cylsPri) {
            ValidateUtils.checkNullParameter(cylsPri == null, "cylsPri is null");
            ValidateUtils.checkNullParameter(cylsPri.intValue() <= 0, "specified cylsPri greater than 0");
            this.cylsPri = cylsPri;
        }

        /**
         * Set owner int value
         *
         * @param owner int value
         * @return Builder this object
         */
        public Builder owner(final Integer owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Set group int value
         *
         * @param group int value
         * @return Builder this object
         */
        public Builder group(final Integer group) {
            this.group = group;
            return this;
        }

        /**
         * Set perms int value
         *
         * @param perms int value
         * @return Builder this object
         */
        public Builder perms(final Integer perms) {
            this.perms = perms;
            return this;
        }

        /**
         * Set cylsPri int value
         *
         * @param cylsPri int value
         * @return Builder this object
         */
        public Builder cylsPri(final Integer cylsPri) {
            this.cylsPri = cylsPri;
            return this;
        }

        /**
         * Set cylsSec int value
         *
         * @param cylsSec int value
         * @return Builder this object
         */
        public Builder cylsSec(final Integer cylsSec) {
            this.cylsSec = cylsSec;
            return this;
        }

        /**
         * Set storageClass string value
         *
         * @param storageClass string value
         * @return Builder this object
         */
        public Builder storageClass(final String storageClass) {
            this.storageClass = storageClass;
            return this;
        }

        /**
         * Set managementClass string value
         *
         * @param managementClass string value
         * @return Builder this object
         */
        public Builder managementClass(final String managementClass) {
            this.managementClass = managementClass;
            return this;
        }

        /**
         * Set dataClass string value
         *
         * @param dataClass string value
         * @return Builder this object
         */
        public Builder dataClass(final String dataClass) {
            this.dataClass = dataClass;
            return this;
        }

        /**
         * Set volumes list value
         *
         * @param volumes list value
         * @return Builder this object
         */
        public Builder volumes(final List<String> volumes) {
            this.volumes = volumes;
            return this;
        }

        /**
         * Set timeout int value
         *
         * @param timeout int value
         * @return Builder this object
         */
        public Builder timeout(final Integer timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Set system string value
         *
         * @param system string value
         * @return Builder this object
         */
        public Builder system(final String system) {
            this.system = system;
            return this;
        }


        /**
         * Return CreateZfsParams object based on Builder this object
         *
         * @return CreateZfsParams object
         */
        public CreateZfsParams build() {
            return new CreateZfsParams(this);
        }

    }

}
