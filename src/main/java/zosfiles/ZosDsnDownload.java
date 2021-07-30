package zosfiles;

import core.ZOSConnection;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rest.IZoweRequest;
import rest.JsonRequest;
import rest.QueryConstants;
import rest.ZosmfHeaders;
import utility.Util;
import utility.UtilDataset;
import utility.UtilIO;
import utility.UtilZosFiles;
import zosfiles.input.DownloadParams;
import zosfiles.response.Dataset;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ZosDsnDownload {
    private static final Logger LOG = LogManager.getLogger(ZosDsnList.class);


    public static List<Dataset> downloadMembers(ZOSConnection connection, String dataSetName, DownloadParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        List<Dataset> datasets = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES  + "/" + dataSetName + ZosFilesConstants.RES_DS_MEMBERS;
        try {
            if (options.getPattern().isPresent()) {
                url += QueryConstants.QUERY_ID + ZosFilesConstants.QUERY_PATTERN + options.getPattern().get();
            }
            String key, value;
            Map<String, String> headers = new HashMap<>();
            key = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(0);
            value = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(1);
            headers.put(key, value);

            if (options.getAttributes().isPresent()) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
                headers.put(key, value);
            }
            if (options.getMaxLength().isPresent()) {
                key = "X-IBM-Max-Items";
                value = options.getMaxLength().get();
                headers.put(key, value);
            } else {
                key = ZosmfHeaders.HEADERS.get("X_IBM_MAX_ITEMS").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
                headers.put(key, value);
            }
            if (options.getResponseTimeout().isPresent()) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
                value = options.getResponseTimeout().get();
                headers.put(key, value);
            }
            LOG.info(url);

            IZoweRequest request = new JsonRequest(connection, new HttpGet(url));
            request.setHeaders(headers);
            JSONObject results = request.httpGet();
            JSONArray items = (JSONArray) results.get("items");
            items.forEach(item -> {
                JSONObject datasetObj = (JSONObject) item;
                datasets.add(UtilDataset.createDatasetObjFromJson(datasetObj));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datasets;
    }
    public static List<Dataset> downloadDsn(ZOSConnection connection, String dataSetName, DownloadParams options) throws IOException {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        List<Dataset> datasets = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES;

        try {
            if (options.getVolume().isPresent()) {
                url += options.getVolume().get();
            }
            url += dataSetName;
            LOG.info(url);

            Map<String, String> headers = UtilZosFiles.generateHeadersBasedOnOptions(options);
            String extension = UtilZosFiles.DEFAULT_FILE_EXTENSION;

            if (options.getExtension().get() != null) {
                extension = options.getExtension().get();
            }

            UtilIO.createDirsSyncFromFilePath(destination(dataSetName, options, extension));

            if (options.getAttributes().isPresent()) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
                headers.put(key, value);
            }
            if (options.getMaxLength().isPresent()) {
                key = "X-IBM-Max-Items";
                value = options.getMaxLength().get();
                headers.put(key, value);
            } else {
                key = ZosmfHeaders.HEADERS.get("X_IBM_MAX_ITEMS").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
                headers.put(key, value);
            }
            if (options.getResponseTimeout().isPresent()) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
                value = options.getResponseTimeout().get();
                headers.put(key, value);
            }
            if (options.getRecall().isPresent()) {
                switch (options.getRecall().get().toLowerCase(Locale.ROOT)) {
                    case "wait":
                        key = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_WAIT").get(0);
                        value = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_WAIT").get(1);
                        headers.put(key, value);
                        break;
                    case "nowait":
                        key = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_NO_WAIT").get(0);
                        value = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_NO_WAIT").get(1);
                        headers.put(key, value);
                        break;
                    case "error":
                        key = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_ERROR").get(0);
                        value = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_ERROR").get(1);
                        headers.put(key, value);
                        break;
                }
            }
            LOG.info(url);

            IZoweRequest request = new JsonRequest(connection, new HttpGet(url));
            request.setHeaders(headers);
            JSONObject results = request.httpGet();
            JSONArray items = (JSONArray) results.get(ZosFilesConstants.RESPONSE_ITEMS);
            items.forEach(item -> {
                JSONObject datasetObj = (JSONObject) item;
                datasets.add(UtilDataset.createDatasetObjFromJson(datasetObj));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datasets;
    }
    // Get a proper destination for the file to be downloaded
    // If the "file" is not provided, we create a folder structure similar to the data set name
    // Note that the "extension" options do not affect the destination if the "file" options were provided
    public static String destination(String dataSetName,DownloadParams options,String extention) {
        if(options.getFile().isPresent()){
            return options.getFile().get();
        }

        String generatedFilePath = UtilZosFiles.getDirsFromDataSet(dataSetName);
        // Method above lowercased characters.
        // In case of preserving original letter case, uppercase all characters.
        if(options.getPreserveOriginalLetterCase().isPresent()) {
            generatedFilePath = generatedFilePath.toUpperCase(Locale.ROOT);
        }

        return generatedFilePath + UtilIO.normalizeExtension(extention);
    }
}
