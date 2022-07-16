/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest;

/**
 * Zowe Request CRUD types
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZoweRequestType {

    /**
     * Http request verb value types
     */
    public enum VerbType {
        GET_JSON, GET_TEXT, PUT_JSON, PUT_TEXT, DELETE_JSON, DELETE_TEXT, POST_JSON, POST_TEXT, GET_STREAM
    }

}
