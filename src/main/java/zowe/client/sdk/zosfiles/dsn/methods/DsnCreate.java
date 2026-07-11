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
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.dsn.input.DsnCreateInputData;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides create dataset and member functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=interface-create-sequential-partitioned-data-set">z/OSMF REST API</a>
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 7.0
 */
public class DsnCreate {

    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * DsnCreate Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Leonid Baranov
     */
    public DsnCreate(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
    }

    /**
     * Alternative DsnCreate constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    DsnCreate(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Creates a new dataset with specified parameters
     *
     * @param dataSetName     name of a dataset to create (e.g. 'DATASET.LIB')
     * @param createInputData to create dataset parameters, see DsnCreateInputData object
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Leonid Baranov
     */
    public Response create(final String dataSetName, final DsnCreateInputData createInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(dataSetName, "dataSetName");
        ValidateUtils.checkNullParameter(createInputData, "createInputData");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES +
                UrlConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(dataSetName);

        final Map<String, Object> createMap = new HashMap<>();
        createInputData.getVolser().ifPresent(v -> createMap.put("volser", v));
        createInputData.getUnit().ifPresent(v -> createMap.put("unit", v));
        createInputData.getDsorg().ifPresent(v -> createMap.put("dsorg", v));
        createInputData.getAlcunit().ifPresent(v -> createMap.put("alcunit", v));
        createInputData.getPrimary().ifPresent(v -> createMap.put("primary", v));
        createInputData.getSecondary().ifPresent(v -> createMap.put("secondary", v));
        createInputData.getDirblk().ifPresent(v -> createMap.put("dirblk", v));
        createInputData.getAvgblk().ifPresent(v -> createMap.put("avgblk", v));
        createInputData.getRecfm().ifPresent(v -> createMap.put("recfm", v));
        createInputData.getBlksize().ifPresent(v -> createMap.put("blksize", v));
        createInputData.getLrecl().ifPresent(v -> createMap.put("lrecl", v));
        createInputData.getStorclass().ifPresent(v -> createMap.put("storclass", v));
        createInputData.getMgntclass().ifPresent(v -> createMap.put("mgntclass", v));
        createInputData.getDataclass().ifPresent(v -> createMap.put("dataclass", v));
        createInputData.getDsntype().ifPresent(v -> createMap.put("dsntype", v));

        request.setUrl(url);
        request.setBody(JsonUtils.asRequestBodyJson(createMap));

        return request.executeRequest();
    }

}
