# z/OS USS Methods Package

Contains APIs to interact with Unix System Services (USS) objects on z/OS (using z/OSMF files REST endpoints).

APIs located in methods package.

## API Examples

**Create a USS file and directory**

````java
package zowe.client.sdk.examples.zosfiles.uss;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.UssCreateInputData;
import zowe.client.sdk.zosfiles.uss.methods.UssCreate;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

/**
 * Class example to test unix system services create command functionality via UssCreate class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class UssCreateExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * The main method performs setup and method calls to test UssCreate
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        Response response = CreateFile(fileNamePath);
        System.out.println(response.toString());
        response = CreateDirectory(dirNamePath);
        System.out.println(response.toString());
    }

    /**
     * Create a Unix directory
     *
     * @param value directory name with a path to create
     * @return Response object
     * @author Frank Giordano
     */
    private static Response CreateDirectory(String value) {
        try {
            UssCreate ussCreate = new UssCreate(connection);
            UssCreateInputData ussCreateInputData = new UssCreateInputData(CreateType.DIR, "-wx-wx-wx");
            return ussCreate.create(value, ussCreateInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * Create a Unix file
     *
     * @param value file name with a path to create
     * @return Response object
     * @author Frank Giordano
     */
    private static Response CreateFile(String value) {
        try {
            UssCreate ussCreate = new UssCreate(connection);
            UssCreateInputData ussCreateInputData = new UssCreateInputData(CreateType.FILE, "-wx-wx-wx");
            return ussCreate.create(value, ussCreateInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

}
`````

**Delete a Unix file and directory**

````java
package zowe.client.sdk.examples.zosfiles.uss;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.methods.UssDelete;

/**
 * Class example to test unix system services delete command functionality via UssDelete class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class UssDeleteExp extends TstZosConnection {

    private static zowe.client.sdk.zosfiles.uss.methods.UssDelete ussDelete;

    /**
     * The main method performs setup and method calls to test UssDelete
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        ussDelete = new UssDelete(connection);
        Response response = DeleteFile(fileNamePath);
        System.out.println(response.toString());
        response = DeleteDirectory(dirNamePath);
        System.out.println(response.toString());
    }

    /**
     * Delete a UNIX file
     *
     * @param value file name with a path to delete
     * @return Response object
     * @author Frank Giordano
     */
    private static Response DeleteFile(String value) {
        try {
            return ussDelete.delete(value);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * Delete UNIX files and directories within recursively
     *
     * @param value directory name with a path to delete
     * @return Response object
     * @author Frank Giordano
     */
    private static Response DeleteDirectory(String value) {
        try {
            return ussDelete.delete(value, true);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);;
        }
    }

}
`````

**Create a file, add data to the file, retrieve entire file content, and retrieve file content via filters and range**

````java
package zowe.client.sdk.examples.zosfiles.uss;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.UssCreateInputData;
import zowe.client.sdk.zosfiles.uss.input.UssGetInputData;
import zowe.client.sdk.zosfiles.uss.methods.UssCreate;
import zowe.client.sdk.zosfiles.uss.methods.UssGet;
import zowe.client.sdk.zosfiles.uss.methods.UssWrite;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

/**
 * Class example to showcase UssGet class functionality.
 *
 * @author Frank Giordano
 * @version 5.0
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
        String fileNamePath = "/xx/xx/xxx";  // where xxx is a file name, the rest a directory path...

        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        getFileTextContentWithSearchFilterNoResults(fileNamePath);
        getFileTextContentWithSearchFilter(fileNamePath);
        getFileTextContent(fileNamePath);
        getFileTextContentWithRange(fileNamePath);
    }

    /**
     * This method sets up the file and its data for the rest of the test methods. As such, this method should
     * be called first in the main method.
     * <p>
     * Retrieve the contents of the fileNamePath value based on its search filter settings.
     * <p>
     * For this case, no search result is returned due to case-sensitive search.
     *
     * @param fileNamePath file name with a path
     * @author Frank Giordano
     */
    private static void getFileTextContentWithSearchFilterNoResults(String fileNamePath) {
        Response response;
        try {
            UssCreate ussCreate = new UssCreate(connection);
            ussCreate.create(fileNamePath, new UssCreateInputData(CreateType.FILE, "rwxr--r--"));

            UssWrite ussWrite = new UssWrite(connection);
            ussWrite.writeText(fileNamePath, "Frank\nFrank2\nApple\nhelp\n");

            UssGet ussGet = new UssGet(connection);
            UssGetInputData ussGetInputData = new UssGetInputData.Builder().insensitive(false).search("apple").build();
            response = ussGet.getCommon(fileNamePath, ussGetInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }

        System.out.println(response.getResponsePhrase().orElse("no response phrase"));
    }

    /**
     * This method performs a search against the fileNamePath value.
     * It returns data from the file from the starting point of the search value.
     *
     * @param fileNamePath file name with a path
     * @author Frank Giordano
     */
    private static void getFileTextContentWithSearchFilter(String fileNamePath) {
        Response response;
        try {
            UssGet ussGet = new UssGet(connection);
            UssGetInputData ussGetInputData = new UssGetInputData.Builder().insensitive(false).search("Apple").build();
            response = ussGet.getCommon(fileNamePath, ussGetInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }

        System.out.println(response.getResponsePhrase().orElse("no response phrase"));
    }

    /**
     * This method returns the entire text content of the fileNamePath value.
     *
     * @param fileNamePath file name with a path
     * @author Frank Giordano
     */
    private static void getFileTextContent(String fileNamePath) {
        String content;
        try {
            UssGet ussGet = new UssGet(connection);
            content = ussGet.getText(fileNamePath);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }

        System.out.println(content);
    }

    /**
     * This method returns the last two records (lines) from the file name path value.
     *
     * @param fileNamePath file name with a path
     * @author Frank Giordano
     */
    private static void getFileTextContentWithRange(String fileNamePath) {
        Response response;
        try {
            UssGet ussGet = new UssGet(connection);
            UssGetInputData ussGetInputData = new UssGetInputData.Builder().recordsRange("-2").build();
            response = ussGet.getCommon(fileNamePath, ussGetInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }

        System.out.println(response.getResponsePhrase().orElse("no response phrase"));
    }

}
`````

**List a USS file and zFS**

````java
package zowe.client.sdk.examples.zosfiles.uss;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.UssListInputData;
import zowe.client.sdk.zosfiles.uss.input.UssListZfsInputData;
import zowe.client.sdk.zosfiles.uss.methods.UssList;
import zowe.client.sdk.zosfiles.uss.model.UnixFile;
import zowe.client.sdk.zosfiles.uss.model.UnixZfs;

import java.util.List;

/**
 * Class example to showcase UssList class functionality.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class UssListExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * The main method performs setup and method calls to test UssList
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
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
            UssListZfsInputData ussListZfsInputData = new UssListZfsInputData.Builder().path(value).build();
            items = ussList.getZfsSystems(ussListZfsInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }

        items.forEach(System.out::println);
    }

    /**
     * Perform a UNIX file list
     *
     * @param value file name with a path
     * @author Frank Giordano
     */
    private static void fileList(String value) {
        List<UnixFile> items;
        try {
            UssList ussList = new UssList(connection);
            UssListInputData ussListInputData = new UssListInputData.Builder().path(value).build();
            items = ussList.getFiles(ussListInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }

        items.forEach(System.out::println);
    }

}
`````

**Connection setup**

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.model.ProfileDao;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 5.0
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
