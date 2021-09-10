/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zosfiles.input;

import java.util.Optional;

/**
 * This interface defines the options that can be sent into the Zos Files function
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class ZosFilesParams {

    /**
     * Response time out value
     */
    private final Optional<String> responseTimeout;

    public ZosFilesParams(String responseTimeout) {
        this.responseTimeout = Optional.ofNullable(responseTimeout);
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     * @author Leonid Baranov
     */
    public Optional<String> getResponseTimeout() {
        return responseTimeout;
    }

    @Override
    public String toString() {
        return "ZosFilesParams{" +
                "responseTimeout=" + responseTimeout +
                '}';
    }

}
