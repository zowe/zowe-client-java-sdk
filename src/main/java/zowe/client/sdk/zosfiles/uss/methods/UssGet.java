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
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.GetParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) read from object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-retrieve-contents-zos-unix-file">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 2.0
 */
public class UssGet {

    private static final Logger LOG = LoggerFactory.getLogger(UssGet.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssGet Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public UssGet(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssGet constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public UssGet(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Get the binary contents of a file
     *
     * @param fileNamePath file name with path
     * @return the byte array contents of the file
     * @throws Exception processing error
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public byte[] getBinary(String fileNamePath) throws Exception {
        GetParams params = new GetParams.Builder().binary(true).build();
        Response response = getCommon(fileNamePath, params);
        return (byte[]) response.getResponsePhrase().orElse(new byte[0]);
    }

    /**
     * Get the text contents of a file
     *
     * @param fileNamePath file name with path
     * @return the text contents of file
     * @throws Exception processing error
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public String getText(String fileNamePath) throws Exception {
        GetParams params = new GetParams.Builder().build();
        Response response = getCommon(fileNamePath, params);
        return (String) response.getResponsePhrase().orElse("");
    }

    /**
     * Get the contents of a file driven by the GetParams object settings
     *
     * @param filePathName file name with path
     * @param params       GetParams object to drive the request
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public Response getCommon(String filePathName, GetParams params) throws Exception {
        ValidateUtils.checkNullParameter(filePathName == null, "file path name is null");
        ValidateUtils.checkIllegalParameter(filePathName.isEmpty(), "file path name not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final StringBuilder url = new StringBuilder("https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + filePathName);

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
        LOG.debug(url.toString());

        final Map<String, String> headers = new HashMap<>();

        if (params.isBinary()) {
            headers.put("X-IBM-Data-Type", "binary");
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_STREAM);
        } else {
            headers.put("X-IBM-Data-Type", "text");
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_TEXT);
        }
        params.getRecordsRange().ifPresent(range -> headers.put("X-IBM-Record-Range", range));

        request.setHeaders(headers);
        request.setUrl(url.toString());

        return RestUtils.getResponse(request);
    }

}