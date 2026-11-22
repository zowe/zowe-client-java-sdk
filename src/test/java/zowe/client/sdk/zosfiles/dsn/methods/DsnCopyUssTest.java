
/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.methods;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.types.FileType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for DsnCopy USS to data set functionality.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class DsnCopyUssTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");

    private PutJsonZosmfRequest mockPutRequest;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);

        Mockito.when(mockPutRequest.executeRequest()).thenReturn(
                new Response("{}", 200, "success"));

        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();
    }

    @Test
    public void tstDsnCopyUssToDatasetSequentialSuccess()
            throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        final Response response = dsnCopy.copyUssToDataset(
                "/u/test/file.txt", "TEST.SEQ");

        assertEquals("{}", response.getResponsePhrase().orElse("n/a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n/a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.SEQ",
                mockPutRequest.getUrl());
        assertCopyBody("/u/test/file.txt", "text", false);
    }

    @Test
    public void tstDsnCopyUssToDatasetMemberSuccess()
            throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        final Response response = dsnCopy.copyUssToDataset(
                "/u/test/file.txt", "TEST.PDS", "MEMBER");

        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.PDS(MEMBER)",
                mockPutRequest.getUrl());
        assertCopyBody("/u/test/file.txt", "text", false);
    }

    @Test
    public void tstDsnCopyUssToDatasetTextTypeSuccess()
            throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        final Response response = dsnCopy.copyUssToDataset(
                "/u/test/file.txt", "TEST.PDS", "MEMBER", FileType.TEXT);

        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.PDS(MEMBER)",
                mockPutRequest.getUrl());
        assertCopyBody("/u/test/file.txt", "text", false);
    }

    @Test
    public void tstDsnCopyUssToDatasetBinaryTypeSuccess()
            throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        final Response response = dsnCopy.copyUssToDataset(
                "/u/test/file.bin", "TEST.SEQ", null, FileType.BINARY);

        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.SEQ",
                mockPutRequest.getUrl());
        assertCopyBody("/u/test/file.bin", "binary", false);
    }

    @Test
    public void tstDsnCopyUssToDatasetExecutableTypeSuccess()
            throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        final Response response = dsnCopy.copyUssToDataset(
                "/u/test/program", "TEST.LOAD", "PROGRAM",
                FileType.EXECUTABLE);

        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.LOAD(PROGRAM)",
                mockPutRequest.getUrl());
        assertCopyBody("/u/test/program", "executable", false);
    }

    @Test
    public void tstDsnCopyUssToDatasetReplaceTrueSuccess()
            throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        final Response response = dsnCopy.copyUssToDataset(
                "/u/test/file.txt", "TEST.PDS", "MEMBER",
                FileType.TEXT, true);

        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.PDS(MEMBER)",
                mockPutRequest.getUrl());
        assertCopyBody("/u/test/file.txt", "text", true);
    }

    @Test
    public void tstDsnCopyUssToDatasetReplaceFalseSuccess()
            throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        final Response response = dsnCopy.copyUssToDataset(
                "/u/test/file.txt", "TEST.PDS", "MEMBER",
                FileType.TEXT, false);

        assertEquals(200, response.getStatusCode().orElse(-1));
        assertCopyBody("/u/test/file.txt", "text", false);
    }

    @Test
    public void tstDsnCopyUssToDatasetNullFromFile() throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        assertThrows(RuntimeException.class,
                () -> dsnCopy.copyUssToDataset(null, "TEST.SEQ"));

        verify(mockPutRequest, never()).executeRequest();
    }

    @Test
    public void tstDsnCopyUssToDatasetBlankFromFile() throws ZosmfRequestException {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        assertThrows(IllegalArgumentException.class,
                () -> dsnCopy.copyUssToDataset(" ", "TEST.SEQ"));

        verify(mockPutRequest, never()).executeRequest();
    }

    @Test
    public void tstDsnCopyUssToDatasetRelativeFromFile() {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dsnCopy.copyUssToDataset("test/file.txt", "TEST.SEQ"));

        assertEquals("fromFile must be an absolute USS path",
                exception.getMessage());
    }

    @Test
    public void tstDsnCopyUssToDatasetNullToDataset() {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);
        assertThrows(RuntimeException.class,
                () -> dsnCopy.copyUssToDataset("/u/test/file.txt", null));
    }

    @Test
    public void tstDsnCopyUssToDatasetBlankToDataset() {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);

        assertThrows(IllegalArgumentException.class,
                () -> dsnCopy.copyUssToDataset("/u/test/file.txt", " "));
    }

    @Test
    public void tstDsnCopyUssToDatasetBlankMember() {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);
        assertThrows(IllegalArgumentException.class,
                () -> dsnCopy.copyUssToDataset(
                        "/u/test/file.txt", "TEST.PDS", " "));
    }

    @Test
    public void tstDsnCopyUssToDatasetNullFileType() {
        final DsnCopy dsnCopy = new DsnCopy(connection, mockPutRequest);
        assertThrows(NullPointerException.class,
                () -> dsnCopy.copyUssToDataset(
                        "/u/test/file.txt", "TEST.SEQ", null, null));
    }

    /**
     * Verify the USS source file, type, and replace option
     * in the JSON request body.
     */
    private void assertCopyBody(final String filename,
                                final String type,
                                final boolean replace) throws ZosmfRequestException {

        final ArgumentCaptor<String> bodyCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(mockPutRequest).setBody(bodyCaptor.capture());

        final String body = bodyCaptor.getValue();

        assertTrue(body.contains("\"request\":\"copy\""));
        assertTrue(body.contains("\"from-file\""));
        assertTrue(body.contains("\"filename\":\"" + filename + "\""));
        assertTrue(body.contains("\"type\":\"" + type + "\""));
        assertTrue(body.contains("\"replace\":" + replace));
        assertFalse(body.contains("\"from-dataset\""));

        verify(mockPutRequest, times(1)).executeRequest();
    }

}
