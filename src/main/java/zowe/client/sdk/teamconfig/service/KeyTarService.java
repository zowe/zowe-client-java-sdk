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
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;
import zowe.client.sdk.teamconfig.keytar.KeyTarImpl;

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

    private static final Logger LOG = LoggerFactory.getLogger(KeyTarService.class);
    private final List<String> serviceNames = List.of("Zowe", "Zowe-Plugin");
    private final String ACCOUNT_NAME = "secure_config_props";

    public KeyTarConfig getKeyTarConfig() throws Exception {
        List<KeyTarConfig> keyTarConfigs = new ArrayList<>();
        for (final String serviceName : serviceNames) {
            KeyTarImpl keyTarImpl = new KeyTarImpl(serviceName, ACCOUNT_NAME);
            try {
                keyTarImpl.processKey();
            } catch (Exception e) {
                continue;
            }
            LOG.debug("KeyTar Value {}", keyTarImpl.getKeyTarValue());
            keyTarConfigs = keyTarImpl.getKeyConfigs();
            break;
        }
        if (keyTarConfigs.isEmpty()) {
            throw new Exception("No zowe configuration information available");
        }
        return keyTarConfigs.get(0);
    }

}
