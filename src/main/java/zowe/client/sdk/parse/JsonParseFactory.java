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
 * Parse factory that generates the desire JSON parse operation
 *
 * @author Frank Giordano
 * @version 5.0
 */
public final class JsonParseFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JsonParseFactory.class);

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private JsonParseFactory() {
        throw new IllegalStateException("Factory class");
    }

    /**
     * Assign json parse response abstract object for type given
     *
     * @param type ParseType type
     * @return JsonParseResponse abstract object
     * @author Frank Giordano
     */
    public static JsonParse buildParser(final ParseType type) {
        LOG.debug(type.name());
        JsonParse parseResponse;
        switch (type) {
            case DATASET:
                parseResponse = DatasetJsonParse.getInstance();
                break;
            case JOB:
                parseResponse = JobJsonParse.getInstance();
                break;
            case JOB_FILE:
                parseResponse = JobFileJsonParse.getInstance();
                break;
            case MEMBER:
                parseResponse = MemberJsonParse.getInstance();
                break;
            case PROPS:
                parseResponse = PropsJsonParse.getInstance();
                break;
            case ZOS_LOG_ITEM:
                parseResponse = ZosLogItemJsonParse.getInstance();
                break;
            case ZOS_LOG_REPLY:
                parseResponse = ZosLogReplyJsonParse.getInstance();
                break;
            case ZOSMF_SYSTEMS:
                parseResponse = SystemsJsonParse.getInstance();
                break;
            case ZOSMF_INFO:
                parseResponse = SystemInfoJsonParse.getInstance();
                break;
            default:
                throw new IllegalStateException("no valid ParseType type specified");
        }
        return parseResponse;
    }

}
