# TeamConfig Package

The TeamConfig package provides API method(s) to retrieve a profile section from Zowe Global Team Configuration with
keytar information to help perform connection processing without hard coding username and password. Keytar represents
credentials stored securely on your computer when performing the Zowe global initialize command which prompts you for
username and password.

## API Example

**Retrieve the default "zosmf" profile from team config. Use it to create a ZOSConnection object without hard coding
username and password and retrieve a list of members from the dataset input string.**

````java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.keytar.KeyTarImpl;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;
import zowe.client.sdk.zosfiles.ZosDsnList;
import zowe.client.sdk.zosfiles.dsn.input.ListParams;

import java.util.List;

public class ListDatasets {

    private static final Logger LOG = LoggerFactory.getLogger(ListDatasets.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * ListDatasets functionality. Calls ListDatasets example methods.
     *
     * @param args for main not used
     * @throws Exception error processing request
     */
    public static void main(String[] args) throws Exception {
        String dataSetName = "CCSQA.ASM.JCL";

        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        ZOSConnection connection = new ZOSConnection(
                profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword());

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

}
`````  

