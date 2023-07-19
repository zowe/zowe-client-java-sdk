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
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.MountParams;
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
     * Perform mount of a file system name to the UNIX file system. Mount mode will be Read-Write type.
     *
     * @param fileSystemName the file system name
     * @param mountPoint     the mount point to be used for mounting the UNIX file system
     * @param fsType         the type of file system to be mounted.
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public Response mount(String fileSystemName, String mountPoint, String fsType) throws Exception {
        return mountCommon(fileSystemName,
                new MountParams.Builder()
                        .action(MountActionType.MOUNT)
                        .mountPoint(mountPoint)
                        .fsType(fsType)
                        .mode(MountModeType.READ_WRITE)
                        .build());
    }

    /**
     * Perform unmount of a file system name
     *
     * @param fileSystemName the file system name
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public Response unmount(String fileSystemName) throws Exception {
        return mountCommon(fileSystemName, new MountParams.Builder().action(MountActionType.UNMOUNT).build());
    }

    /**
     * Perform mount or unmount of a file system name request driven by MountParams settings
     *
     * @param fileSystemName the file system name
     * @param params         MountParams object
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public Response mountCommon(String fileSystemName, MountParams params) throws Exception {
        ValidateUtils.checkNullParameter(fileSystemName == null, "file system name is null");
        ValidateUtils.checkIllegalParameter(fileSystemName.isEmpty(), "file system name not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getAction().isEmpty(), "params action not specified");

        final String action = params.getAction().get().getValue();
        if ("MOUNT".equalsIgnoreCase(action)) {
            ValidateUtils.checkIllegalParameter(params.getMountPoint().isEmpty(), "mountPoint not specified");
            ValidateUtils.checkIllegalParameter(params.getFsType().isEmpty(), "fsType not specified");
        }

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_MFS + "/" + fileSystemName;
        LOG.debug(url);

        if (request == null || !(request instanceof JsonPutRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }

        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("action", action);
        params.getMountPoint().ifPresent(str -> jsonMap.put("mount-point", str));
        params.getFsType().ifPresent(str -> jsonMap.put("fs-type", str));
        params.getMode().ifPresent(str -> jsonMap.put("mode", str.getValue()));

        final StringBuilder jsonStr = new StringBuilder();
        jsonStr.append("{");
        jsonMap.forEach((k, v) -> {
            jsonStr.append("\"").append(k).append("\"");
            jsonStr.append(":");
            jsonStr.append("\"").append(v).append("\"");
            jsonStr.append(",");
        });
        final StringBuilder jsonFinalStr = new StringBuilder(jsonStr.substring(0, jsonStr.length() - 1));
        jsonFinalStr.append("}");
        LOG.debug(jsonFinalStr.toString());

        request.setBody(jsonFinalStr.toString());
        request.setUrl(url);

        return RestUtils.getResponse(request);
    }

}
