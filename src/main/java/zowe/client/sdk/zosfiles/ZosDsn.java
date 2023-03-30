/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.DataSetUtils;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.input.CreateParams;
import zowe.client.sdk.zosfiles.input.ListParams;
import zowe.client.sdk.zosfiles.response.Dataset;
import zowe.client.sdk.zosfiles.types.AttributeType;
import zowe.client.sdk.zosfiles.types.OperationType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ZosDsn class that provides CRUD operations on Datasets
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 1.0
 */
public class ZosDsn {

    private static final Logger LOG = LoggerFactory.getLogger(ZosDsn.class);
    private final ZOSConnection connection;
    private ZoweRequest request;

    /**
     * ZosDsn Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Leonid Baranov
     */
    public ZosDsn(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative ZosDsn constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author Frank Giordano
     */
    public ZosDsn(ZOSConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Create the http body request
     *
     * @param params CreateParams parameters
     * @return body string value for http request
     * @author Leonid Baranov
     */
    private static String buildBody(CreateParams params) {
        final Map<String, Object> jsonMap = new HashMap<>();
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

        final JSONObject jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(String.valueOf(jsonRequestBody));
        return jsonRequestBody.toString();
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
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + EncodeUtils.encodeURIComponent(dataSetName);

        LOG.debug(url);

        final String body = buildBody(params);

        if (request == null || !(request instanceof JsonPostRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.POST_JSON);
        }
        request.setRequest(url, body);
        final Response response = request.executeRequest();

        try {
            RestUtils.checkHttpErrors(response);
        } catch (Exception e) {
            DataSetUtils.checkHttpErrors(e.getMessage(), List.of(dataSetName), OperationType.CREATE);
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
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");
        ValidateUtils.checkNullParameter(member == null, "member is null");
        ValidateUtils.checkIllegalParameter(member.isEmpty(), "member not specified");
        return deleteDsn(String.format("%s(%s)", dataSetName, member));
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
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + EncodeUtils.encodeURIComponent(dataSetName);

        LOG.debug(url);

        if (request == null || !(request instanceof JsonDeleteRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.DELETE_JSON);
        }
        request.setRequest(url);
        final Response response = request.executeRequest();

        try {
            RestUtils.checkHttpErrors(response);
        } catch (Exception e) {
            DataSetUtils.checkHttpErrors(e.getMessage(), List.of(dataSetName), OperationType.DELETE);
        }

        return response;
    }

    /**
     * Retrieves the information about a Dataset.
     *
     * @param dataSetName sequential or partition dataset (e.g. 'DATASET.LIB')
     * @return dataset object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public Dataset getDataSetInfo(String dataSetName) throws Exception {
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");
        Dataset emptyDataSet = new Dataset.Builder().dsname(dataSetName).build();

        final String[] tokens = dataSetName.split("\\.");
        final int length = tokens.length - 1;
        if (1 >= length) {
            return emptyDataSet;
        }

        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(tokens[i]);
            str.append(".");
        }

        String dataSetSearchStr = str.toString();
        dataSetSearchStr = dataSetSearchStr.substring(0, str.length() - 1);
        final ZosDsnList zosDsnList = new ZosDsnList(connection);
        final ListParams params = new ListParams.Builder().attribute(AttributeType.BASE).build();
        final List<Dataset> dsLst = zosDsnList.listDsn(dataSetSearchStr, params);

        final Optional<Dataset> dataSet = dsLst.stream().filter(d -> d.getDsname().orElse("n/a").contains(dataSetName)).findFirst();
        return dataSet.orElse(emptyDataSet);
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
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");
        ValidateUtils.checkNullParameter(member == null, "member is null");
        ValidateUtils.checkIllegalParameter(member.isEmpty(), "member not specified");
        return writeDsn(String.format("%s(%s)", dataSetName, member), content);
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
        ValidateUtils.checkNullParameter(content == null, "content is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + EncodeUtils.encodeURIComponent(dataSetName);

        LOG.debug(url);

        if (request == null || !(request instanceof TextPutRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_TEXT);
        }
        request.setRequest(url, content);
        final Response response = request.executeRequest();

        try {
            RestUtils.checkHttpErrors(response);
        } catch (Exception e) {
            DataSetUtils.checkHttpErrors(e.getMessage(), List.of(dataSetName), OperationType.WRITE);
        }

        return response;
    }

}
