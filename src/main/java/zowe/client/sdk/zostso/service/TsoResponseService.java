package zowe.client.sdk.zostso.service;

import org.json.simple.JSONObject;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.message.ZosmfMessages;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.StartStopResponse;

import java.util.List;

/**
 * TSO response service class
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class TsoResponseService {

    /**
     * Tso command response
     */
    private Response tsoCmdResponse;

    /**
     * z/OSMF response info contains either tso start or stop command phrase, see zosmfResponse object
     */
    private ZosmfTsoResponse zosmfPhraseResponse;

    /**
     * TsoResponseService constructor with Response object
     *
     * @param response Response object
     */
    public TsoResponseService(final Response response) {
        ValidateUtils.checkNullParameter(response == null, "response is null");
        this.tsoCmdResponse = response;
    }

    /**
     * TsoResponseService constructor with ZosmfTsoResponse object
     *
     * @param zosmfResponse ZosmfTsoResponse object
     */
    public TsoResponseService(final ZosmfTsoResponse zosmfResponse) {
        ValidateUtils.checkNullParameter(zosmfResponse == null, "zosmfResponse is null");
        this.zosmfPhraseResponse = zosmfResponse;
    }

    /**
     * Retrieve tso response
     *
     * @return ZosmfTsoResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosmfTsoResponse getZosmfTsoResponse() throws ZosmfRequestException {
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
            final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
            result = (ZosmfTsoResponse) JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).parseResponse(jsonObject);
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
