/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosuss.exception;

/**
 * Custom exception to represent SSH Unix System Services error request
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class IssueUssException extends Exception {

    /**
     * IssueCommandException constructor for message value
     *
     * @param message error message
     * @param err     original throwable exception
     * @author Frank Giordano
     */
    public IssueUssException(final String message, Throwable err) {
        super(message, err);
    }

}
