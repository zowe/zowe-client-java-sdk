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
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.UssWriteInputData;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) write object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-write-data-zos-unix-file">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 6.0
 */
public class UssWrite {

    private static final Logger LOG = LoggerFactory.getLogger(UssWrite.class);
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * UssWrite Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public UssWrite(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative UssWrite constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    UssWrite(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        // request type check deferred
        this.request = request;
    }

    /**
     * Perform UNIX write text content request
     *
     * @param fileNamePath file name with a path
     * @param content      string content to write to file
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public Response writeText(final String fileNamePath, final String content) throws ZosmfRequestException {
        return writeCommon(fileNamePath, new UssWriteInputData.Builder().textContent(content).build());
    }

    /**
     * Perform UNIX write binary content request
     *
     * @param fileNamePath file name with a path
     * @param content      binary content to write to file
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     * @author James Kostrewski
     */
    public Response writeBinary(final String fileNamePath, final byte[] content) throws ZosmfRequestException {
        return writeCommon(fileNamePath, new UssWriteInputData.Builder().binaryContent(content).binary(true).build());
    }

    /**
     * Perform UNIX write request driven by UssWriteInputData settings
     *
     * @param fileNamePath   file name with a path
     * @param writeInputData parameters within a UssWriteInputData object that drives the write action request
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public Response writeCommon(final String fileNamePath, final UssWriteInputData writeInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(fileNamePath, "fileNamePath");
        ValidateUtils.checkNullParameter(writeInputData == null, "writeInputData is null");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(fileNamePath));

        final Map<String, String> headers = new HashMap<>();
        if (writeInputData.isBinary()) {
            headers.put("X-IBM-Data-Type", "binary;");
            if (writeInputData.getBinaryContent().isEmpty()) {
                LOG.debug("binaryContent is empty");
            }
            if (request == null || !(request instanceof PutStreamZosmfRequest)) {
                request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_STREAM);
            }
            request.setBody(writeInputData.getBinaryContent().orElse(new byte[0]));
        } else {
            final StringBuilder textHeader = new StringBuilder("text");
            writeInputData.getFileEncoding().ifPresent(encoding -> textHeader.append(";fileEncoding=").append(encoding));
            if (writeInputData.isCrlf()) {
                textHeader.append(";crlf=true");
            }
            // end with semicolon
            textHeader.append(";");
            headers.put("X-IBM-Data-Type", textHeader.toString());
            if (writeInputData.getTextContent().isEmpty()) {
                LOG.debug("textContent is empty");
            }
            if (request == null || !(request instanceof PutTextZosmfRequest)) {
                request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_TEXT);
            }
            request.setBody(writeInputData.getTextContent().orElse(""));
        }

        request.setHeaders(headers);
        request.setUrl(url);

        return request.executeRequest();
    }

}
