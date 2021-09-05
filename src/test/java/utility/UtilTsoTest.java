/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import rest.Response;
import zostso.zosmf.ZosmfTsoResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

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
        assertTrue("ver".equals(response.getVer().get()));
        assertTrue("servletKey".equals(response.getServletKey().get()));
        assertTrue(response.getReused().get() == true);
        assertTrue(response.getTimeout().get() == true);
    }

    @Test
    public void tstParseJsonStopResponseNullFail() {
        JSONObject json = null;
        String msg = null;
        try {
            UtilTso.parseJsonStopResponse(json);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertTrue("no obj to parse".equals(msg));
    }

    @Test
    public void tstGetZosmfTsoResponseNullFail() {
        Response response = null;
        String msg = null;
        try {
            UtilTso.getZosmfTsoResponse(response);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertTrue("response is null".equals(msg));
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
        assertTrue("results not available".equals(msg));
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
        assertTrue("no results to parse".equals(msg));
    }

    @Test
    public void tstGetZosmfTsoResponseStatusCodeFail() throws Exception {
        Response response = new Response("error", 10);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        String errorValue = zosmfTsoResponse.getMsgData().get().get(0).getMessageText().get();
        assertTrue("error".equals(errorValue));
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
        assertTrue(msg.equals(MISSING_FIELD_ERROR_MSG));
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
        assertTrue(msg.equals(MISSING_FIELD_ERROR_MSG));
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
        assertTrue(msg.equals(MISSING_FIELD_ERROR_MSG));
    }

    @Test
    public void tstGetZosmfTsoResponseMissingTsoDataJsonFieldFailWithEmptyResults() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true}";
        Response response = new Response(new JSONParser().parse(json),200);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertTrue(zosmfTsoResponse.getTsoData().isEmpty() == true);
    }

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOMessageOnlySuccess() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":" +
                "\"--> LOGON proc version = 04/28/2011\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertTrue(zosmfTsoResponse.getTsoData().isEmpty() != true);
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty() == true);
        assertTrue(zosmfTsoResponse.getTimeout().get() == true);
        assertTrue(zosmfTsoResponse.getReused().get() == true);
        assertTrue("0100".equals(zosmfTsoResponse.getVer().get()));
        assertTrue("0100".equals(zosmfTsoResponse.getQueueId().get()));
        assertTrue("ZOSMFAD-71-aabcaaaf".equals(zosmfTsoResponse.getServletKey().get()));
        assertTrue(zosmfTsoResponse.getTsoData().get().get(0).getTsoPrompt().isPresent() == false);
        assertTrue(zosmfTsoResponse.getTsoData().get().get(0).getTsoResponse().isPresent() == false);
        assertTrue("0100".equals(zosmfTsoResponse.getTsoData().get().get(0).getTsoMessage().get().getVersion().get()));
        assertTrue("--> LOGON proc version = 04/28/2011".equals(
                zosmfTsoResponse.getTsoData().get().get(0).getTsoMessage().get().getData().get()));
    }

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOPromptOnlySuccess() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":" +
                "\"--> LOGON proc version = 04/28/2011\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertTrue(zosmfTsoResponse.getTsoData().isEmpty() != true);
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty() == true);
        assertTrue(zosmfTsoResponse.getTimeout().get() == true);
        assertTrue(zosmfTsoResponse.getReused().get() == true);
        assertTrue("0100".equals(zosmfTsoResponse.getVer().get()));
        assertTrue("0100".equals(zosmfTsoResponse.getQueueId().get()));
        assertTrue("ZOSMFAD-71-aabcaaaf".equals(zosmfTsoResponse.getServletKey().get()));
        assertTrue(zosmfTsoResponse.getTsoData().get().get(0).getTsoPrompt().isPresent() == true);
        assertTrue(zosmfTsoResponse.getTsoData().get().get(0).getTsoResponse().isPresent() == false);
        assertTrue("0100".equals(zosmfTsoResponse.getTsoData().get().get(0).getTsoPrompt().get().getVersion().get()));
        assertTrue("--> LOGON proc version = 04/28/2011".equals(
                zosmfTsoResponse.getTsoData().get().get(0).getTsoPrompt().get().getHidden().get()));
    }

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOMessageAmdPromptSuccess() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":" +
                "\"--> LOGON proc version = 04/28/2011\"}},{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":" +
                "\"hidden\"}}]}";
        Response response = new Response(new JSONParser().parse(json), 200);
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertTrue(zosmfTsoResponse.getTsoData().isEmpty() != true);
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty() == true);
        assertTrue(zosmfTsoResponse.getTimeout().get() == true);
        assertTrue(zosmfTsoResponse.getReused().get() == true);
        assertTrue("0100".equals(zosmfTsoResponse.getVer().get()));
        assertTrue("0100".equals(zosmfTsoResponse.getQueueId().get()));
        assertTrue("ZOSMFAD-71-aabcaaaf".equals(zosmfTsoResponse.getServletKey().get()));
        assertTrue(zosmfTsoResponse.getTsoData().get().get(0).getTsoMessage().isPresent() == true);
        assertTrue(zosmfTsoResponse.getTsoData().get().get(0).getTsoPrompt().isPresent() == false);
        assertTrue(zosmfTsoResponse.getTsoData().get().get(0).getTsoResponse().isPresent() == false);
        assertTrue(zosmfTsoResponse.getTsoData().get().get(1).getTsoMessage().isPresent() == false);
        assertTrue(zosmfTsoResponse.getTsoData().get().get(1).getTsoPrompt().isPresent() == true);
        assertTrue(zosmfTsoResponse.getTsoData().get().get(1).getTsoResponse().isPresent() == false);
        assertTrue("0100".equals(zosmfTsoResponse.getTsoData().get().get(0).getTsoMessage().get().getVersion().get()));
        assertTrue("--> LOGON proc version = 04/28/2011".equals(
                zosmfTsoResponse.getTsoData().get().get(0).getTsoMessage().get().getData().get()));
        assertTrue("0100".equals(zosmfTsoResponse.getTsoData().get().get(1).getTsoPrompt().get().getVersion().get()));
        assertTrue("hidden".equals(
                zosmfTsoResponse.getTsoData().get().get(1).getTsoPrompt().get().getHidden().get()));
    }

}
