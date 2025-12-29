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
import zowe.client.sdk.zosfiles.uss.input.UssCreateInputData;
import zowe.client.sdk.zosfiles.uss.input.UssCreateZfsInputData;

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
 * @version 6.0
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
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
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
    UssCreate(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform UNIX create a file or directory name request driven by UssCreateInputData object settings.
     *
     * @param targetPath      the name of the file or directory you are going to create
     * @param createInputData input create parameters, see UssCreateInputData object
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public Response create(final String targetPath, final UssCreateInputData createInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(targetPath, "targetPath");
        ValidateUtils.checkNullParameter(createInputData == null, "createInputData is null");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> createMap = new HashMap<>();
        createMap.put("type", createInputData.getType().getValue());
        createMap.put("mode", createInputData.getMode());

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
    public Response createZfs(final String fileSystemName) throws ZosmfRequestException {
        return createZfsCommon(fileSystemName, new UssCreateZfsInputData.Builder(10).cylsSec(2).timeout(20).build());
    }

    /**
     * Create a ZFS request driven by the UssCreateZfsInputData object settings.
     *
     * @param fileSystemName     ZFS file system name
     * @param createZfsInputData ZFS create input parameters, see UssCreateZfsInputData object
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response createZfsCommon(final String fileSystemName, final UssCreateZfsInputData createZfsInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(fileSystemName, "fileSystemName");
        ValidateUtils.checkNullParameter(createZfsInputData == null, "createZfsInputData is null");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_ZFS_FILES + "/" +
                EncodeUtils.encodeURIComponent(fileSystemName));
        createZfsInputData.getTimeout().ifPresent(timeout -> url.append("?timeout=").append(timeout));

        final Map<String, Object> createZfsMap = new HashMap<>();
        createZfsInputData.getOwner().ifPresent(owner -> createZfsMap.put("owner", owner));
        createZfsInputData.getGroup().ifPresent(group -> createZfsMap.put("group", group));
        createZfsInputData.getPerms().ifPresent(perms -> createZfsMap.put("perms", perms));
        createZfsInputData.getCylsPri().ifPresent(cylsPri -> createZfsMap.put("cylsPri", cylsPri));
        createZfsInputData.getCylsSec().ifPresent(cs -> createZfsMap.put("cylsSec", cs));
        createZfsInputData.getStorageClass().ifPresent(sc -> createZfsMap.put("storageClass", sc));
        createZfsInputData.getManagementClass().ifPresent(mc -> createZfsMap.put("managementClass", mc));
        createZfsInputData.getDataClass().ifPresent(dc -> createZfsMap.put("dataClass", dc));
        if (createZfsInputData.getVolumes().size() == 1) {
            createZfsMap.put("volumes", "[\"" + createZfsInputData.getVolumes().get(0) + "\"]");
        }
        if (createZfsInputData.getVolumes().size() > 1) {
            final StringBuilder volumesStr = new StringBuilder();
            createZfsInputData.getVolumes().forEach(volume -> volumesStr.append("\"").append(volume).append("\","));
            createZfsMap.put("volumes", "[" + volumesStr.substring(0, volumesStr.length() - 1) + "]");
        }
        createZfsMap.put("JSONversion", 1);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }

        request.setUrl(url.toString());
        request.setBody(new JSONObject(createZfsMap).toString());

        return request.executeRequest();
    }

}
