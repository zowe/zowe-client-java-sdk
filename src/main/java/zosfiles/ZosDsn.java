package zosfiles;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.*;
import utility.Util;
import utility.UtilDataset;

public class ZosDsn {

    private static final Logger LOG = LogManager.getLogger(ZosDsn.class);

    /**
     * Replaces a content of a dataset or a dataset member with a new content
     * The new dataset member will be created if a dataset member is not exists
     * @param connection is a connection object
     * @param dataSetName is the name of a dataset or a dataset member (f.e. DATASET.LIB(MEMBER))
     * @param content is a new content of the dataset or a dataset member
     */
    public static void writeDsn(ZOSConnection connection, String dataSetName, String content) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" + dataSetName;

        try {
            LOG.info(url);

            ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, content,
                    ZoweRequestType.RequestType.PUT_TEXT);
            Response response = request.executeHttpRequest();
            UtilDataset.checkHttpErrors(response, dataSetName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete dataset or a dataset member
     * @param connection is a connection object
     * @param dataSetName is the name of a dataset or a dataset member (f.e. 'DATASET.LIB(MEMBER)')
     */
    public static void deleteDsn(ZOSConnection connection, String dataSetName) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" + dataSetName;

        try {
            LOG.info(url);

            ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                    ZoweRequestType.RequestType.DELETE_JSON);
            Response response = request.executeHttpRequest();
            UtilDataset.checkHttpErrors(response, dataSetName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
