/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parsejson;

import org.json.simple.JSONObject;

/**
 * Interface class for object json parsing.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public interface IParseJson<T> {

    /**
     * Transform JSON into T object
     *
     * @param jsonObject
     * @return T object
     */
    T parse(JSONObject jsonObject);

}
