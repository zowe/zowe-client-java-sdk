/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.core;

import kong.unirest.core.Cookie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the equals() and hashCode() implementations of ZosConnection.
 * <p>
 * Verifies that equality and hashing behave consistently across all AuthType modes:
 * BASIC, TOKEN, and SSL.
 *
 * @author Frank Giordano
 * @version 5.0
 */
class ZosConnectionTest {

    @Test
    void tstBasicConnectionsEqualSuccess() {
        final ZosConnection conn1 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");
        final ZosConnection conn2 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");

        assertEquals(conn1, conn2);
        assertEquals(conn1.hashCode(), conn2.hashCode());
    }

    @Test
    void tstBasicConnectionsNotEqualWithDifferentUserSuccess() {
        final ZosConnection conn1 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");
        final ZosConnection conn2 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userB", "passA");

        assertNotEquals(conn1, conn2);
    }

    @Test
    void tstBasicConnectionsNotEqualWithDifferentHost() {
        final ZosConnection conn1 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");
        final ZosConnection conn2 = ZosConnectionFactory
                .createBasicConnection("host2", "443", "userA", "passA");

        assertNotEquals(conn1, conn2);
    }

    @Test
    void tstBasicConnectionsMaskPasswordAndOthersEmptySuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createBasicConnection("zos", "443", "user", "pass", "/zosmf");
        String result = conn.toString();
        System.out.println(result);

        assertTrue(result.contains("password='*****'"));
        assertTrue(result.contains("certPassword=''"));
        assertTrue(result.contains("token=''"));
    }

    @Test
    void tstBasicConnectionBasePathNormalizationSuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createBasicConnection("host1", "443", "user", "pass", "zosmf/api/");

        assertEquals("/zosmf/api", conn.getBasePath().orElse(null));
    }

    @Test
    void tstBasicConnectionBasePathBackslashNormalizationSuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createBasicConnection("host1", "443", "user", "pass", "\\zosmf\\api\\");

        assertEquals("/zosmf/api", conn.getBasePath().orElse(null));
    }

    @Test
    void tstTokenConnectionsEqualSuccess() {
        Cookie cookie = new Cookie("LtpaToken2", "abcdef");
        final ZosConnection conn1 = ZosConnectionFactory
                .createTokenConnection("host1", "443", cookie);
        final ZosConnection conn2 = ZosConnectionFactory
                .createTokenConnection("host1", "443", new Cookie("LtpaToken2", "abcdef"));

        assertEquals(conn1, conn2);
        assertEquals(conn1.hashCode(), conn2.hashCode());
    }

    @Test
    void tstTokenConnectionsMaskTokenAndOtherPasswordsEmptySuccess() {
        Cookie cookie = new Cookie("LtpaToken2", "abcdef");
        final ZosConnection conn = ZosConnectionFactory
                .createTokenConnection("host1", "443", cookie);
        String result = conn.toString();
        System.out.println(result);

        assertTrue(result.contains("host='host1'"));
        assertTrue(result.contains("zosmfPort='443'"));
        assertTrue(result.contains("authType=TOKEN"));
        assertTrue(result.contains("user=''"));
        assertTrue(result.contains("password=''"));
        assertTrue(result.contains("token='*****'"));
        assertTrue(result.contains("certFilePath=''"));
        assertTrue(result.contains("certPassword=''"));
        assertTrue(result.contains("basePath=''"));
    }

    @Test
    void tstTokenConnectionsNotEqualWithDifferentCookieValueSuccess() {
        Cookie cookie1 = new Cookie("LtpaToken2", "abcdef");
        Cookie cookie2 = new Cookie("LtpaToken2", "xyz123");

        final ZosConnection conn1 = ZosConnectionFactory.
                createTokenConnection("host1", "443", cookie1);
        final ZosConnection conn2 = ZosConnectionFactory.
                createTokenConnection("host1", "443", cookie2);

        assertNotEquals(conn1, conn2);
    }

    @Test
    void tstTokenConnectionBasePathNormalizationSuccess() {
        Cookie cookie = new Cookie("LtpaToken2", "abc123");

        final ZosConnection conn = ZosConnectionFactory
                .createTokenConnection("host1", "443", cookie, "zosmf/api/");

        assertEquals("/zosmf/api", conn.getBasePath().orElse(null));
    }

    @Test
    void tstTokenConnectionBasePathBackslashNormalizationSuccess() {
        Cookie cookie = new Cookie("LtpaToken2", "abc123");

        final ZosConnection conn = ZosConnectionFactory
                .createTokenConnection("host1", "443", cookie, "\\zosmf\\api\\");

        assertEquals("/zosmf/api", conn.getBasePath().orElse(null));
    }

    @Test
    void tstSslConnectionsEqualSuccess() {
        final ZosConnection conn1 = ZosConnectionFactory
                .createSslConnection("host1", "443", "/certs/certA.p12", "certpass");
        final ZosConnection conn2 = ZosConnectionFactory
                .createSslConnection("host1", "443", "/certs/certA.p12", "certpass");

        assertEquals(conn1, conn2);
        assertEquals(conn1.hashCode(), conn2.hashCode());
    }

    @Test
    void tstSslConnectionsNotEqualWithDifferentCertFileSuccess() {
        final ZosConnection conn1 = ZosConnectionFactory
                .createSslConnection("host1", "443", "/certs/certA.p12", "certpass");
        final ZosConnection conn2 = ZosConnectionFactory
                .createSslConnection("host1", "443", "/certs/certB.p12", "certpass");

        assertNotEquals(conn1, conn2);
    }

    @Test
    void tstSslConnectionsMaskCertPasswordAndOthersEmptySuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createSslConnection("host1", "443", "/certs/certA.p12", "certpass");
        String result = conn.toString();
        System.out.println(result);

        assertTrue(result.contains("host='host1'"));
        assertTrue(result.contains("zosmfPort='443'"));
        assertTrue(result.contains("authType=SSL"));
        assertTrue(result.contains("user=''"));
        assertTrue(result.contains("password=''"));
        assertTrue(result.contains("token=''"));
        assertTrue(result.contains("certFilePath='/certs/certA.p12'"));
        assertTrue(result.contains("certPassword='*****'"));
        assertTrue(result.contains("basePath=''"));
    }

    @Test
    void tstSslConnectionBasePathNormalizationSuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createSslConnection("host1", "443",
                        "/certs/cert.p12", "certpass", "zosmf/api/");

        assertEquals("/zosmf/api", conn.getBasePath().orElse(null));
    }

    @Test
    void tstSslConnectionBasePathBackslashNormalizationSuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createSslConnection("host1", "443",
                        "/certs/cert.p12", "certpass", "\\zosmf\\api\\");

        assertEquals("/zosmf/api", conn.getBasePath().orElse(null));
    }

    @Test
    void tstDifferentAuthTypesNotEqualSuccess() {
        final ZosConnection basicConn = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");
        Cookie cookie = new Cookie("LtpaToken2", "abcdef");
        final ZosConnection tokenConn = ZosConnectionFactory.
                createTokenConnection("host1", "443", cookie);

        assertNotEquals(basicConn, tokenConn);
    }

    @Test
    void tstEqualityIsSymmetricAndConsistentSuccess() {
        final ZosConnection conn1 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");
        final ZosConnection conn2 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");

        assertTrue(conn1.equals(conn2) && conn2.equals(conn1));
        assertEquals(conn1.hashCode(), conn2.hashCode());

        // consistency
        assertEquals(conn1, conn2);
        assertEquals(conn1, conn2);
    }

    @Test
    void tstNotEqualToNullOrDifferentClassSuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");

        assertNotEquals(null, conn);
        assertNotEquals("stringValue", conn);
    }

    @Test
    void testBasicConnectionsEqualSuccess() {
        final ZosConnection conn1 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");
        final ZosConnection conn2 = ZosConnectionFactory
                .createBasicConnection("host1", "443", "userA", "passA");

        assertEquals(conn1, conn2);
        assertEquals(conn1.hashCode(), conn2.hashCode());
    }

    @Test
    void tstSettersAndGettersForBasicAuthSuccess() {
        final ZosConnection conn = new ZosConnection("myhost", "1443", "/zosmf/api", AuthType.BASIC);
        conn.setUser("testuser");
        conn.setPassword("testpass");

        assertEquals("myhost", conn.getHost());
        assertEquals("1443", conn.getZosmfPort());
        assertEquals("testuser", conn.getUser());
        assertEquals("testpass", conn.getPassword());
        assertEquals(AuthType.BASIC, conn.getAuthType());
        assertEquals("/zosmf/api", conn.getBasePath().orElse(null));
    }

    @Test
    void tstSettersAndGettersForTokenAuthSuccess() {
        Cookie cookie = new Cookie("LtpaToken2", "abc123");
        final ZosConnection conn = new ZosConnection("zoshost", "443", "/api/v1", AuthType.TOKEN);
        conn.setToken(cookie);

        assertEquals(cookie, conn.getToken());
        assertEquals("zoshost", conn.getHost());
        assertEquals("443", conn.getZosmfPort());
        assertEquals(AuthType.TOKEN, conn.getAuthType());
        assertEquals("/api/v1", conn.getBasePath().orElse(null));
    }

    @Test
    void tstSettersAndGettersForSslAuthSuccess() {
        final ZosConnection conn = new ZosConnection("zosssl", "8443", "/zosmf", AuthType.SSL);
        conn.setCertFilePath("/certs/secure.p12");
        conn.setCertPassword("mypassword");

        assertEquals("/certs/secure.p12", conn.getCertFilePath());
        assertEquals("mypassword", conn.getCertPassword());
        assertEquals("zosssl", conn.getHost());
        assertEquals("8443", conn.getZosmfPort());
        assertEquals(AuthType.SSL, conn.getAuthType());
        assertEquals("/zosmf", conn.getBasePath().orElse(null));
    }

    @Test
    void tstToStringIncludesImportantFieldsSuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createBasicConnection("zos", "443", "user", "pass", "/zosmf");
        String result = conn.toString();
        System.out.println(result);

        assertTrue(result.contains("host='zos'"));
        assertTrue(result.contains("zosmfPort='443'"));
        assertTrue(result.contains("authType=BASIC"));
        assertTrue(result.contains("basePath='/zosmf'"));
        assertTrue(result.contains("user='user'"));
        assertTrue(result.contains("password='*****'"));
        assertTrue(result.contains("token=''"));
        assertTrue(result.contains("certFilePath=''"));
        assertTrue(result.contains("certPassword=''"));
    }

    @Test
    void tstEqualsIsReflexiveSymmetricAndConsistentSuccess() {
        final ZosConnection conn1 = ZosConnectionFactory
                .createBasicConnection("host", "443", "user", "pass");
        final ZosConnection conn2 = ZosConnectionFactory
                .createBasicConnection("host", "443", "user", "pass");

        assertEquals(conn1, conn1);  // reflexive
        assertTrue(conn1.equals(conn2) && conn2.equals(conn1)); // symmetric
        assertEquals(conn1, conn2); // consistent
        assertNotEquals(null, conn1); // null
    }

    @Test
    void tstNotEqualToDifferentObjectTypeSuccess() {
        final ZosConnection conn = ZosConnectionFactory
                .createBasicConnection("zos", "443", "user", "pass");
        assertNotEquals("string", conn);
    }

    @Test
    void tstInvalidPortNumbersFailure() {
        // capture and verify port out of range
        IllegalArgumentException outOfRangeEx = assertThrows(IllegalArgumentException.class,
                () -> ZosConnectionFactory.createBasicConnection("zos", "443666666", "user", "pass"));
        assertTrue(outOfRangeEx.getMessage().contains("port"), "invalid port number: 443666666");

        // capture and verify invalid port 0
        IllegalArgumentException zeroPortEx = assertThrows(IllegalArgumentException.class,
                () -> ZosConnectionFactory.createSslConnection("host1", "0", "/certs/cert.p12", "certpass", "zosmf/api/"));
        assertEquals("invalid port number: 0", zeroPortEx.getMessage());

        // capture and verify non-numeric port
        IllegalArgumentException nonNumericEx = assertThrows(IllegalArgumentException.class,
                () -> ZosConnectionFactory.createTokenConnection("host1", "frank", new Cookie("LtpaToken2", "abcdef")));
        assertEquals("non numeric exception: frank", nonNumericEx.getMessage());
    }

}
