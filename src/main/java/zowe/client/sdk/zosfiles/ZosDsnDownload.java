/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.utility.UtilDataset;
import zowe.client.sdk.utility.UtilFiles;
import zowe.client.sdk.utility.UtilRest;
import zowe.client.sdk.zosfiles.input.DownloadParams;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * ZosDsnDownload class that provides download DataSet function
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class ZosDsnDownload {

    private static final Logger LOG = LoggerFactory.getLogger(ZosDsnDownload.class);

    private final ZOSConnection connection;

    /**
     * ZosDsnDownload Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public ZosDsnDownload(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
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
        Util.checkNullParameter(params == null, "params is null");
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getVolume().isPresent()) {
            url += "-(" + params.getVolume().get() + ")/";
        }
        url += Util.encodeURIComponent(dataSetName);
        LOG.debug(url);

        String key, value;
        Map<String, String> headers = UtilFiles.generateHeadersBasedOnOptions(params);

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

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.GET_STREAM);
        request.setHeaders(headers);

        Response response = request.executeRequest();
        if (response.isEmpty()) {
            return null;
        }

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), List.of(dataSetName), UtilDataset.Operation.download);
        }

        return (InputStream) response.getResponsePhrase().orElse(null);
    }

}
