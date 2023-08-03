/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonGetRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.input.ListParams;
import zowe.client.sdk.zosfiles.uss.input.ListZfsParams;
import zowe.client.sdk.zosfiles.uss.methods.UssList;
import zowe.client.sdk.zosfiles.uss.response.UssItem;
import zowe.client.sdk.zosfiles.uss.response.UssZfsItem;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for UssList.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssListTest {

    private ZosConnection connection;
    private JsonGetRequest jsonGetRequest;
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
    public void init() {
        connection = new ZosConnection("1", "1", "1", "1");
        jsonGetRequest = Mockito.mock(JsonGetRequest.class);
        ussList = new UssList(connection);
    }

    @Test
    public void tstUssListFileListSuccess() throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(dataForFileList);
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        UssList ussList = new UssList(connection, jsonGetRequest);
        List<UssItem> items = ussList.fileList(new ListParams.Builder().path("/xxx/xx/x").build());
        // should only contain two items
        assertEquals(2, items.size());
        // verify first item's data
        assertEquals("test", items.get(0).getName().get());
        assertEquals("drwxr-xr-x", items.get(0).getMode().get());
        assertEquals(0, items.get(0).getSize().getAsLong());
        assertEquals(10000518, items.get(0).getUid().getAsLong());
        assertEquals("user", items.get(0).getUser().get());
        assertEquals(8, items.get(0).getGid().getAsLong());
        assertEquals("FRAMEWKG", items.get(0).getGroup().get());
        assertEquals("2022-11-03T10:48:32", items.get(0).getMtime().get());
        // verify second item's data
        assertEquals("test2", items.get(1).getName().get());
        assertEquals("-rwxr-xr-x", items.get(1).getMode().get());
        assertEquals(13545, items.get(1).getSize().getAsLong());
        assertEquals(10000518, items.get(1).getUid().getAsLong());
        assertEquals("user2", items.get(1).getUser().get());
        assertEquals(8, items.get(1).getGid().getAsLong());
        assertEquals("FRAMEWKG", items.get(1).getGroup().get());
        assertEquals("2022-11-12T15:20:11", items.get(1).getMtime().get());
    }

    @Test
    public void tstUssListEmptyFileListSuccess() throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse("{}");
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        UssList ussList = new UssList(connection, jsonGetRequest);
        List<UssItem> items = ussList.fileList(new ListParams.Builder().path("/xxx/xx/x").build());
        assertEquals(0, items.size());
    }

    @Test
    public void tstUssListEmptyFileListWithJsonObjectSuccess() throws Exception {
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        UssList ussList = new UssList(connection, jsonGetRequest);
        List<UssItem> items = ussList.fileList(new ListParams.Builder().path("/xxx/xx/x").build());
        assertEquals(0, items.size());
    }

    @Test
    public void tstUssListZfsListSuccess() throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(dataForZfsList);
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        UssList ussList = new UssList(connection, jsonGetRequest);
        List<UssZfsItem> items = ussList.zfsList(new ListZfsParams.Builder().path("/xxx/xx/x").build());
        // should only contain two items
        assertEquals(1, items.size());
        // verify first item's data
        assertEquals("OMVSGRP.USER.TNGFW.CA31", items.get(0).getName().get());
        assertEquals("/CA31/u/users/framewrk", items.get(0).getMountpoint().get());
        assertEquals("ZFS", items.get(0).getFstname().get());
        assertEquals("noautomove, unmount, acl, synchonly", items.get(0).getMode().get());
        assertEquals(2718, items.get(0).getDev().getAsLong());
        assertEquals(1, items.get(0).getFstype().getAsLong());
        assertEquals(1024, items.get(0).getBsize().getAsLong());
        assertEquals(269231, items.get(0).getBavail().getAsLong());
        assertEquals(1382400, items.get(0).getBlocks().getAsLong());
        assertEquals("CA31", items.get(0).getSysname().get());
        assertEquals(907651, items.get(0).getReadibc().getAsLong());
        assertEquals(42, items.get(0).getWriteibc().getAsLong());
        assertEquals(453057, items.get(0).getDiribc().getAsLong());
    }

    @Test
    public void tstUssListFileListParamsNullFailure() {
        String errMsg = "";
        try {
            ussList.fileList(null);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathEmptyFailure() {
        String errMsg = "";
        try {
            ussList.fileList(new ListParams.Builder().path("").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathEmptyWithSpacesFailure() {
        String errMsg = "";
        try {
            ussList.fileList(new ListParams.Builder().path("    ").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListFileListParamsPathNullFailure() {
        String errMsg = "";
        try {
            ussList.fileList(new ListParams.Builder().path(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("path is null", errMsg);
    }

    @Test
    public void tstUssListZfsListEmptyParamsFailure() {
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("no path or fsname specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameEmptyFailure() {
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().fsname("").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameEmptyWithSpacesFailure() {
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().fsname("    ").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsFsnameNullFailure() {
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().fsname(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("fsname is null", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathAndFsnameFailure() {
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().path("p").fsname("p").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("cannot specify both path and fsname parameters", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathEmptyFailure() {
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().path("").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathEmptyWithSpacesFailure() {
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().path("   ").build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("path not specified", errMsg);
    }

    @Test
    public void tstUssListZfsListParamsPathNullFailure() {
        String errMsg = "";
        try {
            ussList.zfsList(new ListZfsParams.Builder().path(null).build());
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("path is null", errMsg);
    }

}
