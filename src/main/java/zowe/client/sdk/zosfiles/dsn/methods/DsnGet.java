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
import zowe.client.sdk.zosfiles.dsn.input.DsnDownloadInputData;
import zowe.client.sdk.zosfiles.dsn.input.DsnListInputData;
import zowe.client.sdk.zosfiles.dsn.model.Dataset;
import zowe.client.sdk.zosfiles.dsn.types.AttributeType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Provides retrieve dataset and member functionality
 *
 * @author Nikunj Goyal
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnGet {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * DsnGet Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Nikunj Goyal
     */
    public DsnGet(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative DsnGet constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    DsnGet(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
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
        ValidateUtils.checkNullParameter(dataSetName, "dataSetName");
        ValidateUtils.checkIllegalParameter(dataSetName.isBlank(), "dataSetName not specified");

        final String[] tokens = dataSetName.split("\\.");
        final int length = tokens.length - 1;
        if (1 >= length) {
            throw new IllegalArgumentException("invalid dataset name");
        }

        final StringBuilder str = new StringBuilder();
        IntStream.range(0, length).forEach(i -> str.append(tokens[i]).append("."));

        String dataSetSearchStr = str.toString();
        dataSetSearchStr = dataSetSearchStr.substring(0, str.length() - 1);
        final DsnList dsnList = new DsnList(connection);
        final DsnListInputData listInputData = new DsnListInputData.Builder().attribute(AttributeType.BASE).build();
        final List<Dataset> dsLst = dsnList.getDatasets(dataSetSearchStr, listInputData);

        Predicate<Dataset> isExactMatch = d -> dataSetName.equals(d.getDsname());
        final Optional<Dataset> dataSet = dsLst.stream().filter(isExactMatch).findFirst();
        return dataSet.orElseThrow(() -> new ZosmfRequestException("dataset not found"));
    }

    /**
     * Retrieve sequential dataset or dataset member content
     *
     * @param targetName        name of a sequential dataset e.g., DATASET.SEQ.DATA
     *                          or a dataset member e.g., DATASET.LIB(MEMBER)
     * @param downloadInputData to download parameters, see DsnDownloadInputData object
     * @return a content stream
     * @throws ZosmfRequestException request error state
     * @author Nikunj Goyal
     */
    public InputStream get(final String targetName, final DsnDownloadInputData downloadInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(targetName, "targetName");
        ValidateUtils.checkNullParameter(downloadInputData, "downloadInputData");

        String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (downloadInputData.getVolume().isPresent()) {
            url += "-(" + downloadInputData.getVolume().get() + ")/";
        }
        url += EncodeUtils.encodeURIComponent(targetName);

        String key, value;
        final Map<String, String> headers = new HashMap<>();

        if (downloadInputData.isBinary()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(1);
            headers.put(key, value);
        } else if (downloadInputData.getEncoding().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_TEXT").get(0);
            value = String.format("%s%sIBM-%s",
                    ZosmfHeaders.HEADERS.get("X_IBM_TEXT").get(1),
                    ZosmfHeaders.HEADERS.get("X_IBM_TEXT_ENCODING").get(0),
                    downloadInputData.getEncoding().getAsLong()
            );
            headers.put(key, value);
        }

        if (downloadInputData.isReturnEtag()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
            headers.put(key, value);
        }

        if (downloadInputData.getResponseTimeout().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
            value = downloadInputData.getResponseTimeout().get();
            headers.put(key, value);
        }

        key = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(0);
        if (downloadInputData.getEncoding().isPresent()) {
            value = String.valueOf(downloadInputData.getEncoding().getAsLong());
        } else {
            value = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(1);
        }
        headers.put(key, value);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_STREAM);
        }
        request.setHeaders(headers);
        request.setUrl(url);

        return new ByteArrayInputStream((byte[]) request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no dsn get response phrase")));
    }

}
