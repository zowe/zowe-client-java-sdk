package zowe.client.sdk.newzostso.method;

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.newzostso.input.StartInputData;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.message.TsoMessages;
import zowe.client.sdk.zostso.message.TsoResponseMessage;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.service.TsoResponseService;

import java.util.List;

public class IssueTso {

    private final ZosConnection connection;
    private ZosmfRequest request;
    private final String accountNumber;
    private StartInputData startData;

    public IssueTso(ZosConnection connection, String accountNumber) {
        this.connection = connection;
        this.accountNumber = accountNumber;
    }

    public void IssueComment(String command) throws ZosmfRequestException {

        ZosmfTsoResponse startResponse = startTso();


    }

    public void IssueCommend(String command, StartInputData startData) throws ZosmfRequestException {

        this.startData = startData;
        ZosmfTsoResponse startResponse = startTso();
        String sessionKey = startResponse.getServletKey().get();
        ZosmfTsoResponse sendResponse = sendTso(sessionKey, command);
        List<TsoMessages> tsoMessages = sendResponse.getTsoData();
        for (TsoMessages tsoMessagesLst : tsoMessages) {
//            tsoMessagesLst.getTsoPrompt().
        }
                
    }

    private ZosmfTsoResponse sendTso(String sessionKey, String command) throws ZosmfRequestException {

        final String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + sessionKey + TsoConstants.RES_DONT_READ_REPLY;

        String body = "{\"TSO RESPONSE\":{\"VERSION\":\"0100\",\"DATA\":\"" + command + "\"}}";

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        Response response = request.executeRequest();

        final JSONObject jsonObject = JsonParserUtil.parse(response.getResponsePhrase().toString());
        return (ZosmfTsoResponse) JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).parseResponse(jsonObject);
    }

    private ZosmfTsoResponse startTso() throws ZosmfRequestException {
        if (startData == null) {
            startData = new StartInputData();
        }

        String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "?";
        url += TsoConstants.PARAM_ACCT + "=" + this.accountNumber + "&";
        url += TsoConstants.PARAM_PROC + "=" + startData.getLogonProcedure().orElse(TsoConstants.DEFAULT_PROC) + "&";
        url += TsoConstants.PARAM_CHSET + "=" + startData.getCharacterSet().orElse(TsoConstants.DEFAULT_CHSET) + "&";
        url += TsoConstants.PARAM_CPAGE + "=" + startData.getCodePage().orElse(TsoConstants.DEFAULT_CPAGE) + "&";
        url += TsoConstants.PARAM_ROWS + "=" + startData.getRows().orElse(TsoConstants.DEFAULT_ROWS) + "&";
        url += TsoConstants.PARAM_COLS + "=" + startData.getColumns().orElse(TsoConstants.DEFAULT_COLS) + "&";
        url += TsoConstants.PARAM_RSIZE + "=" + startData.getRegionSize().orElse(TsoConstants.DEFAULT_RSIZE);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody("");

        Response response = request.executeRequest();

        final JSONObject jsonObject = JsonParserUtil.parse(response.getResponsePhrase().toString());
        return (ZosmfTsoResponse) JsonParseFactory.buildParser(ParseType.TSO_CONSOLE).parseResponse(jsonObject);
    }


}
