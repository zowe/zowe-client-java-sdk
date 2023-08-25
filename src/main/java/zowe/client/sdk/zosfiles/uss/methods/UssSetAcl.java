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
import zowe.client.sdk.zosfiles.uss.types.DeleteType;

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
     * @author James Kostrewski
     */
    public UssSetAcl(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssSetAcl constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author James Kostrewski
     */
    public UssSetAcl(final ZosConnection connection, final ZosmfRequest request) throws Exception {
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
     * @param value      sets the extended ACL entries that are specified by 'entries'
     * @return Response object
     * @author James Kostrewski
     */
    public Response set(final String targetPath, final String value) {
        return setAclCommon(targetPath, new SetAclParams.Builder().setSet(value).build());
    }

    /**
     * Modifies the specified ACL entry for the file or directory
     *
     * @param targetPath target path of the file or directory
     * @param value      modifies the extended ACL entries that are specified by 'entries'
     * @return Response object
     * @author James Kostrewski
     */
    public Response modify(final String targetPath, final String value) {
        return setAclCommon(targetPath, new SetAclParams.Builder().setModify(value).build());
    }

    /**
     * Deletes the specified ACL entry from the file or directory
     *
     * @param targetPath target path of the file or directory
     * @param value      deletes the extended ACL entries that are specified by 'entries'
     * @return Response object
     * @author James Kostrewski
     */
    public Response delete(final String targetPath, final String value) {
        return setAclCommon(targetPath, new SetAclParams.Builder().setDelete(value).build());
    }

    /**
     * Delete all extended ACL entries by type (setfacl -D type):
     *
     * @param targetPath target path of the file or directory
     * @param deleteType deletes the extended ACL entries that are specified by type
     * @author James Kostrewski
     */
    public Response deleteByType(final String targetPath, final DeleteType deleteType) {
        return setAclCommon(targetPath, new SetAclParams.Builder().setDeleteType(deleteType).build());
    }

    /**
     * Sets the ACL for a USS file or directory request driven by SetAclParams object settings
     *
     * @param targetPath target path of the file or directory
     * @param params     SetAclParams object to drive the request
     * @return Response object
     * @author James Kostrewski
     */
    public Response setAclCommon(final String targetPath, final SetAclParams params) {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(
                params.getSet().isEmpty() && params.getModify().isEmpty() && params.getDelete().isEmpty() &&
                        params.getDeleteType().isEmpty(), "set, modify, delete, and delete type are all empty");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> setMap = new HashMap<>();
        setMap.put("request", "setfacl");
        if (params.isAbort()) {
            setMap.put("abort", params.isAbort());
        }
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
