/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.methods;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.UssListInputData;
import zowe.client.sdk.zosfiles.uss.input.UssListZfsInputData;
import zowe.client.sdk.zosfiles.uss.model.UnixFile;
import zowe.client.sdk.zosfiles.uss.model.UnixZfs;
import zowe.client.sdk.zosfiles.uss.reaponse.UnixFileListResponse;
import zowe.client.sdk.zosfiles.uss.reaponse.UnixZfsListResponse;

import java.util.List;
import java.util.Map;

/**
 * Provides Unix System Services (USS) list object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-files-directories-unix-file-path">z/OSMF REST API file List</a>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-zos-unix-filesystems">z/OSMF REST API zFS List</a>
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class UssList {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * UssList Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public UssList(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative UssList constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    UssList(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform a list of UNIX files operation
     *
     * @param listInputData UssListInputData object
     * @return list of UssItem objects
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public List<UnixFile> getFiles(final UssListInputData listInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(listInputData, "listInputData");

        final String urlStart = connection.getZosmfUrl() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES;
        final StringBuilder url = new StringBuilder(urlStart);

        url.append("?path=").append(EncodeUtils.encodeURIComponent(FileUtils.validatePath(
                listInputData.getPath().orElseThrow(() -> new IllegalArgumentException("path not specified")))));
        listInputData.getGroup().ifPresent(group ->
                url.append("&group=").append(EncodeUtils.encodeURIComponent(group)));
        listInputData.getUser().ifPresent(user ->
                url.append("&user=").append(EncodeUtils.encodeURIComponent(user)));
        listInputData.getMtime().ifPresent(mtime ->
                url.append("&mtime=").append(EncodeUtils.encodeURIComponent(mtime)));
        listInputData.getSize().ifPresent(size ->
                url.append("&size=").append(size));
        listInputData.getName().ifPresent(name ->
                url.append("&name=").append(EncodeUtils.encodeURIComponent(name)));
        listInputData.getPerm().ifPresent(perm ->
                url.append("&perm=").append(EncodeUtils.encodeURIComponent(perm)));
        // If the type parameter is specified with the size parameter, it must be set to 'f'.
        // Sizes that are associated with all other types are unspecified.
        if (listInputData.getSize().isPresent() && listInputData.getType().isPresent()) {
            url.append("&type=f");
        } else {
            listInputData.getType().ifPresent(type -> url.append("&type=").append(type.getValue()));
        }
        listInputData.getDepth().ifPresent(depth -> url.append("&depth=").append(depth));
        if (listInputData.isFilesys()) {
            url.append("&filesys=all");
        }
        if (listInputData.isSymlinks()) {
            url.append("&symlinks=report");
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }

        final int maxLength = listInputData.getMaxLength().orElse(0);
        if (maxLength > 0) {
            request.setHeaders(Map.of("X-IBM-Max-Items", String.valueOf(maxLength)));
        }
        request.setUrl(url.toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(ZosFilesConstants.RESPONSE_PHRASE_ERROR))
                .toString();

        final String context = "getFiles";
        UnixFileListResponse response = JsonUtils.parseResponse(responsePhrase, UnixFileListResponse.class, context);
        return response.getItems() == null ? List.of() : response.getItems();
    }

    /**
     * Perform a list of UNIX filesystems operation
     *
     * @param listZfsInputData UssListZfsInputData parameter object
     * @return list of UssZfs objects
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public List<UnixZfs> getZfsSystems(final UssListZfsInputData listZfsInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(listZfsInputData, "listZfsInputData");
        ValidateUtils.checkIllegalParameter(
                listZfsInputData.getPath().isEmpty() && listZfsInputData.getFsname().isEmpty(),
                "no path or fsname specified"
        );

        final String urlStart = connection.getZosmfUrl() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_MFS;
        final StringBuilder url = new StringBuilder(urlStart);

        listZfsInputData.getPath().ifPresent(path ->
                url.append("?path=").append(EncodeUtils.encodeURIComponent(FileUtils.validatePath(path))));

        listZfsInputData.getFsname().ifPresent(fsname ->
                url.append("?fsname=").append(EncodeUtils.encodeURIComponent(fsname)));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }

        final int maxLength = listZfsInputData.getMaxLength().orElse(0);
        if (maxLength > 0) {
            request.setHeaders(Map.of("X-IBM-Max-Items", String.valueOf(maxLength)));
        }
        request.setUrl(url.toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(ZosFilesConstants.RESPONSE_PHRASE_ERROR))
                .toString();

        final String context = "getZfsSystems";
        UnixZfsListResponse response = JsonUtils.parseResponse(responsePhrase, UnixZfsListResponse.class, context);
        return response.getItems() == null ? List.of() : response.getItems();
    }

}
