/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package rest;

import java.util.Optional;

public class Response {

    private Optional<Object> result;
    private Optional<Integer> statusCode;

    public Response(Optional<Object> result, Optional<Integer> statusCode) {
        if (result == null)
            this.result = Optional.empty();
        else this.result = result;
        if (statusCode == null)
            this.statusCode = Optional.empty();
        this.statusCode = statusCode;
    }

    public Optional<Object> getResult() {
        return result;
    }

    public void setResult(Optional<Object> result) {
        this.result = result;
    }

    public Optional<Integer> getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Optional<Integer> statusCode) {
        this.statusCode = statusCode;
    }

}
