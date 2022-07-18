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
import zowe.client.sdk.zosmfinfo.response.ZosmfPluginInfo;

/**
 * Class to transform JSON into ZosmfPluginInfo object
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ParseZosmfPluginInfoJson implements IParseJson<ZosmfPluginInfo> {

    /**
     * Transform JSON into ZosmfPluginInfo object
     *
     * @param jsonObject
     * @return ZosmfPluginInfo object
     * @author Frank Giordano
     */
    @Override
    public ZosmfPluginInfo parse(JSONObject jsonObject) {
        return new ZosmfPluginInfo.Builder()
                .pluginVersion((String) jsonObject.get("pluginVersion"))
                .pluginDefaultName((String) jsonObject.get("pluginDefaultName"))
                .pluginStatus((String) jsonObject.get("pluginStatus")).build();
    }

}
