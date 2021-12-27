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
import org.json.simple.JSONObject;
import rest.Response;
import rest.ZoweRequest;
import rest.ZoweRequestFactory;
import rest.ZoweRequestType;
import utility.Util;
import utility.UtilDataset;
import utility.UtilRest;
import zosfiles.input.CreateParams;
import zosfiles.input.ListParams;
import zosfiles.response.Dataset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * ZosDsn class that provides CRUD operations on Datasets
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class ZosDsn {

    private static final Logger LOG = LogManager.getLogger(ZosDsn.class);

    private final ZOSConnection connection;

    /**
     * ZosDsn Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Leonid Baranov
     */
    public ZosDsn(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Retrueves the information about a Dataset.
     *
     * @param dataSetName sequential or partition dataset (e.g. 'DATASET.LIB')
     * @return dataset object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public Dataset getDataSetInfo(String dataSetName) throws Exception {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");
        Dataset emptyDataSet = new Dataset.Builder().dsname(dataSetName).build();

        String[] tokens = dataSetName.split("\\.");
        int length = tokens.length - 1;
        if (1 >= length) return emptyDataSet;

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(tokens[i]);
            str.append(".");
        }

        String dataSetSearchStr = str.toString();
        dataSetSearchStr = dataSetSearchStr.substring(0, str.length() - 1);
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        ListParams params = new ListParams.Builder().attribute(UtilDataset.Attribute.BASE).build();
        List<Dataset> dsLst = zosDsnList.listDsn(dataSetSearchStr, params);

        Optional<Dataset> dataSet = dsLst.stream().filter(d -> d.getDsname().get().contains(dataSetName)).findFirst();
        return dataSet.orElse(emptyDataSet);
    }

    /**
     * Replaces the content of an existing sequential data set with new content.
     *
     * @param dataSetName sequential dataset (e.g. 'DATASET.LIB')
     * @param content     new content
     * @return http response object
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public Response writeDsn(String dataSetName, String content) throws Exception {
        Util.checkNullParameter(content == null, "content is null");
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + Util.encodeURIComponent(dataSetName);

        LOG.debug(url);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, content,
                ZoweRequestType.VerbType.PUT_TEXT);
        Response response = request.executeRequest();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), List.of(dataSetName), UtilDataset.Operation.write);
        }

        return response;
    }

    /**
     * Replaces the content of a member of a partitioned data set (PDS or PDSE) with new content.
     * A new dataset member will be created if the specified dataset member does not exist.
     *
     * @param dataSetName dataset name of where the member is located (e.g. 'DATASET.LIB')
     * @param member      name of member to add new content
     * @param content     new content
     * @return http response object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public Response writeDsn(String dataSetName, String member, String content) throws Exception {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");
        Util.checkNullParameter(member == null, "member is null");
        Util.checkIllegalParameter(member.isEmpty(), "member not specified");

        return writeDsn(String.format("%s(%s)", dataSetName, member), content);
    }

    /**
     * Delete a dataset
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @return http response object
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public Response deleteDsn(String dataSetName) throws Exception {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + Util.encodeURIComponent(dataSetName);

        LOG.debug(url);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.DELETE_JSON);
        Response response = request.executeRequest();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), List.of(dataSetName), UtilDataset.Operation.delete);
        }

        return response;
    }

    /**
     * Delete a dataset member
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @param member      name of member to delete
     * @return http response object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public Response deleteDsn(String dataSetName, String member) throws Exception {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");
        Util.checkNullParameter(member == null, "member is null");
        Util.checkIllegalParameter(member.isEmpty(), "member not specified");

        return deleteDsn(String.format("%s(%s)", dataSetName, member));
    }

    /**
     * Creates a new dataset with specified parameters
     *
     * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
     * @param params      create dataset parameters, see CreateParams object
     * @return http response object
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public Response createDsn(String dataSetName, CreateParams params) throws Exception {
        Util.checkNullParameter(params == null, "params is null");
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + Util.encodeURIComponent(dataSetName);

        LOG.debug(url);

        String body = buildBody(params);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, body,
                ZoweRequestType.VerbType.POST_JSON);

        Response response = request.executeRequest();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), List.of(dataSetName), UtilDataset.Operation.create);
        }

        return response;
    }

    /**
     * Create the http body request
     *
     * @param params CreateParams parameters
     * @return body string value for http request
     * @author Leonid Baranov
     */
    private static String buildBody(CreateParams params) {
        var jsonMap = new HashMap<String, Object>();
        params.getVolser().ifPresent(v -> jsonMap.put("volser", v));
        params.getUnit().ifPresent(v -> jsonMap.put("unit", v));
        params.getDsorg().ifPresent(v -> jsonMap.put("dsorg", v));
        params.getAlcunit().ifPresent(v -> jsonMap.put("alcunit", v));
        params.getPrimary().ifPresent(v -> jsonMap.put("primary", v));
        params.getSecondary().ifPresent(v -> jsonMap.put("secondary", v));
        params.getDirblk().ifPresent(v -> jsonMap.put("dirblk", v));
        params.getAvgblk().ifPresent(v -> jsonMap.put("avgblk", v));
        params.getRecfm().ifPresent(v -> jsonMap.put("recfm", v));
        params.getBlksize().ifPresent(v -> jsonMap.put("blksize", v));
        params.getLrecl().ifPresent(v -> jsonMap.put("lrecl", v));
        params.getStorclass().ifPresent(v -> jsonMap.put("storclass", v));
        params.getStorclass().ifPresent(v -> jsonMap.put("mgntclass", v));
        params.getMgntclass().ifPresent(v -> jsonMap.put("mgntclass", v));
        params.getDataclass().ifPresent(v -> jsonMap.put("dataclass", v));
        params.getDsntype().ifPresent(v -> jsonMap.put("dsntype", v));

        var jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(jsonRequestBody);
        return jsonRequestBody.toString();
    }

}
