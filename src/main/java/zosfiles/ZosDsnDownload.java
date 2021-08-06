package zosfiles;

import core.ZOSConnection;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rest.JsonGetRequest;
import rest.Response;
import rest.ZosmfHeaders;
import rest.ZoweRequest;
import utility.Util;
import utility.UtilDataset;
import utility.UtilIO;
import utility.UtilZosFiles;
import zosfiles.input.DownloadParams;
import zosfiles.input.ListParams;
import zosfiles.response.Dataset;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ZosDsnDownload {
    private static final Logger LOG = LogManager.getLogger(ZosDsnList.class);


    public static List<Dataset> downloadMembers(ZOSConnection connection, String dataSetName, DownloadParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        List<Dataset> memberList = null;


        try {
            ListParams parms =  new ListParams.Builder()
                                .volume(options.getVolume().get())
                                .responseTimeout(options.getResponseTimeout().get())
                                .build();
            List<Dataset> response =  ZosDsnList.listMembers(connection, dataSetName, parms);
            memberList = response;

            List<Exception> downloadErrors = null;
            List<String> failedMembers = null ;
            Integer downloadsInitiated = 0;

            String extension = UtilZosFiles.DEFAULT_FILE_EXTENSION;

            if(options.getExtension().get() != null) {
                extension = options.getExtension().get();
            }


//           TODO
//            /**
//             * Function that takes a member and turns it into a promise to download said member
//             * @param mem - an object with a "member" field containing the name of the data set member
//             */
//            const createDownloadPromise = (mem: { member: string }) => {
//                // update the progress bar if any
//                if (options.task != null) {
//                    options.task.statusMessage = "Downloading " + mem.member;
//                    options.task.percentComplete = Math.floor(TaskProgress.ONE_HUNDRED_PERCENT *
//                            (downloadsInitiated / memberList.length));
//                    downloadsInitiated++;
//                }
//                const fileName = options.preserveOriginalLetterCase ? mem.member : mem.member.toLowerCase();
//                return this.dataSet(session, `${dataSetName}(${mem.member})`, {
//                    volume: options.volume,
//                            file: baseDir + IO.FILE_DELIM + fileName + IO.normalizeExtension(extension),
//                            binary: options.binary,
//                            encoding: options.encoding,
//                            responseTimeout: options.responseTimeout
//                }).catch((err) => {
//                // If we should fail fast, rethrow error
//                if (options.failFast || options.failFast === undefined) {
//                    throw err;
//                }
//                downloadErrors.push(err);
//                failedMembers.push(fileName);
//                // Delete the file that could not be downloaded
//                IO.deleteFile(baseDir + IO.FILE_DELIM + fileName + IO.normalizeExtension(extension));
//                });
//            };


            Integer maxConcurrentRequests = options.getNaxConcurrentRequests().get() == null ? 1 : options.getNaxConcurrentRequests().get();


//           TODO
//            if (maxConcurrentRequests === 0) {
//                await Promise.all(memberList.map(createDownloadPromise));
//            } else {
//                await asyncPool(maxConcurrentRequests, memberList, createDownloadPromise);
//            }
            // Handle failed downloads if no errors were thrown yet
            if (downloadErrors.size() > 0) {
                throw new Exception(
                        "Failed to download the following members: \n" + failedMembers + "\n\n"
//                       TODO
//                        downloadErrors.map((err: Error) => err.message).join("\n"),
//                        causeErrors: downloadErrors,
//                        additionalDetails: failedMembers.join("\n")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberList;
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

            String key, value;
            Map<String, String> headers = UtilZosFiles.generateHeadersBasedOnOptions(options);
            String extension = UtilZosFiles.DEFAULT_FILE_EXTENSION;

            if (options.getExtension().get() != null) {
                extension = options.getExtension().get();
            }

            UtilIO.createDirsSyncFromFilePath(destination(dataSetName, options, extension));

            FileOutputStream writeStream =  UtilIO.createWriteStream(destination(dataSetName, options, extension));

//      TODO
//            Object requestOptions = {
//                    resource: endpoint,
//                    reqHeaders,
//                    responseStream: writeStream,
//                    normalizeResponseNewLines: !options.binary,
//                    task: options.task
//            };

            if (options.getReturnEtag().isPresent()) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
                headers.put(key, value);
//              TODO
//                requestOptions.dataToReturn = [CLIENT_PROPERTY.response];
            }


            ZoweRequest request = new JsonGetRequest(connection, Optional.ofNullable(url));
            request.setHeaders(headers);
            Response response = request.executeHttpRequest();
            JSONObject results = (JSONObject) response.getResponsePhrase().orElseThrow(Exception::new);
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
