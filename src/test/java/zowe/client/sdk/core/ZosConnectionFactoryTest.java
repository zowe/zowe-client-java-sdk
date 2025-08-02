package zowe.client.sdk.core;

import kong.unirest.core.Cookie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZosConnectionFactoryTest {

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
}