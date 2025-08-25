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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Utility class for the test package.
 *
 * @author James Kostrewski
 * @version 5.0
 */
public final class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Verifies that a class defined as final and uses all static methods is well-defined.
     *
     * @param clazz class object to verify
     */
    private static void assertUtilityClassWellDefined(final Class<?> clazz) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {

        final boolean isFinal = Modifier.isFinal(clazz.getModifiers());
        if (!isFinal) {
            throw new IllegalStateException("class is not final");
        }

        final int numOfConstructors = clazz.getDeclaredConstructors().length;
        if (numOfConstructors > 1) {
            throw new IllegalStateException("more than one constructor defined");
        }

        final Constructor<?> constructor = clazz.getDeclaredConstructor();
        final boolean isPrivateConstructor = Modifier.isPrivate(constructor.getModifiers());
        if (!isPrivateConstructor) {
            throw new IllegalStateException("constructor is not private");
        }

        for (final Method method : clazz.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(clazz)) {
                throw new IllegalStateException("there exists a non-static method: " + method);
            }
        }

        constructor.setAccessible(true);
        constructor.newInstance();
        constructor.setAccessible(false);
    }

    /**
     * Assertion checks class conforms to being set as final, one private constructor, and with all static methods.
     *
     * @param name class object
     */
    public static void validateClass(Class name, String privateConstructorMsg) {
        try {
            Utils.assertUtilityClassWellDefined(name);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalStateException e) {
            LOG.error("error " + e);
            fail();
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalStateException) {
                assertEquals(privateConstructorMsg, e.getTargetException().getMessage());
            } else {
                LOG.error("error " + e);
                fail();
            }
        }
    }

}
