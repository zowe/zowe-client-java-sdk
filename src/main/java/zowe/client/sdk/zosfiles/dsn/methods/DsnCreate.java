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

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPostRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.dsn.input.CreateParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides create dataset and member functionality
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class DsnCreate {

    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * DsnCreate Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Leonid Baranov
     */
    public DsnCreate(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnCreate constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public DsnCreate(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonPostRequest)) {
            throw new Exception("POST_JSON request type required");
        }
        this.request = request;
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
    public Response create(String dataSetName, CreateParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.trim().isEmpty(), "dataSetName not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + EncodeUtils.encodeURIComponent(dataSetName);

        final String body = buildBody(params);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        return RestUtils.getResponse(request);
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
        return new JSONObject(jsonMap).toString();
    }

}
