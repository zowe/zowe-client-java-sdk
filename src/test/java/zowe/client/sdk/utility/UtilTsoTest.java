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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zostso.zosmf.ZosmfTsoResponse;

import java.util.HashMap;

import static org.junit.Assert.*;

public class UtilTsoTest {

    private static final String MISSING_FIELD_ERROR_MSG = "missing one of the following json field values: queueID," +
            " ver, servletKey, reused and timeout";

    @Test
    public void tstParseJsonStopResponseSuccess() {
        var jsonMap = new HashMap<String, Object>();
        jsonMap.put("ver", "ver");
        jsonMap.put("servletKey", "servletKey");
        jsonMap.put("reused", true);
        jsonMap.put("timeout", true);
        var json = new JSONObject(jsonMap);

        ZosmfTsoResponse response = UtilTso.parseJsonStopResponse(json);
        assertEquals("ver", response.getVer().get());
        assertEquals("servletKey", response.getServletKey().get());
        assertTrue(response.getReused().get());
        assertTrue(response.getTimeout().get());
    }

    @Test
    public void tstParseJsonStopResponseNullFail() {
        JSONObject json = null;
        String msg = null;
        try {
            //noinspection ConstantConditions
            UtilTso.parseJsonStopResponse(json);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("no obj to parse", msg);
    }

    @Test
    public void tstGetZosmfTsoResponseNullFail() {
        Response response = null;
        String msg = null;
        try {
            //noinspection ConstantConditions
            UtilTso.getZosmfTsoResponse(response);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("response is null", msg);
    }

    @Test
    public void tstGetZosmfTsoResponseResultsNullStatusCodeErrorFail() {
        Response response = new Response(null, 10);
        String msg = null;
        try {
            UtilTso.getZosmfTsoResponse(response);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("results not available", msg);
    }

    @Test
    public void tstGetZosmfTsoResponseResultsNullStatusCodeNonErrorFail() {
        Response response = new Response(null, 200);
        String msg = null;
        try {
            UtilTso.getZosmfTsoResponse(response);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("no results to parse", msg);
    }

    @Test
    public void tstGetZosmfTsoResponseStatusCodeFail() throws Exception {
        Response response = new Response("error", 10);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        String errorValue = zosmfTsoResponse.getMsgData().get(0).getMessageText().get();
        assertEquals("error", errorValue);
    }

    @Test
    public void tstGetZosmfTsoResponseMissingQueueIDJsonFieldFail() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"tsoData\":" +
                "[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":\"--> LOGON proc version = 04/28/2011\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        String msg = null;
        try {
            UtilTso.getZosmfTsoResponse(response);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(MISSING_FIELD_ERROR_MSG, msg);
    }

    @Test
    public void tstGetZosmfTsoResponseMissingVerJsonFieldFail() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"queueID\":\"0100\",\"tsoData\":" +
                "[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":\"--> LOGON proc version = 04/28/2011\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        String msg = null;
        try {
            UtilTso.getZosmfTsoResponse(response);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(MISSING_FIELD_ERROR_MSG, msg);
    }

    @Test
    public void tstGetZosmfTsoResponseMissingServletKeyJsonFieldFail() throws Exception {
        String json = "{\"ver\":\"0100\",\"queueID\":\"0100\",\"tsoData\":\n" +
                "[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":\"--> LOGON proc version = 04/28/2011\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        String msg = null;
        try {
            UtilTso.getZosmfTsoResponse(response);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(MISSING_FIELD_ERROR_MSG, msg);
    }

    @Test
    public void tstGetZosmfTsoResponseMissingTsoDataJsonFieldFailWithEmptyResults() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true}";
        Response response = new Response(new JSONParser().parse(json), 200);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertTrue(zosmfTsoResponse.getTsoData().isEmpty());
    }

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOMessageOnlySuccess() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":" +
                "\"--> LOGON proc version = 04/28/2011\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertFalse(zosmfTsoResponse.getTsoData().isEmpty());
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty());
        assertTrue(zosmfTsoResponse.getTimeout().get());
        assertTrue(zosmfTsoResponse.getReused().get());
        assertEquals("0100", zosmfTsoResponse.getVer().get());
        assertEquals("0100", zosmfTsoResponse.getQueueId().get());
        assertEquals("ZOSMFAD-71-aabcaaaf", zosmfTsoResponse.getServletKey().get());
        assertFalse(zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().isPresent());
        assertFalse(zosmfTsoResponse.getTsoData().get(0).getTsoResponse().isPresent());
        assertEquals("0100", zosmfTsoResponse.getTsoData().get(0).getTsoMessage().get().getVersion().get());
        assertEquals("--> LOGON proc version = 04/28/2011", zosmfTsoResponse.getTsoData().get(0).getTsoMessage().get().getData().get());
    }

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOPromptOnlySuccess() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":" +
                "\"--> LOGON proc version = 04/28/2011\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertFalse(zosmfTsoResponse.getTsoData().isEmpty());
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty());
        assertTrue(zosmfTsoResponse.getTimeout().get());
        assertTrue(zosmfTsoResponse.getReused().get());
        assertEquals("0100", zosmfTsoResponse.getVer().get());
        assertEquals("0100", zosmfTsoResponse.getQueueId().get());
        assertEquals("ZOSMFAD-71-aabcaaaf", zosmfTsoResponse.getServletKey().get());
        assertTrue(zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().isPresent());
        assertFalse(zosmfTsoResponse.getTsoData().get(0).getTsoResponse().isPresent());
        assertEquals("0100", zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().get().getVersion().get());
        assertEquals("--> LOGON proc version = 04/28/2011", zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().get().getHidden().get());
    }

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOMessageAmdPromptSuccess() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":" +
                "\"--> LOGON proc version = 04/28/2011\"}},{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":" +
                "\"hidden\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertFalse(zosmfTsoResponse.getTsoData().isEmpty());
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty());
        assertTrue(zosmfTsoResponse.getTimeout().get());
        assertTrue(zosmfTsoResponse.getReused().get());
        assertEquals("0100", zosmfTsoResponse.getVer().get());
        assertEquals("0100", zosmfTsoResponse.getQueueId().get());
        assertEquals("ZOSMFAD-71-aabcaaaf", zosmfTsoResponse.getServletKey().get());
        assertTrue(zosmfTsoResponse.getTsoData().get(0).getTsoMessage().isPresent());
        assertFalse(zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().isPresent());
        assertFalse(zosmfTsoResponse.getTsoData().get(0).getTsoResponse().isPresent());
        assertFalse(zosmfTsoResponse.getTsoData().get(1).getTsoMessage().isPresent());
        assertTrue(zosmfTsoResponse.getTsoData().get(1).getTsoPrompt().isPresent());
        assertFalse(zosmfTsoResponse.getTsoData().get(1).getTsoResponse().isPresent());
        assertEquals("0100", zosmfTsoResponse.getTsoData().get(0).getTsoMessage().get().getVersion().get());
        assertEquals("--> LOGON proc version = 04/28/2011", zosmfTsoResponse.getTsoData().get(0).getTsoMessage().get().getData().get());
        assertEquals("0100", zosmfTsoResponse.getTsoData().get(1).getTsoPrompt().get().getVersion().get());
        assertEquals("hidden", zosmfTsoResponse.getTsoData().get(1).getTsoPrompt().get().getHidden().get());
    }

}
