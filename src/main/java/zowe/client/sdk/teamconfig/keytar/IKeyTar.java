/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.keytar;

import com.starxg.keytar.KeytarException;

import java.util.List;

/**
 * Interface that describes the contract for KeyTar processing
 *
 * @author Frank Giordano
 * @version 1.0
 */
public interface IKeyTar {

    /**
     * Describes the method that will extract KeyTar key information
     */
    public void processKey() throws KeytarException;

    /**
     * Describes the method that will retrieve KeyTar key information
     */
    public List<KeyTarConfig> getKeyConfigs() throws Exception;

    /**
     * Describes the method that will return KeyTar value from KeyTar processing done
     */
    public String getKeyTarValue() throws Exception;

}
