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
import zowe.client.sdk.zosfiles.uss.input.factory.SetAclInputFactory;
import zowe.client.sdk.zosfiles.uss.input.factory.UssSetAclInputData;
import zowe.client.sdk.zosfiles.uss.types.DeleteAclType;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) setfacl functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 6.0
 */
public class UssSetAcl {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * UssSetAcl Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssSetAcl(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative UssSetAcl constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     */
    UssSetAcl(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Sets the ACL for a USS file or directory
     *
     * @param targetPath UNIX path to the target file or directory
     * @param setValue   sets the extended ACL entries that are specified by 'entries'
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response set(final String targetPath, final String setValue) throws ZosmfRequestException {
        return setAclCommon(targetPath, SetAclInputFactory.createSetInput(setValue));
    }

    /**
     * Modifies the specified ACL entry for the file or directory
     *
     * @param targetPath  UNIX path to the target file or directory
     * @param modifyValue modifies the extended ACL entries that are specified by 'entries'
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response modify(final String targetPath, final String modifyValue) throws ZosmfRequestException {
        return setAclCommon(targetPath, SetAclInputFactory.createModifyInput(modifyValue));
    }

    /**
     * Deletes the specified ACL entry from the file or directory
     *
     * @param targetPath  UNIX path to the target file or directory
     * @param deleteValue deletes the extended ACL entries that are specified by 'entries'
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response delete(final String targetPath, final String deleteValue) throws ZosmfRequestException {
        return setAclCommon(targetPath, SetAclInputFactory.createDeleteInput(deleteValue));
    }

    /**
     * Delete all extended ACL entries by type (setfacl -D type):
     *
     * @param targetPath UNIX path to the target file or directory
     * @param deleteType deletes the extended ACL entries that are specified by type
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response deleteByType(final String targetPath, final DeleteAclType deleteType)
            throws ZosmfRequestException {
        return setAclCommon(targetPath, SetAclInputFactory.createDeleteTypeInput(deleteType));
    }

    /**
     * Modifies the specified ACL entry for the file or directory and deletes the specified ACL
     * entry from the file or directory
     *
     * @param targetPath  UNIX path to the target file or directory
     * @param modifyValue modifies the extended ACL entries that are specified by 'entries'
     * @param deleteValue deletes the extended ACL entries that are specified by 'entries' type
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response modifyAndDelete(final String targetPath, final String modifyValue, final String deleteValue)
            throws ZosmfRequestException {
        return setAclCommon(targetPath, SetAclInputFactory.createModifyDeleteInput(modifyValue, deleteValue));
    }

    /**
     * Sets the ACL for a USS file or directory request driven by UssSetAclInputData object settings
     *
     * @param targetPath      UNIX path to the target file or directory
     * @param setAclInputData UssSetAclInputData object to drive the request
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response setAclCommon(final String targetPath, final UssSetAclInputData setAclInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(targetPath, "targetPath");
        ValidateUtils.checkNullParameter(setAclInputData, "setAclInputData");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> setAclMap = new HashMap<>();
        setAclMap.put("request", "setfacl");
        if (setAclInputData.isAbort()) {
            setAclMap.put("abort", setAclInputData.isAbort());
        }
        setAclInputData.getLinks().ifPresent(links -> setAclMap.put("links", links.getValue()));
        setAclInputData.getDeleteType().ifPresent(deleteType -> setAclMap.put("delete-type", deleteType.getValue()));
        setAclInputData.getSet().ifPresent(set -> setAclMap.put("set", set));
        setAclInputData.getModify().ifPresent(modify -> setAclMap.put("modify", modify));
        setAclInputData.getDelete().ifPresent(delete -> setAclMap.put("delete", delete));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(setAclMap).toString());

        return request.executeRequest();
    }

}
