package zowe.client.sdk.core;

import kong.unirest.core.Cookie;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class ZosConnectionFactoryTest {

    @Test
    public void tstBasicZosConnectionInvalidStates() {
        assertThrows(IllegalStateException.class, () ->
            ZosConnectionFactory.createBasicConnection(null, "port", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("", "port", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", null, "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", " ", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", "port", null, "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", "port", "", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", "port", "user", null)
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", "port", "user", "")
        );
    }

    @Test
    public void tstSslZosConnectionInvalidStates() {
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection(null, "port", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("", "port", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", null, "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", " ", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", "port", null, "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", "port", "", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", "port", "user", null)
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", "port", "user", "")
        );
    }

    @Test
    public void tstTokenZosConnectionInvalidStates() {
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection(null, "port", new Cookie("foo", "bar"))
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection("", "port", new Cookie("foo", "bar"))
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection("host", null, new Cookie("foo", "bar"))
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection("host", " ", new Cookie("foo", "bar"))
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection("host", "port", null)
        );
    }

    @org.junit.Test
    public void tstReferenceNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        assertNotSame(zc1, zc2);
    }

    @org.junit.Test
    public void tstReferenceNotEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        final ZosConnection zc2 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello1", "world"));
        assertNotSame(zc1, zc2);
    }

    @org.junit.Test
    public void tstReferenceEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @org.junit.Test
    public void tstReferenceEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @org.junit.Test
    public void tstEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        assertEquals(zc1, zc2);
    }

    @org.junit.Test
    public void tstEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        final ZosConnection zc2 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        assertEquals(zc1, zc2);
    }

    @org.junit.Test
    public void tstNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test2", "zosmfPort", "user", "password");
        assertNotEquals(zc1, zc2);
    }

    @org.junit.Test
    public void tstNotEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world1"));
        final ZosConnection zc2 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        assertNotEquals(zc1, zc2);
    }

    @org.junit.Test
    public void tstHashCodeMapWithSecondHostDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test2", "zosmfPort", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @org.junit.Test
    public void tstHashCodeMapWithSecondZosmfPortDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort2", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @org.junit.Test
    public void tstHashCodeMapWithSecondUserDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user2", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @org.junit.Test
    public void tstHashCodeMapWithSecondPasswordDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password2");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @org.junit.Test
    public void tstHashCodeMapNoDuplicateSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(1, zcs.size());
    }

    @org.junit.Test
    public void tstBasePathSetGetSuccess() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        connection.setBasePath("/custom/path");
        assertEquals("/custom/path",
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : ""));
    }

    @org.junit.Test
    public void tstBasePathDefaultEmptySuccess() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        assertTrue(connection.getBasePath().isEmpty());
    }

    @org.junit.Test
    public void tstBasePathEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        zc1.setBasePath("/custom/path");
        zc2.setBasePath("/custom/path");
        assertEquals(zc1, zc2);
    }

    @org.junit.Test
    public void tstBasePathNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        zc1.setBasePath("/custom/path1");
        zc2.setBasePath("/custom/path2");
        assertNotEquals(zc1, zc2);
    }

    @org.junit.Test
    public void tstBasePathHashCodeMapWithDifferentPathsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        zc1.setBasePath("/custom/path1");
        zc2.setBasePath("/custom/path2");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @org.junit.Test
    public void tstBasePathHashCodeMapWithSamePathsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        zc1.setBasePath("/custom/path");
        zc2.setBasePath("/custom/path");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(1, zcs.size());
    }

    @org.junit.Test
    public void tstBasePathTokenConnectionSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        zc1.setBasePath("/custom/path");
        assertEquals("/custom/path", (zc1.getBasePath().isPresent() ? zc1.getBasePath().get() : ""));
    }

    @org.junit.Test
    public void tstBasePathSslConnectionSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createSslConnection("test", "zosmfPort", "certPath", "certPassword");
        zc1.setBasePath("/custom/path");
        assertEquals("/custom/path", (zc1.getBasePath().isPresent() ? zc1.getBasePath().get() : ""));
    }
}