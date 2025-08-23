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
import zowe.client.sdk.zosfiles.uss.input.UssChangeModeInputData;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) chmod functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public class UssChangeMode {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * UssChangeMode Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssChangeMode(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative UssChangeMode constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     */
    UssChangeMode(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Change the mode of a UNIX file or directory request driven by UssChangeModeInputData object settings
     *
     * @param targetPath          identifies the UNIX file or directory to be the target of the operation
     * @param changeModeInputData change mode response parameters, see UssChangeModeInputData object
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewsk
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public Response change(final String targetPath, final UssChangeModeInputData changeModeInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(targetPath, "targetPath");
        ValidateUtils.checkNullParameter(changeModeInputData == null, "changeModeInputData is null");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> changeModeMap = new HashMap<>();
        changeModeMap.put("request", "chmod");
        if (changeModeInputData.isRecursive()) {
            changeModeMap.put("recursive", "true");
        }
        changeModeInputData.getLinks().ifPresent(type -> changeModeMap.put("links", type.getValue()));
        changeModeMap.put("mode", changeModeInputData.getMode()
                .orElseThrow(() -> new IllegalArgumentException("mode not specified")));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(changeModeMap).toString());

        return request.executeRequest();
    }

}
