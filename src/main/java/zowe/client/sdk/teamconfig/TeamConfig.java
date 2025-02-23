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
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
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
 * keytar information to help perform ZosConnection processing without hard coding username and password. This class only
 * supports Zowe Global Team Configuration provided by Zowe V2.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class TeamConfig {

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
     * @throws TeamConfigException error processing team configuration
     * @author Frank Giordano
     */
    public TeamConfig(final KeyTarService keyTarService, final TeamConfigService teamConfigService)
            throws TeamConfigException {
        this.keyTarService = keyTarService;
        this.teamConfigService = teamConfigService;
        config();
    }

    /**
     * Initialize dependency objects
     *
     * @throws TeamConfigException error processing team configuration
     * @author Frank Giordano
     */
    private void config() throws TeamConfigException {
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
     * @author Frank Giordano
     */
    public ProfileDao getDefaultProfileByName(final String name) {
        ValidateUtils.checkNullParameter(name == null, "name is null");
        ValidateUtils.checkIllegalParameter(name.isBlank(), "name not specified");
        final Optional<String> defaultName = Optional.ofNullable(teamConfig.getDefaults().get(name));
        final Predicate<Profile> isProfileName = i -> i.getName().equals(defaultName.orElse(name));
        final Optional<Profile> base = teamConfig.getProfiles().stream().filter(isBaseProfile).findFirst();

        final Optional<Profile> target = teamConfig.getProfiles().stream().filter(isProfileName).findFirst();
        if (target.isEmpty()) {
            throw new IllegalStateException("TeamConfig profile " + name + " not found");
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
     * @author Frank Giordano
     */
    public ProfileDao getDefaultProfileFromPartitionByName(final String profileName, final String partitionName) {
        ValidateUtils.checkNullParameter(profileName == null, "profileName is null");
        ValidateUtils.checkIllegalParameter(profileName.isBlank(), "profileName not specified");
        ValidateUtils.checkNullParameter(partitionName == null, "partitionName is null");
        ValidateUtils.checkIllegalParameter(partitionName.isBlank(), "partitionName not specified");
        final Optional<String> defaultName = Optional.ofNullable(teamConfig.getDefaults().get(profileName));
        final Predicate<Profile> isProfileName = i -> i.getName().equals(defaultName.orElse(profileName));
        final Predicate<Partition> isPartitionName = i -> i.getName().equals(partitionName);
        final Optional<Profile> base = teamConfig.getProfiles().stream().filter(isBaseProfile).findFirst();

        final Optional<Partition> partition = teamConfig.getPartitions().stream().filter(isPartitionName).findFirst();
        if (partition.isEmpty()) {
            throw new IllegalStateException("TeamConfig partition " + partitionName + " not found");
        }

        final Optional<Profile> target = partition.get().getProfiles().stream().filter(isProfileName).findFirst();
        if (target.isEmpty()) {
            throw new IllegalStateException("TeamConfig profile " + profileName + " within partition not found");
        }

        final Map<String, String> props = partition.get().getProperties();
        mergeProperties.setHost(props.get("host"));
        mergeProperties.setPort(props.get("port"));

        merge(target, base);
        return new ProfileDao(target.get(), keyTarConfig.getUserName(), keyTarConfig.getPassword(),
                mergeProperties.getHost().orElse(null), mergeProperties.getPort().orElse(null));
    }

    /**
     * Take two profile objects and determine if they have host and port values to be merged.
     *
     * @param target Optional Profile object
     * @param base   Optional Profile object
     * @author Frank Giordano
     */
    private void merge(final Optional<Profile> target, final Optional<Profile> base) {
        final Optional<Map<String, String>> targetProps = Optional.ofNullable(target.get().getProperties());
        final Optional<Map<String, String>> baseProps = Optional.ofNullable(base.get().getProperties());
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

    private static class MergeProperties {

        private Optional<String> host = Optional.empty();
        private Optional<String> port = Optional.empty();

        public Optional<String> getHost() {
            return host;
        }

        public void setHost(final String host) {
            this.host = Optional.ofNullable(host);
        }

        public Optional<String> getPort() {
            return port;
        }

        public void setPort(final String port) {
            this.port = Optional.ofNullable(port);
        }

    }

}
