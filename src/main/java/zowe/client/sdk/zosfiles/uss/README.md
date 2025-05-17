# z/OS USS Methods Package

Contains APIs to interact with Unix System Services (USS) objects on z/OS (using z/OSMF files REST endpoints).

APIs located in methods package.

## API Examples

**Create a USS file and directory**

````java
package zowe.client.sdk.examples.zosfiles.uss;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.CreateParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCreate;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

/**
 * Class example to test unix system services create command functionality via UssCreate class.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class UssCreateExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * Main method performs setup and method calls to test UssCreate
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        Response response = CreateFile(fileNamePath);
        System.out.println("status code = " +
                (response.getStatusCode().isEmpty() ? "no status code available" : response.getStatusCode().getAsInt()));
        response = CreateDirectory(dirNamePath);
        System.out.println("status code = " +
                (response.getStatusCode().isEmpty() ? "no status code available" : response.getStatusCode().getAsInt()));
    }

    /**
     * Create a Unix directory
     *
     * @param value directory name with path to create
     * @return Response object
     * @author Frank Giordano
     */
    private static Response CreateDirectory(String value) {
        try {
            UssCreate ussCreate = new UssCreate(connection);
            CreateParams params = new CreateParams(CreateType.DIR, "-wx-wx-wx");
            return ussCreate.create(value, params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Create a Unix file
     *
     * @param value file name with path to create
     * @return Response object
     * @author Frank Giordano
     */
    private static Response CreateFile(String value) {
        try {
            UssCreate ussCreate = new UssCreate(connection);
            CreateParams params = new CreateParams(CreateType.FILE, "-wx-wx-wx");
            return ussCreate.create(value, params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

}
`````

**Delete a Unix file and directory**

````java
package zowe.client.sdk.examples.zosfiles.uss;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.methods.UssDelete;

/**
 * Class example to test unix system services delete command functionality via UssDelete class.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class UssDeleteExp extends TstZosConnection {

    private static zowe.client.sdk.zosfiles.uss.methods.UssDelete ussDelete;

    /**
     * Main method performs setup and method calls to test UssDelete
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);
        ussDelete = new UssDelete(connection);
        Response response = DeleteFile(fileNamePath);
        System.out.println("status code = " +
                (response.getStatusCode().isEmpty() ? "no status code available" : response.getStatusCode().getAsInt()));
        response = DeleteDirectory(dirNamePath);
        System.out.println("status code = " +
                (response.getStatusCode().isEmpty() ? "no status code available" : response.getStatusCode().getAsInt()));
    }

    /**
     * Delete a UNIX file
     *
     * @param value file name with path to delete
     * @return Response object
     * @author Frank Giordano
     */
    private static Response DeleteFile(String value) {
        try {
            return ussDelete.delete(value);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Delete UNIX files and directories within recursively
     *
     * @param value directory name with path to delete
     * @return Response object
     * @author Frank Giordano
     */
    private static Response DeleteDirectory(String value) {
        try {
            return ussDelete.delete(value, true);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

}
`````

**Create a file, add data to file, retrieve entire file content, and retrieve file content via filters and range**

````java
package zowe.client.sdk.examples.zosfiles.uss;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.CreateParams;
import zowe.client.sdk.zosfiles.uss.input.GetParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCreate;
import zowe.client.sdk.zosfiles.uss.methods.UssGet;
import zowe.client.sdk.zosfiles.uss.methods.UssWrite;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

/**
 * Class example to showcase UssGet class functionality.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class UssGetExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * Main method performs setup and method calls to test UssGet
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String fileNamePath = "/xx/xx/xxx";  // where xxx is a file name the rest a directory path...

        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        getFileTextContentWithSearchFilterNoResults(fileNamePath);
        getFileTextContentWithSearchFilter(fileNamePath);
        getFileTextContent(fileNamePath);
        getFileTextContentWithRange(fileNamePath);
    }

    /**
     * This method setups the file and its data for the rest of the test methods. As such, this method should
     * be called first in the main method.
     * <p>
     * Retrieve the contents of the fileNamePath value based on its search filter settings.
     * <p>
     * For this case, no search result is returned due to case-sensitive search.
     *
     * @param fileNamePath file name with path
     * @author Frank Giordano
     */
    private static void getFileTextContentWithSearchFilterNoResults(String fileNamePath) {
        Response response;
        try {
            UssCreate ussCreate = new UssCreate(connection);
            ussCreate.create(fileNamePath, new CreateParams(CreateType.FILE, "rwxr--r--"));

            UssWrite ussWrite = new UssWrite(connection);
            ussWrite.writeText(fileNamePath, "Frank\nFrank2\nApple\nhelp\n");

            UssGet ussGet = new UssGet(connection);
            GetParams params = new GetParams.Builder().insensitive(false).search("apple").build();
            response = ussGet.getCommon(fileNamePath, params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }

        System.out.println(response.getResponsePhrase().orElse("no response phrase"));
    }

    /**
     * This method performs a search against the fileNamePath value.
     * It returns data from the file from the starting point of the search value.
     *
     * @param fileNamePath file name with path
     * @author Frank Giordano
     */
    private static void getFileTextContentWithSearchFilter(String fileNamePath) {
        Response response;
        try {
            UssGet ussGet = new UssGet(connection);
            GetParams params = new GetParams.Builder().insensitive(false).search("Apple").build();
            response = ussGet.getCommon(fileNamePath, params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }

        System.out.println(response.getResponsePhrase().orElse("no response phrase"));
    }

    /**
     * This method returns the entire text content of the fileNamePath value.
     *
     * @param fileNamePath file name with path
     * @author Frank Giordano
     */
    private static void getFileTextContent(String fileNamePath) {
        String content;
        try {
            UssGet ussGet = new UssGet(connection);
            content = ussGet.getText(fileNamePath);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }

        System.out.println(content);
    }

    /**
     * This method returns the last two records (lines) from the file name path value.
     *
     * @param fileNamePath file name with path
     * @author Frank Giordano
     */
    private static void getFileTextContentWithRange(String fileNamePath) {
        Response response;
        try {
            UssGet ussGet = new UssGet(connection);
            GetParams params = new GetParams.Builder().recordsRange("-2").build();
            response = ussGet.getCommon(fileNamePath, params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }

        System.out.println(response.getResponsePhrase().orElse("no response phrase"));
    }

}
`````

**List a USS file and zFS**

````java
package zowe.client.sdk.examples.zosfiles.uss;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.ListParams;
import zowe.client.sdk.zosfiles.uss.input.ListZfsParams;
import zowe.client.sdk.zosfiles.uss.methods.UssList;
import zowe.client.sdk.zosfiles.uss.response.UnixFile;
import zowe.client.sdk.zosfiles.uss.response.UnixZfs;

import java.util.List;

/**
 * Class example to showcase UssList class functionality.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class UssListExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * Main method performs setup and method calls to test UssList
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        fileList(fileNamePath);
        zfsList(dirNamePath);
    }

    /**
     * Perform a UNIX zFS list
     *
     * @param value file name with path
     * @author Frank Giordano
     */
    private static void zfsList(String value) {
        List<UnixZfs> items;
        try {
            UssList ussList = new UssList(connection);
            ListZfsParams params = new ListZfsParams.Builder().path(value).build();
            items = ussList.getZfsSystems(params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }

        items.forEach(System.out::println);
    }

    /**
     * Perform a UNIX file list
     *
     * @param value file name with path
     * @author Frank Giordano
     */
    private static void fileList(String value) {
        List<UnixFile> items;
        try {
            UssList ussList = new UssList(connection);
            ListParams params = new ListParams.Builder().path(value).build();
            items = ussList.getFiles(params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }

        items.forEach(System.out::println);
    }

}
`````

````java
package zowe.client.sdk.examples.utility;

import zowe.client.sdk.rest.Response;

/**
 * Utility class containing helper method(s).
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class Util {

    /**
     * Extract response phrase string value if any from Response object.
     *
     * @param response object
     * @return string value
     * @author Frank Giordano
     */
    public static String getResponsePhrase(Response response) {
        if (response == null || response.getResponsePhrase().isEmpty()) {
            return null;
        }
        return response.getResponsePhrase().get().toString();
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
 * @version 3.0
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
        return (new ZosConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
