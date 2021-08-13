package zosfiles;

import core.ZOSConnection;
import org.json.simple.JSONObject;
import rest.*;
import utility.Util;
import utility.UtilDataset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosfiles.input.CreateParams;

public class ZosDsnCreate {
    private static final Logger LOG = LogManager.getLogger(ZosDsn.class);

    public static void createDsn(ZOSConnection connection, String dataSetName, CreateParams params) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" + dataSetName;

        try {
            LOG.info(url);

            String body = buildBody(params);

            ZoweRequest request =  ZoweRequestFactory.buildRequest(connection, url, body,
                    ZoweRequestType.RequestType.POST_JSON);

            Response response = request.executeHttpRequest();
            UtilDataset.checkHttpErrors(response, dataSetName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildBody(CreateParams params) {
        JSONObject reqBody = new JSONObject();

        params.getVolser().ifPresent(v -> reqBody.put("volser", v));
        params.getUnit().ifPresent(v -> reqBody.put("unit", v));
        params.getDsorg().ifPresent(v -> reqBody.put("dsorg", v));
        params.getAlcunit().ifPresent(v -> reqBody.put("alcunit", v));
        params.getPrimary().ifPresent(v -> reqBody.put("primary", v));
        params.getSecondary().ifPresent(v -> reqBody.put("secondary", v));
        params.getDirblk().ifPresent(v -> reqBody.put("dirblk", v));
        params.getAvgblk().ifPresent(v -> reqBody.put("avgblk", v));
        params.getRecfm().ifPresent(v -> reqBody.put("recfm", v));
        params.getBlksize().ifPresent(v -> reqBody.put("blksize", v));
        params.getLrecl().ifPresent(v -> reqBody.put("lrecl", v));
        params.getStorclass().ifPresent(v -> reqBody.put("storclass", v));
        params.getStorclass().ifPresent(v -> reqBody.put("mgntclass", v));
        params.getMgntclass().ifPresent(v -> reqBody.put("mgntclass", v));
        params.getDataclass().ifPresent(v -> reqBody.put("dataclass", v));
        params.getDsntype().ifPresent(v -> reqBody.put("dsntype", v));

        LOG.debug(reqBody);
        return reqBody.toString();
    }

}