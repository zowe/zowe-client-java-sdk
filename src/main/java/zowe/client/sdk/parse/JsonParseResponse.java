/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parse;

import org.json.simple.JSONObject;

/**
 * Base abstract class that conforms to json parse operation
 *
 * @author Frank Giordano
 * @version 2.0
 */
public abstract class JsonParseResponse {

    /**
     * JSON data value to be parsed
     */
    protected final JSONObject data;

    /**
     * JsonParseResponse constructor
     *
     * @param data json data value to be parsed
     */
    public JsonParseResponse(JSONObject data) {
        this.data = data;
    }

    /**
     * Parse the data json value given in constructor
     *
     * @return Object value of parsed json data
     */
    public abstract Object parseResponse();

}

