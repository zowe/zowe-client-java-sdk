/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest.type;

/**
 * Zosmf Request CRUD types
 *
 * @author Frank Giordano
 * @version 5.0
 */
public enum ZosmfRequestType {

    /**
     * Get JSON type
     */
    GET_JSON,
    /**
     * Get a TEXT type
     */
    GET_TEXT,
    /**
     * Put JSON type
     */
    PUT_JSON,
    /**
     * Put a TEXT type
     */
    PUT_TEXT,
    /**
     * Delete JSON type
     */
    DELETE_JSON,
    /**
     * Delete TEXT type
     */
    DELETE_TEXT,
    /**
     * Post-JSON type
     */
    POST_JSON,
    /**
     * Post-TEXT type
     */
    POST_TEXT,
    /**
     * Get STREAM type
     */
    GET_STREAM,
    /**
     * Put STREAM type
     */
    PUT_STREAM

}
