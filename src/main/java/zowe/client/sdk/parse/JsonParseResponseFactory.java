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
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.utility.ValidateUtils;

/**
 * Parse factory that generates the desire json parse operation
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class JsonParseResponseFactory {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private JsonParseResponseFactory() {
        throw new IllegalStateException("Factory class");
    }

    /**
     * Assign json parse response abstract object for type given
     *
     * @param data json object
     * @param type ParseType type
     * @return JsonParseResponse abstract object
     * @throws Exception invalid ParseType value
     * @author Frank Giordano
     */
    public static JsonParseResponse buildParser(JSONObject data, ParseType type) throws Exception {
        ValidateUtils.checkNullParameter(data == null, "json data is null");
        JsonParseResponse parseResponse;
        switch (type) {
            case DATASET:
                parseResponse = new DatasetParseResponse(data);
                break;
            case JOB:
                parseResponse = new JobParseResponse(data);
                break;
            case JOB_FILE:
                parseResponse = new JobFileParseResponse(data);
                break;
            case MEMBER:
                parseResponse = new MemberParseResponse(data);
                break;
            case MVS_CONSOLE:
                parseResponse = new MvsConsoleParseResponse(data);
                break;
            case PROPS:
                parseResponse = new PropsParseResponse(data);
                break;
            case TSO_CONSOLE:
                parseResponse = new TsoParseResponse(data);
                break;
            case TSO_STOP:
                parseResponse = new TsoStopParseResponse(data);
                break;
            case UNIX_FILE:
                parseResponse = new UnixFileParseResponse(data);
                break;
            case UNIX_ZFS:
                parseResponse = new UnixZfsParseResponse(data);
                break;
            case ZOS_LOG_ITEM:
                parseResponse = new ZosLogItemParseResponse(data);
                break;
            case ZOS_LOG_REPLY:
                parseResponse = new ZosLogReplyParseResponse(data);
                break;
            case ZOSMF_SYSTEMS:
                parseResponse = new SystemsParseResponse(data);
                break;
            case ZOSMF_INFO:
                parseResponse = new SystemInfoParseResponse(data);
                break;
            default:
                throw new IllegalStateException("no valid parse type specified");
        }
        return parseResponse;
    }

}
