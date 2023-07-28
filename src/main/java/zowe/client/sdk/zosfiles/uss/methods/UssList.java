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
import org.json.simple.parser.JSONParser;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonGetRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.ListParams;
import zowe.client.sdk.zosfiles.uss.input.ListZfsParams;
import zowe.client.sdk.zosfiles.uss.response.UssItem;
import zowe.client.sdk.zosfiles.uss.response.UssZfsItem;

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
 * @version 2.0
 */
public class UssList {

    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssList Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public UssList(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssList constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public UssList(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonGetRequest)) {
            throw new Exception("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform list of UNIX files operation
     *
     * @param params ListParams object
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public List<UssItem> fileList(ListParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final StringBuilder url = new StringBuilder("https://" + connection.getHost() + ":" +
                connection.getZosmfPort() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES);

        url.append("?path=").append(params.getPath().get());
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

        final Response response = getResponse(url.toString(), params.getMaxLength().orElse(0));

        final List<UssItem> items = new ArrayList<>();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(String.valueOf(
                response.getResponsePhrase().orElseThrow(() -> new Exception("error retrieving uss list"))));
        final JSONArray jsonArray = (JSONArray) jsonObject.get("items");
        if (jsonArray != null) {
            for (Object jsonObj : jsonArray) {
                items.add(parseJsonUssListResponse((JSONObject) jsonObj));
            }
        }

        return items;
    }

    /**
     * Perform list of UNIX filesystems operation
     *
     * @param params ListZfsParams parameter object
     * @return list of UssZfsItem objects
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public List<UssZfsItem> zfsList(ListZfsParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getPath().isEmpty() && params.getFsname().isEmpty(),
                "no path or fsname specified");

        final StringBuilder url = new StringBuilder("https://" + connection.getHost() + ":" +
                connection.getZosmfPort() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_MFS);

        params.getPath().ifPresent(path -> url.append("?path=").append(EncodeUtils.encodeURIComponent(path)));
        params.getFsname().ifPresent(fsname -> url.append("?fsname=").append(EncodeUtils.encodeURIComponent(fsname)));

        final Response response = getResponse(url.toString(), params.getMaxLength().orElse(0));

        final List<UssZfsItem> items = new ArrayList<>();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(String.valueOf(
                response.getResponsePhrase().orElseThrow(() -> new Exception("error retrieving uss zfs list"))));
        final JSONArray jsonArray = (JSONArray) jsonObject.get("items");
        if (jsonArray != null) {
            for (Object jsonObj : jsonArray) {
                items.add(parseJsonUssZfsListResponse((JSONObject) jsonObj));
            }
        }
        return items;
    }

    /**
     * Perform zowe request and retrieve response
     *
     * @param url       string value
     * @param maxLength length max request value
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private Response getResponse(String url, int maxLength) throws Exception {
        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }

        request.setUrl(url);
        if (maxLength > 0) {
            request.setHeaders(Map.of("X-IBM-Max-Items", String.valueOf(maxLength)));
        }

        return RestUtils.getResponse(request);
    }

    /**
     * Transform JSON into UssItem object
     *
     * @param jsonObject JSON object
     * @return ListItem object
     * @author Frank Giordano
     */
    private UssItem parseJsonUssListResponse(JSONObject jsonObject) {
        return new UssItem.Builder()
                .name(jsonObject.get("name") != null ? (String) jsonObject.get("name") : null)
                .mode(jsonObject.get("mode") != null ? (String) jsonObject.get("mode") : null)
                .size(jsonObject.get("size") != null ? (Long) jsonObject.get("size") : null)
                .uid(jsonObject.get("uid") != null ? (Long) jsonObject.get("uid") : null)
                .user(jsonObject.get("user") != null ? (String) jsonObject.get("user") : null)
                .gid(jsonObject.get("gid") != null ? (Long) jsonObject.get("gid") : null)
                .group(jsonObject.get("group") != null ? (String) jsonObject.get("group") : null)
                .mtime(jsonObject.get("mtime") != null ? (String) jsonObject.get("mtime") : null)
                .build();
    }

    /**
     * Transform JSON into UssZfsItem object
     *
     * @param jsonObject JSON object
     * @return ListItem object
     * @author Frank Giordano
     */
    private UssZfsItem parseJsonUssZfsListResponse(JSONObject jsonObject) {
        final StringBuilder modeStr = new StringBuilder();
        try {
            final JSONArray modeLst = (JSONArray) jsonObject.get("mode");
            final int size = modeLst.size();
            for (int i = 0; i < size; i++) {
                if (size - 1 == i) {
                    modeStr.append(modeLst.get(i).toString());
                } else {
                    modeStr.append(modeLst.get(i).toString()).append(", ");
                }
            }
        } catch (Exception ignored) {
        }
        return new UssZfsItem.Builder()
                .name(jsonObject.get("name") != null ? (String) jsonObject.get("name") : null)
                .mountpoint(jsonObject.get("mountpoint") != null ? (String) jsonObject.get("mountpoint") : null)
                .fstname(jsonObject.get("fstname") != null ? (String) jsonObject.get("fstname") : null)
                .status(jsonObject.get("status") != null ? (String) jsonObject.get("status") : null)
                .mode(modeStr.toString())
                .dev(jsonObject.get("dev") != null ? (Long) jsonObject.get("dev") : null)
                .fstype(jsonObject.get("fstype") != null ? (Long) jsonObject.get("fstype") : null)
                .bsize(jsonObject.get("bsize") != null ? (Long) jsonObject.get("bsize") : null)
                .bavail(jsonObject.get("bavail") != null ? (Long) jsonObject.get("bavail") : null)
                .blocks(jsonObject.get("blocks") != null ? (Long) jsonObject.get("blocks") : null)
                .sysname(jsonObject.get("sysname") != null ? (String) jsonObject.get("sysname") : null)
                .readibc(jsonObject.get("readibc") != null ? (Long) jsonObject.get("readibc") : null)
                .writeibc(jsonObject.get("writeibc") != null ? (Long) jsonObject.get("writeibc") : null)
                .diribc(jsonObject.get("diribc") != null ? (Long) jsonObject.get("diribc") : null)
                .returnedRows(jsonObject.get("returnedRows") != null ? (Long) jsonObject.get("returnedRows") : null)
                .totalRows(jsonObject.get("totalRows") != null ? (Long) jsonObject.get("totalRows") : null)
                .moreRows(jsonObject.get("moreRows") != null ? (Boolean) jsonObject.get("moreRows") : false)
                .build();
    }

}
