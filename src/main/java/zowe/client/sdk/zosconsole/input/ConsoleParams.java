/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.input;

import java.util.Optional;

/**
 * Common z/OS Consoles API Parameters.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ConsoleParams {

    /**
     * The z/OS mvs console to direct the commands.
     */
    private Optional<String> consoleName = Optional.empty();

    /**
     * The z/OSMF Console API returns '\r' or '\r\n' where line-breaks. Can attempt to replace these
     * sequences with '\n', but there may be cases where that is not preferable. Specify false to prevent processing.
     */
    private Optional<Boolean> processResponses = Optional.empty();

    /**
     * Retrieve consoleName value
     *
     * @return consoleName value
     * @author Frank Giordano
     */
    public Optional<String> getConsoleName() {
        return consoleName;
    }

    /**
     * Assign consoleName value
     *
     * @param consoleName value
     * @author Frank Giordano
     */
    public void setConsoleName(String consoleName) {
        this.consoleName = Optional.ofNullable(consoleName);
    }

    /**
     * Retrieve processResponses value
     *
     * @return processResponses value
     * @author Frank Giordano
     */
    public Optional<Boolean> getProcessResponses() {
        return processResponses;
    }

    /**
     * Assign processResponses value
     *
     * @param processResponses value
     * @author Frank Giordano
     */
    public void setProcessResponses(Boolean processResponses) {
        this.processResponses = Optional.ofNullable(processResponses);
    }

    @Override
    public String toString() {
        return "ConsoleParams{" +
                "consoleName=" + consoleName +
                ", processResponses=" + processResponses +
                '}';
    }

}
