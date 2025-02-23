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
import zowe.client.sdk.zosfiles.uss.input.CopyParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) copy functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssCopy {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * UssCopy Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssCopy(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssCopy constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     */
    public UssCopy(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Copy a Unix file or directory to another location
     *
     * @param fromPath   the source path of the file or directory to copy
     * @param targetPath target path of where the file or directory will be copied too
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public Response copy(final String fromPath, final String targetPath) throws ZosmfRequestException {
        return copyCommon(targetPath, new CopyParams.Builder().from(fromPath).build());
    }

    /**
     * Copy a Unix file or directory to another location request driven by CopyParams object settings
     *
     * @param targetPath target path of where the file or directory will be copied too
     * @param params     CopyParams parameters that specifies copy action request
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public Response copyCommon(final String targetPath, final CopyParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> copyMap = new HashMap<>();
        copyMap.put("request", "copy");
        copyMap.put("from", FileUtils.validatePath(params.getFrom()
                .orElseThrow(() -> new IllegalStateException("copy /'from/' not specified"))));
        if (!params.isOverwrite()) {
            copyMap.put("overwrite", "false");
        }
        if (params.isRecursive()) {
            copyMap.put("recursive", "true");
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        connection.getCookie().ifPresentOrElse(c -> request.setCookie(c), () -> request.setCookie(null));
        request.setBody(new JSONObject(copyMap).toString());

        return request.executeRequest();
    }

}
