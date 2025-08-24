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
package zowe.client.sdk.zosfiles.dsn.input;

import java.util.Optional;

/**
 * Parameter container class for the Zos Files function
 *
 * @author Leonid Baranov
 * @version 5.0
 */
public class DsnZosFilesInputData {

    /**
     * Response time out value
     */
    private final String responseTimeout;

    /**
     * DsnZosFilesInputData constructor
     *
     * @param responseTimeout response time out value
     * @author Leonid Baranov
     */
    public DsnZosFilesInputData(final String responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     */
    public Optional<String> getResponseTimeout() {
        return Optional.ofNullable(responseTimeout);
    }

    /**
     * Return string value representing a DsnZosFilesInputData object
     *
     * @return string representation of DsnZosFilesInputData
     */
    @Override
    public String toString() {
        return "DsnZosFilesInputData{" +
                "responseTimeout=" + responseTimeout +
                '}';
    }

}
