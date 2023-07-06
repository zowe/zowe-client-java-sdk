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

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

/**
 * Provides unix system service write to object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-write-data-zos-unix-file">z/OSMF
 * REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssWrite {

    private static final Logger LOG = LoggerFactory.getLogger(UssDelete.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssWrite Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author James Kostrewski
     */
    public UssWrite(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssWrite constructor with ZoweRequest object. This is mainly
     * used for internal code unit testing with mockito, and it is not
     * recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request any compatible ZoweRequest Interface type object
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public UssWrite(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Perform a write to UNIX object request
     *
     * @param destName name of file or directory with path
     * @param params standard but optional header to coorelate with previous
     * requests on same unix file
     * @param content new content
     * @return http response object
     * @throws Exception error processing request
     * @author James Kostrewski
     */
    public Response write(String destName, WriteParams params, String content) throws Exception {
        ValidateUtils.checkNullParameter(destName == null, "destName is null");
        ValidateUtils.checkNullParameter(content == null, "content is null");
        String url;
        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                    + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + destName;
        Map<String, String> map = new HashMap<>();
        if (params.dataType.equals("text")) {
            map.put("X-IBM-Data-Type", dataType);
        } 
        LOG.debug(url);

        if (request == null || !(request instanceof TextPutRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_TEXT);
        }
        request.setHeaders(map);
        request.setUrl(url);
        request.setBody(content);
        //System.out.println(content);
        return RestUtils.getResponse(request);
    }

    public static void main(String[] args) throws Exception {
        final String hostName = "47.19.64.77";
        final String zosmfPort = "443";
        final String userName = "FGIORDA";
        final String password = "R9$WFu77";
        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);

        UssWrite myWrite = new UssWrite(connection);
        
        myWrite.write("/u/fgiorda/test","text", "this is a test message");
        
    }
}
