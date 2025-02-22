# z/OSMF AUTH Package

The z/OSMF authentication services API is provided for z/OSMF tasks and vendor applications.

This API is used to obtain or delete authentication tokens (a JSON Web Token and an LTPA token) on the user's
authentication request when logging in to or out of z/OSMF.

This API can also be used to change a z/OSMF user’s password.

API located in methods package.

## API Examples

**Submit a z/OSMF log in request and retrieve tokens**

````java
package zowe.client.sdk.examples.zosmfauth;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfauth.response.ZosmfLoginResponse;

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
        System.out.println(loginResponse);
        Cookies cookies = loginResponse.getCookies();
        // display jwtToken 
        System.out.println(cookies.get(0).toString());
        // display LtpaToken
        System.out.println(cookies.get(1).toString());

        // request to log out of server and delete authentication tokens
        ZosmfLogout logout = new ZosmfLogout(connection);
        Response response = logout.logout(cookies);

        // display response
        System.out.println(response);
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
