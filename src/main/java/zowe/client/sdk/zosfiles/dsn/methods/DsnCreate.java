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
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
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

    private ZosmfRequest request;

    /**
     * DsnCreate Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Leonid Baranov
     */
    public DsnCreate(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnCreate constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public DsnCreate(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Creates a new dataset with specified parameters
     *
     * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
     * @param params      create dataset parameters, see CreateParams object
     * @return http response object
     * @author Leonid Baranov
     */
    public Response create(final String dataSetName, final CreateParams params) {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isBlank(), "dataSetName not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" +
                EncodeUtils.encodeURIComponent(dataSetName);

        final Map<String, Object> createMap = new HashMap<>();
        params.getVolser().ifPresent(v -> createMap.put("volser", v));
        params.getUnit().ifPresent(v -> createMap.put("unit", v));
        params.getDsorg().ifPresent(v -> createMap.put("dsorg", v));
        params.getAlcunit().ifPresent(v -> createMap.put("alcunit", v));
        params.getPrimary().ifPresent(v -> createMap.put("primary", v));
        params.getSecondary().ifPresent(v -> createMap.put("secondary", v));
        params.getDirblk().ifPresent(v -> createMap.put("dirblk", v));
        params.getAvgblk().ifPresent(v -> createMap.put("avgblk", v));
        params.getRecfm().ifPresent(v -> createMap.put("recfm", v));
        params.getBlksize().ifPresent(v -> createMap.put("blksize", v));
        params.getLrecl().ifPresent(v -> createMap.put("lrecl", v));
        params.getStorclass().ifPresent(v -> createMap.put("storclass", v));
        params.getStorclass().ifPresent(v -> createMap.put("mgntclass", v));
        params.getMgntclass().ifPresent(v -> createMap.put("mgntclass", v));
        params.getDataclass().ifPresent(v -> createMap.put("dataclass", v));
        params.getDsntype().ifPresent(v -> createMap.put("dsntype", v));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(createMap).toString());

        return request.executeRequest();
    }

}
