
/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.input.factory;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.zosfiles.uss.types.DeleteAclType;
import zowe.client.sdk.zosfiles.uss.types.LinkType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SetAclInputFactory and UssSetAclInputData.Builder.
 */
class SetAclInputFactoryTest {

    @Test
    void tstCreateDeleteTypeInputWithDefaultValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createDeleteTypeInput(DeleteAclType.ACCESS);

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.of(DeleteAclType.ACCESS), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstCreateDeleteTypeInputWithAbortValueSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createDeleteTypeInput(DeleteAclType.ACCESS, true);

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.of(DeleteAclType.ACCESS), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstCreateDeleteTypeInputWithAbortAndLinksValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createDeleteTypeInput(
                DeleteAclType.ACCESS,
                true,
                LinkType.SUPPRESS
        );

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.of(DeleteAclType.ACCESS), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstCreateDeleteInputWithDefaultValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createDeleteInput("user:test:rwx");

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.of("user:test:rwx"), inputData.getDelete());
    }

    @Test
    void tstCreateDeleteInputWithAbortAndLinksValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createDeleteInput(
                "user:test:rwx",
                true,
                LinkType.SUPPRESS
        );

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.of("user:test:rwx"), inputData.getDelete());
    }

    @Test
    void tstCreateModifyInputWithDefaultValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createModifyInput("user:test:rwx");

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.of("user:test:rwx"), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstCreateModifyInputWithAbortAndLinksValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createModifyInput(
                "user:test:rwx",
                true,
                LinkType.SUPPRESS
        );

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.of("user:test:rwx"), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstCreateSetInputWithDefaultValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createSetInput("user:test:rwx");

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.of("user:test:rwx"), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstCreateSetInputWithAbortAndLinksValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createSetInput(
                "user:test:rwx",
                true,
                LinkType.SUPPRESS
        );

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.of("user:test:rwx"), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstCreateModifyDeleteInputWithDefaultValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createModifyDeleteInput(
                "user:test:rwx",
                "group:test:r-x"
        );

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.of("user:test:rwx"), inputData.getModify());
        assertEquals(Optional.of("group:test:r-x"), inputData.getDelete());
    }

    @Test
    void tstCreateModifyDeleteInputWithAbortAndLinksValuesSuccess() {
        final UssSetAclInputData inputData = SetAclInputFactory.createModifyDeleteInput(
                "user:test:rwx",
                "group:test:r-x",
                true,
                LinkType.SUPPRESS
        );

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.of("user:test:rwx"), inputData.getModify());
        assertEquals(Optional.of("group:test:r-x"), inputData.getDelete());
    }

    @Test
    void tstBuilderCreatesDeleteTypeInputDataSuccess() {
        final UssSetAclInputData inputData = new UssSetAclInputData.Builder()
                .setDeleteType(DeleteAclType.ACCESS)
                .setAbort(true)
                .setLinks(LinkType.SUPPRESS)
                .build();

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.of(DeleteAclType.ACCESS), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstBuilderCreatesSetInputDataSuccess() {
        final UssSetAclInputData inputData = new UssSetAclInputData.Builder()
                .setSet("user:test:rwx")
                .setAbort(true)
                .setLinks(LinkType.SUPPRESS)
                .build();

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.of("user:test:rwx"), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstBuilderCreatesModifyInputDataSuccess() {
        final UssSetAclInputData inputData = new UssSetAclInputData.Builder()
                .setModify("user:test:rwx")
                .setAbort(true)
                .setLinks(LinkType.SUPPRESS)
                .build();

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.of("user:test:rwx"), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    @Test
    void tstBuilderCreatesDeleteInputDataSuccess() {
        final UssSetAclInputData inputData = new UssSetAclInputData.Builder()
                .setDelete("user:test:rwx")
                .setAbort(true)
                .setLinks(LinkType.SUPPRESS)
                .build();

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.of("user:test:rwx"), inputData.getDelete());
    }

    @Test
    void tstBuilderCreatesModifyDeleteInputDataSuccess() {
        final UssSetAclInputData inputData = new UssSetAclInputData.Builder()
                .setModify("user:test:rwx")
                .setDelete("group:test:r-x")
                .setAbort(true)
                .setLinks(LinkType.SUPPRESS)
                .build();

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.SUPPRESS), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.of("user:test:rwx"), inputData.getModify());
        assertEquals(Optional.of("group:test:r-x"), inputData.getDelete());
    }

    @Test
    void tstBuilderRejectsNullDeleteTypeValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setDeleteType(null)
        );
    }

    @Test
    void tstBuilderRejectsNullSetValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setSet(null)
        );
    }

    @Test
    void tstBuilderRejectsBlankSetValueFailure() {
        assertThrows(IllegalArgumentException.class, () ->
                new UssSetAclInputData.Builder().setSet("")
        );
    }

    @Test
    void tstBuilderRejectsNullModifyValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setModify(null)
        );
    }

    @Test
    void tstBuilderRejectsBlankModifyValueFailure() {
        assertThrows(IllegalArgumentException.class, () ->
                new UssSetAclInputData.Builder().setModify("")
        );
    }

    @Test
    void tstBuilderRejectsNullDeleteValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setDelete(null)
        );
    }

    @Test
    void tstBuilderRejectsBlankDeleteValueFailure() {
        assertThrows(IllegalArgumentException.class, () ->
                new UssSetAclInputData.Builder().setDelete("")
        );
    }

    @Test
    void tstBuilderRejectsNullLinksValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setLinks(null)
        );
    }

}
