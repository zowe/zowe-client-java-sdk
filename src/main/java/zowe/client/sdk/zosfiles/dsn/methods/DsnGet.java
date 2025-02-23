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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetStreamZosmfRequest;
import zowe.client.sdk.rest.ZosmfHeaders;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.dsn.input.DownloadParams;
import zowe.client.sdk.zosfiles.dsn.input.ListParams;
import zowe.client.sdk.zosfiles.dsn.response.Dataset;
import zowe.client.sdk.zosfiles.dsn.types.AttributeType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Provides retrieve dataset and member functionality
 *
 * @author Nikunj Goyal
 * @author Frank Giordano
 * @version 2.0
 */
public class DsnGet {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * DsnGet Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Nikunj Goyal
     */
    public DsnGet(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnGet constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public DsnGet(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof GetStreamZosmfRequest)) {
            throw new IllegalStateException("GET_STREAM request type required");
        }
        this.request = request;
    }

    /**
     * Retrieve dataset information.
     *
     * @param dataSetName sequential or partition dataset (e.g. 'DATASET.LIB')
     * @return dataset object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Dataset getDsnInfo(final String dataSetName) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isBlank(), "dataSetName not specified");
        Dataset emptyDataSet = new Dataset.Builder().dsname(dataSetName).build();

        final String[] tokens = dataSetName.split("\\.");
        final int length = tokens.length - 1;
        if (1 >= length) {
            return emptyDataSet;
        }

        final StringBuilder str = new StringBuilder();
        IntStream.of(0, length).forEach(i -> str.append(tokens[i]).append("."));

        String dataSetSearchStr = str.toString();
        dataSetSearchStr = dataSetSearchStr.substring(0, str.length() - 1);
        final DsnList dsnList = new DsnList(connection);
        final ListParams params = new ListParams.Builder().attribute(AttributeType.BASE).build();
        final List<Dataset> dsLst = dsnList.getDatasets(dataSetSearchStr, params);

        final Optional<Dataset> dataSet = dsLst.stream()
                .filter(d -> d.getDsname().orElse("n/a").contains(dataSetName)).findFirst();
        return dataSet.orElse(emptyDataSet);
    }

    /**
     * Retrieve sequential dataset or dataset member content
     *
     * @param targetName name of a sequential dataset e.g. DATASET.SEQ.DATA
     *                   or a dataset member e.g. DATASET.LIB(MEMBER))
     * @param params     download params parameters, see DownloadParams object
     * @return a content stream
     * @throws ZosmfRequestException request error state
     * @author Nikunj Goyal
     */
    public InputStream get(final String targetName, final DownloadParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(targetName == null, "targetName is null");
        ValidateUtils.checkIllegalParameter(targetName.isBlank(), "targetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getVolume().isPresent()) {
            url += "-(" + params.getVolume().get() + ")/";
        }
        url += EncodeUtils.encodeURIComponent(targetName);

        String key, value;
        final Map<String, String> headers = new HashMap<>();

        if (params.isBinary()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(1);
            headers.put(key, value);
        } else if (params.getEncoding().isPresent()) {
            key = ZosmfHeaders.X_IBM_TEXT;
            value = ZosmfHeaders.X_IBM_TEXT + ZosmfHeaders.X_IBM_TEXT_ENCODING + params.getEncoding();
            headers.put(key, value);
        }

        if (params.isReturnEtag()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
            headers.put(key, value);
        }

        if (params.getResponseTimeout().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
            value = ZosmfHeaders.HEADERS.get(params.getResponseTimeout().toString()).get(1);
            headers.put(key, value);
        }

        key = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(0);
        value = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(1);
        headers.put(key, value);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_STREAM);
        }
        request.setUrl(url);
        connection.getCookie().ifPresentOrElse(c -> request.setCookie(c), () -> request.setCookie(null));
        request.setHeaders(headers);

        return new ByteArrayInputStream((byte[]) request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no dsn get response phrase")));
    }

}
