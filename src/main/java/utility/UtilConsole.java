package utility;

import zosconsole.ConsoleResponse;
import zosconsole.zosmf.ZosmfIssueResponse;

/**
 * Utility Class contains helper methods for console response commands response processing
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class UtilConsole {

    /**
     * Populate the console response with the details returned from the z/OSMF console API.
     * Method takes two parameters: response from z/OSMF command and response to be populated.
     * Method adds response to a collection of z/OSMF responses, mark response as "succeeded"
     * (response.success = true) and populate other fields of response with values from z/OSMF response.
     *
     * @param zosmfResponse zosmf console response, @see ZosmfIssueResponse
     * @param response console response to be populated, @see ConsoleResponse
     * @param processResponses boolean if set to true, append command response string to the console API response
     * @return ConsoleResponse populated console response, @see ConsoleResponse
     * @author Frank Giordano
     */
    public static ConsoleResponse populate(ZosmfIssueResponse zosmfResponse, ConsoleResponse response,
                                           boolean processResponses) {
        response.setZosmfResponse(zosmfResponse);
        response.setSuccess(true);

        if (zosmfResponse.getSolKeyDetected().isPresent())
            response.setKeywordDetected(true);

        // Append the command response string to the console response.
        if (zosmfResponse.getCmdResponse().isPresent() && zosmfResponse.getCmdResponse().get().length() > 0
                && processResponses) {
            // the IBM responses sometimes have \r and \r\n, we will process them our here and return them with just \n.
            response.setCommandResponse(zosmfResponse.getCmdResponse().get().replace('\r', '\n'));
            // If there are messages append a line-break to ensure that additional messages collected are displayed properly.
            if (response.getCommandResponse().get().length() > 0
                    && (response.getCommandResponse().get().indexOf("\n")
                    != response.getCommandResponse().get().length() - 1)) {
                response.setCommandResponse(response.getCommandResponse() + "\n");
            }
        }

        // If the response key is present, set the last response key value in the response.
        if (zosmfResponse.getCmdResponseKey().isPresent()) {
            response.setLastResponseKey(zosmfResponse.getCmdResponseKey().get());
        }

        // Collect the response url.
        if (zosmfResponse.getCmdResponseUrl().isPresent()) {
            response.setCmdResponseUrl(zosmfResponse.getCmdResponseUrl().get());
        }

        return response;
    }

}

