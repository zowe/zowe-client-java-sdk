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
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) move functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 6.0
 */
public class UssMove {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * UssMove Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssMove(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative UssMove constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     */
    UssMove(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Move a UNIX file or directory with overwriting set as true
     *
     * @param fromPath   UNIX path to the source file or directory to move
     * @param targetPath UNIX path to the target file or directory will be moved too
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public Response move(final String fromPath, final String targetPath) throws ZosmfRequestException {
        return moveCommon(fromPath, targetPath, true);
    }

    /**
     * Move a UNIX file or directory with overwritten value specified
     *
     * @param fromPath   UNIX path to the source file or directory to move
     * @param targetPath UNIX path to the target file or directory will be moved too
     * @param overwrite  true if you want to override existing data at a target path or false to not override
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response move(final String fromPath, final String targetPath, final boolean overwrite)
            throws ZosmfRequestException {
        return moveCommon(fromPath, targetPath, overwrite);
    }

    /**
     * Move a UNIX file or directory
     *
     * @param fromPath   UNIX path to the source file or directory to move
     * @param targetPath UNIX path to the target file or directory will be moved too
     * @param overwrite  true if you want to override existing data at a target path or false to not override
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     * @author Frank Giordano
     */
    private Response moveCommon(final String fromPath, final String targetPath, final boolean overwrite)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(fromPath, "fromPath");
        ValidateUtils.checkIllegalParameter(targetPath, "targetPath");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> moveMap = new HashMap<>();
        moveMap.put("request", "move");
        moveMap.put("from", FileUtils.validatePath(fromPath));
        moveMap.put("overwrite", overwrite);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(moveMap).toString());

        return request.executeRequest();
    }

}
