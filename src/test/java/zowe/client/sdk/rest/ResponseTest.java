/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest;

import kong.unirest.core.Cookies;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for Response.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class ResponseTest {

    @Test
    public void tstResponseConstructorWithoutTokensSuccess() {
        final Response response = new Response("hello", 200, "ok");

        assertTrue(response.getResponsePhrase().isPresent());
        assertEquals("hello", response.getResponsePhrase().get());
        assertTrue(response.getStatusCode().isPresent());
        assertEquals(200, response.getStatusCode().getAsInt());
        assertTrue(response.getStatusText().isPresent());
        assertEquals("ok", response.getStatusText().get());
        assertNull(response.getTokens());
    }

    @Test
    public void tstResponseConstructorWithTokensSuccess() {
        final Cookies tokens = Mockito.mock(Cookies.class);
        final Response response = new Response("hello", 200, "ok", tokens);

        assertTrue(response.getResponsePhrase().isPresent());
        assertEquals("hello", response.getResponsePhrase().get());
        assertTrue(response.getStatusCode().isPresent());
        assertEquals(200, response.getStatusCode().getAsInt());
        assertTrue(response.getStatusText().isPresent());
        assertEquals("ok", response.getStatusText().get());
        assertEquals(tokens, response.getTokens());
    }

    @Test
    public void tstGetResponsePhraseWithNullValueSuccess() {
        final Response response = new Response(null, 200, "ok");

        assertFalse(response.getResponsePhrase().isPresent());
    }

    @Test
    public void tstGetResponsePhraseWithStringValueSuccess() {
        final Response response = new Response("sample response", 200, "ok");

        assertTrue(response.getResponsePhrase().isPresent());
        assertEquals("sample response", response.getResponsePhrase().get());
    }

    @Test
    public void tstGetResponsePhraseWithByteArrayValueSuccess() {
        final byte[] bytes = "hello".getBytes();
        final Response response = new Response(bytes, 200, "ok");

        assertTrue(response.getResponsePhrase().isPresent());
        assertArrayEquals(bytes, (byte[]) response.getResponsePhrase().get());
    }

    @Test
    public void tstGetStatusCodeWithNullValueSuccess() {
        final Response response = new Response("hello", null, "ok");

        final OptionalInt statusCode = response.getStatusCode();
        assertFalse(statusCode.isPresent());
    }

    @Test
    public void tstGetStatusCodeWithValueSuccess() {
        final Response response = new Response("hello", 201, "created");

        assertTrue(response.getStatusCode().isPresent());
        assertEquals(201, response.getStatusCode().getAsInt());
    }

    @Test
    public void tstGetStatusTextWithNullValueSuccess() {
        final Response response = new Response("hello", 200, null);

        assertFalse(response.getStatusText().isPresent());
    }

    @Test
    public void tstGetStatusTextWithValueSuccess() {
        final Response response = new Response("hello", 200, "ok");

        assertTrue(response.getStatusText().isPresent());
        assertEquals("ok", response.getStatusText().get());
    }

    @Test
    public void tstGetResponsePhraseAsStringWithStringValueSuccess() {
        final Response response = new Response("hello world", 200, "ok");

        assertTrue(response.getResponsePhraseAsString().isPresent());
        assertEquals("hello world", response.getResponsePhraseAsString().get());
    }

    @Test
    public void tstGetResponsePhraseAsStringWithNullValueSuccess() {
        final Response response = new Response(null, 200, "ok");

        assertFalse(response.getResponsePhraseAsString().isPresent());
    }

    @Test
    public void tstGetResponsePhraseAsStringWithObjectValueSuccess() {
        final Object phrase = 12345;
        final Response response = new Response(phrase, 200, "ok");

        assertTrue(response.getResponsePhraseAsString().isPresent());
        assertEquals("12345", response.getResponsePhraseAsString().get());
    }

    @Test
    public void tstGetResponsePhraseAsBytesWithByteArrayValueSuccess() {
        final byte[] bytes = "binary".getBytes();
        final Response response = new Response(bytes, 200, "ok");

        assertTrue(response.getResponsePhraseAsBytes().isPresent());
        assertArrayEquals(bytes, response.getResponsePhraseAsBytes().get());
    }

    @Test
    public void tstGetResponsePhraseAsBytesWithStringValueSuccess() {
        final Response response = new Response("not bytes", 200, "ok");

        assertFalse(response.getResponsePhraseAsBytes().isPresent());
    }

    @Test
    public void tstHasResponsePhraseWithNullValueSuccess() {
        final Response response = new Response(null, 200, "ok");

        assertFalse(response.hasResponsePhrase());
    }

    @Test
    public void tstHasResponsePhraseWithValueSuccess() {
        final Response response = new Response("hello", 200, "ok");

        assertTrue(response.hasResponsePhrase());
    }

    @Test
    public void tstHasTextResponsePhraseWithNullValueSuccess() {
        final Response response = new Response(null, 200, "ok");

        assertFalse(response.hasTextResponsePhrase());
    }

    @Test
    public void tstHasTextResponsePhraseWithNonStringValueSuccess() {
        final Response response = new Response(123, 200, "ok");

        assertFalse(response.hasTextResponsePhrase());
    }

    @Test
    public void tstHasTextResponsePhraseWithBlankStringValueSuccess() {
        final Response response = new Response("   ", 200, "ok");

        assertFalse(response.hasTextResponsePhrase());
    }

    @Test
    public void tstHasTextResponsePhraseWithEmptyJsonStringValueSuccess() {
        final Response response = new Response("{}", 200, "ok");

        assertFalse(response.hasTextResponsePhrase());
    }

    @Test
    public void tstHasTextResponsePhraseWithValidStringValueSuccess() {
        final Response response = new Response("valid response", 200, "ok");

        assertTrue(response.hasTextResponsePhrase());
    }

    @Test
    public void tstGetResponsePhraseAsStringOrDefaultSuccess() {
        final Response response = new Response("valid response", 200, "ok");

        assertEquals("valid response", response.getResponsePhraseAsStringOrDefault("default"));
    }

    @Test
    public void tstGetResponsePhraseAsStringOrDefaultForDefaultValueSuccess() {
        final Response response = new Response(null, 200, "ok");

        assertEquals("default", response.getResponsePhraseAsStringOrDefault("default"));
    }

    @Test
    public void tstGetResponsePhraseAsStringOrDefaultWithBlankResponsePhraseSuccess() {
        final Response response = new Response("", 200, "ok");

        assertEquals("", response.getResponsePhraseAsStringOrDefault("default"));
    }

    @Test
    public void tstToStringSuccess() {
        final Response response = new Response("hello", 200, "ok");

        final String value = response.toString();

        assertTrue(value.contains("responsePhrase=hello"));
        assertTrue(value.contains("statusCode=200"));
        assertTrue(value.contains("statusText=ok"));
    }

}
