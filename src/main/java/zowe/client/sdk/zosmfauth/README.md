# z/OSMF AUTH Package

The z/OSMF authentication API services are provided for z/OSMF tasks and vendor applications.

These services are used to get or delete authentication tokens (a JSON Web Token and an LTPA token) on the user's
authentication request when logging in to or out of z/OSMF. Services also include an API to change a z/OSMF user’s
password.

Each API is located in the method package.

With the token retrieved, it can be used for authentication in place of basic authentication.

See detailed examples below.

## API Examples

```java
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
 * @version 4.0
 */
public class ZosmfLoginExp extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection and showcases examples calling
     * the zosmfauth APIs.
     * <p>
     * In addition, the examples showcases the usage of token authentication rather than
     * basic authentication. 
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) throws ZosmfRequestException {
        ZosConnection connection = new ZosConnection.Builder(AuthType.BASIC)
                .host(hostName)
                .zosmfPort(zosmfPort)
                .user(userName)
                .password(password)
                .build();
        ZosmfLogin login = new ZosmfLogin(connection);

        // request to log into server and retrieve authentication tokens
        ZosmfLoginResponse loginResponse = login.login();
        // display response
        System.out.println(loginResponse);

        Cookies tokens = loginResponse.getCookies();
        Cookie jwtToken = tokens.get(0);
        Cookie ltpaToken = tokens.get(1);
        // display jwtToken
        System.out.println(jwtToken);
        // display LtpaToken
        System.out.println(ltpaToken);

        // Perform an API call with token authentication used
        String datasetName = "xxx.xxx.xxx";
        String memberName = "xxx";
        DownloadParams params = new DownloadParams.Builder().build();

        // following defines a TOKEN authentication usage
        // redefined connection object with no username and password specified
        // if you do specify a username and password, they will be ignored
        // use jwtToken value for the token parameter. 
        connection = new ZosConnection.Builder(AuthType.TOKEN)
                .host(hostName).zosmfPort(zosmfPort).token(jwtToken).build();
        // use jwtToken 
        downloadDsnMember(connection, datasetName, memberName, params);

        // now use LtpaToken as cookie token authentication
        connection = new ZosConnection.Builder(AuthType.TOKEN)
                .host(hostName).zosmfPort(zosmfPort).token(ltpaToken).build();
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

        // this request should fail
        try {
            downloadDsnMemberWithToken(connection, datasetName, memberName, params);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // disable token authentication for the next API call.
        connection = new ZosConnection.Builder(AuthType.BASIC)
                .host(hostName).zosmfPort(zosmfPort).user(userName).password(password).build();
        // change z/OSMF user's password
        ZosmfPassword zosmfPassword = new ZosmfPassword(connection);
        PasswordParams passwordParams =
                new PasswordParams(connection.getUser(), connection.getPassword(), "xxx");
        response = zosmfPassword.changePassword(passwordParams);
        System.out.println(response);
    }

    /**
     * Download a dataset member using TOKEN authentication.
     *
     * @param connection ZosConnection object
     * @param dsName     name of a dataset
     * @param memName    member name that exists within the specified dataset name
     * @param params     download parameter object
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
```

**Connection setup**

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.model.ProfileDao;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 4.0
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
        TeamConfig teamConfig = new TeamConfig();
        ProfileDao profile = teamConfig.getDefaultProfile("zosmf");
        return (new ZosConnection.Builder(AuthType.BASIC)
                .host(profile.getHost())
                .zosmfPort(profile.getPort())
                .user(profile.getUser())
                .password(profile.getPassword())
                .build());
    }

}
`````
