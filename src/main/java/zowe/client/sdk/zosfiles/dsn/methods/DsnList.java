/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.methods;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.dsn.input.ListParams;
import zowe.client.sdk.zosfiles.dsn.response.Dataset;
import zowe.client.sdk.zosfiles.dsn.response.Member;
import zowe.client.sdk.zosfiles.dsn.types.AttributeType;

import java.util.*;

/**
 * Provides list dataset and member functionality
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class DsnList {

    private static final Logger LOG = LoggerFactory.getLogger(DsnList.class);

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * DsnList constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public DsnList(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnList constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public DsnList(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Get a list of Dataset objects
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @param params      list parameters, see ListParams object
     * @return A String list of Dataset names
     * @throws ZosmfRequestException request error state
     * @author Nikunj Goyal
     */
    public List<Dataset> getDatasets(final String dataSetName, final ListParams params)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isBlank(), "dataSetName not specified");

        final Map<String, String> headers = new HashMap<>();
        final List<Dataset> datasets = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + QueryConstants.QUERY_ID +
                ZosFilesConstants.QUERY_DS_LEVEL + EncodeUtils.encodeURIComponent(dataSetName);

        if (params.getVolume().isPresent()) {
            url += QueryConstants.COMBO_ID + ZosFilesConstants.QUERY_VOLUME +
                    EncodeUtils.encodeURIComponent(params.getVolume().get());
        }
        if (params.getStart().isPresent()) {
            url += QueryConstants.COMBO_ID + ZosFilesConstants.QUERY_START + params.getStart().get();
        }

        return getResult(getResponse(params, headers, url), datasets, null);
    }

    /**
     * Get a list of member objects from a partition Dataset
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @param params      list parameters, see ListParams object
     * @return list of member objects
     * @throws ZosmfRequestException request error state
     * @author Nikunj Goyal
     */
    public List<Member> getMembers(final String dataSetName, final ListParams params)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isBlank(), "dataSetName not specified");

        final Map<String, String> headers = new HashMap<>();
        final List<Member> members = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" +
                EncodeUtils.encodeURIComponent(dataSetName) + ZosFilesConstants.RES_DS_MEMBERS;

        if (params.getPattern().isPresent()) {
            url += QueryConstants.QUERY_ID + ZosFilesConstants.QUERY_PATTERN +
                    EncodeUtils.encodeURIComponent(params.getPattern().get());
        }

        return getResult(getResponse(params, headers, url), null, members);
    }

    /**
     * Retrieve a list result of either members or datasets from rest api response.
     * Use null of either datasetLst or memberLst to denote the list object being processed.
     *
     * @param response   Response object containing rest api result
     * @param datasetLst dataset arraylist object container
     * @param memberLst  member arraylist object container
     * @param <T>        DataSet or Member object
     * @return one of the container filled with either member or datasets
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> getResult(final Response response, final List<T> datasetLst,
                                  final List<T> memberLst) throws ZosmfRequestException {
        if (response.getStatusCode().isEmpty()) {
            LOG.debug("status code not returned");
            if (datasetLst == null) {
                return memberLst;
            } else {
                return datasetLst;
            }
        }

        if (response.getResponsePhrase().isEmpty()) {
            LOG.debug(ZosFilesConstants.RESPONSE_PHRASE_ERROR);
            if (datasetLst == null) {
                return memberLst;
            } else {
                return datasetLst;
            }
        }

        final int statusCode = response.getStatusCode().getAsInt();
        if (!(statusCode >= 100 && statusCode <= 299)) {
            if (response.getStatusText().isEmpty()) {
                LOG.debug("status text not returned");
                if (datasetLst == null) {
                    return memberLst;
                } else {
                    return datasetLst;
                }
            }
            LOG.debug("rest status code {}", response.getStatusCode().getAsInt());
            LOG.debug("rest status text {}", response.getStatusText().get());
            final String errMsg = "http status error code: " + statusCode + ", status text: " +
                    response.getStatusText().get() + ", response phrase: " + response.getResponsePhrase().get();
            throw new IllegalStateException(errMsg);
        }

        final String jsonStr = response.getResponsePhrase().get().toString();
        final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
        if (jsonObject.isEmpty()) {
            if (datasetLst == null) {
                return memberLst;
            } else {
                return datasetLst;
            }
        }

        final JSONArray items = (JSONArray) jsonObject.get(ZosFilesConstants.RESPONSE_ITEMS);
        for (final Object obj : items) {
            if (datasetLst == null) {
                memberLst.add((T) JsonParseFactory.buildParser(ParseType.MEMBER).parseResponse(obj));
            } else {
                datasetLst.add((T) JsonParseFactory.buildParser(ParseType.DATASET).parseResponse(obj));
            }
        }

        if (datasetLst == null) {
            return memberLst;
        } else {
            return datasetLst;
        }
    }

    /**
     * Perform the http request and return its response.
     *
     * @param params  list parameters
     * @param headers list of headers for http request
     * @param url     url for http request
     * @return response object with http response info
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private Response getResponse(final ListParams params, final Map<String, String> headers, final String url)
            throws ZosmfRequestException {
        setHeaders(params, headers);
        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setHeaders(headers);
        request.setUrl(url);

        return request.executeRequest();
    }

    /**
     * Generate the http headers for the request
     *
     * @param params  list parameters
     * @param headers list of headers for http request
     * @author Nikunj Goyal
     */
    private void setHeaders(final ListParams params, final Map<String, String> headers) {
        String key = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(0);
        String value = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(1);
        headers.put(key, value);

        if (params.getAttribute().isPresent()) {
            AttributeType attribute = params.getAttribute().get();
            if (attribute == AttributeType.BASE) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
            } else if (attribute == AttributeType.VOL) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_VOL").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_VOL").get(1);
            }
            headers.put(key, value);
        }
        if (params.getMaxLength().isPresent()) {
            key = "X-IBM-Max-Items";
            value = params.getMaxLength().get();
        } else {
            key = ZosmfHeaders.HEADERS.get("X_IBM_MAX_ITEMS").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_MAX_ITEMS").get(1);
        }
        headers.put(key, value);
        if (params.getResponseTimeout().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
            value = params.getResponseTimeout().get();
            headers.put(key, value);
        }
        if (params.getRecall().isPresent()) {
            switch (params.getRecall().get().toLowerCase(Locale.ROOT)) {
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
    }

}
