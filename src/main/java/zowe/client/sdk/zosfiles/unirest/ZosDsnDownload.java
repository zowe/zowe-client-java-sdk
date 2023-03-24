/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.unirest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.ZosmfHeaders;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.rest.unirest.StreamGetRequest;
import zowe.client.sdk.rest.unirest.ZoweRequest;
import zowe.client.sdk.rest.unirest.ZoweRequestFactory;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.unirest.UniRestUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.input.DownloadParams;

import java.io.InputStream;
import java.util.Map;

/**
 * ZosDsnDownload class that provides download DataSet function
 *
 * @author Nikunj Goyal
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosDsnDownload {

    private static final Logger LOG = LoggerFactory.getLogger(ZosDsnDownload.class);
    private final ZOSConnection connection;
    private ZoweRequest request;

    /**
     * ZosDsnDownload Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public ZosDsnDownload(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative ZosDsnDownload constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ZosDsnDownload(ZOSConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof StreamGetRequest)) {
            throw new Exception("GET_STREAM request type required");
        }
        this.request = request;
    }

    /**
     * Downloads a sequential dataset or dataset member content
     *
     * @param dataSetName name of a sequential dataset e.g. DATASET.SEQ.DATA
     *                    or a dataset member e.g. DATASET.LIB(MEMBER))
     * @param params      download params parameters, see DownloadParams object
     * @return a content stream
     * @throws Exception error processing request
     * @author Nikunj Goyal
     */
    public InputStream downloadDsn(String dataSetName, DownloadParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getVolume().isPresent()) {
            url += "-(" + params.getVolume().get() + ")/";
        }
        url += EncodeUtils.encodeURIComponent(dataSetName);
        LOG.debug(url);

        String key, value;
        final Map<String, String> headers = FileUtils.generateHeadersBasedOnOptions(params);

        if (params.getReturnEtag().orElse(false)) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
            headers.put(key, value);
        }

        if (params.getBinary().orElse(false)) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(1);
            headers.put(key, value);
        }

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_STREAM);
        }
        request.setUrl(url);
        request.setHeaders(headers);

        return (InputStream) UniRestUtils.getResponse(request).getResponsePhrase().get();
    }

}
