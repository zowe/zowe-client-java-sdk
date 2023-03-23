/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility.unirest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.rest.unirest.Response;
import zowe.client.sdk.rest.unirest.ZoweRequest;
import zowe.client.sdk.utility.RestUtils;

/**
 * Utility Class for Rest related static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public final class UniRestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(UniRestUtils.class);

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private UniRestUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Response getResponse(ZoweRequest request) throws Exception {
        zowe.client.sdk.rest.unirest.Response response = request.executeRequest();

        if (response.getStatusCode().isEmpty()) {
            throw new Exception("no response status code returned");
        }

        if (response.getResponsePhrase().isEmpty()) {
            throw new Exception("no response phrase returned");
        }

        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            if (response.getStatusText().isEmpty()) {
                throw new Exception("no response status text returned");
            }
            LOG.debug("Rest status code {}", response.getStatusCode().get());
            LOG.debug("Rest status text {}", response.getStatusText().get());
        }
        return response;
    }

}
