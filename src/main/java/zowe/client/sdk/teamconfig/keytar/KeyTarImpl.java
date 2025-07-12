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

import com.starxg.keytar.Keytar;
import com.starxg.keytar.KeytarException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.utility.ValidateUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

/**
 * Implementation class for IkeyTar interface that contains the logic for KeyTar processing
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class KeyTarImpl implements IKeyTar {

    private static final Logger LOG = LoggerFactory.getLogger(KeyTarImpl.class);

    /**
     * List of KeyTarConfig objects - OS might contain multiple OS stores
     */
    private final List<KeyTarConfig> keyTarConfigs = new ArrayList<>();

    /**
     * Represents a string value used for KeyTar querying for OS credential store value
     */
    private String serviceName;

    /**
     * Represents a string value used for KeyTar querying for OS credential store value
     */
    private String accountName;

    /**
     * Represents a string value of the retrieved OS credential store
     */
    private String keyString;

    /**
     * Return keyString json value parsed into a KeyTarConfig object.
     *
     * @return list of KeyTarConfig objects
     * @throws TeamConfigException error processing team configuration
     */
    @Override
    public List<KeyTarConfig> getKeyConfigs() throws TeamConfigException {
        ValidateUtils.checkNullParameter(keyString == null, "keyString is null, perform processKey first");
        ValidateUtils.checkIllegalParameter(keyString.isBlank(), "keyString is empty");
        if (!keyTarConfigs.isEmpty()) {
            return keyTarConfigs;
        }
        return parseJson();
    }

    /**
     * Return a keyString value after KeyTar has been fully processed.
     *
     * @return list of KeyTarConfig objects
     */
    @Override
    public String getKeyTarValue() {
        ValidateUtils.checkNullParameter(keyString == null, "keyString is null, perform processKey first");
        ValidateUtils.checkIllegalParameter(keyString.isBlank(), "keyString is empty");
        return keyString;
    }

    /**
     * Parse KeyTar JSON string returned when querying the OS credential store.
     *
     * @return list of KeyTarConfig objects
     * @throws TeamConfigException error processing team configuration
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    private List<KeyTarConfig> parseJson() throws TeamConfigException {
        final JSONObject jsonKeyTar;
        try {
            jsonKeyTar = (JSONObject) new JSONParser().parse(keyString);
        } catch (ParseException e) {
            throw new TeamConfigException("Error parsing KeyTar string.", e);
        }

        final Set<String> keyTarKeys = jsonKeyTar.keySet();
        for (final String keyVal : keyTarKeys) {
            JSONObject jsonVal = (JSONObject) jsonKeyTar.get(keyVal);
            keyTarConfigs.add(new KeyTarConfig(keyVal,
                    (String) jsonVal.get("profiles.base.properties.user"),
                    (String) jsonVal.get("profiles.base.properties.password")));
        }
        return keyTarConfigs;
    }

    /**
     * Retrieve the OS credential store by querying the OS with service and account name.
     * Assign the value to keyString.
     *
     * @throws TeamConfigException error processing team configuration
     * @author Frank Giordano
     */
    @Override
    public void processKey() throws TeamConfigException {
        ValidateUtils.checkNullParameter(serviceName == null, "serviceName is null");
        ValidateUtils.checkIllegalParameter(serviceName.isBlank(), "serviceName is empty");
        ValidateUtils.checkNullParameter(accountName == null, "accountName is null");
        ValidateUtils.checkIllegalParameter(accountName.isBlank(), "accountName is empty");
        final Keytar instance = Keytar.getInstance();
        final String encodedString;
        try {
            encodedString = instance.getPassword(serviceName, accountName);
        } catch (KeytarException e) {
            throw new TeamConfigException("Error retrieving KeyTar password", e);
        }
        LOG.debug("KeyTar encodedString retrieved {}", encodedString);
        if (encodedString == null) {
            throw new TeamConfigException("Unknown service name or account name");
        }
        final byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        this.keyString = new String(decodedBytes);
    }

    /**
     * Set the account name.
     *
     * @param accountName string value used for OS credential store querying
     * @author Frank Giordano
     */
    @Override
    public void setAccountName(final String accountName) {
        this.accountName = accountName;
    }

    /**
     * Set the service name.
     *
     * @param serviceName string value used for OS credential store querying
     * @author Frank Giordano
     */
    @Override
    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

}
