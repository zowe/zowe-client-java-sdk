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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

import java.util.Map;

/**
 * Provides Unix System Services (USS) delete object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-delete-unix-file-directory">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public class UssDelete {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * UssDelete Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssDelete(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssDelete constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public UssDelete(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform UNIX delete file or directory name request
     *
     * @param targetPath the name of the file or directory you are going to delete
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response delete(final String targetPath) throws ZosmfRequestException {
        return delete(targetPath, false);
    }

    /**
     * Perform UNIX delete file or directory name request with recursive flag
     *
     * @param targetPath the name of the file or directory you are going to delete
     * @param recursive  flag indicates if contents of directory should also be deleted
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response delete(final String targetPath, final boolean recursive) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }

        if (recursive) {
            request.setHeaders(Map.of("X-IBM-Option", "recursive"));
        }
        request.setUrl(url);

        return request.executeRequest();
    }

    /**
     * Delete UNIX zFS Filesystem
     *
     * @param fileSystemName file system name
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response deleteZfs(final String fileSystemName) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(fileSystemName == null, "fileSystemName is null");
        ValidateUtils.checkIllegalParameter(fileSystemName.isBlank(), "fileSystemName not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_ZFS_FILES + "/" +
                EncodeUtils.encodeURIComponent(fileSystemName);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }
        request.setUrl(url);

        return request.executeRequest();
    }

}
