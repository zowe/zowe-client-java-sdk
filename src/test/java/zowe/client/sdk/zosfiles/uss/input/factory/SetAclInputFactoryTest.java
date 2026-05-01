
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

    /**
     * Verify createDeleteTypeInput creates input data with default values.
     */
    @Test
    void tstCreateDeleteTypeInputWithDefaultValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createDeleteTypeInput(DeleteAclType.ACCESS);

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.of(DeleteAclType.ACCESS), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    /**
     * Verify createDeleteTypeInput creates input data with abort value.
     */
    @Test
    void tstCreateDeleteTypeInputWithAbortValueSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createDeleteTypeInput(DeleteAclType.ACCESS, true);

        assertTrue(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.of(DeleteAclType.ACCESS), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    /**
     * Verify createDeleteTypeInput creates input data with abort and links values.
     */
    @Test
    void tstCreateDeleteTypeInputWithAbortAndLinksValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createDeleteTypeInput(
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

    /**
     * Verify createDeleteInput creates input data with default values.
     */
    @Test
    void tstCreateDeleteInputWithDefaultValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createDeleteInput("user:test:rwx");

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.of("user:test:rwx"), inputData.getDelete());
    }

    /**
     * Verify createDeleteInput creates input data with abort and links values.
     */
    @Test
    void tstCreateDeleteInputWithAbortAndLinksValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createDeleteInput(
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

    /**
     * Verify createModifyInput creates input data with default values.
     */
    @Test
    void tstCreateModifyInputWithDefaultValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createModifyInput("user:test:rwx");

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.empty(), inputData.getSet());
        assertEquals(Optional.of("user:test:rwx"), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    /**
     * Verify createModifyInput creates input data with abort and links values.
     */
    @Test
    void tstCreateModifyInputWithAbortAndLinksValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createModifyInput(
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

    /**
     * Verify createSetInput creates input data with default values.
     */
    @Test
    void tstCreateSetInputWithDefaultValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createSetInput("user:test:rwx");

        assertFalse(inputData.isAbort());
        assertEquals(Optional.of(LinkType.FOLLOW), inputData.getLinks());
        assertEquals(Optional.empty(), inputData.getDeleteType());
        assertEquals(Optional.of("user:test:rwx"), inputData.getSet());
        assertEquals(Optional.empty(), inputData.getModify());
        assertEquals(Optional.empty(), inputData.getDelete());
    }

    /**
     * Verify createSetInput creates input data with abort and links values.
     */
    @Test
    void tstCreateSetInputWithAbortAndLinksValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createSetInput(
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

    /**
     * Verify createModifyDeleteInput creates input data with default values.
     */
    @Test
    void tstCreateModifyDeleteInputWithDefaultValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createModifyDeleteInput(
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

    /**
     * Verify createModifyDeleteInput creates input data with abort and links values.
     */
    @Test
    void tstCreateModifyDeleteInputWithAbortAndLinksValuesSuccess() {
        final SetAclInputFactory factory = new SetAclInputFactory();

        final UssSetAclInputData inputData = factory.createModifyDeleteInput(
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

    /**
     * Verify builder creates delete-type input data.
     */
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

    /**
     * Verify builder creates set input data.
     */
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

    /**
     * Verify builder creates modify input data.
     */
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

    /**
     * Verify builder creates delete input data.
     */
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

    /**
     * Verify builder creates modify and delete input data.
     */
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

    /**
     * Verify builder rejects null deleteType value.
     */
    @Test
    void tstBuilderRejectsNullDeleteTypeValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setDeleteType(null)
        );
    }

    /**
     * Verify builder rejects null set value.
     */
    @Test
    void tstBuilderRejectsNullSetValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setSet(null)
        );
    }

    /**
     * Verify builder rejects blank set value.
     */
    @Test
    void tstBuilderRejectsBlankSetValueFailure() {
        assertThrows(IllegalArgumentException.class, () ->
                new UssSetAclInputData.Builder().setSet("")
        );
    }

    /**
     * Verify builder rejects null modify value.
     */
    @Test
    void tstBuilderRejectsNullModifyValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setModify(null)
        );
    }

    /**
     * Verify builder rejects blank modify value.
     */
    @Test
    void tstBuilderRejectsBlankModifyValueFailure() {
        assertThrows(IllegalArgumentException.class, () ->
                new UssSetAclInputData.Builder().setModify("")
        );
    }

    /**
     * Verify builder rejects null delete value.
     */
    @Test
    void tstBuilderRejectsNullDeleteValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setDelete(null)
        );
    }

    /**
     * Verify builder rejects blank delete value.
     */
    @Test
    void tstBuilderRejectsBlankDeleteValueFailure() {
        assertThrows(IllegalArgumentException.class, () ->
                new UssSetAclInputData.Builder().setDelete("")
        );
    }

    /**
     * Verify builder rejects null links value.
     */
    @Test
    void tstBuilderRejectsNullLinksValueFailure() {
        assertThrows(NullPointerException.class, () ->
                new UssSetAclInputData.Builder().setLinks(null)
        );
    }

}
