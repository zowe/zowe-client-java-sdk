/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.uss.input.MountParams;

/**
 * Provides Unix System Services (USS) mount and unmount of a fil system name
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-mount-zos-unix-file-system">z/OSMF REST MOUNT API</a>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-unmount-unix-file-system">z/OSMF REST UNMOUNT API</a>
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssMount {

    private static final Logger LOG = LoggerFactory.getLogger(UssMount.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssMount Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public UssMount(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssMount constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public UssMount(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Perform mount of a file system name to the USS UNIX file system
     *
     * @param fileSystemName the file system name
     * @param mountPoint     the mount point to be used for mounting the UNIX file system
     * @return Response object
     * @author Frank Giordano
     */
    public Response mount(String fileSystemName, String mountPoint) {

        return null;
    }

    /**
     * Perform unmount of a file system name
     *
     * @param fileSystemName the file system name
     * @return Response object
     * @author Frank Giordano
     */
    public Response unMount(String fileSystemName) {

        return null;
    }

    /**
     * Perform mount or unmount of a file system name request driven by MountParams settings
     *
     * @param fileSystemName the file system name 
     * @return Response object
     * @author Frank Giordano
     */
    public Response mountCommon(String fileSystemName, MountParams params) {

        return null;
    }

}
