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
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.WriteParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides unix system services write to object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-write-data-zos-unix-file">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 2.0
 */
public class UssWrite {

    private static final Logger LOG = LoggerFactory.getLogger(UssWrite.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssWrite Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public UssWrite(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssWrite constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author Frank Giordano
     */
    public UssWrite(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Perform write text content request
     *
     * @param fileNamePath file name with path
     * @param content      string content to write to file
     * @return Response object
     * @author James Kostrewski
     */
    public Response writeText(String fileNamePath, String content) throws Exception {
        return writeCommon(fileNamePath, new WriteParams.Builder().textContent(content).build());
    }

    /**
     * Perform write binary content request
     *
     * @param fileNamePath file name with path
     * @param content      binary content to write to file
     * @return Response object
     * @author James Kostrewski
     */
    public Response writeBinary(String fileNamePath, byte[] content) throws Exception {
        return writeCommon(fileNamePath, new WriteParams.Builder().binaryContent(content).binary(true).build());
    }

    /**
     * Perform write request based on WriteParams settings
     *
     * @param fileNamePath file name with path
     * @param params       WriteParams parameters that specifies write action request
     * @return Response object
     * @throws Exception processing error
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public Response writeCommon(String fileNamePath, WriteParams params) throws Exception {
        ValidateUtils.checkNullParameter(fileNamePath == null, "file name path is null");
        ValidateUtils.checkIllegalParameter(fileNamePath.isEmpty(), "fil name path not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + fileNamePath;
        LOG.debug(url);

        final Map<String, String> map = new HashMap<>();

        if (params.binary) {
            map.put("X-IBM-Data-Type", "binary;");
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_STREAM);
            request.setBody(params.binaryContent.orElse(new byte[0]));
        } else {
            final StringBuilder customHeader = new StringBuilder("text");
            params.getFileEncoding().ifPresent(encoding -> customHeader.append(";fileEncoding=").append(encoding));
            if (params.isCrlf()) {
                customHeader.append(";crlf=true");
            }
            // end with semicolon
            customHeader.append(";");
            map.put("X-IBM-Data-Type", customHeader.toString());
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_TEXT);
            request.setBody(params.textContent.orElse(""));
        }

        request.setHeaders(map);
        request.setUrl(url);

        return RestUtils.getResponse(request);
    }

}
