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
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.get.GetInput;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) read from object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-retrieve-contents-zos-unix-file">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 4.0
 */
public class UssGet {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * UssGet Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public UssGet(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative UssGet constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     * @author James Kostrewski
     */
    UssGet(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        // request type check deferred 
        this.request = request;
    }

    /**
     * Get the binary contents of a UNIX file
     *
     * @param fileNamePath file name with a path
     * @return the byte array contents of the file
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public byte[] getBinary(final String fileNamePath) throws ZosmfRequestException {
        GetInput params = new GetInput.Builder().binary(true).build();
        Response response = getCommon(fileNamePath, params);
        return (byte[]) response.getResponsePhrase().orElse(new byte[0]);
    }

    /**
     * Get the text contents of a UNIX file
     *
     * @param fileNamePath file name with a path
     * @return the text contents of a file
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public String getText(final String fileNamePath) throws ZosmfRequestException {
        GetInput params = new GetInput.Builder().build();
        Response response = getCommon(fileNamePath, params);
        return (String) response.getResponsePhrase().orElse("");
    }

    /**
     * Get the contents of a UNIX file driven by the GetParams object settings
     *
     * @param fileNamePath file name with a path
     * @param params       GetParams object to drive the request
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public Response getCommon(final String fileNamePath, final GetInput params) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(fileNamePath, "fileNamePath");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(fileNamePath)));

        params.getSearch().ifPresent(str -> url.append("?search=").append(EncodeUtils.encodeURIComponent(str)));
        params.getResearch().ifPresent(str -> url.append("?research=").append(EncodeUtils.encodeURIComponent(str)));
        if (!params.isInsensitive()) {
            if (params.getQueryCount() > 1) {
                url.append("&insensitive=false");
            } else {
                url.append("?insensitive=false");
            }
        }
        params.getMaxReturnSize().ifPresent(size -> {
            if (params.getQueryCount() > 1) {
                url.append("&maxreturnsize=").append(size);
            } else {
                url.append("?maxreturnsize=").append(size);
            }
        });

        final Map<String, String> headers = new HashMap<>();

        if (params.isBinary()) {
            headers.put("X-IBM-Data-Type", "binary");
            if (request == null || !(request instanceof GetStreamZosmfRequest)) {
                request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_STREAM);
            }
        } else {
            headers.put("X-IBM-Data-Type", "text");
            if (request == null || !(request instanceof GetTextZosmfRequest)) {
                request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_TEXT);
            }
        }
        params.getRecordsRange().ifPresent(range -> headers.put("X-IBM-Record-Range", range));

        request.setHeaders(headers);
        request.setUrl(url.toString());

        return request.executeRequest();
    }

}
