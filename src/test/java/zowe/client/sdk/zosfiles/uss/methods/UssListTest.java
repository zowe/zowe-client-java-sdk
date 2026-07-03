/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.methods;

import com.fasterxml.jackson.databind.JsonNode;
import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.UssListInputData;
import zowe.client.sdk.zosfiles.uss.input.UssListZfsInputData;
import zowe.client.sdk.zosfiles.uss.model.UnixFile;
import zowe.client.sdk.zosfiles.uss.model.UnixZfs;
import zowe.client.sdk.zosfiles.uss.types.ListFilterType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssList.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class UssListTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private GetJsonZosmfRequest mockJsonGetRequest;
    private GetJsonZosmfRequest mockJsonGetRequestToken;
    private UssList ussList;
    private final static String dataForFileList = "{\n" +
            "   \"items\": [\n" +
            "      {\n" +
            "         \"name\": \"test\",\n" +
            "         \"mode\": \"drwxr-xr-x\",\n" +
            "         \"size\": 0,\n" +
            "         \"uid\": 10000518,\n" +
            "         \"user\": \"user\",\n" +
            "         \"gid\": 8,\n" +
            "         \"group\": \"FRAMEWKG\",\n" +
            "         \"mtime\": \"2022-11-03T10:48:32\",\n" +
            "         \"target\": \"target\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"name\": \"test2\",\n" +
            "         \"mode\": \"-rwxr-xr-x\",\n" +
            "         \"size\": 13545,\n" +
            "         \"uid\": 10000518,\n" +
            "         \"user\": \"user2\",\n" +
            "         \"gid\": 8,\n" +
            "         \"group\": \"FRAMEWKG\",\n" +
            "         \"mtime\": \"2022-11-12T15:20:11\",\n" +
            "         \"target\": \"target\"\n" +
            "      }\n" +
            "   ],\n" +
            "   \"returnedRows\": 7,\n" +
            "   \"totalRows\": 7,\n" +
            "   \"JSONversion\": 1\n" +
            "}";
    private final static String partialDataForFileList = "{\n" +
            "   \"items\": [\n" +
            "      {\n" +
            "         \"size\": 0\n" +
            "      },\n" +
            "      {\n" +
            "         \"name\": \"test2\"\n" +
            "      }\n" +
            "   ],\n" +
            "   \"returnedRows\": 7,\n" +
            "   \"totalRows\": 7,\n" +
            "   \"JSONversion\": 1\n" +
            "}";
    private final static String dataForZfsList = "{\n" +
            "   \"items\": [\n" +
            "      {\n" +
            "         \"name\": \"OMVSGRP.USER.TNGFW.CA31\",\n" +
            "         \"mountpoint\": \"/CA31/u/users/framewrk\",\n" +
            "         \"fstname\": \"ZFS\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"mode\": [\n" +
            "            \"noautomove\",\n" +
            "            \"unmount\",\n" +
            "            \"acl\",\n" +
            "            \"synchonly\"\n" +
            "         ],\n" +
            "         \"dev\": 2718,\n" +
            "         \"fstype\": 1,\n" +
            "         \"bsize\": 1024,\n" +
            "         \"bavail\": 269231,\n" +
            "         \"blocks\": 1382400,\n" +
            "         \"sysname\": \"CA31\",\n" +
            "         \"readibc\": 907651,\n" +
            "         \"writeibc\": 42,\n" +
            "         \"diribc\": 453057\n" +
            "      }\n" +
            "   ],\n" +
            "   \"JSONversion\": 1\n" +
            "}";

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        mockJsonGetRequestToken = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        mockJsonGetRequestToken = Mockito.mock(GetJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonGetRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).getUrl();

        ussList = new UssList(connection);
    }

    @Test
    public void tstUssListFileListEmptyResponseToggleTokenSuccess() throws Exception {
        final UssList ussList = new UssList(tokenConnection, mockJsonGetRequestToken);
        List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonGetRequestToken.getHeaders().toString());
        // should only contain two items
        assertEquals(0, items.size());
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequestToken.getUrl());
    }

    @Test
    public void tstUssListFileListNullResponseFailure() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(null, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        String msg = "";
        try {
            ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ZosFilesConstants.RESPONSE_PHRASE_ERROR, msg);
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListFileListSuccess() throws ZosmfRequestException {
        final JsonNode json = JsonUtils.parse(dataForFileList);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
        // should only contain two items
        assertEquals(2, items.size());
        // verify first item's data
        assertEquals("test", items.get(0).getName());
        assertEquals("drwxr-xr-x", items.get(0).getMode());
        assertEquals(0, items.get(0).getSize());
        assertEquals(10000518, items.get(0).getUid());
        assertEquals("user", items.get(0).getUser());
        assertEquals(8, items.get(0).getGid());
        assertEquals("FRAMEWKG", items.get(0).getGroup());
        assertEquals("2022-11-03T10:48:32", items.get(0).getMtime());
        assertEquals("target", items.get(0).getTarget());
        // verify second item's data
        assertEquals("test2", items.get(1).getName());
        assertEquals("-rwxr-xr-x", items.get(1).getMode());
        assertEquals(13545, items.get(1).getSize());
        assertEquals(10000518, items.get(1).getUid());
        assertEquals("user2", items.get(1).getUser());
        assertEquals(8, items.get(1).getGid());
        assertEquals("FRAMEWKG", items.get(1).getGroup());
        assertEquals("2022-11-12T15:20:11", items.get(1).getMtime());
        assertEquals("target", items.get(1).getTarget());
    }

    @Test
    public void tstUssListFileListPartialSuccess() throws ZosmfRequestException {
        final JsonNode json = JsonUtils.parse(partialDataForFileList);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
        // should only contain two items
        assertEquals(2, items.size());
        // verify first item's data
        assertTrue(items.get(0).getName().isEmpty());
        assertTrue(items.get(0).getMode().isEmpty());
        assertEquals(0, items.get(0).getSize());
        assertEquals(0, items.get(0).getUid());
        assertTrue(items.get(0).getUser().isEmpty());
        assertEquals(0, items.get(0).getGid());
        assertTrue(items.get(0).getGroup().isEmpty());
        assertTrue(items.get(0).getMtime().isEmpty());
        assertTrue(items.get(0).getTarget().isEmpty());
        // verify second item's data
        assertEquals("test2", items.get(1).getName());
        assertTrue(items.get(1).getMode().isEmpty());
        assertEquals(0, items.get(1).getSize());
        final String toStr = "UnixFile{name=test2, mode=, size=0, uid=0, user=, gid=0, group=, mtime=, target=}";
        assertEquals(toStr, items.get(1).toString());
        assertTrue(items.get(1).getUser().isEmpty());
        assertEquals(0, items.get(1).getGid());
        assertTrue(items.get(1).getGroup().isEmpty());
        assertTrue(items.get(1).getMtime().isEmpty());
        assertTrue(items.get(1).getTarget().isEmpty());
    }

    @Test
    public void tstUssListEmptyFileListSuccess() throws ZosmfRequestException {
        final JsonNode json = JsonUtils.parse("{}");
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());
        assertEquals(0, items.size());
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListEmptyFileListWithJsonObjectSuccess() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder().path("/xxx/xx/x").build());
        assertEquals(0, items.size());
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListZfsListSuccess() throws ZosmfRequestException {
        final JsonNode json = JsonUtils.parse(dataForZfsList);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixZfs> items = ussList.getZfsSystems(new UssListZfsInputData.Builder().path("/xxx/xx/x").build());
        // should only contain one item
        assertEquals(1, items.size());
        // verify first item's data
        assertEquals("OMVSGRP.USER.TNGFW.CA31", items.get(0).getName());
        assertEquals("/CA31/u/users/framewrk", items.get(0).getMountpoint());
        assertEquals("ZFS", items.get(0).getFstname());
        assertEquals("noautomove,unmount,acl,synchonly", items.get(0).getMode());
        assertEquals(2718, items.get(0).getDev());
        assertEquals(1, items.get(0).getFstype());
        assertEquals(1024, items.get(0).getBsize());
        assertEquals(269231, items.get(0).getBavail());
        assertEquals(1382400, items.get(0).getBlocks());
        assertEquals("CA31", items.get(0).getSysname());
        assertEquals(907651, items.get(0).getReadibc());
        assertEquals(42, items.get(0).getWriteibc());
        assertEquals(453057, items.get(0).getDiribc());
        assertEquals("https://1:443/zosmf/restfiles/mfs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListFileListParamsNullFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("listInputData is null", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathEmptyFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(new UssListInputData.Builder().path("").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListFileListParamsInvalidPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(new UssListInputData.Builder().path("hello").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathEmptyWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(new UssListInputData.Builder().path("    ").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathNullFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(new UssListInputData.Builder().path(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path is null", errMsg);
    }

    @Test
    public void tstUssListZfsListEmptyParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("no path or fsname specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameEmptyFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().fsname("").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameEmptyWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().fsname("    ").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameNullFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().fsname(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname is null", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathAndFsnameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().path("p").fsname("p").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("cannot specify both path and fsname parameters", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathEmptyFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().path("").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsInvalidPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().path("hello").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathEmptyWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().path("   ").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathNullFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new UssListZfsInputData.Builder().path(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path is null", errMsg);
    }

    @Test
    public void tstUssListNullConnectionFailure() {
        try {
            new UssList(null);
        } catch (NullPointerException e) {
            assertEquals("connection is null", e.getMessage());
        }
    }

    @Test
    public void tstUssListSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        UssList ussList = new UssList(connection, request);
        assertNotNull(ussList);
    }

    @Test
    public void tstUssListSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssList(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstUssListSecondaryConstructorWithNullRequestFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssList(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstUssListSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a GetJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new UssList(connection, request)
        );
        assertEquals("GET_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstUssListFileListWithAllOptionalParamsSuccess() throws Exception {
        Mockito.when(mockJsonGetRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequestToken);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .group("FRAMEWKG")
                .user("mvsuser")
                .mtime("+7")
                .size(1024)
                .name("*.txt")
                .perm("755")
                .type(ListFilterType.DIRECTORY)
                .depth(2)
                .filesys(true)
                .symlinks(true)
                .maxLength(10)
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequestToken.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&group=FRAMEWKG&user=mvsuser&mtime=%2B7&size=1024&name=*.txt&perm=755&type=f&depth=2&filesys=all&symlinks=report", url);
        String headers = mockJsonGetRequestToken.getHeaders().toString();
        assertTrue(headers.contains("X-IBM-Max-Items=10"), "Headers should contain X-IBM-Max-Items=10: " + headers);
    }

    @Test
    public void tstUssListFileListWithSizeAndTypeBothSetSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .size(500)
                .type(ListFilterType.FILE)
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&size=500&type=f", url);
    }

    @Test
    public void tstUssListFileListWithOnlyTypeNoSizeSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .type(ListFilterType.SYMBOLIC_LINK)
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&type=l", url);
    }

    @Test
    public void tstUssListFileListWithFilesysTrueSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .filesys(true)
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&filesys=all", url);
    }

    @Test
    public void tstUssListFileListWithSymlinksTrueSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .symlinks(true)
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&symlinks=report", url);
    }

    @Test
    public void tstUssListFileListWithMaxLengthSuccess() throws Exception {
        Mockito.when(mockJsonGetRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequestToken);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .maxLength(5)
                .build());

        assertEquals(0, items.size());
        String headers = mockJsonGetRequestToken.getHeaders().toString();
        assertTrue(headers.contains("X-IBM-Max-Items=5"), "Headers should contain X-IBM-Max-Items=5: " + headers);
    }

    @Test
    public void tstUssListZfsListWithFsnameSuccess() throws Exception {
        final JsonNode json = JsonUtils.parse(dataForZfsList);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixZfs> items = ussList.getZfsSystems(new UssListZfsInputData.Builder()
                .fsname("OMVSGRP.USER.TNGFW.CA31")
                .build());

        assertEquals(1, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/mfs?fsname=OMVSGRP.USER.TNGFW.CA31", url);
    }

    @Test
    public void tstUssListZfsListWithMaxLengthSuccess() throws Exception {
        final JsonNode json = JsonUtils.parse(dataForZfsList);
        Mockito.when(mockJsonGetRequestToken.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        doCallRealMethod().when(mockJsonGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequestToken);
        final List<UnixZfs> items = ussList.getZfsSystems(new UssListZfsInputData.Builder()
                .path("/usr/lpp")
                .maxLength(20)
                .build());

        assertEquals(1, items.size());
        String headers = mockJsonGetRequestToken.getHeaders().toString();
        assertTrue(headers.contains("X-IBM-Max-Items=20"), "Headers should contain X-IBM-Max-Items=20: " + headers);
    }

    @Test
    public void tstUssListZfsListNullInputDataFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> ussList.getZfsSystems(null)
        );
        assertEquals("listZfsInputData is null", exception.getMessage());
    }

    @Test
    public void tstUssListFileListWithOnlyGroupSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .group("FRAMEWKG")
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&group=FRAMEWKG", url);
    }

    @Test
    public void tstUssListFileListWithOnlyUserSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .user("mvsuser")
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&user=mvsuser", url);
    }

    @Test
    public void tstUssListFileListWithOnlyMtimeSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .mtime("-30")
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&mtime=-30", url);
    }

    @Test
    public void tstUssListFileListWithOnlySizeSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .size(2048)
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&size=2048", url);
    }

    @Test
    public void tstUssListFileListWithOnlyNameSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .name("test.txt")
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&name=test.txt", url);
    }

    @Test
    public void tstUssListFileListWithOnlyPermSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .perm("777")
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&perm=777", url);
    }

    @Test
    public void tstUssListFileListWithOnlyDepthSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new UssListInputData.Builder()
                .path("/usr/lpp")
                .depth(3)
                .build());

        assertEquals(0, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/fs?path=%2Fusr%2Flpp&depth=3", url);
    }

    @Test
    public void tstUssListZfsListWithPathSuccess() throws Exception {
        final JsonNode json = JsonUtils.parse(dataForZfsList);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixZfs> items = ussList.getZfsSystems(new UssListZfsInputData.Builder()
                .path("/usr/lpp")
                .build());

        assertEquals(1, items.size());
        String url = mockJsonGetRequest.getUrl();
        assertEquals("https://1:443/zosmf/restfiles/mfs?path=%2Fusr%2Flpp", url);
    }

    @Test
    public void tstUssListZfsListEmptyResponseSuccess() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixZfs> items = ussList.getZfsSystems(new UssListZfsInputData.Builder()
                .path("/usr/lpp")
                .build());

        assertEquals(0, items.size());
    }

    @Test
    public void tstUssListFileListNullInputDataAssertThrowsFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> ussList.getFiles(null)
        );
        assertEquals("listInputData is null", exception.getMessage());
    }

    @Test
    public void tstUssListFileListMissingPathThrowsFailure() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final UssListInputData input = new UssListInputData.Builder().build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ussList.getFiles(input));
        assertEquals("path not specified", ex.getMessage());
    }

    @Test
    public void tstUssListZfsListZeroMaxLengthNoHeaderSuccess() throws Exception {
        final JsonNode json = JsonUtils.parse(dataForZfsList);
        Mockito.when(mockJsonGetRequestToken.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        doCallRealMethod().when(mockJsonGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequestToken);
        final List<UnixZfs> items = ussList.getZfsSystems(new UssListZfsInputData.Builder()
                .path("/usr/lpp")
                .maxLength(0)
                .build());

        assertEquals(1, items.size());
        String headers = mockJsonGetRequestToken.getHeaders().toString();
        assertFalse(headers.contains("X-IBM-Max-Items"),
                "Headers must NOT contain X-IBM-Max-Items when maxLength is 0: " + headers);
    }

    @Test
    public void tstUssListZfsListNullResponsePhraseThrowsFailure() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequestToken.executeRequest()).thenReturn(
                new Response(null, 200, "success"));
        doCallRealMethod().when(mockJsonGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).getUrl();

        final UssList ussList = new UssList(connection, mockJsonGetRequestToken);
        final UssListZfsInputData input = new UssListZfsInputData.Builder()
                .path("/usr/lpp")
                .build();

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> ussList.getZfsSystems(input));
        assertEquals(ZosFilesConstants.RESPONSE_PHRASE_ERROR, ex.getMessage());
    }

}
