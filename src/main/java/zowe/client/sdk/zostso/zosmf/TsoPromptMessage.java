/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.zosmf;

import java.util.Optional;

/**
 * TSO Prompt interface for one of TSO/E messages
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class TsoPromptMessage {

    /**
     * JSON version for message format
     */
    private Optional<String> version;

    /**
     * Hidden value
     */
    private Optional<String> hidden;

    /**
     * TsoPromptMessage constructor
     *
     * @author Frank Giordano
     */
    public TsoPromptMessage() {
    }

    /**
     * TsoPromptMessage constructor
     *
     * @param version JSON version for message format
     * @param hidden  hidden value
     * @author Frank Giordano
     */
    public TsoPromptMessage(String version, String hidden) {
        this.version = Optional.ofNullable(version);
        this.hidden = Optional.ofNullable(hidden);
    }

    /**
     * Retrieve version specified
     *
     * @return version value
     * @author Frank Giordano
     */
    public Optional<String> getVersion() {
        return version;
    }

    /**
     * Assign version value
     *
     * @param version JSON version for message format
     * @author Frank Giordano
     */
    public void setVersion(String version) {
        this.version = Optional.ofNullable(version);
    }

    /**
     * Retrieve hidden specified
     *
     * @return hidden value
     * @author Frank Giordano
     */
    public Optional<String> getHidden() {
        return hidden;
    }

    /**
     * Assign hidden value
     *
     * @param hidden hidden value
     * @author Frank Giordano
     */
    public void setHidden(String hidden) {
        this.hidden = Optional.ofNullable(hidden);
    }

    @Override
    public String toString() {
        return "TsoPromptMessage{" +
                "version=" + version +
                ", hidden=" + hidden +
                '}';
    }

}
