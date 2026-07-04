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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;
import zowe.client.sdk.teamconfig.model.ConfigContainer;
import zowe.client.sdk.teamconfig.model.Partition;
import zowe.client.sdk.teamconfig.model.Profile;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

/**
 * Class containing unit test for TeamConfig.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class TeamConfigTest {

    private TeamConfigService teamConfigServiceMock;
    private KeyTarService keyTarServiceMock;

    @BeforeEach
    public void init() {
        teamConfigServiceMock = Mockito.mock(TeamConfigService.class);
        keyTarServiceMock = Mockito.mock(KeyTarService.class);
    }

    @Test
    public void tstTeamConfigGetDefaultProfileFailure() throws TeamConfigException {
        final List<Profile> profiles = List.of(new Profile("frank1", "zosmf", Map.of("port", "433"), null));
        final Map<String, String> defaults = Map.of("zosmf", "frank");
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(null, null, profiles, defaults, null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "", ""));

        TeamConfig teamConfig;
        try {
            teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        } catch (TeamConfigException e) {
            throw new RuntimeException(e.getMessage());
        }
        String errMsg = "";
        try {
            teamConfig.getDefaultProfile("zosmf");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("Found no profile of type zosmf in Zowe client configuration.", errMsg);
    }

    @Test
    public void tstTeamConfigGetDefaultProfileTypeNotFoundFailure() throws TeamConfigException {
        final List<Profile> profiles = List.of(new Profile("frank", "zosmf1", Map.of("port", "433"), null));
        final Map<String, String> defaults = Map.of("zosmf", "frank");
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(null, null, profiles, defaults, null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "", ""));

        TeamConfig teamConfig;
        try {
            teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        } catch (TeamConfigException e) {
            throw new RuntimeException(e.getMessage());
        }
        String errMsg = "";
        try {
            teamConfig.getDefaultProfile("zosmf");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("Found no profile of type zosmf in Zowe client configuration.", errMsg);
    }

    @Test
    public void tstTeamConfigGetDefaultProfileSuccess() throws TeamConfigException {
        final List<Profile> profiles = List.of(new Profile("frank", "zosmf", Map.of("port", "433"), null));
        final Map<String, String> defaults = Map.of("zosmf", "frank");
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(null, null, profiles, defaults, null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "", ""));

        TeamConfig teamConfig;
        try {
            teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        } catch (TeamConfigException e) {
            throw new RuntimeException(e.getMessage());
        }
        String errMsg = "";
        ProfileDao profileDao = null;
        try {
            profileDao = teamConfig.getDefaultProfile("zosmf");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("", errMsg);
        assert (profileDao != null);
        assertEquals("frank", profileDao.getProfile().getName());
    }

    @Test
    public void tstTeamConfigGetDefaultProfileHostAndPortValuesSuccess() throws TeamConfigException {
        final List<Profile> profiles = List.of(new Profile("frank", "zosmf", Map.of("port", "433", "host", "host"), null));
        final Map<String, String> defaults = Map.of("zosmf", "frank");
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(null, null, profiles, defaults, null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        ProfileDao profileDao = teamConfig.getDefaultProfile("zosmf");
        assertEquals("host", profileDao.getHost());
        assertEquals("433", profileDao.getPort());
    }

    @Test
    public void tstTeamConfigGetDefaultProfileMergeNonBaseHostValueSuccess() throws TeamConfigException {
        final List<Profile> profiles = List.of(new Profile("frank", "zosmf", Map.of("port", "433", "host", "host"), null),
                new Profile("base", "base", Map.of("port", "433", "host", "host1"), null));
        final Map<String, String> defaults = Map.of("zosmf", "frank");
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(null, null, profiles, defaults, null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        ProfileDao profileDao = teamConfig.getDefaultProfile("zosmf");
        assertEquals("host", profileDao.getHost());
    }

    @Test
    public void tstTeamConfigGetDefaultProfileMergeBaseHostValueSuccess() throws TeamConfigException {
        final List<Profile> profiles = List.of(new Profile("frank", "zosmf", Map.of("port", "433"), null),
                new Profile("base", "base", Map.of("port", "433", "host", "host1"), null));
        final Map<String, String> defaults = Map.of("zosmf", "frank");
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(null, null, profiles, defaults, null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        ProfileDao profileDao = teamConfig.getDefaultProfile("zosmf");
        assertEquals("host1", profileDao.getHost());
    }

    @Test
    public void tstTeamConfigGetDefaultProfileUserNameAndPasswordValuesSuccess() throws TeamConfigException {
        final List<Profile> profiles = List.of(new Profile("frank", "zosmf", Map.of("port", "433"), null));
        final Map<String, String> defaults = Map.of("zosmf", "frank");
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(null, null, profiles, defaults, null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfile("zosmf");
        assertEquals("username", profileDao.getUser());
        assertEquals("pwd", profileDao.getPassword());
    }

    @Test
    public void tstTeamConfigGetDefaultProfileFromPartitionPartitionNotFoundFailure() throws TeamConfigException {
        final List<Profile> partitionProfiles = List.of(new Profile("lpar1zosmf", "zosmf", Map.of("host", "lpar1.example.com", "port", "443"), null));
        final Partition partition = new Partition("lpar1", Map.of(), partitionProfiles);
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(List.of(partition), null, List.of(), Map.of(), null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        String errMsg = "";
        try {
            teamConfig.getDefaultProfileFromPartition("lpar1zosmf", "nonexistent");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("Found no nonexistent in Zowe client configuration.", errMsg);
    }

    @Test
    public void tstTeamConfigGetDefaultProfileFromPartitionProfileNotFoundFailure() throws TeamConfigException {
        final List<Profile> partitionProfiles = List.of(new Profile("lpar1zosmf", "zosmf", Map.of("host", "lpar1.example.com", "port", "443"), null));
        final Partition partition = new Partition("lpar1", Map.of(), partitionProfiles);
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(List.of(partition), null, List.of(), Map.of(), null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        String errMsg = "";
        try {
            teamConfig.getDefaultProfileFromPartition("nonexistent", "lpar1");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("Found no nonexistent within Zowe client configuration partition", errMsg);
    }

    @Test
    public void tstTeamConfigGetDefaultProfileFromPartitionSuccess() throws TeamConfigException {
        final List<Profile> partitionProfiles = List.of(new Profile("lpar1zosmf", "zosmf", Map.of("host", "lpar1.example.com", "port", "443"), null));
        final Partition partition = new Partition("lpar1", Map.of(), partitionProfiles);
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(List.of(partition), null, List.of(), Map.of(), null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfileFromPartition("lpar1zosmf", "lpar1");
        assertNotNull(profileDao);
        assertEquals("lpar1zosmf", profileDao.getProfile().getName());
        assertEquals("lpar1.example.com", profileDao.getHost());
        assertEquals("443", profileDao.getPort());
    }

    @Test
    public void tstTeamConfigGetDefaultProfileFromPartitionUserNameAndPasswordSuccess() throws TeamConfigException {
        final List<Profile> partitionProfiles = List.of(new Profile("lpar1zosmf", "zosmf", Map.of("host", "lpar1.example.com", "port", "443"), null));
        final Partition partition = new Partition("lpar1", Map.of(), partitionProfiles);
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(List.of(partition), null, List.of(), Map.of(), null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "lpar1user", "lpar1pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfileFromPartition("lpar1zosmf", "lpar1");
        assertEquals("lpar1user", profileDao.getUser());
        assertEquals("lpar1pwd", profileDao.getPassword());
    }

    @Test
    public void tstTeamConfigGetDefaultProfileFromPartitionMergeBaseHostSuccess() throws TeamConfigException {
        final List<Profile> partitionProfiles = List.of(new Profile("lpar1zosmf", "zosmf", Map.of("port", "443"), null));
        final Partition partition = new Partition("lpar1", Map.of(), partitionProfiles);
        final List<Profile> baseProfiles = List.of(new Profile("base", "base", Map.of("host", "base.example.com", "port", "10443"), null));
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(List.of(partition), null, baseProfiles, Map.of(), null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfileFromPartition("lpar1zosmf", "lpar1");
        assertEquals("base.example.com", profileDao.getHost());
        assertEquals("443", profileDao.getPort());
    }

    @Test
    public void tstTeamConfigGetDefaultProfileFromPartitionMergeNonBaseHostSuccess() throws TeamConfigException {
        final List<Profile> partitionProfiles = List.of(new Profile("lpar1zosmf", "zosmf", Map.of("host", "lpar1.example.com", "port", "443"), null));
        final Partition partition = new Partition("lpar1", Map.of(), partitionProfiles);
        final List<Profile> baseProfiles = List.of(new Profile("base", "base", Map.of("host", "base.example.com", "port", "10443"), null));
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(
                new ConfigContainer(List.of(partition), null, baseProfiles, Map.of(), null));
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "username", "pwd"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfileFromPartition("lpar1zosmf", "lpar1");
        assertEquals("lpar1.example.com", profileDao.getHost());
    }

    // -------------------------------------------------------------------------
    // Tests using the Zowe team config JSON structure:
    //   profiles: zosmf (port 443), tso (account/codePage/logonProcedure),
    //             ssh (port 22), rse (port 6800/basePath/protocol),
    //             base (host myzos.ibm.com, rejectUnauthorized, secure: user/password)
    //   defaults: zosmf->zosmf, tso->tso, ssh->ssh, rse->rse, base->base
    //   autoStore: true
    // -------------------------------------------------------------------------

    private ConfigContainer buildZoweConfig() {
        final Profile zosmf = new Profile("zosmf", "zosmf", Map.of("port", "443"), List.of());
        final Profile tso = new Profile("tso", "tso",
                Map.of("account", "", "codePage", "1047", "logonProcedure", "IZUFPROC"), List.of());
        final Profile ssh = new Profile("ssh", "ssh", Map.of("port", "22"), List.of());
        final Profile rse = new Profile("rse", "rse",
                Map.of("port", "6800", "basePath", "rseapi", "protocol", "https"), List.of());
        final Profile base = new Profile("base", "base",
                Map.of("host", "myzos.ibm.com", "rejectUnauthorized", "true"),
                List.of("user", "password"));
        final List<Profile> profiles = List.of(zosmf, tso, ssh, rse, base);
        final Map<String, String> defaults = Map.of(
                "zosmf", "zosmf", "tso", "tso", "ssh", "ssh", "rse", "rse", "base", "base");
        return new ConfigContainer(List.of(), "./zowe.schema.json", profiles, defaults, true);
    }

    @Test
    public void tstZoweJsonGetDefaultZosmfProfileSuccess() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfile("zosmf");
        assertNotNull(profileDao);
        assertEquals("zosmf", profileDao.getProfile().getName());
        assertEquals("443", profileDao.getPort());
        // host comes from merged base profile
        assertEquals("myzos.ibm.com", profileDao.getHost());
        assertEquals("true", profileDao.getProfile().getProperties().get("rejectUnauthorized"));
    }

    @Test
    public void tstZoweJsonGetDefaultZosmfProfileUserAndPasswordSuccess() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfile("zosmf");
        assertEquals("myuser", profileDao.getUser());
        assertEquals("mypassword", profileDao.getPassword());
    }

    @Test
    public void tstZoweJsonGetDefaultSshProfileSuccess() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfile("ssh");
        assertNotNull(profileDao);
        assertEquals("ssh", profileDao.getProfile().getName());
        assertEquals("22", profileDao.getPort());
        assertEquals("myzos.ibm.com", profileDao.getHost());
    }

    @Test
    public void tstZoweJsonGetDefaultRseProfileSuccess() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfile("rse");
        assertNotNull(profileDao);
        assertEquals("rse", profileDao.getProfile().getName());
        assertEquals("6800", profileDao.getPort());
        assertEquals("myzos.ibm.com", profileDao.getHost());
        assertEquals("rseapi", profileDao.getProfile().getProperties().get("basePath"));
        assertEquals("https", profileDao.getProfile().getProperties().get("protocol"));
    }

    @Test
    public void tstZoweJsonGetDefaultTsoProfileSuccess() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfile("tso");
        assertNotNull(profileDao);
        assertEquals("tso", profileDao.getProfile().getName());
        assertEquals("1047", profileDao.getProfile().getProperties().get("codePage"));
        assertEquals("IZUFPROC", profileDao.getProfile().getProperties().get("logonProcedure"));
        assertEquals("myzos.ibm.com", profileDao.getHost());
    }

    @Test
    public void tstZoweJsonGetDefaultBaseProfileHostSuccess() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfile("base");
        assertNotNull(profileDao);
        assertEquals("base", profileDao.getProfile().getName());
        assertEquals("myzos.ibm.com", profileDao.getHost());
        assertEquals("true", profileDao.getProfile().getProperties().get("rejectUnauthorized"));
    }

    @Test
    public void tstZoweJsonBaseProfileSecureFieldsSuccess() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final ProfileDao profileDao = teamConfig.getDefaultProfile("base");
        assertEquals(List.of("user", "password"), profileDao.getProfile().getSecure());
    }

    @Test
    public void tstZoweJsonUnknownProfileTypeFailure() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        String errMsg = "";
        try {
            teamConfig.getDefaultProfile("ftp");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("Found no profile of type ftp in Zowe client configuration.", errMsg);
    }

    // -------------------------------------------------------------------------
    // Tests for updateProfile
    // -------------------------------------------------------------------------

    @Test
    public void tstUpdateProfileSuccess() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));
        Mockito.doNothing().when(teamConfigServiceMock).updateTeamConfig(any(), any(), any());

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        final Map<String, String> updates = Map.of("host", "newhost.ibm.com", "port", "8443");
        teamConfig.updateProfile("zosmf", updates);

        Mockito.verify(teamConfigServiceMock).updateTeamConfig(any(), Mockito.eq("zosmf"), Mockito.eq(updates));
    }

    @Test
    public void tstUpdateProfileNullPropertiesFailure() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        String errMsg = "";
        try {
            teamConfig.updateProfile("zosmf", null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("updatedProperties is null", errMsg);
    }

    @Test
    public void tstUpdateProfileBlankNameFailure() throws TeamConfigException {
        Mockito.when(teamConfigServiceMock.getTeamConfig(any())).thenReturn(buildZoweConfig());
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        String errMsg = "";
        try {
            teamConfig.updateProfile("  ", Map.of("host", "newhost.ibm.com"));
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("profileName is either null or empty", errMsg);
    }

    @Test
    public void tstUpdateProfileRefreshesInMemoryConfigSuccess() throws TeamConfigException {
        final ConfigContainer initialConfig = buildZoweConfig();
        final Profile updatedZosmf = new Profile("zosmf", "zosmf", Map.of("port", "8443"), List.of());
        final Profile base = new Profile("base", "base",
                Map.of("host", "newhost.ibm.com", "rejectUnauthorized", "true"), List.of("user", "password"));
        final ConfigContainer refreshedConfig = new ConfigContainer(
                List.of(), "./zowe.schema.json", List.of(updatedZosmf, base),
                Map.of("zosmf", "zosmf", "base", "base"), true);

        Mockito.when(teamConfigServiceMock.getTeamConfig(any()))
                .thenReturn(initialConfig)
                .thenReturn(refreshedConfig);
        Mockito.when(keyTarServiceMock.getKeyTarConfig()).thenReturn(
                new KeyTarConfig("", "myuser", "mypassword"));
        Mockito.doNothing().when(teamConfigServiceMock).updateTeamConfig(any(), any(), any());

        final TeamConfig teamConfig = new TeamConfig(keyTarServiceMock, teamConfigServiceMock);
        teamConfig.updateProfile("zosmf", Map.of("host", "newhost.ibm.com", "port", "8443"));

        final ProfileDao profileDao = teamConfig.getDefaultProfile("zosmf");
        assertEquals("8443", profileDao.getPort());
        assertEquals("newhost.ibm.com", profileDao.getHost());
    }

}
