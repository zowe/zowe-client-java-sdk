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

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;
import zowe.client.sdk.teamconfig.keytar.KeyTarImpl;
import zowe.client.sdk.teamconfig.model.ConfigContainer;
import zowe.client.sdk.teamconfig.model.Partition;
import zowe.client.sdk.teamconfig.model.Profile;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;
import zowe.client.sdk.utility.ValidateUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TeamConfig class provides API method(s) to retrieve a profile section from Zowe Global Team Configuration with
 * keytar information to help perform ZosConnection processing without hard coding username and password. This class only
 * supports Zowe Global Team Configuration provided by Zowe V2.
 *
 * @author Frank Giordano
 * @version 4.0
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
     * KeyTarConfig dependency
     */
    private KeyTarConfig keyTarConfig;
    /**
     * ConfigContainer dependency
     */
    private ConfigContainer teamConfig;

    /**
     * Default TeamConfig constructor without arguments.
     *
     * @throws TeamConfigException error processing team configuration
     */
    public TeamConfig() throws TeamConfigException {
        this.keyTarService = new KeyTarService(new KeyTarImpl());
        this.teamConfigService = new TeamConfigService();
        config();
    }

    /**
     * TeamConfig constructor. This is mainly used for internal code unit testing with mockito.
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
     * Retrieve the default profile for the specified profile type from Zowe Global Team Configuration.
     * Merge properties accordingly with the base profile. Credential store information is also retrieved and
     * piggybacked on a returned ProfileDao object.
     *
     * @param profileType profile type
     * @return ProfileDao object
     * @author Frank Giordano
     */
    public ProfileDao getDefaultProfile(final String profileType) {
        ValidateUtils.checkNullParameter(profileType == null, "profileType is null");
        ValidateUtils.checkIllegalParameter(profileType.isBlank(), "profileType not specified");
        final Optional<String> defaultName = Optional.ofNullable(teamConfig.getDefaults().get(profileType));
        final Predicate<Profile> isProfileName = i -> i.getName().equals(defaultName.orElse(profileType));
        final Optional<Profile> base = teamConfig.getProfiles().stream().filter(isBaseProfile).findFirst();

        Optional<Profile> target = teamConfig.getProfiles().stream().filter(isProfileName).findFirst();
        if (target.isEmpty() || !target.get().getType().equalsIgnoreCase(profileType)) {
            throw new IllegalStateException("Found no profile of type " + profileType + " in Zowe client configuration.");
        } else {
            target = Optional.of(merge(target.orElse(null), base.orElse(null)));
            return new ProfileDao(target.get(), keyTarConfig.getUserName(), keyTarConfig.getPassword(),
                    target.get().getProperties().get("host"), target.get().getProperties().get("port"));
        }
    }

    /**
     * Retrieve the default profile from partition by profile and partition names given from Zowe Global Team Configuration.
     * Merge properties accordingly with the base profile. Credential store information is also retrieved and piggybacked on
     * a returned ProfileDao object.
     *
     * @param profileName   profile name
     * @param partitionName partition name
     * @return ProfileDao object
     * @author Frank Giordano
     */
    public ProfileDao getDefaultProfileFromPartition(final String profileName, final String partitionName) {
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
            throw new IllegalStateException("Found no " + partitionName + " in Zowe client configuration.");
        }

        Optional<Profile> target = partition.get().getProfiles().stream().filter(isProfileName).findFirst();
        if (target.isEmpty()) {
            throw new IllegalStateException("Found no " + profileName + " within Zowe client configuration partition");
        }

        target = Optional.of(merge(target.orElse(null), base.orElse(null)));
        return new ProfileDao(target.get(), keyTarConfig.getUserName(), keyTarConfig.getPassword(),
                target.get().getProperties().get("host"), target.get().getProperties().get("port"));
    }

    /**
     * Take two profile objects and merge all non-duplicate properties into the target.
     *
     * @param target Profile object
     * @param base   Profile object
     * @return target profile object
     * @author Frank Giordano
     */
    private Profile merge(Profile target, final Profile base) {
        Optional<Map<String, String>> targetProps = Optional.empty();
        Optional<Map<String, String>> baseProps = Optional.empty();
        if (target != null) {
            targetProps = Optional.ofNullable(target.getProperties());
        }
        if (base != null) {
            baseProps = Optional.ofNullable(base.getProperties());
        }
        if (targetProps.isPresent() && baseProps.isPresent()) {
            // If duplicate key, keep the old value
            Map<String, String> mergedMap =
                    Stream.concat(targetProps.get().entrySet().stream().filter(Objects::nonNull),
                                    baseProps.get().entrySet().stream().filter(Objects::nonNull))
                            .filter(entry -> entry.getValue() != null)
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (oldValue, newValue) -> oldValue));
            return new Profile(target.getName(), target.getType(), new JSONObject(mergedMap), target.getSecure());
        }

        return target;
    }

}
