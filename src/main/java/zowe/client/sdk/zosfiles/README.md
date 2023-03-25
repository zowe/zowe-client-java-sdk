# z/OS Files Package

Contains APIs to interact with files and data sets on z/OS (using z/OSMF files REST endpoints).

## API Examples

**Create a dataset**

````java
package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.ZosDsn;
import zowe.client.sdk.zosfiles.input.CreateParams;

/**
 * Class example to showcase CreateDataset functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class CreateDatasetTst extends TstZosConnection {

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CreateDataset functionality. Calls CreateDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "xxx";
        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        createPartitionDataSet(dataSetName);
        dataSetName = "xxx";
        createSequentialDataSet(dataSetName);

    }

    /**
     * Create a new sequential data set
     *
     * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void createSequentialDataSet(String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.createDsn(dataSetName, sequential());
        System.out.println("http response code " + response.getStatusCode());
    }

    /**
     * Create a new partition data set
     *
     * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void createPartitionDataSet(String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.createDsn(dataSetName, partitioned());
        System.out.println("http response code " + response.getStatusCode());
    }

    /**
     * Example of a prebuilt CreateParams for creating a binary dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams binary() {
        return new CreateParams.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(10)
                .secondary(10)
                .dirblk(25)
                .recfm("U")
                .blksize(27998)
                .lrecl(27998)
                .build();
    }

    /**
     * Example of a prebuilt CreateParams for creating a c dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams c() {
        return new CreateParams.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(25)
                .recfm("VB")
                .blksize(32760)
                .lrecl(260)
                .build();
    }

    /**
     * Example of a prebuilt CreateParams for creating classic dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams classic() {
        return new CreateParams.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(25)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    /**
     * Example of a prebuilt CreateParams for creating partitioned dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams partitioned() {
        return new CreateParams.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(5)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    /**
     * Example of a prebuilt CreateParams for creating sequential dataset
     *
     * @return prebuilt CreateParams
     */
    public static CreateParams sequential() {
        return new CreateParams.Builder()
                .dsorg("PS")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

}

`````

**Copy a data set**

````java
package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.ZosDsnCopy;
import zowe.client.sdk.zosfiles.input.CopyParams;

/**
 * Class example to showcase CopyDataset functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class CopyDatasetTst extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CopyDataset functionality. Calls CopyDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error processing examples
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String fromDataSetName = "xxx";
        String toDataSetName = "xxx";
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        copyDataset(connection, fromDataSetName, toDataSetName);
        copyDatasetByCopyParams(connection, fromDataSetName, toDataSetName);
        fromDataSetName = "xxx";  // specify a partition dataset only no member
        toDataSetName = "xxx"; // specify a partition dataset only no member
        copyFullPartitionDatasetByCopyParams(connection, fromDataSetName, toDataSetName);
    }

    /**
     * Example on how to call ZosDsnCopy copy method.
     * Copy method accepts a from and too strings for copying.
     * <p>
     * This copy method allows the following copy operations:
     * <p>
     * - sequential dataset to sequential dataset
     * - sequential dataset to partition dataset member
     * - partition dataset member to partition dataset member
     * - partition dataset member to partition dataset non-existing member
     * - partition dataset member to sequential dataset
     * <p>
     * This example sends false value for copyAllMembers parameter in copy method to indicate we
     * are not copying all members in a partition dataset to another.
     *
     * @param connection      ZOSConnection
     * @param fromDataSetName source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.DATASET' or 'TARGET.DATASET(MEMBER)')
     * @throws Exception error processing copy request
     * @author Frank Giordano
     */
    public static void copyDataset(ZOSConnection connection, String fromDataSetName, String toDataSetName)
            throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        Response response = zosDsnCopy.copy(fromDataSetName, toDataSetName, true, false);
        System.out.println("http response code " + response.getStatusCode());
    }

    /**
     * Example on how to call ZosDsnCopy copy method.
     * Copy method accepts a CopyParams object.
     * <p>
     * This copy method allows the following copy operations:
     * <p>
     * - sequential dataset to sequential dataset
     * - sequential dataset to partition dataset member
     * - partition dataset member to partition dataset member
     * - partition dataset member to partition dataset non-existing member
     * - partition dataset member to sequential dataset
     *
     * @param connection      ZOSConnection
     * @param fromDataSetName source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.DATASET' or 'TARGET.DATASET(MEMBER)')
     * @throws Exception error processing copy request
     * @author Frank Giordano
     */
    public static void copyDatasetByCopyParams(zowe.client.sdk.core.ZOSConnection connection, String fromDataSetName,
                                               String toDataSetName) throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        // 'replace' builder variable here will be true by default if not specified in builder.
        // 'copyAllMembers' builder variable here will be false by default
        CopyParams copyParams = new CopyParams.Builder().fromDataSet(fromDataSetName).toDataSet(toDataSetName).build();
        Response response = zosDsnCopy.copy(copyParams);
        System.out.println("http response code " + response.getStatusCode());
    }

    /**
     * Example on how to call ZosDsnCopy copy method.
     * Copy method accepts a CopyParams object.
     * <p>
     * This copy method is different from the other two examples above as it
     * sets the copyAllMember variable true to indicate that the copy operation will be performed
     * on a partition dataset to another partition dataset copying all its members to the target.
     *
     * @param connection      ZOSConnection
     * @param fromDataSetName source dataset (e.g. 'SOURCE.PARTITION.DATASET')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.PARTITION.DATASET')
     * @throws Exception error processing copy request
     * @author Frank Giordano
     */
    public static void copyFullPartitionDatasetByCopyParams(ZOSConnection connection, String fromDataSetName,
                                                            String toDataSetName) throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        // 'replace' here will be true by default if not specified in builder.
        CopyParams copyParams = new CopyParams.Builder()
                .fromDataSet(fromDataSetName).toDataSet(toDataSetName).copyAllMembers(true).build();
        Response response = zosDsnCopy.copy(copyParams);
        System.out.println("http response code " + response.getStatusCode());
    }

}

`````

**Delete a data set**

````java
package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.ZosDsn;

/**
 * Class example to showcase DeleteDataset functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class DeleteDatasetTst extends TstZosConnection {

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DeleteDataset functionality. Calls DeleteDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "xxx";
        String member = "xxx";
        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        deleteDataSet(dataSetName);
        deleteMember(dataSetName, member);
    }

    /**
     * @param dataSetName name of a dataset to delete (e.g. 'DATASET.LIB')
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void deleteDataSet(String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.deleteDsn(dataSetName);
        System.out.println("http response code " + response.getStatusCode());
    }

    /**
     * @param dataSetName name of a dataset where member should be located (e.g. 'DATASET.LIB')
     * @param member      name of member to delete
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void deleteMember(String dataSetName, String member) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.deleteDsn(dataSetName, member);
        System.out.println("http response code " + response.getStatusCode());
    }

}

`````

**Download a data set**

````java
package zowe.client.sdk.examples.zosfiles;

import org.apache.commons.io.IOUtils;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosfiles.ZosDsnDownload;
import zowe.client.sdk.zosfiles.input.DownloadParams;

import java.io.InputStream;
import java.io.StringWriter;

/**
 * Class example to showcase DownloadDataset functionality.
 *
 * @author Leonid Baranov
 * @version 2.0
 */
public class DownloadDatasetTst extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DownloadDataset functionality. Calls DownloadDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String datasetMember = "xxx";
        DownloadParams params = new DownloadParams.Builder().build();
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        DownloadDatasetTst.downloadDsnMember(connection, datasetMember, params);
    }

    /**
     * Download dataset members
     *
     * @param connection ZOSConnection object
     * @param name       data set name
     * @param params     download parameters object
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public static void downloadDsnMember(zowe.client.sdk.core.ZOSConnection connection, String name, DownloadParams params) throws Exception {
        try (InputStream inputStream = new ZosDsnDownload(connection).downloadDsn(name, params)) {
            if (inputStream != null) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStream, writer, "UTF8");
                String content = writer.toString();
                System.out.println(content);
            }
        }
    }

}
`````

**List a data set**

````java
package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosfiles.ZosDsnList;
import zowe.client.sdk.zosfiles.input.ListParams;
import zowe.client.sdk.zosfiles.response.Dataset;
import zowe.client.sdk.zosfiles.response.Member;
import zowe.client.sdk.zosfiles.types.AttributeType;

import java.util.List;

/**
 * Class example to showcase ListDatasets functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class ListDatasetsTst extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * ListDatasets functionality. Calls ListDatasets example methods.
     *
     * @param args for main not used
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String dataSetMask = "xxx";
        String dataSetName = "xxx";
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        ListDatasetsTst.listDsn(connection, dataSetMask);
        ListDatasetsTst.listDsnVol(connection, dataSetMask);
        ListDatasetsTst.listMembersWithAllAttributes(connection, dataSetName);
        ListDatasetsTst.listMembers(connection, dataSetName);
    }

    /**
     * List out all members and its attribute values of the given data set
     *
     * @param connection  ZOSConnection object
     * @param dataSetName data set name
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public static void listMembersWithAllAttributes(ZOSConnection connection, String dataSetName) throws Exception {
        ListParams params = new ListParams.Builder().attribute(AttributeType.BASE).build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<Member> datasets = zosDsnList.listDsnMembers(dataSetName, params);
        datasets.forEach(m -> System.out.println(m.toString()));
    }

    /**
     * List out all members of the given data set
     *
     * @param connection  ZOSConnection object
     * @param dataSetName data set name
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public static void listMembers(ZOSConnection connection, String dataSetName) throws Exception {
        ListParams params = new ListParams.Builder().attribute(AttributeType.MEMBER).build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<Member> datasets = zosDsnList.listDsnMembers(dataSetName, params);
        datasets.forEach(m -> System.out.println(m.toString()));
    }

    /**
     * List out all data sets of the given data set. Each dataset returned will contain all of its properties.
     *
     * @param connection  ZOSConnection object
     * @param dataSetName data set name
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public static void listDsn(ZOSConnection connection, String dataSetName) throws Exception {
        ListParams params = new ListParams.Builder().attribute(AttributeType.BASE).build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<Dataset> datasets = zosDsnList.listDsn(dataSetName, params);
        datasets.forEach(i -> System.out.println(i));
    }

    /**
     * List out all data sets of the given data set. Each dataset returned will contain its volume property.
     *
     * @param connection  ZOSConnection object
     * @param dataSetName data set name
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void listDsnVol(ZOSConnection connection, String dataSetName) throws Exception {
        ListParams params = new ListParams.Builder().attribute(AttributeType.VOL).build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<Dataset> datasets = zosDsnList.listDsn(dataSetName, params);
        datasets.forEach(i -> System.out.println(i));
    }

}
`````

**Write to a data set**

````java
package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosfiles.ZosDsn;

/**
 * Class example to showcase WriteDataset functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class WriteDatasetTst extends TstZosConnection {

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * WriteDataset functionality. Calls WriteDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "xxx";
        String member = "xxx";
        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        var content = "NEW CONTENT\nTHE SECOND LINE UPDATED";
        WriteDatasetTst.writeToDsnMember(dataSetName, member, content);
    }

    /**
     * Write to the given member name specified replacing its content. If it does exist, it will be created.
     *
     * @param dataSetName name of a dataset where member should be located (e.g. 'DATASET.LIB')
     * @param member      name of member to write
     * @param content     content for write
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void writeToDsnMember(String dataSetName, String member, String content) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.writeDsn(dataSetName, member, content);
        System.out.println("http response code " + response.getStatusCode());
    }

}

`````

**List a data set info**

````java
package zowe.client.sdk.examples.zosfiles;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosfiles.ZosDsn;
import zowe.client.sdk.zosfiles.response.Dataset;

/**
 * Class example to showcase ZosDsn getDataSetInfo functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class DataSetInfoTst extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * ZosDsn getDataSetInfo functionality.
     *
     * @param args for main not used
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "xxx";
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        System.out.println(DataSetInfoTst.getDataSetInfo(connection, dataSetName));
    }

    private static Dataset getDataSetInfo(zowe.client.sdk.core.ZOSConnection connection, String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        return zosDsn.getDataSetInfo(dataSetName);
    }

}
`````

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZOSConnection;
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
    public static ZOSConnection getSecureZosConnection() throws Exception {
        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        return (new ZOSConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
