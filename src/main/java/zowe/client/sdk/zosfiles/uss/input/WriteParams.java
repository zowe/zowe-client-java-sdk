/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.input;

import java.util.Optional;

/**
 * Parameter container class for unix system services write to file object
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-write-data-zos-unix-file">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @author James Kostrewski
 * @version 2.0
 */
public class WriteParams {

    public Optional<String> textContent;
    public Optional<byte[]> binaryContent;
    public Optional<String> fileEncoding;
    public Optional<String> crlf;
    public boolean binary;

    // TODO build builder pattern below...

}
