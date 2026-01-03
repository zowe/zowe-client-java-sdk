# z/OS Files Package

Contains APIs to interact with datasets and members files on z/OS (using z/OSMF files REST endpoints).

APIs are located in the methods package.

## API Examples

**Copy dataset and member**

````java
package zowe.client.sdk.examples.zosfiles.dsn;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DsnCopyInputData;
import zowe.client.sdk.zosfiles.dsn.methods.DsnCopy;

/**
 * Class example to showcase CopyDataset functionality via DsnCopy class.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnCopyExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DsnCopy functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        String fromDataSetName = "xxx";
        String toDataSetName = "xxx";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        copyDataset(connection, fromDataSetName, toDataSetName);
        copyDatasetByCopyParams(connection, fromDataSetName, toDataSetName);
        fromDataSetName = "xxx";  // specify a partition dataset only no member
        toDataSetName = "xxx"; // specify a partition dataset only no member
        copyFullPartitionDatasetByCopyParams(connection, fromDataSetName, toDataSetName);
    }

    /**
     * Example on how to call DsnCopy copy method.
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
     * This example sends a false value for copyAllMembers parameter in the copy method to indicate we
     * are not copying all members in a partition dataset to another.
     *
     * @param connection      ZosConnection object
     * @param fromDataSetName source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.DATASET' or 'TARGET.DATASET(MEMBER)')
     * @author Frank Giordano
     */
    public static void copyDataset(ZosConnection connection, String fromDataSetName, String toDataSetName) {
        Response response;
        try {
            DsnCopy dsnCopy = new DsnCopy(connection);
            response = dsnCopy.copy(fromDataSetName, toDataSetName, true, false);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

    /**
     * Example on how to call DsnCopy copyCommon method.
     * Copy method accepts a DsnCopyInputData object.
     * <p>
     * This copy method allows the following copy operations:
     * <p>
     * - sequential dataset to sequential dataset
     * - sequential dataset to partition dataset member
     * - partition dataset member to partition dataset member
     * - partition dataset member to partition dataset non-existing member
     * - partition dataset member to sequential dataset
     *
     * @param connection      ZosConnection object
     * @param fromDataSetName source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.DATASET' or 'TARGET.DATASET(MEMBER)')
     * @author Frank Giordano
     */
    public static void copyDatasetByCopyParams(ZosConnection connection, String fromDataSetName, String toDataSetName) {
        Response response;
        try {
            DsnCopy dsnCopy = new DsnCopy(connection);
            // 'replace' builder variable here will be true by default if not specified in builder.
            // 'copyAllMembers' builder variable here will be false by default
            DsnCopyInputData dsnCopyInputData = new DsnCopyInputData.Builder().fromDataSet(fromDataSetName).toDataSet(toDataSetName).build();
            response = dsnCopy.copyCommon(dsnCopyInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

    /**
     * Example on how to call DsnCopy copyCommon method.
     * Copy method accepts a DsnCopyInputData object.
     * <p>
     * This copy method is different from the other two examples above as it
     * sets the copyAllMember variable true to indicate that the copy operation will be performed
     * on a partition dataset to another partition dataset copying all its members to the target.
     *
     * @param connection      ZosConnection object
     * @param fromDataSetName source dataset (e.g. 'SOURCE.PARTITION.DATASET')
     * @param toDataSetName   destination dataset (e.g. 'TARGET.PARTITION.DATASET')
     * @author Frank Giordano
     */
    public static void copyFullPartitionDatasetByCopyParams(ZosConnection connection, String fromDataSetName,
                                                            String toDataSetName) {
        Response response;
        try {
            DsnCopy dsnCopy = new DsnCopy(connection);
            // 'replace' here will be true by default if not specified in the builder.
            DsnCopyInputData dsnCopyInputData = new DsnCopyInputData.Builder()
                    .fromDataSet(fromDataSetName)
                    .toDataSet(toDataSetName)
                    .copyAllMembers(true).build();
            response = dsnCopy.copyCommon(dsnCopyInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

}
`````

**Create a dataset**

````java
package zowe.client.sdk.examples.zosfiles.dsn;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DsnCreateInputData;
import zowe.client.sdk.zosfiles.dsn.methods.DsnCreate;

/**
 * Class example to showcase CreateDataset functionality via DsnCreate class.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnCreateExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * The Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DsnCreate functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        String dataSetName = "xxx";
        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        createPartitionDataSet(dataSetName);
        dataSetName = "xxx";
        createSequentialDataSet(dataSetName);
    }

    /**
     * Create a new sequential dataset.
     *
     * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
     * @author Frank Giordano
     */
    public static void createSequentialDataSet(String dataSetName) {
        Response response;
        try {
            DsnCreate dsnCreate = new DsnCreate(connection);
            response = dsnCreate.create(dataSetName, sequential());
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

    /**
     * Create a new partition dataset.
     *
     * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
     * @author Frank Giordano
     */
    public static void createPartitionDataSet(String dataSetName) {
        Response response;
        try {
            DsnCreate dsnCreate = new DsnCreate(connection);
            response = dsnCreate.create(dataSetName, partitioned());
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

    /**
     * Example of a prebuilt UssCreateInputData for creating a binary dataset.
     *
     * @return prebuilt DsnCreateInputData
     */
    public static DsnCreateInputData binary() {
        return new DsnCreateInputData.Builder()
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
     * Example of a prebuilt DsnCreateInputData for creating a c dataset
     *
     * @return prebuilt DsnCreateInputData
     */
    public static DsnCreateInputData c() {
        return new DsnCreateInputData.Builder()
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
     * Example of a prebuilt DsnCreateInputData for creating classic dataset
     *
     * @return prebuilt DsnCreateInputData
     */
    public static DsnCreateInputData classic() {
        return new DsnCreateInputData.Builder()
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
     * Example of a prebuilt DsnCreateInputData for creating partitioned dataset
     *
     * @return prebuilt DsnCreateInputData
     */
    public static DsnCreateInputData partitioned() {
        return new DsnCreateInputData.Builder()
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
     * Example of a prebuilt DsnCreateInputData for creating sequential dataset
     *
     * @return prebuilt DsnCreateInputData
     */
    public static DsnCreateInputData sequential() {
        return new DsnCreateInputData.Builder()
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
````

**Retrieve dataset information**

````java
package zowe.client.sdk.examples.zosfiles.dsn;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.methods.DsnGet;
import zowe.client.sdk.zosfiles.dsn.model.Dataset;

/**
 * Class example to showcase retrieval of dataset information functionality via DsnGet class.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnGetInfoExp extends TstZosConnection {

    /**
     * The Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DsnGet functionality.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String dataSetName = "xxx";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        System.out.println(DsnGetInfoExp.getDataSetInfo(connection, dataSetName));
    }

    /**
     * Retrieve dataset information.
     *
     * @param connection  ZosConnection object
     * @param dataSetName name of a dataset
     * @return Dataset object
     * @author Frank Giordano
     */
    public static Dataset getDataSetInfo(ZosConnection connection, String dataSetName) {
        try {
            DsnGet dsnGet = new DsnGet(connection);
            return dsnGet.getDsnInfo(dataSetName);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }
    }

}
`````

**Delete dataset and member**

````java
package zowe.client.sdk.examples.zosfiles.dsn;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.methods.DsnDelete;

/**
 * Class example to showcase DeleteDataset functionality via DsnDelete class.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnDeleteExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DeleteDataset functionality. Calls DeleteDataset example methods.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        String dataSetName = "xxx";
        String member = "xxx";
        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        deleteDataSet(dataSetName);
        deleteMember(dataSetName, member);
    }

    /**
     * Delete a dataset
     *
     * @param dataSetName name of a dataset to delete (e.g. 'DATASET.LIB')
     * @author Frank Giordano
     */
    public static void deleteDataSet(String dataSetName) {
        Response response;
        try {
            DsnDelete zosDsn = new DsnDelete(connection);
            response = zosDsn.delete(dataSetName);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

    /**
     * Delete a partition dataset member
     *
     * @param dataSetName name of a dataset where the member should be located (e.g. 'DATASET.LIB')
     * @param member      name of member to delete
     * @author Frank Giordano
     */
    public static void deleteMember(String dataSetName, String member) {
        Response response;
        try {
            DsnDelete zosDsn = new DsnDelete(connection);
            response = zosDsn.delete(dataSetName, member);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

}
`````

**Retrieve dataset and member content**

```java
package zowe.client.sdk.examples.zosfiles.dsn;

import org.apache.commons.io.IOUtils;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DsnDownloadInputData;
import zowe.client.sdk.zosfiles.dsn.methods.DsnGet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Class example to showcase retrieve dataset and member content functionality DsnGet class.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnGetExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DsnGet class functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        String datasetName = "xxx";
        String datasetSeqName = "xxx";
        String memberName = "xxx";
        DsnDownloadInputData dsnDownloadInputData = new DsnDownloadInputData.Builder().build();
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        DsnGetExp.downloadDsnMember(connection, datasetName, memberName, dsnDownloadInputData);
        DsnGetExp.downloadDsnSequential(connection, datasetSeqName, dsnDownloadInputData);
    }

    /**
     * Download a dataset member.
     *
     * @param connection ZosConnection object
     * @param dsName     name of a dataset
     * @param memName    member name that exists within the specified dataset name
     * @param dsnDownloadInputData     download parameter object
     * @author Leonid Baranov
     */
    public static void downloadDsnMember(ZosConnection connection, String dsName, String memName,
                                         DsnDownloadInputData dsnDownloadInputData) {
        try (InputStream inputStream = new DsnGet(connection).get(String.format("%s(%s)", dsName, memName), dsnDownloadInputData)) {
            System.out.println(getTextStreamData(inputStream));
        } catch (ZosmfRequestException e) {
            throw new RuntimeException(getByteResponseStatus(e));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Download a sequential dataset.
     *
     * @param connection ZosConnection object
     * @param dsName     name of a sequential dataset
     * @param dsnDownloadInputData     download parameter object
     * @author Frank Giordano
     */
    public static void downloadDsnSequential(ZosConnection connection, String dsName, DsnDownloadInputData dsnDownloadInputData) {
        try (InputStream inputStream = new DsnGet(connection).get(dsName, dsnDownloadInputData)) {
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
    public static String getByteResponseStatus(ZosmfRequestException e) {
        byte[] byteMsg = (byte[]) e.getResponse().getResponsePhrase().orElse(new byte[0]);
        ByteArrayInputStream errorStream = new ByteArrayInputStream(byteMsg);
        String errMsg;
        try {
            errMsg = getTextStreamData(errorStream);
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
    public static String getTextStreamData(final InputStream inputStream) throws IOException {
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

**Retrieve dataset list**

````java
package zowe.client.sdk.examples.zosfiles.dsn;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DsnListInputData;
import zowe.client.sdk.zosfiles.dsn.methods.DsnList;
import zowe.client.sdk.zosfiles.dsn.model.Dataset;
import zowe.client.sdk.zosfiles.dsn.model.Member;
import zowe.client.sdk.zosfiles.dsn.types.AttributeType;

import java.util.List;

/**
 * Class example to showcase ListDatasets functionality via DsnList class.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnListExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DsnList functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        String dataSetMask = "xxx";
        String dataSetName = "xxx";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        DsnListExp.listDsn(connection, dataSetMask);
        DsnListExp.listDsnVol(connection, dataSetMask);
        DsnListExp.listMembersWithAllAttributes(connection, dataSetName);
        DsnListExp.listMembers(connection, dataSetName);
    }

    /**
     * List out all members and its attribute values of the given data set
     *
     * @param connection  ZosConnection object
     * @param dataSetName name of a dataset
     * @author Leonid Baranov
     */
    public static void listMembersWithAllAttributes(ZosConnection connection, String dataSetName) {
        List<Member> datasets;
        try {
            DsnListInputData dsnListInputData = new DsnListInputData.Builder().attribute(AttributeType.BASE).build();
            DsnList dsnList = new DsnList(connection);
            datasets = dsnList.getMembers(dataSetName, dsnListInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }
        datasets.forEach(m -> System.out.println(m.toString()));
    }

    /**
     * List out all members of the given data set
     *
     * @param connection  ZosConnection object
     * @param dataSetName name of a dataset
     * @author Leonid Baranov
     */
    public static void listMembers(ZosConnection connection, String dataSetName) {
        List<Member> datasets;
        try {
            DsnListInputData dsnListInputData = new DsnListInputData.Builder().attribute(AttributeType.MEMBER).build();
            DsnList dsnList = new DsnList(connection);
            datasets = dsnList.getMembers(dataSetName, dsnListInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }
        datasets.forEach(m -> System.out.println(m.toString()));
    }

    /**
     * List out all data sets of the given data set. Each dataset returned will contain all of its properties.
     *
     * @param connection  ZosConnection object
     * @param dataSetName name of a dataset
     * @author Leonid Baranov
     */
    public static void listDsn(ZosConnection connection, String dataSetName) {
        List<Dataset> datasets;
        try {
            DsnListInputData dsnListInputData = new DsnListInputData.Builder().attribute(AttributeType.BASE).build();
            DsnList dsnList = new DsnList(connection);
            datasets = dsnList.getDatasets(dataSetName, dsnListInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }
        datasets.forEach(System.out::println);
    }

    /**
     * List out all data sets of the given data set. Each dataset returned will contain its volume property.
     *
     * @param connection  ZosConnection object
     * @param dataSetName name of a dataset
     * @author Frank Giordano
     */
    public static void listDsnVol(ZosConnection connection, String dataSetName) {
        List<Dataset> datasets;
        try {
            DsnListInputData dsnListInputData = new DsnListInputData.Builder().attribute(AttributeType.VOL).build();
            DsnList dsnList = new DsnList(connection);
            datasets = dsnList.getDatasets(dataSetName, dsnListInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }
        datasets.forEach(System.out::println);
    }

}
`````

**Write to dataset and member**

````java
package zowe.client.sdk.examples.zosfiles.dsn;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.methods.DsnWrite;

/**
 * Class example to showcase WriteDataset functionality via DsnWrite class.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnWriteExp extends TstZosConnection {

    private static ZosConnection connection;

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DsnWrite functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        String dataSetName = "xxx";
        String datasetSeqName = "xxx";
        String member = "xxx";
        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        var content = "NEW CONTENT\nTHE SECOND LINE UPDATED";
        DsnWriteExp.writeToDsnMember(dataSetName, member, content);
        DsnWriteExp.writeToDsnSequential(datasetSeqName, content);
    }

    /**
     * Write to the given member name specified replacing its content. If it does exist, it will be created.
     *
     * @param dataSetName name of a dataset where the member should be located (e.g. 'DATASET.LIB')
     * @param member      name of member to write
     * @param content     content for write
     * @author Frank Giordano
     */
    public static void writeToDsnMember(String dataSetName, String member, String content) {
        Response response;
        try {
            DsnWrite dsnWrite = new DsnWrite(connection);
            response = dsnWrite.write(dataSetName, member, content);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

    /**
     * Write to the given content to sequential dataset.
     *
     * @param dataSetName name of sequential dataset (e.g. 'DATASET.LIB')
     * @param content     content for write
     * @author Frank Giordano
     */
    public static void writeToDsnSequential(String dataSetName, String content) {
        Response response;
        try {
            DsnWrite dsnWrite = new DsnWrite(connection);
            response = dsnWrite.write(dataSetName, content);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().getResponsePhrase().isPresent()) {
                String response = e.getResponse().getResponsePhrase().get().toString();
                if (!resp.isBlank() && !"{}".equals(response)) {
                    errMsg = response;
                }
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

}
`````

Connection setup

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
 * @version 6.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final int zosmfPort = 0;
    public static final String userName = "xxx";
    public static final String password = "xxx";

    // or use the following method to retrieve Zowe OS credential store for your
    // secure Zowe V2 credentials you entered when you initially set up Zowe Global Team Configuration.
    public static ZosConnection getSecureZosConnection() throws TeamConfigException {
        TeamConfig teamConfig = new TeamConfig();
        ProfileDao profile = teamConfig.getDefaultProfile("zosmf");
        return (ZosConnectionFactory.createBasicConnection(
                profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
