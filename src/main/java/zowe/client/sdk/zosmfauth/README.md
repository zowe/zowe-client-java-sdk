# z/OSMF AUTH Package

The z/OSMF authentication services API is provided for z/OSMF tasks and vendor applications.

This API is used to obtain or delete authentication tokens (a JSON Web Token and an LTPA token) on the user's
authentication request when logging in to or out of z/OSMF.

This API can also be used to change a z/OSMF user’s password.

API located in methods package.

With the token retrieved it can be used for authentication in place of basic authentication. See detail example below.

## API Examples

**Submit a z/OSMF log in request and retrieve tokens**

````java
package zowe.client.sdk.examples.zosmfauth;

import kong.unirest.core.Cookie;
import kong.unirest.core.Cookies;
import org.apache.commons.io.IOUtils;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DownloadParams;
import zowe.client.sdk.zosfiles.dsn.methods.DsnGet;
import zowe.client.sdk.zosmfauth.methods.ZosmfLogin;
import zowe.client.sdk.zosmfauth.methods.ZosmfLogout;
import zowe.client.sdk.zosmfauth.response.ZosmfLoginResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Class example to showcase z/OSMF AUTH APIs functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfLoginExp extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection, and performs a login and logout API call.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) throws ZosmfRequestException {
        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);
        ZosmfLogin login = new ZosmfLogin(connection);

        // request to log into server and obtain authentication tokens
        ZosmfLoginResponse loginResponse = login.login();
        // display response
        System.out.println(loginResponse);

        Cookies cookies = loginResponse.getCookies();
        Cookie jwtToken = cookies.get(0);
        Cookie ltpaToken = cookies.get(1);
        // display jwtToken
        System.out.println(jwtToken);
        // display LtpaToken
        System.out.println(ltpaToken);

        // Perform a API call with token authentication used
        String datasetName = "CCSQA.ASM.JCL.INSTHELP";
        String memberName = "APPLY";
        DownloadParams params = new DownloadParams.Builder().build();

        // redefine connection object with no username and password specified
        connection = new ZosConnection(hostName, zosmfPort, "", "");
        // set the token in connection cookie field
        connection.setCookie(jwtToken);

        // use jwtToken as cookie authentication
        downloadDsnMemberWithToken(connection, jwtToken, datasetName, memberName, params);
        // now use LtpaToken as cookie authentication
        downloadDsnMemberWithToken(connection, ltpaToken, datasetName, memberName, params);

        // redefine connection object with username and password specified
        connection = new ZosConnection(hostName, zosmfPort, userName, password);

        // perform the same request without token
        downloadDsnMember(connection, datasetName, memberName, params);

        // set the token in connection cookie field
        connection.setCookie(jwtToken);

        // use jwtToken as cookie authentication
        downloadDsnMemberWithToken(connection, jwtToken, datasetName, memberName, params);
        // now use LtpaToken as cookie authentication
        downloadDsnMemberWithToken(connection, ltpaToken, datasetName, memberName, params);

        // perform the same request without token
        connection.setCookie(null);
        downloadDsnMember(connection, datasetName, memberName, params);

        // request to log out of server and delete jwtToken authentication token
        ZosmfLogout logout = new ZosmfLogout(connection);
        Response response = logout.logout(jwtToken);
        // display response
        System.out.println(response);
        // delete LtpaToken authentication token
        response = logout.logout(ltpaToken);
        // display response
        System.out.println(response);

        // use deleted token for authentication for next request
        connection.setCookie(jwtToken);
        // this request should fail
        downloadDsnMemberWithToken(connection, jwtToken, datasetName, memberName, params);
    }

    /**
     * Download a dataset member using token cookie authentication.
     *
     * @param connection ZosConnection object
     * @param dsName     name of a dataset
     * @param memName    member name that exists within the specified dataset name
     * @param params     download parameters object
     * @author Leonid Baranov
     */
    private static void downloadDsnMember(ZosConnection connection, String dsName, String memName,
                                          DownloadParams params) {
        try (InputStream inputStream = new DsnGet(connection).get(String.format("%s(%s)", dsName, memName), params)) {
            System.out.println(getTextStreamData(inputStream));
        } catch (ZosmfRequestException e) {
            throw new RuntimeException(getByteResponseStatus(e));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Download a dataset member using basic authentication.
     *
     * @param connection ZosConnection object
     * @param dsName     name of a dataset
     * @param memName    member name that exists within the specified dataset name
     * @param params     download parameters object
     * @author Leonid Baranov
     */
    private static void downloadDsnMemberWithToken(ZosConnection connection, Cookie cookie, String dsName, String memName,
                                                   DownloadParams params) {
        connection.setCookie(cookie);
        try (InputStream inputStream = new DsnGet(connection).get(String.format("%s(%s)", dsName, memName), params)) {
            System.out.println(getTextStreamData(inputStream));
        } catch (ZosmfRequestException e) {
            throw new RuntimeException(getByteResponseStatus(e));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert exception message's byte stream of data into a string
     *
     * @param e ZosmfRequestException object
     * @return string value
     * @author Frank Giordano
     */
    private static String getByteResponseStatus(ZosmfRequestException e) {
        byte[] byteMsg = (byte[]) e.getResponse().getResponsePhrase().get();
        ByteArrayInputStream errorStream = new ByteArrayInputStream(byteMsg);
        String errMsg;
        try {
            errMsg = getTextStreamData(errorStream);
            if (errMsg.isEmpty()) {
                errMsg = e.getMessage();
            }
        } catch (IOException ex) {
            errMsg = "error processing response";
        }
        return errMsg;
    }

    /**
     * Convert a byte stream of data into a string
     *
     * @param inputStream byte stream od data
     * @return string value
     * @throws IOException error processing byte stream
     * @author Frank Giordano
     */
    private static String getTextStreamData(final InputStream inputStream) throws IOException {
        if (inputStream != null) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "UTF8");
            inputStream.close();
            return writer.toString();
        }
        return null;
    }

}
`````

**Connection setup**

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.keytar.KeyTarImpl;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final String zosmfPort = "xxx";
    public static final String userName = "xxx";
    public static final String password = "xxx";

    // or use the following method to retrieve Zowe OS credential store for your
    // secure Zowe V2 credentials you entered when you initially set up Zowe Global Team Configuration.
    public static ZosConnection getSecureZosConnection() throws TeamConfigException {
        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        return (new ZosConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
