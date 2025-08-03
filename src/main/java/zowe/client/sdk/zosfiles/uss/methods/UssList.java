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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.UnixZfsJsonParse;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.ListParams;
import zowe.client.sdk.zosfiles.uss.input.ListZfsParams;
import zowe.client.sdk.zosfiles.uss.response.UnixFile;
import zowe.client.sdk.zosfiles.uss.response.UnixZfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides Unix System Services (USS) list object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-files-directories-unix-file-path">z/OSMF REST API file List</a>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-zos-unix-filesystems">z/OSMF REST API zFS List</a>
 *
 * @author Frank Giordano
 * @version 4.0
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
        ValidateUtils.checkIllegalParameter(connection == null, "connection is null");
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
    public UssList(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkIllegalParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform a list of UNIX files operation
     *
     * @param params ListParams object
     * @return list of UssItem objects
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public List<UnixFile> getFiles(final ListParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final StringBuilder url = new StringBuilder("https://" + connection.getHost() + ":" +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") +
                connection.getZosmfPort() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES);

        url.append("?path=").append(EncodeUtils.encodeURIComponent(FileUtils.validatePath(
                params.getPath().orElseThrow(() -> new IllegalArgumentException("path not specified")))));
        params.getGroup().ifPresent(group -> url.append("&group=").append(EncodeUtils.encodeURIComponent(group)));
        params.getUser().ifPresent(user -> url.append("&user=").append(EncodeUtils.encodeURIComponent(user)));
        params.getMtime().ifPresent(mtime -> url.append("&mtime=").append(EncodeUtils.encodeURIComponent(mtime)));
        params.getSize().ifPresent(size -> url.append("&size=").append(size));
        params.getName().ifPresent(name -> url.append("&name=").append(EncodeUtils.encodeURIComponent(name)));
        params.getPerm().ifPresent(perm -> url.append("&perm=").append(EncodeUtils.encodeURIComponent(perm)));
        // If type parameter is specified with the size parameter, it must be set to 'f'.
        // Sizes that are associated with all other types are unspecified.
        if (params.getSize().isPresent() && params.getType().isPresent()) {
            url.append("&type=f");
        } else {
            params.getType().ifPresent(type -> url.append("&type=").append(type.getValue()));
        }
        params.getDepth().ifPresent(depth -> url.append("&depth=").append(depth));
        if (params.isFilesys()) {
            url.append("&filesys=all");
        }
        if (params.isSymlinks()) {
            url.append("&symlinks=report");
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }

        final int maxLength = params.getMaxLength().orElse(0);
        if (maxLength > 0) {
            request.setHeaders(Map.of("X-IBM-Max-Items", String.valueOf(maxLength)));
        }
        request.setUrl(url.toString());

        final Response response = request.executeRequest();

        final List<UnixFile> items = new ArrayList<>();
        final JSONObject jsonObject = JsonParserUtil.parse(String.valueOf(response.getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(ZosFilesConstants.RESPONSE_PHRASE_ERROR))));
        final JSONArray jsonArray = (JSONArray) jsonObject.get("items");
        if (jsonArray != null) {
            for (final Object jsonObj : jsonArray) {
                items.add((UnixFile) JsonParseFactory.buildParser(ParseType.UNIX_FILE).parseResponse(jsonObj));
            }
        }

        return items;
    }

    /**
     * Perform a list of UNIX filesystems operation
     *
     * @param params ListZfsParams parameter object
     * @return list of UssZfsItem objects
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public List<UnixZfs> getZfsSystems(final ListZfsParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getPath().isEmpty() && params.getFsname().isEmpty(),
                "no path or fsname specified");

        final StringBuilder url = new StringBuilder("https://" + connection.getHost() + ":" +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") +
                connection.getZosmfPort() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_MFS);

        params.getPath().ifPresent(path -> url.append("?path=").append(
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(path))));
        params.getFsname().ifPresent(fsname -> url.append("?fsname=").append(EncodeUtils.encodeURIComponent(fsname)));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }

        final int maxLength = params.getMaxLength().orElse(0);
        if (maxLength > 0) {
            request.setHeaders(Map.of("X-IBM-Max-Items", String.valueOf(maxLength)));
        }
        request.setUrl(url.toString());

        final Response response = request.executeRequest();

        final List<UnixZfs> items = new ArrayList<>();
        final JSONObject jsonObject = JsonParserUtil.parse(String.valueOf(response.getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(ZosFilesConstants.RESPONSE_PHRASE_ERROR))));
        final JSONArray jsonArray = (JSONArray) jsonObject.get("items");
        if (jsonArray != null) {
            for (final Object obj : jsonArray) {
                final JSONObject jsonObj = (JSONObject) obj;
                final StringBuilder modeStr = new StringBuilder();
                try {
                    final JSONArray modeLst = (JSONArray) jsonObj.get("mode");
                    final int size = modeLst.size();
                    for (int i = 0; i < size; i++) {
                        if (size - 1 == i) {
                            modeStr.append(modeLst.get(i).toString());
                        } else {
                            modeStr.append(modeLst.get(i).toString()).append(",");
                        }
                    }
                } catch (Exception ignored) {
                }

                final UnixZfsJsonParse parse = (UnixZfsJsonParse) JsonParseFactory.buildParser(ParseType.UNIX_ZFS);
                items.add(parse.parseResponse(jsonObj, modeStr.toString()));
            }
        }
        return items;
    }

}

