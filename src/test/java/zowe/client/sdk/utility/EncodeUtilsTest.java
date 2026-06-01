/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class containing unit test for EncodeUtils.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class EncodeUtilsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstEncodeUtilsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        UtilsTestHelper.validateClass(EncodeUtils.class, privateConstructorExceptionMsg);
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" -> \"{1}\"")
    @MethodSource("encodeURIComponentCases")
    void tstEncodeURIComponentSuccess(String input, String expected) {
        assertEquals(expected, EncodeUtils.encodeURIComponent(input));
    }

    static Stream<Arguments> encodeURIComponentCases() {
        return Stream.of(
                Arguments.of("abc", "abc"),
                Arguments.of("a b", "a%20b"),
                Arguments.of("a+b", "a%2Bb"),
                Arguments.of("a&b", "a%26b"),
                Arguments.of("a=b", "a%3Db"),
                Arguments.of("a?b", "a%3Fb"),
                Arguments.of("!", "!"),
                Arguments.of("'", "'"),
                Arguments.of("(", "("),
                Arguments.of(")", ")"),
                Arguments.of("~", "~"),
                Arguments.of("/", "%2F"),
                Arguments.of(":", "%3A"),
                Arguments.of("@", "%40"),
                Arguments.of("$", "%24"),
                Arguments.of(",", "%2C"),
                Arguments.of("✓", "%E2%9C%93"),
                Arguments.of("©", "%C2%A9"),
                Arguments.of("你好", "%E4%BD%A0%E5%A5%BD"),
                Arguments.of("email@test.com", "email%40test.com"),
                Arguments.of("a/b/c", "a%2Fb%2Fc"),
                Arguments.of("100%", "100%25")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidEncodeURIComponentCases")
    void tstEncodeURIComponent_invalidInputsFailure(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> EncodeUtils.encodeURIComponent(input));
    }

    static Stream<String> invalidEncodeURIComponentCases() {
        return Stream.of(
                null,
                ""
        );
    }

}
