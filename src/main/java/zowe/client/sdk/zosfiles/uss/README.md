# z/OS USS Methods Package

Contains APIs to interact with Unix System Services (USS) objects on z/OS (using z/OSMF files REST endpoints).

APIs located in methods package.

## API Examples

**Create a USS file and directory**

````java
package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.input.CreateParams;
import zowe.client.sdk.zosfiles.uss.methods.UssCreate;
import zowe.client.sdk.zosfiles.uss.types.CreateType;

/**
 * Class example to test unix system services create command functionality via UssCreate class.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class CreateUssTst extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * Main method performs setup and method calls to test UssCreate
     *
     * @param args for main not used
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        Response response = CreateFile(fileNamePath);
        System.out.println(response.getStatusCode().get());
        response = CreateDirectory(dirNamePath);
        System.out.println(response.getStatusCode().get());
    }

    /**
     * Create a Unix System Service directory
     *
     * @param value directory name with path to create
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private static Response CreateDirectory(String value) throws Exception {
        UssCreate ussCreate = new UssCreate(connection);
        CreateParams params = new CreateParams(CreateType.DIR, "rwxr--r--");
        return ussCreate.create(value, params);
    }

    /**
     * Create a Unix System Service file
     *
     * @param value file name with path to create
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private static Response CreateFile(String value) throws Exception {
        UssCreate ussCreate = new UssCreate(connection);
        CreateParams params = new CreateParams(CreateType.FILE, "rwxrwxrwx");
        return ussCreate.create(value, params);
    }

}

`````

**Delete a USS file and directory**

````java
package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.uss.methods.UssDelete;

/**
 * Class example to test unix system services delete command functionality via UssDelete class.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class DeleteUssTst extends TstZosConnection {

    private static ZosConnection connection;
    private static UssDelete ussDelete;

    /**
     * Main method performs setup and method calls to test UssDelete
     *
     * @param args for main not used
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        ussDelete = new UssDelete(connection);
        Response response = DeleteFile(fileNamePath);
        System.out.println(response.getStatusCode().get());
        response = DeleteDirectory(dirNamePath);
        System.out.println(response.getStatusCode().get());
    }

    /**
     * Delete a Unix System Service file
     *
     * @param value file name with path to delete
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private static Response DeleteFile(String value) throws Exception {
        return ussDelete.delete(value);
    }

    /**
     * Delete a Unix System Service path along with all file and directories within recursively
     *
     * @param value directory name with path to delete
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private static Response DeleteDirectory(String value) throws Exception {
        return ussDelete.delete(value, true);
    }

}
`````

**List a USS file and zFS**

````java

package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosfiles.uss.input.ListParams;
import zowe.client.sdk.zosfiles.uss.input.ListZfsParams;
import zowe.client.sdk.zosfiles.uss.methods.UssList;
import zowe.client.sdk.zosfiles.uss.response.UssItem;
import zowe.client.sdk.zosfiles.uss.response.UssZfsItem;

import java.util.List;

/**
 * Class example to showcase UssList class functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ListUssTst extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * Main method performs setup and method calls to test UssList
     *
     * @param args for main not used
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String fileNamePath = "/xxx/xx/xx";
        String dirNamePath = "/xxx/xx/xx";

        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        fileList(fileNamePath);
        zfsList(dirNamePath);
    }

    /**
     * Perform a Unix System Service zFS list
     *
     * @param value file name with path
     * @throws Exception processing error
     */
    private static void zfsList(String value) throws Exception {
        UssList ussList = new UssList(connection);
        ListZfsParams params = new ListZfsParams.Builder().path(value).build();
        List<UssZfsItem> items = ussList.zfsList(params);
        items.forEach(System.out::println);
    }

    /**
     * Perform a Unix System Service file list
     *
     * @param value file name with path
     * @throws Exception processing error
     */
    private static void fileList(String value) throws Exception {
        UssList ussList = new UssList(connection);
        ListParams params = new ListParams.Builder().name(value).build();
        List<UssItem> items = ussList.fileList(params);
        items.forEach(System.out::println);
    }

}
`````

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.teamconfig.TeamConfig;
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
    public static ZosConnection getSecureZosConnection() throws Exception {
        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        return (new ZosConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
