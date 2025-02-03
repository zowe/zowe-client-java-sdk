/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso;

import org.junit.Test;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.service.TsoResponseService;

import static org.junit.Assert.*;

/**
 * Class containing unit tests for TsoResponseService.
 *
 * @author Frank Giordano
 * @version 2.0
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TsoResponseServiceTest {

    @Test
    public void tstGetZosmfTsoResponseMissingQueueIDJsonFieldFail() throws ZosmfRequestException {
        final String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"tsoData\":" +
                "[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":\"--> LOGON proc version = 04/28/2011\"}}]}";
        final Response mockResponse = new Response(JsonParserUtil.parse(json), 200, "success");
        final ZosmfTsoResponse zosmfTsoResponse = new TsoResponseService(mockResponse).getZosmfTsoResponse();
        assertTrue(zosmfTsoResponse.getQueueId().isEmpty());
    }

    @Test
    public void tstGetZosmfTsoResponseMissingServletKeyJsonFieldFail() throws ZosmfRequestException {
        final String json = "{\"ver\":\"0100\",\"queueID\":\"0100\",\"tsoData\":\n" +
                "[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":\"--> LOGON proc version = 04/28/2011\"}}]}";
        final Response mockResponse = new Response(JsonParserUtil.parse(json), 200, "success");
        final ZosmfTsoResponse zosmfTsoResponse = new TsoResponseService(mockResponse).getZosmfTsoResponse();
        assertTrue(zosmfTsoResponse.getServletKey().isEmpty());
    }

    @Test
    public void tstGetZosmfTsoResponseMissingTsoDataJsonFieldFailWithEmptyResults() throws ZosmfRequestException {
        final String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true}";
        final Response mockResponse = new Response(JsonParserUtil.parse(json), 200, "success");
        final ZosmfTsoResponse zosmfTsoResponse = new TsoResponseService(mockResponse).getZosmfTsoResponse();
        assertTrue(zosmfTsoResponse.getTsoData().isEmpty());
    }

    @Test
    public void tstGetZosmfTsoResponseMissingVerJsonFieldFail() throws ZosmfRequestException {
        final String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"queueID\":\"0100\",\"tsoData\":" +
                "[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":\"--> LOGON proc version = 04/28/2011\"}}]}";
        final Response mockResponse = new Response(JsonParserUtil.parse(json), 200, "success");
        final ZosmfTsoResponse response = new TsoResponseService(mockResponse).getZosmfTsoResponse();
        assertTrue(response.getVer().isEmpty());
    }

    @Test
    public void tstGetZosmfTsoResponseNullFail() {
        String msg = "";
        try {
            new TsoResponseService((Response) null).getZosmfTsoResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("response is null", msg);
    }

    @Test
    public void tstGetZosmfTsoResponseResultsNullStatusCodeErrorFail() {
        Response response = new Response(null, 10, "fail");
        String msg = "";
        try {
            new TsoResponseService(response).getZosmfTsoResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("no tsoCmdResponse phrase", msg);
    }

    @Test
    public void tstGetZosmfTsoResponseResultsNullStatusCodeNonErrorFail() {
        Response response = new Response(null, 200, "success");
        String msg = "";
        try {
            new TsoResponseService(response).getZosmfTsoResponse();
        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals("no tsoCmdResponse phrase", msg);
    }

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOMessageAmdPromptSuccess() throws ZosmfRequestException {
        final String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":" +
                "\"--> LOGON proc version = 04/28/2011\"}},{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":" +
                "\"hidden\"}}]}";
        final Response response = new Response(JsonParserUtil.parse(json), 200, "success");
        final ZosmfTsoResponse zosmfTsoResponse = new TsoResponseService(response).getZosmfTsoResponse();
        assertFalse(zosmfTsoResponse.getTsoData().isEmpty());
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty());
        assertTrue(zosmfTsoResponse.isTimeout());
        assertTrue(zosmfTsoResponse.isReused());
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

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOMessageOnlySuccess() throws ZosmfRequestException {
        final String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":" +
                "\"--> LOGON proc version = 04/28/2011\"}}]}";
        final Response response = new Response(JsonParserUtil.parse(json), 200, "success");
        final ZosmfTsoResponse zosmfTsoResponse = new TsoResponseService(response).getZosmfTsoResponse();
        assertFalse(zosmfTsoResponse.getTsoData().isEmpty());
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty());
        assertTrue(zosmfTsoResponse.isTimeout());
        assertTrue(zosmfTsoResponse.isReused());
        assertEquals("0100", zosmfTsoResponse.getVer().get());
        assertEquals("0100", zosmfTsoResponse.getQueueId().get());
        assertEquals("ZOSMFAD-71-aabcaaaf", zosmfTsoResponse.getServletKey().get());
        assertFalse(zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().isPresent());
        assertFalse(zosmfTsoResponse.getTsoData().get(0).getTsoResponse().isPresent());
        assertEquals("0100", zosmfTsoResponse.getTsoData().get(0).getTsoMessage().get().getVersion().get());
        assertEquals("--> LOGON proc version = 04/28/2011",
                zosmfTsoResponse.getTsoData().get(0).getTsoMessage().get().getData().get());
    }

    @Test
    public void tstGetZosmfTsoResponseResultsWithTSOPromptOnlySuccess() throws ZosmfRequestException {
        final String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"," +
                "\"reused\":true,\"timeout\":true,\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":" +
                "\"--> LOGON proc version = 04/28/2011\"}}]}";
        final Response response = new Response(JsonParserUtil.parse(json), 200, "success");
        final ZosmfTsoResponse zosmfTsoResponse = new TsoResponseService(response).getZosmfTsoResponse();
        assertFalse(zosmfTsoResponse.getTsoData().isEmpty());
        assertTrue(zosmfTsoResponse.getMsgData().isEmpty());
        assertTrue(zosmfTsoResponse.isTimeout());
        assertTrue(zosmfTsoResponse.isReused());
        assertEquals("0100", zosmfTsoResponse.getVer().get());
        assertEquals("0100", zosmfTsoResponse.getQueueId().get());
        assertEquals("ZOSMFAD-71-aabcaaaf", zosmfTsoResponse.getServletKey().get());
        assertTrue(zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().isPresent());
        assertFalse(zosmfTsoResponse.getTsoData().get(0).getTsoResponse().isPresent());
        assertEquals("0100", zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().get().getVersion().get());
        assertEquals("--> LOGON proc version = 04/28/2011",
                zosmfTsoResponse.getTsoData().get(0).getTsoPrompt().get().getHidden().get());
    }

    @Test
    public void tstGetZosmfTsoResponseStatusCodeFail() throws ZosmfRequestException {
        final Response response = new Response("error", 10, "fail");
        final ZosmfTsoResponse zosmfTsoResponse = new TsoResponseService(response).getZosmfTsoResponse();
        final String errorValue = zosmfTsoResponse.getMsgData().get(0).getMessageText().get();
        assertEquals("error", errorValue);
    }

}
