package zowe.client.sdk.zosworkflow.methods;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosworkflow.WorkflowConstants;
import zowe.client.sdk.utility.WorkflowUtils;
import zowe.client.sdk.zosworkflow.input.WorkflowCreateInputData;

import java.util.HashMap;
import java.util.Map;

/**
 * WorkflowCreate class to handle workflow create on z/OS
 *
 * @author Carlo Maria Proietti
 */
public class WorkflowCreate {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowCreate.class);
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * Workflow create constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Carlo Maria Proietti
     */
    public WorkflowCreate(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    WorkflowCreate(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Create a workflow on z/OS by workflow name, workflow definition file, system, owner and
     * eventual other optional properties.
     *
     * @param workflowName name of the workflow to create
     * @param workflowDefinitionFile location of the workflow definition file
     * @param system nickname of the system on which the workflow is to be created.
     * @param owner user ID of the workflow owner.
     * @param version version number.
     * @return workflow document with details about the created workflow
     * @throws ZosmfRequestException request error state
     */
    public Response create(final String workflowName,
                           final String workflowDefinitionFile,
                           final String system,
                           final String owner,
                           final String version,
                           final Map<String, Object> optionalProperties) throws ZosmfRequestException {
        return this.createCommon(WorkflowCreateInputData.builder()
                .workflowName(workflowName)
                .workflowDefinitionFile(workflowDefinitionFile)
                .system(system)
                .owner(owner)
                .optionalProperties(optionalProperties)
                .version(version)
                .build());
    }

    /**
     * Create a workflow on z/OS by WorkflowModifyInputData object.
     *
     * @param createInputData cancel job parameters, see JobModifyInputData object
     * @return job document with details about the submitted job
     * @throws ZosmfRequestException request error state
     * @author Carlo Maria Proietti
     */
    @SuppressWarnings("DuplicatedCode")
    public Response createCommon(final WorkflowCreateInputData createInputData) throws ZosmfRequestException {
        // validation of input's mandatory parts
        ValidateUtils.checkNullParameter(createInputData, "createInputData");
        ValidateUtils.checkIllegalParameter(createInputData.getWorkflowName().isEmpty(), WorkflowConstants.WORKFLOW_NAME_ILLEGAL_MSG);
        ValidateUtils.checkIllegalParameter(createInputData.getWorkflowDefinitionFile().isEmpty(), WorkflowConstants.WORKFLOW_DEFINITION_FILE_ILLEGAL_MSG);
        ValidateUtils.checkIllegalParameter(createInputData.getSystem().isEmpty(), WorkflowConstants.WORKFLOW_SYSTEM_ILLEGAL_MSG);
        ValidateUtils.checkIllegalParameter(createInputData.getOwner().isEmpty(), WorkflowConstants.WORKFLOW_OWNER_ILLEGAL_MSG);
        ValidateUtils.checkIllegalParameter(createInputData.getVersion().isEmpty(), WorkflowConstants.WORKFLOW_VERSION_ILLEGAL_MSG);

        // generate full url request
        final String url = connection.getZosmfUrl() +
                WorkflowConstants.RESOURCE_PREFIX +
                WorkflowConstants.FILE_DELIM +
                createInputData.getVersion().get() +
                WorkflowConstants.RESOURCE_SUFFIX;

        // generate JSON string body for the request; mandatory properties
        final Map<String, Object> createMap = new HashMap<>();
        createMap.put("workflowName", createInputData.getWorkflowName().get());
        createMap.put("workflowDefinitionFile", createInputData.getWorkflowDefinitionFile().get());
        createMap.put("system", createInputData.getSystem().get());
        createMap.put("owner", createInputData.getOwner().get());
        // generate JSON string body for the request; optional properties
        final Map<String, Object> properties = createInputData.getOptionalProperties();
        if (properties != null) {
            properties.forEach((k, v) -> {
                // assure that the key is known to z/OSMF
                WorkflowUtils.validateOptionalPropertyKey(k);
                createMap.put(k, v);
            });
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setBody(new JSONObject(createMap).toString());
        request.setUrl(url);

        return request.executeRequest();
    }
}
