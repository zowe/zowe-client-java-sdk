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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;
import zowe.client.sdk.teamconfig.model.ConfigContainer;
import zowe.client.sdk.teamconfig.model.Profile;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 * Class containing unit test for TeamConfig.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class TeamConfigTest {

    private TeamConfigService teamConfigServiceMock;
    private KeyTarService keyTarServiceMock;

    @Before
    public void init() {
        teamConfigServiceMock = Mockito.mock(TeamConfigService.class);
        keyTarServiceMock = Mockito.mock(KeyTarService.class);
    }

    @Test
    public void tstGetDefaultProfileByNameFailure() throws TeamConfigException {
        final JSONObject props = new JSONObject(Map.of("port", "433"));
        final List<Profile> profiles = List.of(new Profile("frank1", "zosmf", props, null));
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
            teamConfig.getDefaultProfileByName("zosmf");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("No TeamConfig default profile for zosmf is found.", errMsg);
    }

    @Test
    public void tstGetDefaultProfileByNameTypeNotFoundFailure() throws TeamConfigException {
        final JSONObject props = new JSONObject(Map.of("port", "433"));
        final List<Profile> profiles = List.of(new Profile("frank", "zosmf1", props, null));
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
            teamConfig.getDefaultProfileByName("zosmf");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("No TeamConfig default profile for type zosmf is found.", errMsg);
    }

    @Test
    public void tstGetDefaultProfileByNamSuccess() throws TeamConfigException {
        final JSONObject props = new JSONObject(Map.of("port", "433"));
        final List<Profile> profiles = List.of(new Profile("frank", "zosmf", props, null));
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
            profileDao = teamConfig.getDefaultProfileByName("zosmf");
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("", errMsg);
        assertEquals("frank", profileDao.getProfile().getName());
    }

}
