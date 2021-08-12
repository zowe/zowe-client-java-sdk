package zosfiles;

import core.ZOSConnection;
import rest.*;
import utility.Util;
import utility.UtilDataset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosfiles.input.CreateParams;
import zosfiles.input.ListParams;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ZosDsnCreate {
    private static final Logger LOG = LogManager.getLogger(ZosDsn.class);

    public static void createDsn(ZOSConnection connection, String dataSetName, CreateParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        Map<String, String> headers = new HashMap<>();
        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" + dataSetName;

        try {
            LOG.info(url);

            setHeaders(options, headers);
            ZoweRequest request =  ZoweRequestFactory.buildRequest(connection, url, null,
                    ZoweRequestType.RequestType.POST_JSON);
            request.setAdditionalHeaders(headers);
            Response response = request.executeHttpRequest();
            UtilDataset.checkHttpErrors(response, dataSetName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setHeaders(CreateParams options, Map<String, String> headers) {
//        String key, value;
//        headers.put(key, value);

    }
}