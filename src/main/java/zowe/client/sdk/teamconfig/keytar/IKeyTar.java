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
 * @version 2.0
 */
public interface IKeyTar {

    /**
     * Describes the method that will retrieve KeyTar key information
     *
     * @return list of KeyTarConfig objects
     * @throws Exception error processing
     */
    public List<KeyTarConfig> getKeyConfigs() throws Exception;

    /**
     * Describes the method that will return KeyTar value from KeyTar processing done
     *
     * @return keyValue string containing KeyTar Json representation
     */
    public String getKeyTarValue();

    /**
     * Describes the method that will extract KeyTar key information
     *
     * @throws KeytarException error processing
     */
    public void processKey() throws KeytarException;

    /**
     * set required service name used for OS credential store querying
     *
     * @param accountName string value used for OS credential store querying
     */
    public void setAccountName(String accountName);

    /**
     * set required account name used for OS credential store querying
     *
     * @param serviceName string value used for OS credential store querying
     */
    public void setServiceName(String serviceName);

}
