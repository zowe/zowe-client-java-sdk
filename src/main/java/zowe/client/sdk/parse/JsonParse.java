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

/**
 * Interface that conforms to json parse operation
 *
 * @author Frank Giordano
 * @version 3.0
 */
public interface JsonParse {

    /**
     * Parse the given json data
     *
     * @param args json data to parse
     * @return Object value of parsed json data
     * @author Frank Giordano
     */
    Object parseResponse(final Object... args);

}

