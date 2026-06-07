package zowe.client.sdk.zosmfworkflow.methods;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;


public class WorkflowDelete {

    private final ZosConnection connection;
    private ZosmfRequest request;

    public WorkflowDelete(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    WorkflowDelete(final ZosConnection connection,
                   final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;

        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }

        this.request = request;
    }

    public Response delete(final String workflowKey)
            throws ZosmfRequestException {

        ValidateUtils.checkNullParameter(workflowKey, "workflowKey");

        final String url =
                connection.getZosmfUrl()
                        + WorkflowConstants.RESOURCE
                        + "/"
                        + workflowKey;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(
                    connection,
                    ZosmfRequestType.DELETE_JSON);
        }

        request.setUrl(url);

        return request.executeRequest();
    }
}
