/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.uss.methods.UssDelete;
import zowe.client.sdk.zosfiles.uss.methods.UssWrite;
import zowe.client.sdk.zosmfworkflow.WorkflowConstants;
import zowe.client.sdk.zosmfworkflow.input.WorkflowCreateInputData;
import zowe.client.sdk.zosmfworkflow.response.WorkflowCreateLocalResponse;
import zowe.client.sdk.zosmfworkflow.response.WorkflowCreateResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides create workflow functionality through the z/OSMF workflow REST API.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-create-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowCreate {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String CONTEXT = "create";
    private final ZosConnection connection;
    private ZosmfRequest request;
    // package-private so unit tests can inject mock USS helpers; lazily created for real use
    UssWrite ussWrite;
    UssDelete ussDelete;

    /**
     * WorkflowCreate constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Ashish Kumar Dash
     */
    public WorkflowCreate(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowCreate constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Ashish Kumar Dash
     */
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
     * Create a z/OSMF workflow on the target system.
     *
     * @param createInputData workflow creation parameters
     * @return workflow details returned by z/OSMF
     * @throws ZosmfRequestException request error state
     * @author Ashish Kumar Dash
     */
    public WorkflowCreateResponse create(final WorkflowCreateInputData createInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(createInputData, "createInputData");
        ValidateUtils.checkIllegalParameter(createInputData.getWorkflowName(), "workflowName");
        ValidateUtils.checkIllegalParameter(createInputData.getWorkflowDefinitionFile(), "workflowDefinitionFile");
        ValidateUtils.checkIllegalParameter(createInputData.getSystem(), "system");
        ValidateUtils.checkIllegalParameter(createInputData.getOwner(), "owner");

        final String url = connection.getZosmfUrl() + WorkflowConstants.WORKFLOWS_RESOURCE;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setUrl(url);

        try {
            request.setBody(OBJECT_MAPPER.writeValueAsString(createInputData));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("error serializing workflow create request", e);
        }

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no workflow create response phrase"))
                .toString();

        return JsonUtils.parseResponse(responsePhrase, WorkflowCreateResponse.class, CONTEXT);
    }

    /**
     * Create a z/OSMF workflow from local files, deleting the uploaded temporary USS files on completion.
     * <p>
     * The workflow definition file and optional variable input file referenced in the input data are treated as
     * local paths. They are uploaded to a temporary USS location before the workflow is created.
     *
     * @param createInputData workflow creation parameters holding local file paths
     * @return local create workflow details, see WorkflowCreateLocalResponse object
     * @throws ZosmfRequestException request error state
     * @author Ashish Kumar Dash
     */
    public WorkflowCreateLocalResponse createLocal(final WorkflowCreateInputData createInputData)
            throws ZosmfRequestException {
        return createLocal(createInputData, false, null);
    }

    /**
     * Create a z/OSMF workflow from local files.
     * <p>
     * The workflow definition file and optional variable input file referenced in the input data are treated as
     * local paths. They are uploaded to a temporary USS location before the workflow is created. When keepFiles
     * is false, the uploaded temporary files are deleted after the creation completes.
     *
     * @param createInputData workflow creation parameters holding local file paths
     * @param keepFiles       true to retain the uploaded temporary USS files, false to delete them after creation
     * @param customDir       optional USS directory to upload the temporary files to; when null or empty, a default
     *                        temporary directory is used
     * @return local create workflow details, see WorkflowCreateLocalResponse object
     * @throws ZosmfRequestException request error state
     * @author Ashish Kumar Dash
     */
    public WorkflowCreateLocalResponse createLocal(final WorkflowCreateInputData createInputData,
                                                   final boolean keepFiles,
                                                   final String customDir) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(createInputData, "createInputData");
        ValidateUtils.checkIllegalParameter(createInputData.getWorkflowName(), "workflowName");
        ValidateUtils.checkIllegalParameter(createInputData.getWorkflowDefinitionFile(), "workflowDefinitionFile");
        ValidateUtils.checkIllegalParameter(createInputData.getSystem(), "system");
        ValidateUtils.checkIllegalParameter(createInputData.getOwner(), "owner");

        if (ussWrite == null) {
            ussWrite = new UssWrite(connection);
        }
        if (ussDelete == null) {
            ussDelete = new UssDelete(connection);
        }

        // upload the local workflow definition file to a temporary USS location
        final String tempDefinitionFile = buildTempPath(createInputData.getWorkflowDefinitionFile(), customDir);
        uploadLocalFile(createInputData.getWorkflowDefinitionFile(), tempDefinitionFile);

        // upload the optional local variable input file
        String tempVariableInputFile = null;
        final String localVariableInputFile = createInputData.getVariableInputFile();
        if (localVariableInputFile != null && !localVariableInputFile.isBlank()) {
            tempVariableInputFile = buildTempPath(localVariableInputFile, customDir);
            uploadLocalFile(localVariableInputFile, tempVariableInputFile);
        }

        // create the workflow pointing at the uploaded USS paths
        final WorkflowCreateInputData ussInputData = createInputData.toBuilder()
                .workflowDefinitionFile(tempDefinitionFile)
                .variableInputFile(tempVariableInputFile)
                .build();
        final WorkflowCreateResponse workflowCreateResponse = create(ussInputData);

        // retain or delete the uploaded temporary files
        final List<String> tempFiles = new ArrayList<>();
        tempFiles.add(tempDefinitionFile);
        if (tempVariableInputFile != null) {
            tempFiles.add(tempVariableInputFile);
        }

        final List<String> filesKept = new ArrayList<>();
        final List<String> failedToDelete = new ArrayList<>();
        if (keepFiles) {
            filesKept.addAll(tempFiles);
        } else {
            for (final String tempFile : tempFiles) {
                try {
                    ussDelete.delete(tempFile);
                } catch (ZosmfRequestException e) {
                    failedToDelete.add(tempFile);
                }
            }
        }

        return new WorkflowCreateLocalResponse(workflowCreateResponse, filesKept, failedToDelete);
    }

    /**
     * Build a temporary USS path for a local file.
     *
     * @param localFile local file path
     * @param customDir optional USS directory to place the temporary file in
     * @return temporary USS path
     */
    private String buildTempPath(final String localFile, final String customDir) {
        final String baseName = Paths.get(localFile).getFileName().toString();
        if (customDir != null && !customDir.isBlank()) {
            return customDir + "/" + baseName;
        }
        final String user = connection.getUser() == null ? "" : connection.getUser();
        return WorkflowConstants.TEMP_PATH + "/" + user + System.currentTimeMillis() + baseName;
    }

    /**
     * Upload a local file to a target USS path as binary content.
     *
     * @param localFile  local file path to read
     * @param remoteFile target USS path to write
     * @throws ZosmfRequestException request error state
     */
    private void uploadLocalFile(final String localFile, final String remoteFile) throws ZosmfRequestException {
        final byte[] content;
        try {
            content = Files.readAllBytes(Paths.get(localFile));
        } catch (IOException e) {
            throw new IllegalStateException("unable to read local file: " + localFile, e);
        }
        ussWrite.writeBinary(remoteFile, content);
    }

}
