package zosfiles;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.*;
import utility.Util;
import utility.UtilDataset;

public class ZosDsn {
    private static final Logger LOG = LogManager.getLogger(ZosDsn.class);

    public static void writeDsn(ZOSConnection connection, String dataSetName, String content) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" + dataSetName;

        try {
            LOG.info(url);

            ZoweRequest request = new TextPutRequest(connection, url, content);
            Response response = request.executeHttpRequest();
            UtilDataset.checkHttpErrors(response, dataSetName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
