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

import kong.unirest.core.json.JSONObject;
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

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * This class holds the helper functions that are used to gather z/OSMF information through the z/OSMF APIs.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosmfStatus {

    private static final Logger LOG = LoggerFactory.getLogger(ZosmfStatus.class);

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * CheckStatus Constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosmfStatus(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative CheckStatus constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    ZosmfStatus(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
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

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no z/osmf status response phrase"))
                .toString();

        final ZosmfInfoResponse response = JsonUtils.parseResponse(responsePhrase, ZosmfInfoResponse.class, "get");

        JSONObject jsonStr = new JSONObject(responsePhrase);
        Optional.ofNullable(jsonStr.optJSONArray("plugins"))
                .ifPresent(plugins -> {
                    ZosmfPlugin[] zosmfPluginsInfo = IntStream.range(0, plugins.length())
                            .mapToObj(i -> safeParse(String.valueOf(plugins.get(i)), ZosmfPlugin.class))
                            .filter(Optional::isPresent) // Filter out any empty Optionals (failed parses)
                            .map(Optional::get) // Unwrap the Optionals
                            .toArray(ZosmfPlugin[]::new);
                    response.withZosmfPluginsInfo(zosmfPluginsInfo);
                });

        if (response.getZosmfPluginsInfo() == null) {
            return response.withZosmfPluginsInfo(new ZosmfPlugin[0]);
        }
        return response;
    }

    // A helper method to wrap the potentially throwing `parseResponse` call.
    private <T> Optional<T> safeParse(String responseString, Class<T> classs) {
        try {
            return Optional.ofNullable(JsonUtils.parseResponse(responseString, classs, "get"));
        } catch (Exception e) {
            LOG.error("Failed to parse response: {}", responseString, e);
            return Optional.empty();
        }
    }

}
