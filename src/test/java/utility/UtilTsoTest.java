package utility;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import rest.Response;
import zostso.zosmf.ZosmfTsoResponse;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class UtilTsoTest {

    @Test
    public void tstParseJsonStopResponseSuccess() {
        JSONObject json = new JSONObject();
        json.put("ver", "ver");
        json.put("servletKey", "servletKey");
        json.put("reused", true);
        json.put("timeout", true);

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
        Response response = new Response(null, Optional.of(10));
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
        Response response = new Response(null, Optional.of(200));
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
        Response response = new Response(Optional.of("error"), Optional.of(10));
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        String errorValue = zosmfTsoResponse.getMsgData().get().get(0).getMessageText().get();
        assertTrue("error".equals(errorValue));
    }

    @Test
    public void tstGetZosmfTsoResponseMissingJsonFieldFail() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"tsoData\":\n" +
                "[{\"TSO MESSAGE\":{\"VERSION\":\"0100\",\"DATA\":\"--> LOGON proc version = 04/28/2011\"}}]}";
        Response response = new Response(Optional.of(new JSONParser().parse(json)), Optional.of(200));
        String msg = null;
        try {
            UtilTso.getZosmfTsoResponse(response);
        } catch (Exception e) {
           msg = e.getMessage();
        }
        String error = "missing one of the following json field values: queueID, ver, servletKey, " +
        "reused and timeout";
        assertTrue(msg.equals(error));
    }

    @Test
    public void tstGetZosmfTsoResponseMissingTsoDataJsonFieldFailWithEmptyResults() throws Exception {
        String json = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"queueID\":\"0100\"" +
                "\"reused\":true,\"timeout\":true}";
        Response response = new Response(Optional.of(new JSONParser().parse(json)), Optional.of(200));
        ZosmfTsoResponse zosmfTsoResponse = UtilTso.getZosmfTsoResponse(response);
        assertTrue(zosmfTsoResponse.getTsoData().isEmpty() == true);
    }


}
