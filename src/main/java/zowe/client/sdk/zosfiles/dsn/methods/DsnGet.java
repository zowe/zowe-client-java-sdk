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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.StreamGetRequest;
import zowe.client.sdk.rest.ZosmfHeaders;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.dsn.input.DownloadParams;
import zowe.client.sdk.zosfiles.dsn.input.ListParams;
import zowe.client.sdk.zosfiles.dsn.response.Dataset;
import zowe.client.sdk.zosfiles.dsn.types.AttributeType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Provides retrieve dataset and member functionality
 *
 * @author Nikunj Goyal
 * @author Frank Giordano
 * @version 2.0
 */
public class DsnGet {

    private static final Logger LOG = LoggerFactory.getLogger(DsnGet.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * DsnGet Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public DsnGet(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnGet constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public DsnGet(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof StreamGetRequest)) {
            throw new Exception("GET_STREAM request type required");
        }
        this.request = request;
    }

    /**
     * Retrieve information about a Dataset.
     *
     * @param dataSetName sequential or partition dataset (e.g. 'DATASET.LIB')
     * @return dataset object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public Dataset getDsnInfo(String dataSetName) throws Exception {
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
        final DsnList zosDsnList = new DsnList(connection);
        final ListParams params = new ListParams.Builder().attribute(AttributeType.BASE).build();
        final List<Dataset> dsLst = zosDsnList.listDsn(dataSetSearchStr, params);

        final Optional<Dataset> dataSet = dsLst.stream().filter(d -> d.getDsname().orElse("n/a").contains(dataSetName)).findFirst();
        return dataSet.orElse(emptyDataSet);
    }

    /**
     * Retrieve sequential dataset or dataset member content
     *
     * @param dataSetName name of a sequential dataset e.g. DATASET.SEQ.DATA
     *                    or a dataset member e.g. DATASET.LIB(MEMBER))
     * @param params      download params parameters, see DownloadParams object
     * @return a content stream
     * @throws Exception error processing request
     * @author Nikunj Goyal
     */
    public InputStream get(String dataSetName, DownloadParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getVolume().isPresent()) {
            url += "-(" + params.getVolume().get() + ")/";
        }
        url += EncodeUtils.encodeURIComponent(dataSetName);
        LOG.debug(url);

        final Map<String, String> headers = FileUtils.generateHeadersBasedOnOptions(params);

        if (params.getReturnEtag().orElse(false)) {
            final String key = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(0);
            final String value = ZosmfHeaders.HEADERS.get("X_IBM_RETURN_ETAG").get(1);
            headers.put(key, value);
        }

        if (params.getBinary().orElse(false)) {
            final String key = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(0);
            final String value = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(1);
            headers.put(key, value);
        }

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_STREAM);
        }
        request.setUrl(url);
        request.setHeaders(headers);

        return new ByteArrayInputStream((byte[]) RestUtils.getResponse(request).getResponsePhrase().get());
    }

}
