/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.teamconfig.keytar.IKeyTar;
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * KeyTarService class that provides a service layer to perform KeyTar processing and retrieval of credentials and
 * Zowe Global Team Configuration location information.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class KeyTarService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(KeyTarService.class);
    /**
     * List of service names used for KeyTar querying of OS credential store
     */
    private final List<String> serviceNames = List.of("Zowe", "Zowe-Plugin");
    /**
     * Account name used for KeyTar querying of OS credential store
     */
    private final String ACCOUNT_NAME = "secure_config_props";
    /**
     * IKeyTar implementation holder
     */
    private IKeyTar keyTar;

    /**
     * KeyTarService constructor
     *
     * @param keyTar IKeyTar implementation Object
     * @author Frank Giordano
     */
    public KeyTarService(IKeyTar keyTar) {
        this.keyTar = keyTar;
    }

    /**
     * Return KeyTarConfig containing team config location and OS credential information.
     *
     * @return KeyTarConfig object
     * @throws Exception error processing
     * @author Frank Giordano
     */
    public KeyTarConfig getKeyTarConfig() throws Exception {
        List<KeyTarConfig> keyTarConfigs = new ArrayList<>();
        keyTar.setAccountName(ACCOUNT_NAME);
        for (String serviceName : serviceNames) {
            keyTar.setServiceName(serviceName);
            try {
                keyTar.processKey();
            } catch (Exception e) {
                LOG.debug(e.getMessage());
                continue;
            }
            LOG.debug("KeyTar Value {}", keyTar.getKeyTarValue());
            keyTarConfigs = keyTar.getKeyConfigs();
            break;
        }
        if (keyTarConfigs.isEmpty()) {
            throw new Exception("No zowe configuration information available.");
        }
        if (keyTarConfigs.size() > 1) {
            LOG.debug("Multiple configuration files retrieved. Returning the first one on list.");
        }
        return keyTarConfigs.get(0);
    }

}
