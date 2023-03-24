# z/OS Files Package

Contains APIs to interact with files and data sets on z/OS (using z/OSMF files REST endpoints).

## API Examples

**Create a dataset**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosfiles.ZosDsn;
import zosfiles.input.CreateParams;

/**
 * Class example to showcase CreateDataset functionality.
 */
public class CreateDataset extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(CreateDataset.class);

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CreateDataset functionality. Calls CreateDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "XXX";
        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        createDataSet(dataSetName);
    }

    /**
     * Create a new partition data set
     *
     * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
     * @throws Exception error processing request
     */
    public static void createDataSet(String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.createDsn(dataSetName, partitioned());
        LOG.info("http response code " + response.getStatusCode());
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
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosfiles.ZosDsnCopy;
import zosfiles.input.CopyParams;

/**
 * Class example to showcase CopyDataset functionality.
 */
public class CopyDataset extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(CopyDataset.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CopyDataset functionality. Calls CopyDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error processing examples
     */
    public static void main(String[] args) throws Exception {
        String fromDataSetName = "XXX";
        String toDataSetName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        copyDataset(connection, fromDataSetName, toDataSetName);
        copyDatasetByCopyParams(connection, fromDataSetName, toDataSetName);
        fromDataSetName = "XXX";  // specify a partition dataset only no member
        toDataSetName = "XXX"; // specify a partition dataset only no member
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
     */
    public static void copyDataset(ZOSConnection connection, String fromDataSetName, String toDataSetName)
            throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        Response response = zosDsnCopy.copy(fromDataSetName, toDataSetName, true, false);
        LOG.info("http response code " + response.getStatusCode());
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
     */
    public static void copyDatasetByCopyParams(ZOSConnection connection, String fromDataSetName, String toDataSetName)
            throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        // 'replace' builder variable here will be true by default if not specified in builder.
        // 'copyAllMembers' builder variable here will be false by default
        CopyParams copyParams = new CopyParams.Builder().fromDataSet(fromDataSetName).toDataSet(toDataSetName).build();
        Response response = zosDsnCopy.copy(copyParams);
        LOG.info("http response code " + response.getStatusCode());
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
     */
    public static void copyFullPartitionDatasetByCopyParams(ZOSConnection connection, String fromDataSetName,
                                                            String toDataSetName) throws Exception {
        ZosDsnCopy zosDsnCopy = new ZosDsnCopy(connection);
        // 'replace' here will be true by default if not specified in builder.
        CopyParams copyParams = new CopyParams.Builder()
                .fromDataSet(fromDataSetName).toDataSet(toDataSetName).copyAllMembers(true).build();
        Response response = zosDsnCopy.copy(copyParams);
        LOG.info("http response code " + response.getStatusCode());
    }

}
`````

**Delete a data set**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosfiles.ZosDsn;

/**
 * Class example to showcase DeleteDataset functionality.
 */
public class DeleteDataset extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(DeleteDataset.class);

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DeleteDataset functionality. Calls DeleteDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "XXX";
        String member = "XXX";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        deleteDataSet(dataSetName);
        deleteMember(dataSetName, member);
    }

    /**
     * @param dataSetName name of a dataset to delete (e.g. 'DATASET.LIB')
     * @throws Exception error processing request
     */
    public static void deleteDataSet(String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.deleteDsn(dataSetName);
        LOG.info("http response code " + response.getStatusCode());
    }

    /**
     * @param dataSetName name of a dataset where member should be located (e.g. 'DATASET.LIB')
     * @param member      name of member to delete
     * @throws Exception error processing request
     */
    public static void deleteMember(String dataSetName, String member) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.deleteDsn(dataSetName, member);
        LOG.info("http response code " + response.getStatusCode());
    }

}
`````

**Download a data set**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.UtilIO;
import zosfiles.ZosDsnDownload;
import zosfiles.input.DownloadParams;

import java.io.InputStream;
import java.io.StringWriter;

/**
 * Class example to showcase DownloadDataset functionality.
 */
public class DownloadDataset extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(DownloadDataset.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DownloadDataset functionality. Calls DownloadDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     */
    public static void main(String[] args) throws Exception {
        String datasetMember = "XXX";

        DownloadParams params = new DownloadParams.Builder().build();
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        DownloadDataset.downloadDsnMember(connection, datasetMember, params);
    }

    /**
     * Download dataset members
     *
     * @param connection ZOSConnection object
     * @param name       data set name
     * @param params     download parameters object
     * @throws Exception error processing request
     */
    public static void downloadDsnMember(ZOSConnection connection, String name, DownloadParams params) throws Exception {
        try (InputStream inputStream = new ZosDsnDownload(connection).downloadDsn(name, params)) {
            if (inputStream != null) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStream, writer, UtilIO.UTF8);
                String content = writer.toString();
                LOG.info(content);
            }
        }
    }

}
`````

**List a data set**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.UtilDataset;
import zosfiles.ZosDsnList;
import zosfiles.input.ListParams;
import zosfiles.response.Dataset;

import java.util.List;

/**
 * Class example to showcase ListDatasets functionality.
 */
public class ListDatasets extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(ListDatasets.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * ListDatasets functionality. Calls ListDatasets example methods.
     *
     * @param args for main not used
     * @throws Exception error processing request
     */
    public static void main(String[] args) throws Exception {
        String dataSetMask = "XXX";
        String dataSetName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        ListDatasets.listDsn(connection, dataSetMask);
        ListDatasets.listDsnVol(connection, dataSetMask);
        ListDatasets.listMembers(connection, dataSetName);
    }

    /**
     * List out all members of the given data set
     *
     * @param connection  ZOSConnection object
     * @param dataSetName data set name
     * @throws Exception error processing request
     */
    public static void listMembers(ZOSConnection connection, String dataSetName) throws Exception {
        ListParams params = new ListParams.Builder().build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<String> datasets = zosDsnList.listDsnMembers(dataSetName, params);
        datasets.forEach(LOG::info);
    }

    /**
     * List out all data sets of the given data set. Each dataset returned will contain all of its properties.
     *
     * @param connection  ZOSConnection object
     * @param dataSetName data set name
     * @throws Exception error processing request
     */
    public static void listDsn(ZOSConnection connection, String dataSetName) throws Exception {
        ListParams params = new ListParams.Builder().attribute(UtilDataset.Attribute.BASE).build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<Dataset> datasets = zosDsnList.listDsn(dataSetName, params);
        datasets.forEach(LOG::info);
    }

    /**
     * List out all data sets of the given data set. Each dataset returned will contain its volume property.
     *
     * @param connection  ZOSConnection object
     * @param dataSetName data set name
     * @throws Exception error processing request
     */
    public static void listDsnVol(ZOSConnection connection, String dataSetName) throws Exception {
        ListParams params = new ListParams.Builder().attribute(UtilDataset.Attribute.VOL).build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<Dataset> datasets = zosDsnList.listDsn(dataSetName, params);
        datasets.forEach(LOG::info);
    }

}
`````

**Write to a data set**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosfiles.ZosDsn;

/**
 * Class example to showcase WriteDataset functionality.
 */
public class WriteDataset extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(WriteDataset.class);

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * WriteDataset functionality. Calls WriteDataset example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "XXX";
        String member = "XXX";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        var content = "NEW CONTENT\nTHE SECOND LINE UPDATED";
        WriteDataset.writeToDsnMember(dataSetName, member, content);
    }

    /**
     * Write to the given member name specified replacing its content. If it does exist, it will be created.
     *
     * @param dataSetName name of a dataset where member should be located (e.g. 'DATASET.LIB')
     * @param member      name of member to write
     * @param content     content for write
     * @throws Exception error processing request
     */
    public static void writeToDsnMember(String dataSetName, String member, String content) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        Response response = zosDsn.writeDsn(dataSetName, member, content);
        LOG.info("http response code " + response.getStatusCode());
    }

}
`````

**List a data set info**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosfiles.ZosDsn;
import zosfiles.response.Dataset;

/**
 * Class example to showcase ZosDsn getDataSetInfo functionality.
 */
public class DataSetInfo extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(DataSetInfo.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * ZosDsn getDataSetInfo functionality.
     *
     * @param args for main not used
     * @throws Exception error processing request
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        LOG.info(DataSetInfo.getDataSetInfo(connection, dataSetName));
    }

    private static Dataset getDataSetInfo(ZOSConnection connection, String dataSetName) throws Exception {
        ZosDsn zosDsn = new ZosDsn(connection);
        return zosDsn.getDataSetInfo(dataSetName);
    }

}
`````

````java
package examples;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 */
public class ZosConnection {

    public static final String hostName = "XXX";
    public static final String zosmfPort = "XXX";
    public static final String userName = "XXX";
    public static final String password = "XXX";

}
`````
