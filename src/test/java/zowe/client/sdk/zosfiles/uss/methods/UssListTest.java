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

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.ListParams;
import zowe.client.sdk.zosfiles.uss.input.ListZfsParams;
import zowe.client.sdk.zosfiles.uss.response.UnixFile;
import zowe.client.sdk.zosfiles.uss.response.UnixZfs;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssList.
 *
 * @author Frank Giordano
 * @version 4.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssListTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
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
            "         \"mtime\": \"2022-11-03T10:48:32\"\n" +
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
            "         \"mtime\": \"2022-11-12T15:20:11\"\n" +
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
            "         \"size\": 0,\n" +
            "      },\n" +
            "      {\n" +
            "         \"name\": \"test2\",\n" +
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

    @Before
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
    public void tstUssListFileListEmptyResponseSuccess() throws Exception {
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new ListParams.Builder().path("/xxx/xx/x").build());
        // should only contain two items
        assertEquals(0, items.size());
        assertEquals("https://1:1/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListFileListEmptyResponseToggleTokenSuccess() throws Exception {
        final UssList ussList = new UssList(tokenConnection, mockJsonGetRequestToken);
        List<UnixFile> items = ussList.getFiles(new ListParams.Builder().path("/xxx/xx/x").build());
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonGetRequestToken.getHeaders().toString());
        // should only contain two items
        assertEquals(0, items.size());
        assertEquals("https://1:1/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequestToken.getUrl());
    }

    @Test
    public void tstUssListFileListNullResponseFailure() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(null, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        String msg = "";
        try {
            ussList.getFiles(new ListParams.Builder().path("/xxx/xx/x").build());
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(ZosFilesConstants.RESPONSE_PHRASE_ERROR, msg);
        assertEquals("https://1:1/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListFileListSuccess() throws ZosmfRequestException {
        final JSONObject json = JsonParserUtil.parse(dataForFileList);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new ListParams.Builder().path("/xxx/xx/x").build());
        assertEquals("https://1:1/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
        // should only contain two items
        assertEquals(2, items.size());
        // verify first item's data
        assertEquals("test", items.get(0).getName().orElse("n\\a"));
        assertEquals("drwxr-xr-x", items.get(0).getMode().orElse("n\\a"));
        assertEquals(0, items.get(0).getSize().orElse(-1));
        assertEquals(10000518, items.get(0).getUid().orElse(-1));
        assertEquals("user", items.get(0).getUser().orElse("n\\a"));
        assertEquals(8, items.get(0).getGid().orElse(-1));
        assertEquals("FRAMEWKG", items.get(0).getGroup().orElse("n\\a"));
        assertEquals("2022-11-03T10:48:32", items.get(0).getMtime().orElse("n\\a"));
        assertEquals("target", items.get(0).getTarget().orElse("n\\a"));
        // verify second item's data
        assertEquals("test2", items.get(1).getName().orElse("n\\a"));
        assertEquals("-rwxr-xr-x", items.get(1).getMode().orElse("n\\a"));
        assertEquals(13545, items.get(1).getSize().orElse(-1));
        assertEquals(10000518, items.get(1).getUid().orElse(-1));
        assertEquals("user2", items.get(1).getUser().orElse("n\\a"));
        assertEquals(8, items.get(1).getGid().orElse(-1));
        assertEquals("FRAMEWKG", items.get(1).getGroup().orElse("n\\a"));
        assertEquals("2022-11-12T15:20:11", items.get(1).getMtime().orElse("n\\a"));
        assertEquals("target", items.get(1).getTarget().orElse("n\\a"));
    }

    @Test
    public void tstUssListFileListPartialSuccess() throws ZosmfRequestException {
        final JSONObject json = JsonParserUtil.parse(partialDataForFileList);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new ListParams.Builder().path("/xxx/xx/x").build());
        assertEquals("https://1:1/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
        // should only contain two items
        assertEquals(2, items.size());
        // verify first item's data
        assertTrue(items.get(0).getName().isEmpty());
        assertTrue(items.get(0).getMode().isEmpty());
        assertEquals(0, items.get(0).getSize().orElse(-1));
        assertTrue(items.get(0).getUid().isEmpty());
        assertTrue(items.get(0).getUser().isEmpty());
        assertTrue(items.get(0).getGid().isEmpty());
        assertTrue(items.get(0).getGroup().isEmpty());
        assertTrue(items.get(0).getMtime().isEmpty());
        assertTrue(items.get(0).getTarget().isEmpty());
        // verify second item's data
        assertEquals("test2", items.get(1).getName().orElse("n\\a"));
        assertTrue(items.get(1).getMode().isEmpty());
        assertEquals(-1, items.get(1).getSize().orElse(-1));
        assertTrue(items.get(1).getUid().isEmpty());
        assertTrue(items.get(1).getUser().isEmpty());
        assertTrue(items.get(1).getGid().isEmpty());
        assertTrue(items.get(1).getGroup().isEmpty());
        assertTrue(items.get(1).getMtime().isEmpty());
        assertTrue(items.get(1).getTarget().isEmpty());
    }

    @Test
    public void tstUssListEmptyFileListSuccess() throws ZosmfRequestException {
        final JSONObject json = JsonParserUtil.parse("{}");
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new ListParams.Builder().path("/xxx/xx/x").build());
        assertEquals(0, items.size());
        assertEquals("https://1:1/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListEmptyFileListWithJsonObjectSuccess() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixFile> items = ussList.getFiles(new ListParams.Builder().path("/xxx/xx/x").build());
        assertEquals(0, items.size());
        assertEquals("https://1:1/zosmf/restfiles/fs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListZfsListSuccess() throws ZosmfRequestException {
        final JSONObject json = JsonParserUtil.parse(dataForZfsList);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final UssList ussList = new UssList(connection, mockJsonGetRequest);
        final List<UnixZfs> items = ussList.getZfsSystems(new ListZfsParams.Builder().path("/xxx/xx/x").build());
        // should only contain one item
        assertEquals(1, items.size());
        // verify first item's data
        assertEquals("OMVSGRP.USER.TNGFW.CA31", items.get(0).getName().orElse("n\\a"));
        assertEquals("/CA31/u/users/framewrk", items.get(0).getMountpoint().orElse("n\\a"));
        assertEquals("ZFS", items.get(0).getFstname().orElse("n\\a"));
        assertEquals("noautomove,unmount,acl,synchonly", items.get(0).getMode().orElse("n\\a"));
        assertEquals(2718, items.get(0).getDev().orElse(-1));
        assertEquals(1, items.get(0).getFstype().orElse(-1));
        assertEquals(1024, items.get(0).getBsize().orElse(-1));
        assertEquals(269231, items.get(0).getBavail().orElse(-1));
        assertEquals(1382400, items.get(0).getBlocks().orElse(-1));
        assertEquals("CA31", items.get(0).getSysname().orElse("n\\a"));
        assertEquals(907651, items.get(0).getReadibc().orElse(-1));
        assertEquals(42, items.get(0).getWriteibc().orElse(-1));
        assertEquals(453057, items.get(0).getDiribc().orElse(-1));
        assertEquals("https://1:1/zosmf/restfiles/mfs?path=%2Fxxx%2Fxx%2Fx", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstUssListFileListParamsNullFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathEmptyFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(new ListParams.Builder().path("").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListFileListParamsInvalidPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(new ListParams.Builder().path("hello").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathEmptyWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(new ListParams.Builder().path("    ").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathNullFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getFiles(new ListParams.Builder().path(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path is null", errMsg);
    }

    @Test
    public void tstUssListZfsListEmptyParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("no path or fsname specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameEmptyFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().fsname("").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameEmptyWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().fsname("    ").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameNullFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().fsname(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname is null", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathAndFsnameFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().path("p").fsname("p").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("cannot specify both path and fsname parameters", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathEmptyFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().path("").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsInvalidPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().path("hello").build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathEmptyWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().path("   ").build());
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathNullFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussList.getZfsSystems(new ListZfsParams.Builder().path(null).build());
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("path is null", errMsg);
    }

    @Test
    public void tstNullConnectionFailure() {
        try {
            new UssList(null);
        } catch (NullPointerException e) {
            assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

}
