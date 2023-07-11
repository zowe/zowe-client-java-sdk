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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Utility class for test package.
 *
 * @author Frank Giordano
 */
public final class Utils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Verifies that a class defined as final and uses all static methods is well-defined.
     *
     * @param clazz class to verify.
     */
    private static void assertUtilityClassWellDefined(final Class<?> clazz) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {

        boolean isFinal = Modifier.isFinal(clazz.getModifiers());
        if (!isFinal) {
            throw new IllegalStateException("class is not final");
        }

        int numOfConstructors = clazz.getDeclaredConstructors().length;
        if (numOfConstructors > 1) {
            throw new IllegalStateException("more than one constructor defined");
        }

        final Constructor<?> constructor = clazz.getDeclaredConstructor();
        boolean isPrivateConstructor = constructor.isAccessible() || Modifier.isPrivate(constructor.getModifiers());
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
     * @param name class object name
     */
    public static void validateClass(Class name, String privateConstructorMsg) {
        try {
            Utils.assertUtilityClassWellDefined(name);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalStateException e) {
            e.printStackTrace();
            fail();
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalStateException) {
                assertEquals(privateConstructorMsg, e.getTargetException().getMessage());
            } else {
                e.printStackTrace();
                fail();
            }
        }
    }

}
