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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.parse.type.ParseType;

/**
 * Parse factory that generates the desire json parse operation
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class JsonParseResponseFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JsonParseResponseFactory.class);

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private JsonParseResponseFactory() {
        throw new IllegalStateException("Factory class");
    }

    /**
     * Assign json parse response abstract object for type given
     *
     * @param type ParseType type
     * @return JsonParseResponse abstract object
     * @author Frank Giordano
     */
    public static JsonParseResponse buildParser(final ParseType type) {
        LOG.debug(type.name());
        JsonParseResponse parseResponse;
        switch (type) {
            case DATASET:
                parseResponse = DatasetParseResponse.getInstance();
                break;
            case JOB:
                parseResponse = JobParseResponse.getInstance();
                break;
            case JOB_FILE:
                parseResponse = JobFileParseResponse.getInstance();
                break;
            case MEMBER:
                parseResponse = MemberParseResponse.getInstance();
                break;
            case MVS_CONSOLE:
                parseResponse = MvsConsoleParseResponse.getInstance();
                break;
            case PROPS:
                parseResponse = PropsParseResponse.getInstance();
                break;
            case TSO_CONSOLE:
                parseResponse = TsoParseResponse.getInstance();
                break;
            case TSO_STOP:
                parseResponse = TsoStopParseResponse.getInstance();
                break;
            case UNIX_FILE:
                parseResponse = UnixFileParseResponse.getInstance();
                break;
            case UNIX_ZFS:
                parseResponse = UnixZfsParseResponse.getInstance();
                break;
            case ZOS_LOG_ITEM:
                parseResponse = ZosLogItemParseResponse.getInstance();
                break;
            case ZOS_LOG_REPLY:
                parseResponse = ZosLogReplyParseResponse.getInstance();
                break;
            case ZOSMF_SYSTEMS:
                parseResponse = SystemsParseResponse.getInstance();
                break;
            case ZOSMF_INFO:
                parseResponse = SystemInfoParseResponse.getInstance();
                break;
            default:
                throw new IllegalStateException("no valid ParseType type specified");
        }
        return parseResponse;
    }

}
