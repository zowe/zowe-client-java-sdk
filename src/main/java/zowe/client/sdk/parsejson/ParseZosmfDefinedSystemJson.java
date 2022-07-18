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
import zowe.client.sdk.zosmfinfo.response.DefinedSystem;

/**
 * Class to transform JSON into DefinedSystem object
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ParseZosmfDefinedSystemJson implements IParseJson<DefinedSystem> {

    /**
     * Transform JSON into DefinedSystem object
     *
     * @param jsonObject
     * @return DefinedSystem object
     * @author Frank Giordano
     */
    @Override
    public DefinedSystem parse(JSONObject jsonObject) {
        return new DefinedSystem.Builder()
                .systemNickName((String) jsonObject.get("systemNickName"))
                .groupNames((String) jsonObject.get("groupNames"))
                .cpcSerial((String) jsonObject.get("cpcSerial"))
                .zosVR((String) jsonObject.get("zosVR"))
                .systemName((String) jsonObject.get("systemName"))
                .jesType((String) jsonObject.get("jesType"))
                .sysplexName((String) jsonObject.get("sysplexName"))
                .jesMemberName((String) jsonObject.get("jesMemberName"))
                .httpProxyName((String) jsonObject.get("httpProxyName"))
                .ftpDestinationName((String) jsonObject.get("ftpDestinationName"))
                .url((String) jsonObject.get("url"))
                .cpcName((String) jsonObject.get("cpcName")).build();
    }

}
