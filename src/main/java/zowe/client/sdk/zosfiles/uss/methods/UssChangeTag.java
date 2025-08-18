/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.methods;

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.UssChangeTagInputData;
import zowe.client.sdk.zosfiles.uss.types.ChangeTagAction;
import zowe.client.sdk.zosfiles.uss.types.ChangeTagType;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) chtag functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @author Frank Giordano
 * @version 4.0
 */
public class UssChangeTag {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * UssChangeTag Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssChangeTag(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative UssChangeTag constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     */
    UssChangeTag(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Change the tag of a UNIX file to a binary type
     *
     * @param fileNamePath file name with a path
     * @return Response Object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response binary(final String fileNamePath) throws ZosmfRequestException {
        return changeCommon(fileNamePath, new UssChangeTagInputData.Builder()
                .action(ChangeTagAction.SET).type(ChangeTagType.BINARY).build());
    }

    /**
     * Change the tag of a UNIX file to a text type
     *
     * @param fileNamePath file name with a path
     * @param codeSet      code set name. i.e., IBM-1047
     * @return Response Object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response text(final String fileNamePath, final String codeSet) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(fileNamePath, "fileNamePath");
        ValidateUtils.checkIllegalParameter(codeSet, "codeSet");

        return changeCommon(fileNamePath, new UssChangeTagInputData.Builder()
                .action(ChangeTagAction.SET).type(ChangeTagType.TEXT).codeset(codeSet).build());
    }

    /**
     * Remove tag of a UNIX file
     *
     * @param fileNamePath file name with a path
     * @return Response Object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response remove(final String fileNamePath) throws ZosmfRequestException {
        return changeCommon(fileNamePath, new UssChangeTagInputData.Builder().action(ChangeTagAction.REMOVE).build());
    }

    /**
     * Retrieve existing UNIX file tag information
     *
     * @param fileNamePath file name with a path
     * @return Response Object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response get(final String fileNamePath) throws ZosmfRequestException {
        return changeCommon(fileNamePath, new UssChangeTagInputData.Builder().action(ChangeTagAction.LIST).build());
    }

    /**
     * Change tag of a UNIX file request driven by ChangeTagParams object settings
     *
     * @param fileNamePath file name with a path
     * @param params       for parameters for the change tag request, see ChangeTagParams object
     * @return Response Object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response changeCommon(final String fileNamePath, final UssChangeTagInputData params) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(fileNamePath, "fileNamePath");
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getAction().isEmpty(), "action not specified");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(fileNamePath));

        final Map<String, Object> changeTagMap = new HashMap<>();
        changeTagMap.put("request", "chtag");
        changeTagMap.put("action", params.getAction().get().getValue());
        params.getType().ifPresent(type -> changeTagMap.put("type", type.getValue()));
        params.getCodeset().ifPresent(codeset -> changeTagMap.put("codeset", codeset));
        if (!params.isRecursive()) {
            changeTagMap.put("recursive", "false");
        }
        params.getLinks().ifPresent(links -> changeTagMap.put("links", links.getValue()));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(changeTagMap).toString());

        return request.executeRequest();
    }

}
