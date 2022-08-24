/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;
import zowe.client.sdk.teamconfig.model.ConfigContainer;
import zowe.client.sdk.teamconfig.model.Partition;
import zowe.client.sdk.teamconfig.model.Profile;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;
import zowe.client.sdk.utility.ValidateUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * TeamConfig class provides API method(s) to retrieve a profile section from Zowe Global Team Configuration with
 * keytar information to help perform ZOSConnection processing without hard coding username and password. This class only
 * supports Zowe Global Team Configuration provided by Zowe V2.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class TeamConfig {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(TeamConfig.class);
    /**
     * TeamConfigService dependency
     */
    private final TeamConfigService teamConfigService;
    /**
     * KeyTarService dependency
     */
    private final KeyTarService keyTarService;
    /**
     * Base profile constant
     */
    private final String BASE_PROFILE_NAME = "base";
    /**
     * Is Base profile predicate
     */
    private final Predicate<Profile> isBaseProfile = i -> i.getName().equals(BASE_PROFILE_NAME);
    /**
     * Properties object for merging properties between profile types
     */
    private final MergeProperties mergeProperties = new MergeProperties();
    /**
     * KeyTarConfig dependency
     */
    private KeyTarConfig keyTarConfig;
    /**
     * ConfigContainer dependency
     */
    private ConfigContainer teamConfig;

    /**
     * TeamConfig constructor
     *
     * @param keyTarService     required KeyTarService dependency
     * @param teamConfigService required TeamConfigService dependency
     * @throws Exception error processing
     * @author Frank Giordano
     */
    public TeamConfig(KeyTarService keyTarService, TeamConfigService teamConfigService) throws Exception {
        this.keyTarService = keyTarService;
        this.teamConfigService = teamConfigService;
        config();
    }

    /**
     * Initialize dependency objects
     *
     * @throws Exception error processing
     * @author Frank Giordano
     */
    private void config() throws Exception {
        keyTarConfig = keyTarService.getKeyTarConfig();
        teamConfig = teamConfigService.getTeamConfig(keyTarConfig);
        LOG.debug("keyTarConfig {}", keyTarConfig);
        LOG.debug("teamConfig {}", teamConfig);
    }

    /**
     * Retrieve default profile by profile name given from Zowe Global Team Configuration.
     * Merge properties accordingly if needed due to absence of important properties from default profile with
     * base profile. Credential store information is also retrieved and piggybacked on returned ProfileDao object.
     *
     * @param name profile name
     * @return ProfileDao object
     * @throws Exception error processing
     * @author Frank Giordano
     */
    public ProfileDao getDefaultProfileByName(String name) throws Exception {
        ValidateUtils.checkNullParameter(name == null, "name is null");
        ValidateUtils.checkIllegalParameter(name.isEmpty(), "name not specified");
        Optional<String> defaultName = Optional.ofNullable(teamConfig.getDefaults().get(name));
        Predicate<Profile> isProfileName = i -> i.getName().equals(defaultName.orElse(name));
        Optional<Profile> base = teamConfig.getProfiles().stream().filter(isBaseProfile).findFirst();

        Optional<Profile> target = teamConfig.getProfiles().stream().filter(isProfileName).findFirst();
        if (target.isEmpty()) {
            throw new Exception("Profile " + name + " not found");
        }

        merge(target, base);
        return new ProfileDao(target.get(), keyTarConfig.getUserName(), keyTarConfig.getPassword(),
                mergeProperties.getHost().orElse(null), mergeProperties.getPort().orElse(null));
    }

    /**
     * Retrieve default profile from partition by profile and partition names given from Zowe Global Team Configuration.
     * Merge properties accordingly if needed due to absence of important properties from default profile with
     * base profile. Credential store information is also retrieved and piggybacked on returned ProfileDao object.
     *
     * @param profileName   profile name
     * @param partitionName partition name
     * @return ProfileDao object
     * @throws Exception error processing
     * @author Frank Giordano
     */
    public ProfileDao getDefaultProfileFromPartitionByName(String profileName, String partitionName) throws Exception {
        ValidateUtils.checkNullParameter(profileName == null, "profileName is null");
        ValidateUtils.checkIllegalParameter(profileName.isEmpty(), "profileName not specified");
        ValidateUtils.checkNullParameter(partitionName == null, "partitionName is null");
        ValidateUtils.checkIllegalParameter(partitionName.isEmpty(), "partitionName not specified");
        Optional<String> defaultName = Optional.ofNullable(teamConfig.getDefaults().get(profileName));
        Predicate<Profile> isProfileName = i -> i.getName().equals(defaultName.orElse(profileName));
        Predicate<Partition> isPartitionName = i -> i.getName().equals(partitionName);
        Optional<Profile> base = teamConfig.getProfiles().stream().filter(isBaseProfile).findFirst();

        Optional<Partition> partition = teamConfig.getPartitions().stream().filter(isPartitionName).findFirst();
        if (partition.isEmpty()) {
            throw new Exception("Partition " + partitionName + " not found");
        }

        Optional<Profile> target = partition.get().getProfiles().stream().filter(isProfileName).findFirst();
        if (target.isEmpty()) {
            throw new Exception("Profile " + profileName + " within partition not found");
        }

        Map<String, String> props = partition.get().getProperties();
        mergeProperties.setHost(props.get("host"));
        mergeProperties.setPort(props.get("port"));

        merge(target, base);
        return new ProfileDao(target.get(), keyTarConfig.getUserName(), keyTarConfig.getPassword(),
                mergeProperties.getHost().orElse(null), mergeProperties.getPort().orElse(null));
    }

    /**
     * Take two profile objects and determine if they have host and port values to be merged.
     *
     * @param target profile object
     * @param base   profile object
     * @author Frank Giordano
     */
    private void merge(Optional<Profile> target, Optional<Profile> base) {
        Optional<Map<String, String>> targetProps = Optional.ofNullable(target.get().getProperties());
        Optional<Map<String, String>> baseProps = Optional.ofNullable(base.get().getProperties());
        if (mergeProperties.getHost().isEmpty() && targetProps.isPresent()) {
            mergeProperties.setHost(targetProps.get().get("host"));
        }
        if (mergeProperties.getPort().isEmpty() && targetProps.isPresent()) {
            mergeProperties.setPort(targetProps.get().get("port"));
        }
        if (mergeProperties.getHost().isEmpty() && baseProps.isPresent()) {
            mergeProperties.setHost(baseProps.get().get("host"));
        }
        if (mergeProperties.getPort().isEmpty() && baseProps.isPresent()) {
            mergeProperties.setPort(baseProps.get().get("port"));
        }
    }

    private class MergeProperties {
        private Optional<String> host = Optional.empty();
        private Optional<String> port = Optional.empty();

        public Optional<String> getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = Optional.ofNullable(host);
        }

        public Optional<String> getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = Optional.ofNullable(port);
        }
    }

}