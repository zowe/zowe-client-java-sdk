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
import zowe.client.sdk.utility.ValidateUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

/**
 * Implementation class for IkeyTar interface that contains the logic for KeyTar processing
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class KeyTarImpl implements IKeyTar {

    /**
     * Logger
     */
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
     * @throws Exception error processing
     * @author Frank Giordano
     */
    @Override
    public List<KeyTarConfig> getKeyConfigs() throws Exception {
        ValidateUtils.checkNullParameter(keyString == null, "keyString is null, perform processKey first");
        ValidateUtils.checkIllegalParameter(keyString.isEmpty(), "keyString is empty");
        if (!keyTarConfigs.isEmpty()) {
            return keyTarConfigs;
        }
        return parseJson();
    }

    /**
     * Return keyString value after KeyTar has been fully processed.
     *
     * @return list of KeyTarConfig objects
     * @author Frank Giordano
     */
    @Override
    public String getKeyTarValue() {
        ValidateUtils.checkNullParameter(keyString == null, "keyString is null, perform processKey first");
        ValidateUtils.checkIllegalParameter(keyString.isEmpty(), "keyString is empty");
        return keyString;
    }

    /**
     * Parse KeyTar json string returned when querying the OS credential store.
     *
     * @return list of KeyTarConfig objects
     * @throws ParseException error processing
     * @author Frank Giordano
     */
    private List<KeyTarConfig> parseJson() throws ParseException {
        JSONObject jsonKeyTar = (JSONObject) new JSONParser().parse(keyString);
        @SuppressWarnings("unchecked")
        Set<String> keyTarKeys = jsonKeyTar.keySet();
        for (String keyVal : keyTarKeys) {
            JSONObject jsonVal = (JSONObject) jsonKeyTar.get(keyVal);
            keyTarConfigs.add(new KeyTarConfig(keyVal,
                    (String) jsonVal.get("profiles.base.properties.user"),
                    (String) jsonVal.get("profiles.base.properties.password")));
        }
        return keyTarConfigs;
    }

    /**
     * Retrieve the OS credential store by querying the OS with service and account name. Assign the value to keyString.
     *
     * @throws KeytarException error processing
     * @author Frank Giordano
     */
    @Override
    public void processKey() throws KeytarException {
        ValidateUtils.checkNullParameter(serviceName == null, "serviceName is null");
        ValidateUtils.checkIllegalParameter(serviceName.isEmpty(), "serviceName is empty");
        ValidateUtils.checkNullParameter(accountName == null, "accountName is null");
        ValidateUtils.checkIllegalParameter(accountName.isEmpty(), "accountName is empty");
        Keytar instance = Keytar.getInstance();
        String encodedString = instance.getPassword(serviceName, accountName);
        LOG.debug("KeyTar encodedString retrieved {}", encodedString);
        if (encodedString == null) {
            throw new NullPointerException("Unknown service name or account name");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        this.keyString = new String(decodedBytes);
    }

    @Override
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
