package zosfiles;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.*;
import utility.Util;
import utility.UtilDataset;
import utility.UtilZosFiles;
import zosfiles.input.DownloadParams;

import java.io.InputStream;
import java.util.*;

public class ZosDsnDownload {

    private static final Logger LOG = LogManager.getLogger(ZosDsnDownload.class);

    public static InputStream downloadDsn(ZOSConnection connection, String dataSetName, DownloadParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        try {
            if (options.getVolume().isPresent()) {
                url += options.getVolume().get();
            }
            url += dataSetName;
            LOG.info(url);

            String key, value;
            Map<String, String> headers = UtilZosFiles.generateHeadersBasedOnOptions(options);

            if (options.getReturnEtag().isPresent()) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
                headers.put(key, value);
//              TODO
//                requestOptions.dataToReturn = [CLIENT_PROPERTY.response];
            }

            ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                    ZoweRequestType.RequestType.GET_STREAM);
            request.setAdditionalHeaders(headers);

            Response response = request.executeHttpRequest();
            UtilDataset.checkHttpErrors(response, dataSetName);

            if (response.getResponsePhrase().isPresent()) {
                return (InputStream) response.getResponsePhrase().get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
