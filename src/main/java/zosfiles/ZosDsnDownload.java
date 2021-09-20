/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.*;
import utility.Util;
import utility.UtilDataset;
import utility.UtilRest;
import utility.UtilZosFiles;
import zosfiles.input.DownloadParams;

import java.io.InputStream;
import java.util.Map;

/**
 * ZosDsnDownload class that provides download DataSet function
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class ZosDsnDownload {

    private static final Logger LOG = LogManager.getLogger(ZosDsnDownload.class);

    private final ZOSConnection connection;

    /**
     * ZosDsnDownload Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public ZosDsnDownload(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Downloads dataset
     *
     * @param dataSetName name of a sequential dataset
     * @param params      download params parameters, see DownloadParams object
     * @return a content stream
     * @throws Exception error processing request
     * @author Nikunj Goyal
     */
    public InputStream downloadDsn(String dataSetName, DownloadParams params) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(params == null, "params is null");
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getVolume().isPresent()) {
            url += params.getVolume().get();
        }
        url += Util.encodeURIComponent(dataSetName);
        LOG.debug(url);

        String key, value;
        Map<String, String> headers = UtilZosFiles.generateHeadersBasedOnOptions(params);

        if (params.getReturnEtag().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
            headers.put(key, value);
        }

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.GET_STREAM);
        request.setAdditionalHeaders(headers);

        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return null;

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), dataSetName, "download");
        }

        return (InputStream) response.getResponsePhrase().orElse(null);
    }

    /**
     * Downloads dataset member content
     *
     * @param dataSetName name of a partitioned dataset
     * @param member    name of one member in the partitioned dataset
     * @param params      download params parameters, see DownloadParams object
     * @return a content stream
     * @throws Exception error processing request
     * @author Nikunj Goyal
     */
    public InputStream downloadDsn(String dataSetName, String member, DownloadParams params) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(params == null, "params is null");
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        String downloadPath = String.format("%s(%s)",
                Util.encodeURIComponent(dataSetName), Util.encodeURIComponent(member));

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getVolume().isPresent()) {
            url += params.getVolume().get();
        }
        url += downloadPath;
        LOG.debug(url);

        String key, value;
        Map<String, String> headers = UtilZosFiles.generateHeadersBasedOnOptions(params);

        if (params.getReturnEtag().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
            headers.put(key, value);
        }

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.GET_STREAM);
        request.setAdditionalHeaders(headers);

        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return null;

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), downloadPath, "download");
        }

        return (InputStream) response.getResponsePhrase().orElse(null);
    }

}
