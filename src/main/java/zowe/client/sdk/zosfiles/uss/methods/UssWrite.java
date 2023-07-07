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
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.WriteParams;

/**
 * Provides unix system service write to object functionality
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
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public UssWrite(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Perform a write string content request
     *
     * @param value   file name with path
     * @param content string content to write to file
     * @return Response object
     */
    public Response writeFile(String value, String content) {
        // TODO
        return writeCommon(value, null);
    }

    /**
     * Perform a write binary content request
     *
     * @param value   file name with path
     * @param content binary content to write to file
     * @return Response object
     */
    public Response writeBinary(String value, byte[] content) {
        // TODO
        return writeCommon(value, null);
    }

    /**
     * Perform a write request based on WriteParams settings
     *
     * @param value  file name with path
     * @param params WriteParams parameters that specifies write action request
     * @return Response object
     */
    public Response writeCommon(String value, WriteParams params) {
        ValidateUtils.checkNullParameter(value == null, "value is null");
        ValidateUtils.checkIllegalParameter(value.isEmpty(), "value not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + value;
        // TODO
        return null;
    }

}
