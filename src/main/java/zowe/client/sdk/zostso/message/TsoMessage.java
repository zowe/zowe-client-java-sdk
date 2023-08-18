/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.message;

import java.util.Optional;

/**
 * TSO Message interface for one of TSO/E messages
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TsoMessage {

    /**
     * JSON version for message format
     */
    private Optional<String> version;

    /**
     * Description of the data type
     */
    private Optional<String> data;

    /**
     * TsoMessage constructor
     *
     * @author Frank Giordano
     */
    public TsoMessage() {
    }

    /**
     * TsoMessage constructor
     *
     * @param version JSON version for message format
     * @param data    description of the data type
     * @author Frank Giordano
     */
    public TsoMessage(final String version, final String data) {
        this.version = Optional.ofNullable(version);
        this.data = Optional.ofNullable(data);
    }

    /**
     * Retrieve version specified
     *
     * @return version value
     */
    public Optional<String> getVersion() {
        return version;
    }

    /**
     * Assign version value
     *
     * @param version JSON version for message format
     */
    public void setVersion(final String version) {
        this.version = Optional.ofNullable(version);
    }

    /**
     * Retrieve data specified
     *
     * @return data value
     */
    public Optional<String> getData() {
        return data;
    }

    /**
     * Assign version value
     *
     * @param data description os the data type
     */
    public void setData(final String data) {
        this.data = Optional.ofNullable(data);
    }

    @Override
    public String toString() {
        return "TsoMessage{" +
                "version=" + version +
                ", data=" + data +
                '}';
    }

}
