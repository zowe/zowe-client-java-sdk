# TeamConfig Package

The TeamConfig package provides API method(s) to retrieve a profile section from Zowe Global Team Configuration with
keytar information to help perform connection processing without hard coding username and password. Keytar represents
credentials stored securely on your computer when performing the Zowe global initialize command which prompts you for
username and password.

## Description of retrieving a profile.

With the following team configuration:

    "$schema": "./zowe.schema.json",
        "profiles": {
            "frank": {
                "type": "zosmf",
                "properties": {
                    "port": 4433
                },
                "secure": []
            }
        },
        "defaults": {
            "zosmf": "frank"
        }
    }  

To retrieve the default profile of type "zosmf" with the credentials from the credential store, perform the following:

ProfileDao profile = teamConfig.getDefaultProfile("zosmf");

This should return the profile named "frank" with its attributes and credentials.

## API Example

**Retrieve the default "zosmf" profile from team config. Use it to create a ZOSConnection object without hard coding
username and password and retrieve a list of members from the dataset input string.**

````java
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.zosfiles.dsn.input.list.ListInput;
import zowe.client.sdk.zosfiles.dsn.methods.DsnList;
import zowe.client.sdk.zosfiles.dsn.response.MemberDocument;

import java.util.List;

/**
 * Class example to showcase team config functionality via TeamConfig class.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class TeamConfigExp {

    /**
     * Main method defines TeamConfig object and operation to retrieve the default
     * zosmf profile from Zowe Team Configuration.
     * <p>
     * Zowe Team Configuration contains the connection information for z/OSMF REST API.
     * <p>
     * Moreover, the retrieval of the zosmf profile will retrieve secure Zowe V2 credentials
     * (username/password) entered via the Zowe Global Team Configuration command.
     * <p>
     * Calls TeamConfigExp.listMembers example method.
     *
     * @param args for main not used
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public static void main(String[] args) throws ZosmfRequestException {
        TeamConfig teamConfig;
        try {
            teamConfig = new TeamConfig();
        } catch (TeamConfigException e) {
            throw new RuntimeException(e.getMessage());
        }
        ProfileDao profile = teamConfig.getDefaultProfile("zosmf");
        return (ZosConnectionFactory.createBasicConnection(
                profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));

        TeamConfigExp.listMembers(connection, "CCSQA.ASM.JCL");
    }

    /**
     * List out all members of the given dataset
     *
     * @param connection  ZosConnection object
     * @param dataSetName data set name
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public static void listMembers(ZosConnection connection, String dataSetName) throws ZosmfRequestException {
        zowe.client.sdk.zosfiles.dsn.input.list.ListInput params = new zowe.client.sdk.zosfiles.dsn.input.list.ListInput.Builder().build();
        DsnList dsnList = new DsnList(connection);
        List<MemberDocument> datasets = dsnList.getMembers(dataSetName, params);
        datasets.forEach(System.out::println);
    }

}
`````  

