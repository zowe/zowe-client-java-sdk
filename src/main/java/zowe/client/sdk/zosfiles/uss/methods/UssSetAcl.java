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
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.SetAclParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) setfacl functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssSetAcl {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * UssSetAcl Constructor
     *
     * @param connection connection information, see ZosConnection object
     */
    public UssSetAcl(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssSetAcl constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request any compatible ZoweRequest Interface object
     * @throws Exception processing error
     */
    public UssSetAcl(ZosConnection connection, ZosmfRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Sets the ACL for a USS file or directory
     *
     * @param targetPath target path of the file or directory
     * @param params GetAclParams object to drive the request
     * @return String representation of facl response phrase
     * @throws Exception processing error
     */
    public Response setAcl(String targetPath, SetAclParams params) {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> setMap = new HashMap<>();
        setMap.put("request", "setfacl");
        setMap.put("abort", params.isAbort());
        params.getLinks().ifPresent(links -> setMap.put("links", links.getValue()));
        params.getDeleteType().ifPresent(deleteType -> setMap.put("delete-type", deleteType.getValue()));
        params.getSet().ifPresent(set -> setMap.put("set", set));
        params.getModify().ifPresent(modify -> setMap.put("modify", modify));
        params.getDelete().ifPresent(delete -> setMap.put("delete", delete));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(setMap).toString());

        return request.executeRequest();
    }


}
