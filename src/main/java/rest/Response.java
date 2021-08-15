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

    private Optional<Object> responsePhrase;
    private Optional<Integer> statusCode;

    public Response(Optional<Object> responsePhrase, Optional<Integer> statusCode) {
        if (responsePhrase == null)
            this.responsePhrase = Optional.empty();
        else this.responsePhrase = responsePhrase;
        if (statusCode == null)
            this.statusCode = Optional.empty();
        this.statusCode = statusCode;
    }

    public Optional<Object> getResponsePhrase() {
        return responsePhrase;
    }

    public void setResponsePhrase(Optional<Object> result) {
        this.responsePhrase = responsePhrase;
    }

    public Optional<Integer> getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Optional<Integer> statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isEmpty() {
        return (responsePhrase.isEmpty() && statusCode.isEmpty());
    }

}
