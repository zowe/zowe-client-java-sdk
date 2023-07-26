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
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.WriteParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) write to object functionality
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
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public UssWrite(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof StreamPutRequest)) {
            throw new Exception("PUT_STREAM request type required");
        }
        this.request = request;
    }

    /**
     * Perform UNIX write text content request
     *
     * @param fileNamePath file name with path
     * @param content      string content to write to file
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public Response writeText(String fileNamePath, String content) throws Exception {
        return writeCommon(fileNamePath, new WriteParams.Builder().textContent(content).build());
    }

    /**
     * Perform UNIX write binary content request
     *
     * @param fileNamePath file name with path
     * @param content      binary content to write to file
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public Response writeBinary(String fileNamePath, byte[] content) throws Exception {
        return writeCommon(fileNamePath, new WriteParams.Builder().binaryContent(content).binary(true).build());
    }

    /**
     * Perform UNIX write request driven by WriteParams settings
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
        ValidateUtils.checkIllegalParameter(fileNamePath.isEmpty(), "file name path not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + fileNamePath;
        LOG.debug(url);

        final Map<String, String> headers = new HashMap<>();

        if (params.isBinary()) {
            headers.put("X-IBM-Data-Type", "binary;");
            if (params.getBinaryContent().isEmpty()) {
                LOG.debug("binaryContent is empty");
            }
            if (request == null) {
                request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_STREAM);
            }
            request.setBody(params.getBinaryContent().orElse(new byte[0]));
        } else {
            final StringBuilder textHeader = new StringBuilder("text");
            params.getFileEncoding().ifPresent(encoding -> textHeader.append(";fileEncoding=").append(encoding));
            if (params.isCrlf()) {
                textHeader.append(";crlf=true");
            }
            // end with semicolon
            textHeader.append(";");
            headers.put("X-IBM-Data-Type", textHeader.toString());
            if (params.getTextContent().isEmpty()) {
                LOG.debug("textContent is empty");
            }
            if (request == null || !(request instanceof TextPutRequest)) {
                request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_TEXT);
            }
            request.setBody(params.getTextContent().orElse(""));
        }

        request.setHeaders(headers);
        request.setUrl(url);

        return RestUtils.getResponse(request);
    }

}
