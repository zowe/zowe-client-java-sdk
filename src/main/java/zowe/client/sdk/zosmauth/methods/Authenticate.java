package zowe.client.sdk.zosmauth.methods;

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

/**
 * Provides Unix System Services (USS) authenticate function
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=services-log-in-zosmf-server">z/OSMF REST API </a>
 * 
 * @author Esteban Sandoval
 * @author Frank Giordano
 * @version 2.0
 */
public class Authenticate {
    private final ZosConnection connection;

    private ZosmfRequest request;


//                                              maybe???


    private ZosmfRequestType type;

    /**
     * Autheniticate Constructor
     * 
     * @param connection connection information, see ZosConnection object
     * @author Esteban Sandoval
     */
    public Authenticate(final ZosConnection connection, final ZosmfRequest request){
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        this.request = request;
        
    }
    
    
    // probably will need this

    //public final static String X_CSRF_ZOSMF_HEADER = "X_CSRF_ZOSMF_HEADER";

    //              *****headers?****


//EncodeUtils.encodeAuthComponent(connection)


    public Response checkCred() throws ZosmfRequestException{
        final String url = "https://" + connection.getHost() +  ":" + EncodeUtils.encodeAuthComponent(connection);
        
        if (request == null){
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);

        }
        return request.executeRequest();

    }
    
}
