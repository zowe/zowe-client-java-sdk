/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
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
import zowe.client.sdk.zosfiles.dsn.input.DsnRenameInputData;
import zowe.client.sdk.zosfiles.dsn.types.RenameType;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides various update actions against a dataset: rename (including member), migrate,
 * recall migrated dataset, and delete a migrated dataset.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=interface-zos-data-set-member-utilities">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class DsnUpdate {

    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * DsnUpdate Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public DsnUpdate(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
    }

    /**
     * Alternative DsnUpdate constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link PutJsonZosmfRequest} implementation object
     * @author Frank Giordano
     */
    DsnUpdate(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Rename a dataset or a member of a dataset
     *
     * @param renameInputData rename parameters, see DsnRenameInputData object
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response rename(DsnRenameInputData renameInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(renameInputData, "renameInputData");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES +
                UrlConstants.URL_PATH_DELIM);

        final Map<String, Object> renameMap = new HashMap<>();
        final Map<String, Object> fromDatasetMap = new HashMap<>();
        renameMap.put("request", "rename");
        fromDatasetMap.put("dsn", renameInputData.getSourceDatasetName());

        if (renameInputData.getType() == RenameType.DATASET) {
            url.append(renameInputData.getDestinationDatasetName());
        } else if (renameInputData.getType() == RenameType.MEMBER) {
            url.append(renameInputData.getSourceDatasetName())
                    .append("(")
                    .append(renameInputData.getDestinationMemberName())
                    .append(")");
            fromDatasetMap.put("member", renameInputData.getSourceMemberName());
        }
        renameMap.put("from-dataset", fromDatasetMap);

        request.setUrl(url.toString());
        request.setBody(JsonUtils.asRequestBodyJson(renameMap));

        return request.executeRequest();
    }

    /**
     * Migrate a dataset
     *
     * @param datasetName name of a dataset (e.g. 'DATASET.LIB')
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Ashish-Kumar-Dash
     */
    public Response migrate(final String datasetName) throws ZosmfRequestException {
        return migrateCommon(datasetName, null);
    }

    /**
     * Migrate a dataset with a wait option
     *
     * @param datasetName name of a dataset (e.g. 'DATASET.LIB')
     * @param wait        if true, the function waits for completion of the request
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Ashish-Kumar-Dash
     */
    public Response migrate(final String datasetName, final boolean wait) throws ZosmfRequestException {
        return migrateCommon(datasetName, wait);
    }

    /**
     * Common helper method to migrate a dataset
     *
     * @param datasetName name of a dataset
     * @param wait        optional wait parameter
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Ashish-Kumar-Dash
     */
    private Response migrateCommon(final String datasetName, final Boolean wait)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(datasetName, "datasetName");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES +
                UrlConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(datasetName);

        final Map<String, Object> migrateMap = new HashMap<>();
        migrateMap.put("request", "hmigrate");
        if (wait != null) {
            migrateMap.put("wait", wait);
        }

        request.setUrl(url);
        request.setBody(JsonUtils.asRequestBodyJson(migrateMap));

        return request.executeRequest();
    }

    /**
     * Delete a migrated dataset
     *
     * @param datasetName name of a dataset (e.g. 'DATASET.LIB')
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Charishma Alam
     */
    public Response deleteMigrated(final String datasetName) throws ZosmfRequestException {
        return deleteMigratedCommon(datasetName, null, null);
    }

    /**
     * Delete a migrated dataset with wait and purge options
     *
     * @param datasetName name of a dataset (e.g. 'DATASET.LIB')
     * @param wait        if true, the function waits for completion of the request
     * @param purge       if true, the function uses PURGE=YES on ARCHDEL request
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Charishma Alam
     */
    public Response deleteMigrated(final String datasetName, final boolean wait, final boolean purge)
            throws ZosmfRequestException {
        return deleteMigratedCommon(datasetName, wait, purge);
    }

    /**
     * Common helper method to delete a migrated dataset
     *
     * @param datasetName name of a dataset
     * @param wait        optional wait parameter
     * @param purge       optional purge parameter
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Charishma Alam
     */
    private Response deleteMigratedCommon(final String datasetName, final Boolean wait, final Boolean purge)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(datasetName, "datasetName");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES +
                UrlConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(datasetName);

        final Map<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("request", "hdelete");
        if (wait != null) {
            deleteMap.put("wait", wait);
        }
        if (purge != null) {
            deleteMap.put("purge", purge);
        }

        request.setUrl(url);
        request.setBody(JsonUtils.asRequestBodyJson(deleteMap));

        return request.executeRequest();
    }

    /**
     * Recall a migrated dataset
     *
     * @param datasetName name of a dataset (e.g. 'DATASET.LIB')
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Shaurya2k06
     */
    public Response recallMigrated(final String datasetName) throws ZosmfRequestException {
        return recallMigratedCommon(datasetName, null);
    }

    /**
     * Recall a migrated dataset with a wait option
     *
     * @param datasetName name of a dataset (e.g. 'DATASET.LIB')
     * @param wait        if true, the function waits for completion of the request
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Shaurya2k06
     */
    public Response recallMigrated(final String datasetName, final boolean wait) throws ZosmfRequestException {
        return recallMigratedCommon(datasetName, wait);
    }

    /**
     * Common helper method to recall a migrated dataset
     *
     * @param datasetName name of a dataset
     * @param wait        optional wait parameter
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    private Response recallMigratedCommon(final String datasetName, final Boolean wait)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(datasetName, "datasetName");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES +
                UrlConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(datasetName);

        final Map<String, Object> recallMap = new HashMap<>();
        recallMap.put("request", "hrecall");
        if (wait != null) {
            recallMap.put("wait", wait);
        }

        request.setUrl(url);
        request.setBody(JsonUtils.asRequestBodyJson(recallMap));

        return request.executeRequest();
    }

}
