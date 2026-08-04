/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


/**
 * Class containing unit test for ProfileDao.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class ProfileDaoTest {

    @Test
    void tstCreateProfileDaoSuccess() {
        Profile profile = createProfile();

        ProfileDao dao = new ProfileDao(
                profile,
                "USER1",
                "PASSWORD",
                "zos.example.com",
                "443");

        assertSame(profile, dao.getProfile());
        assertEquals("USER1", dao.getUser());
        assertEquals("PASSWORD", dao.getPassword());
        assertEquals("zos.example.com", dao.getHost());
        assertEquals("443", dao.getPort());
    }

    @Test
    void tstReturnToStringWithMaskedPasswordSuccess() {
        Profile profile = createProfile();

        ProfileDao dao = new ProfileDao(
                profile,
                "USER1",
                "PASSWORD",
                "zos.example.com",
                "443");

        assertEquals(
                "ProfileDao{profile=" + profile +
                        ", user='USER1', password='*****', host='zos.example.com', port='443'}",
                dao.toString());
    }

    @Test
    void tstReturnToStringWithEmptyPasswordSuccess() {
        Profile profile = createProfile();

        ProfileDao dao = new ProfileDao(
                profile,
                "USER1",
                "",
                "zos.example.com",
                "443");

        assertEquals(
                "ProfileDao{profile=" + profile +
                        ", user='USER1', password='', host='zos.example.com', port='443'}",
                dao.toString());
    }

    @Test
    void tstReturnToStringWithNullPasswordSuccess() {
        Profile profile = createProfile();

        ProfileDao dao = new ProfileDao(
                profile,
                "USER1",
                null,
                "zos.example.com",
                "443");

        assertEquals(
                "ProfileDao{profile=" + profile +
                        ", user='USER1', password='', host='zos.example.com', port='443'}",
                dao.toString());
    }

    @Test
    void tstReturnToStringWithNullUserSuccess() {
        Profile profile = createProfile();

        ProfileDao dao = new ProfileDao(
                profile,
                null,
                "PASSWORD",
                "zos.example.com",
                "443");

        assertEquals(
                "ProfileDao{profile=" + profile +
                        ", user='', password='*****', host='zos.example.com', port='443'}",
                dao.toString());
    }

    private Profile createProfile() {
        return new Profile("name", "type", null, List.of("1"));
    }

}
