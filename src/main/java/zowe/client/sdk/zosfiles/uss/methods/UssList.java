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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonGetRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
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
import java.util.OptionalInt;

/**
 * Provides Unix System Services (USS) list object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-files-directories-unix-file-path">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssList {

    private static final Logger LOG = LoggerFactory.getLogger(UssList.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssList Constructor
     *
     * @param connection connection information, see ZOSConnection object
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
     * @param request    any compatible ZoweRequest Interface type object
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public UssList(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Perform list z/OS UNIX files operation
     *
     * @param params ListParams object
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private List<UssItem> fileList(ListParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getName().isEmpty(), "params name is empty");

        final StringBuilder url = new StringBuilder("https://" + connection.getHost() + ":" +
                connection.getZosmfPort() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES);

        url.append("?path=").append(params.getName().get());
        params.getGroup().ifPresent(group -> url.append("?group=").append(group));
        params.getUser().ifPresent(user -> url.append("?user=").append(user));
        params.getMtime().ifPresent(mtime -> url.append("?mtime=").append(mtime));
        params.getSize().ifPresent(size -> url.append("?size=").append(size));
        params.getPerm().ifPresent(perm -> url.append("?perm=").append(perm));
        // If type parameter is specified with the size parameter, it must be set to 'f'.
        // Sizes that are associated with all other types are unspecified.
        if (params.getSize().isPresent() && params.getType().isPresent()) {
            url.append("?type=f");
        } else {
            params.getType().ifPresent(type -> url.append("?type=").append(type.getValue()));
        }
        params.getDepth().ifPresent(depth -> url.append("?depth=").append(depth));
        if (params.isFilesys()) {
            url.append("?filesys=all");
        }
        if (params.isSymlinks()) {
            url.append("?symlinks=report");
        }
        LOG.debug(url.toString());

        Response response = getResponse(url.toString(), params.getMaxLength());

        List<UssItem> items = new ArrayList<>();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse((String) response.getResponsePhrase()
                .orElseThrow(() -> new Exception("error retrieving uss list")));
        final JSONArray jsonArray = (JSONArray) jsonObject.get("items");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                items.add(parseJsonUssListResponse((JSONObject) jsonArray.get(i)));
            }
        }

        return items;
    }

    /**
     * Perform list z/OS UNIX filesystems operation
     *
     * @param params
     * @return
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public List<UssZfsItem> zfsList(ListZfsParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getPath().isEmpty() && params.getFsname().isEmpty(),
                "no path or fsname specified");
        ValidateUtils.checkIllegalParameter(params.getPath().isPresent() && params.getFsname().isPresent(),
                "specify either path or fsname");

        final StringBuilder url = new StringBuilder("https://" + connection.getHost() + ":" +
                connection.getZosmfPort() + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_MFS);

        params.getPath().ifPresent(path -> url.append("?path=").append(path));
        params.getFsname().ifPresent(name -> url.append("?fsname=").append(name));
        LOG.debug(url.toString());

        Response response = getResponse(url.toString(), params.getMaxLength());

        List<UssZfsItem> items = new ArrayList<>();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse((String) response.getResponsePhrase()
                .orElseThrow(() -> new Exception("error retrieving uss zfs list")));
        final JSONArray jsonArray = (JSONArray) jsonObject.get("items");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                // TODO
            }
        }
        return items;
    }

    /**
     * Perform zowe request and retrieve response
     *
     * @param url       string value
     * @param maxLength length max request value
     * @return response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private Response getResponse(String url, OptionalInt maxLength) throws Exception {
        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }

        request.setUrl(url);
        if (maxLength.orElse(0) > 0) {
            request.setHeaders(Map.of("X-IBM-Max-Items", String.valueOf(maxLength)));
        }

        return RestUtils.getResponse(request);
    }

    /**
     * Transform JSON into ListItem object
     *
     * @param jsonObject JSON object
     * @return ListItem object
     * @author Frank Giordano
     */
    private UssItem parseJsonUssListResponse(JSONObject jsonObject) {
        return new UssItem.Builder()
                .name((String) jsonObject.get("name"))
                .mode((String) jsonObject.get("mode"))
                .size((Long) jsonObject.get("size"))
                .uid((Long) jsonObject.get("uid"))
                .user((String) jsonObject.get("user"))
                .gid((Long) jsonObject.get("gid"))
                .group((String) jsonObject.get("group"))
                .mtime((String) jsonObject.get("mtime"))
                .build();
    }

}
