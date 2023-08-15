package zowe.client.sdk.zostso.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import zowe.client.sdk.parse.JsonParseResponse;
import zowe.client.sdk.parse.JsonParseResponseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.message.ZosmfMessages;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.StartStopResponse;

import java.util.List;

public class TsoResponseService {

    /**
     * Tso command response
     */
    private Response tsoCmdResponse;
    /**
     * z/OSMF response info contains either tso start or stop command phrase, see zosmfResponse object
     */
    private ZosmfTsoResponse zosmfPhaseResponse;

    public TsoResponseService(Response response) {
        ValidateUtils.checkNullParameter(response == null, "response is null");
        this.tsoCmdResponse = response;
    }

    public TsoResponseService(ZosmfTsoResponse zosmfResponse) {
        ValidateUtils.checkNullParameter(zosmfResponse == null, "zosmfResponse is null");
        this.zosmfPhaseResponse = zosmfResponse;
    }

    /**
     * Retrieve tso response
     *
     * @return ZosmfTsoResponse object
     * @throws Exception error processing response
     * @author Frank Giordano
     */
    public ZosmfTsoResponse getZosmfTsoResponse() throws Exception {
        ZosmfTsoResponse result;
        final int statusCode = tsoCmdResponse.getStatusCode()
                .orElseThrow(() -> new IllegalStateException("status code not specified"));
        if (RestUtils.isHttpError(statusCode)) {
            final String tsoCmdResponsePhrase = (String) tsoCmdResponse.getResponsePhrase()
                    .orElseThrow(() -> new IllegalStateException("no tsoCmdResponse phrase"));
            final ZosmfMessages zosmfMsg = new ZosmfMessages(tsoCmdResponsePhrase, null, null);
            result = new ZosmfTsoResponse.Builder().msgData(List.of(zosmfMsg)).build();
        } else {
            final String jsonStr = tsoCmdResponse.getResponsePhrase()
                    .orElseThrow(() -> new IllegalStateException("no tsoCmdResponse phrase")).toString();
            final JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonStr);
            final JsonParseResponse parser = JsonParseResponseFactory.buildParser(jsonObject, ParseType.TSO_CONSOLE);
            result = (ZosmfTsoResponse) parser.parseResponse();
        }

        return result;
    }

    /**
     * Populate either a Tso start or stop command phrase
     *
     * @return StartStopResponse object
     * @author Frank Giordano
     */
    public StartStopResponse setStartStopResponse() {
        final StartStopResponse startStopResponse = new StartStopResponse(false, zosmfPhaseResponse,
                zosmfPhaseResponse.getServletKey().orElse(""));

        startStopResponse.setSuccess(zosmfPhaseResponse.getServletKey().isPresent());
        if (!zosmfPhaseResponse.getMsgData().isEmpty()) {
            final ZosmfMessages zosmfMsg = zosmfPhaseResponse.getMsgData().get(0);
            final String msgText = zosmfMsg.getMessageText().orElse(TsoConstants.ZOSMF_UNKNOWN_ERROR);
            startStopResponse.setFailureResponse(msgText);
        }

        return startStopResponse;
    }

}
