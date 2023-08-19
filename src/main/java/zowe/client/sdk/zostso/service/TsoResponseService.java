package zowe.client.sdk.zostso.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import zowe.client.sdk.parse.JsonParseFactory;
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
    private ZosmfTsoResponse zosmfPhraseResponse;

    public TsoResponseService(final Response response) {
        ValidateUtils.checkNullParameter(response == null, "response is null");
        this.tsoCmdResponse = response;
    }

    public TsoResponseService(final ZosmfTsoResponse zosmfResponse) {
        ValidateUtils.checkNullParameter(zosmfResponse == null, "zosmfResponse is null");
        this.zosmfPhraseResponse = zosmfResponse;
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
        if (!(statusCode >= 100 && statusCode <= 299)) {
            final String tsoCmdResponsePhrase = (String) tsoCmdResponse.getResponsePhrase()
                    .orElseThrow(() -> new IllegalStateException("no tsoCmdResponse phrase"));
            final ZosmfMessages zosmfMsg = new ZosmfMessages(tsoCmdResponsePhrase, null, null);
            result = new ZosmfTsoResponse.Builder().msgData(List.of(zosmfMsg)).build();
        } else {
            final String jsonStr = tsoCmdResponse.getResponsePhrase()
                    .orElseThrow(() -> new IllegalStateException("no tsoCmdResponse phrase")).toString();
            final JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonStr);
            result = (ZosmfTsoResponse) JsonParseFactory.buildParser(ParseType.TSO_CONSOLE)
                    .setJsonObject(jsonObject).parseResponse();
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
        final StartStopResponse startStopResponse = new StartStopResponse(false, zosmfPhraseResponse,
                zosmfPhraseResponse.getServletKey().orElse(""));

        startStopResponse.setSuccess(zosmfPhraseResponse.getServletKey().isPresent());
        if (!zosmfPhraseResponse.getMsgData().isEmpty()) {
            final ZosmfMessages zosmfMsg = zosmfPhraseResponse.getMsgData().get(0);
            final String msgText = zosmfMsg.getMessageText().orElse(TsoConstants.ZOSMF_UNKNOWN_ERROR);
            startStopResponse.setFailureResponse(msgText);
        }

        return startStopResponse;
    }

}
