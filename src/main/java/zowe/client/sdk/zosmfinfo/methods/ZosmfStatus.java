/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo.methods;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfinfo.ZosmfConstants;
import zowe.client.sdk.zosmfinfo.model.ZosmfPlugin;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class holds the helper functions that are used to gather z/OSMF information through the z/OSMF APIs.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=service-retrieve-zosmf-information">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class ZosmfStatus {

    private static final String PLUGINS_FIELD = "plugins";
    private static final Logger LOG = LoggerFactory.getLogger(ZosmfStatus.class);
    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * CheckStatus Constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosmfStatus(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
    }

    /**
     * Alternative CheckStatus constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link GetJsonZosmfRequest} implementation object
     * @author Frank Giordano
     */
    ZosmfStatus(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Get z/OSMF information
     *
     * @return ZosmfInfoResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosmfInfoResponse get() throws ZosmfRequestException {
        final String url = connection.getZosmfUrl() + ZosmfConstants.INFO;

        request.setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no z/osmf status response phrase"))
                .toString();

        final ZosmfInfoResponse response = JsonUtils.parseResponse(responsePhrase, ZosmfInfoResponse.class, "get");

        final JsonNode root = JsonUtils.parse(responsePhrase);
        if (root.path(PLUGINS_FIELD).isArray()) {
            final ArrayNode plugins = JsonUtils.getArrayByField(root, PLUGINS_FIELD);
            final List<ZosmfPlugin> zosmfPluginsInfo = new ArrayList<>();
            for (final JsonNode plugin : plugins) {
                safeParsePlugin(plugin.toString(), ZosmfPlugin.class).ifPresent(zosmfPluginsInfo::add);
            }
            response.withZosmfPluginsInfo(zosmfPluginsInfo.toArray(new ZosmfPlugin[0]));
        }

        if (response.getZosmfPluginsInfo() == null) {
            return response.withZosmfPluginsInfo(new ZosmfPlugin[0]);
        }
        return response;
    }

    // A helper method to wrap the potentially throwing `parseResponse` call.
    private <T> Optional<T> safeParsePlugin(String responseString,
                                            @SuppressWarnings("SameParameterValue") Class<T> classs) {
        try {
            return Optional.ofNullable(JsonUtils.parseResponse(responseString, classs, "get"));
        } catch (Exception e) {
            LOG.warn("Skipping invalid z/OSMF plugin entry: {}", responseString, e);
            return Optional.empty();
        }
    }

}
