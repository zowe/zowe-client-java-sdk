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
import java.util.*;

/**
 * ZosDsnDownload class that provides download DataSet function
 *
 * @version 1.0
 */
public class ZosDsnDownload {

    private static final Logger LOG = LogManager.getLogger(ZosDsnDownload.class);

    private final ZOSConnection connection;

    /**
     * ZosDsnDownload Constructor
     *
     * @param connection connection information, see ZOSConnection object
     */
    public ZosDsnDownload(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Downloads dataset or dataset member content
     *
     * @param dataSetName name of a dataset or a dataset member (f.e. DATASET.LIB(MEMBER))
     * @param options     download options parameters, see DownloadParams object
     * @return a content stream
     */
    public InputStream downloadDsn(String dataSetName, DownloadParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName not specified");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        try {
            if (options.getVolume().isPresent()) {
                url += options.getVolume().get();
            }
            url += dataSetName;
            LOG.debug(url);

            String key, value;
            Map<String, String> headers = UtilZosFiles.generateHeadersBasedOnOptions(options);

            if (options.getReturnEtag().isPresent()) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
                headers.put(key, value);
                // TODO
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
                UtilDataset.checkHttpErrors(e.getMessage(), dataSetName);
            }
            if (response.getResponsePhrase().isPresent()) {
                return (InputStream) response.getResponsePhrase().get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
