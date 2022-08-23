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

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class TeamConfig {

    private static final Logger LOG = LoggerFactory.getLogger(TeamConfig.class);
    private final TeamConfigService teamConfigService;
    private final KeyTarService keyTarService;
    private final String BASE_PROFILE_NAME = "base";
    private final Predicate<Profile> isBaseProfile = i -> i.getName().equals(BASE_PROFILE_NAME);
    private final MergeProperties mergeProperties = new MergeProperties();
    private KeyTarConfig keyTarConfig;
    private ConfigContainer teamConfig;

    public TeamConfig(KeyTarService keyTarService, TeamConfigService teamConfigService) throws Exception {
        this.keyTarService = keyTarService;
        this.teamConfigService = teamConfigService;
        config();
    }

    private void config() throws Exception {
        keyTarConfig = keyTarService.getKeyTarConfig();
        teamConfig = teamConfigService.getTeamConfig(keyTarConfig);
        LOG.debug("keyTarConfig {}", keyTarConfig);
        LOG.debug("teamConfig {}", teamConfig);
    }

    public ProfileDao getDefaultProfileByName(String name) throws Exception {
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

    public ProfileDao getDefaultProfileFromPartitionByName(String profileName, String partitionName) throws Exception {
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
