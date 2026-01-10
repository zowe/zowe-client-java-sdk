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

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.UssMountInputData;
import zowe.client.sdk.zosfiles.uss.types.MountActionType;
import zowe.client.sdk.zosfiles.uss.types.MountModeType;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) mount and unmount of a file system name
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-mount-zos-unix-file-system">z/OSMF REST MOUNT API</a>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-unmount-unix-file-system">z/OSMF REST UNMOUNT API</a>
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class UssMount {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * UssMount Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public UssMount(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative UssMount constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    UssMount(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform mount of a file system name to the UNIX file system. Mount mode will be Read-Write type.
     *
     * @param fileSystemName the file system name
     * @param mountPoint     the mount point to be used for mounting the UNIX file system
     * @param fsType         the type of file system to be mounted.
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response mount(final String fileSystemName, final String mountPoint, final String fsType)
            throws ZosmfRequestException {
        return mountCommon(fileSystemName,
                new UssMountInputData.Builder()
                        .action(MountActionType.MOUNT)
                        .mountPoint(mountPoint)
                        .fsType(fsType)
                        .mode(MountModeType.READ_WRITE)
                        .build());
    }

    /**
     * Perform unmounting of a file system name
     *
     * @param fileSystemName the file system name
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response unmount(final String fileSystemName) throws ZosmfRequestException {
        return mountCommon(fileSystemName, new UssMountInputData.Builder().action(MountActionType.UNMOUNT).build());
    }

    /**
     * Perform mount or unmount of a file system name request driven by UssMountInputData settings
     *
     * @param fileSystemName the file system name
     * @param mountInputData UssMountInputData object
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response mountCommon(final String fileSystemName, final UssMountInputData mountInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(fileSystemName, "fileSystemName");
        ValidateUtils.checkNullParameter(mountInputData, "mountInputData");
        ValidateUtils.checkIllegalParameter(mountInputData.getAction().isEmpty(), "mountInputData action not specified");

        final String action = mountInputData.getAction().get().getValue();
        if ("MOUNT".equalsIgnoreCase(action)) {
            ValidateUtils.checkIllegalParameter(mountInputData.getMountPoint().isEmpty(), "mountPoint not specified");
            ValidateUtils.checkIllegalParameter(mountInputData.getFsType().isEmpty(), "fsType not specified");
        }

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_MFS + "/" +
                EncodeUtils.encodeURIComponent(fileSystemName);

        final Map<String, Object> mountMap = new HashMap<>();
        mountMap.put("action", action);
        mountInputData.getMountPoint().ifPresent(str -> mountMap.put("mount-point", str));
        mountInputData.getFsType().ifPresent(str -> mountMap.put("fs-type", str));
        mountInputData.getMode().ifPresent(str -> mountMap.put("mode", str.getValue()));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(mountMap).toString());

        return request.executeRequest();
    }

}
