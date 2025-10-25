package zowe.client.sdk.zosconsole.method;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ConsoleUtils;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.response.ConsoleGetResponse;

/**
 * Get synchronous z/OS console response messages from a console issue command request (see ConsoleCmd class).
 * <p>
 * This class is used to retrieve the console response messages not provided to the console issue command request
 * within a certain time interval of approximately 3 seconds. It is possible that the command responses exist after
 * the time interval and can be retrieved.
 * <p>
 * Response messages are stored in the response key of the console issue command request.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ConsoleGet {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * ConsoleGet constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ConsoleGet(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative ConsoleGet constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    ConsoleGet(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Retrieve synchronous z/OS console response messages from a console issue command request's response value.
     *
     * @param responseKey response key from the issue console command request
     * @return ConsoleResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleGetResponse getResponse(final String responseKey) throws ZosmfRequestException {
        return getResponseCommon(responseKey, "", true);
    }

    /**
     * Retrieve synchronous z/OS console response messages from a console issue command request's
     * response value and console name.
     *
     * @param responseKey response key from the issue console command request
     * @param consoleName name of the console that is used to issue the command
     * @return ConsoleResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleGetResponse getResponse(final String responseKey, final String consoleName)
            throws ZosmfRequestException {
        return getResponseCommon(responseKey, consoleName, true);
    }

    /**
     * Common method with all inputs to retrieve any outstanding synchronous z/OS console response messages
     * from a console issue command request.
     *
     * @param responseKey     response key from the issue console command request
     * @param consoleName     name of the console that is used to issue the command
     * @param processResponse process console command response
     * @return ConsoleResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ConsoleGetResponse getResponseCommon(final String responseKey, final String consoleName,
                                                boolean processResponse) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(responseKey, "responseKey");

        final String url = connection.getZosmfUrl() + ConsoleConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(consoleName.isBlank() ? ConsoleConstants.RES_DEF_CN : consoleName) +
                "/solmsgs/" + responseKey;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no issue console response phrase"))
                .toString();

        final String context = "getResponseCommon";
        ConsoleGetResponse response = JsonUtils.parseResponse(responsePhrase, ConsoleGetResponse.class, context);
        if (processResponse) {
            response = response.withCmdResponse(ConsoleUtils.processCmdResponse(response.getCmdResponse()));
        }
        return response;
    }

}
