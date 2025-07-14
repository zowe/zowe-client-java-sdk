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
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.CreateParams;
import zowe.client.sdk.zosfiles.uss.input.CreateZfsParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) create object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-create-unix-file-directory">z/OSMF REST API UNIX File</a>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-create-zos-unix-zfs-filesystem">z/OSMF REST API ZFS File</a>
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 4.0
 */
public class UssCreate {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * UssCreate Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssCreate(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssCreate constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public UssCreate(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform UNIX create a file or directory name request driven by CreateParams object settings.
     *
     * @param targetPath the name of the file or directory you are going to create
     * @param params     to create response parameters, see CreateParams object
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public Response create(final String targetPath, final CreateParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> createMap = new HashMap<>();
        createMap.put("type", params.getType().getValue());
        createMap.put("mode", params.getMode());

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(createMap).toString());

        return request.executeRequest();
    }

    /**
     * Create a ZFS using default values of 755 permissions, 10 primary and 2 secondary cylinders allocated,
     * and a timeout of 20 seconds.
     *
     * @param fileSystemName ZFS file system name
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response createZfs(String fileSystemName) throws ZosmfRequestException {
        return createZfsCommon(fileSystemName, new CreateZfsParams.Builder(10).cylsSec(2).timeout(20).build());
    }

    /**
     * Create a ZFS request driven by the CreateZfsParams object settings.
     *
     * @param fileSystemName ZFS file system name
     * @param params         create ZFS response parameters, see CreateZfsParams object
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response createZfsCommon(String fileSystemName, CreateZfsParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(fileSystemName == null, "fileSystemName is null");
        ValidateUtils.checkIllegalParameter(fileSystemName.isBlank(), "fileSystemName not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final StringBuilder url = new StringBuilder("https://" + connection.getHost() + ":" +
                connection.getZosmfPort() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_ZFS_FILES + "/" +
                EncodeUtils.encodeURIComponent(fileSystemName));
        params.getTimeout().ifPresent(timeout -> url.append("?timeout=").append(timeout));

        final Map<String, Object> createZfsMap = new HashMap<>();
        params.getOwner().ifPresent(owner -> createZfsMap.put("owner", owner));
        params.getGroup().ifPresent(group -> createZfsMap.put("group", group));
        params.getPerms().ifPresent(perms -> createZfsMap.put("perms", perms));
        params.getCylsPri().ifPresent(cylsPri -> createZfsMap.put("cylsPri", cylsPri));
        params.getCylsSec().ifPresent(cs -> createZfsMap.put("cylsSec", cs));
        params.getStorageClass().ifPresent(sc -> createZfsMap.put("storageClass", sc));
        params.getManagementClass().ifPresent(mc -> createZfsMap.put("managementClass", mc));
        params.getDataClass().ifPresent(dc -> createZfsMap.put("dataClass", dc));
        if (params.getVolumes().size() == 1) {
            createZfsMap.put("volumes", "[\"" + params.getVolumes().get(0) + "\"]");
        }
        if (params.getVolumes().size() > 1) {
            final StringBuilder volumesStr = new StringBuilder();
            params.getVolumes().forEach(volume -> volumesStr.append("\"").append(volume).append("\","));
            createZfsMap.put("volumes", "[" + volumesStr.substring(0, volumesStr.length() - 1) + "]");
        }
        createZfsMap.put("JSONversion", 1);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }

        final Map<String, String> headers = new HashMap<>();
        params.getSystem().ifPresent(system -> headers.put("X-IBM-Target-System", system));

        request.setHeaders(headers);
        request.setUrl(url.toString());
        request.setBody(new JSONObject(createZfsMap).toString());

        return request.executeRequest();
    }

}
