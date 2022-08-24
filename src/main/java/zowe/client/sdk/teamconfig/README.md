# TeamConfig Package

Contains APIs to read Zowe Global Team Configuration and return profile information with prudent information needed
especially for secure authentication.

## API Examples

**Retrieve zosmf profile team from team config and use it to create ZOSConnection object without hard coding username
and password and retrieve a list of members from the dataset input string**

````java
public class ListDatasets extends ZosConnection {

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
        ZOSConnection connection = new ZOSConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword());
        ListDatasets.listMembers(connection, dataSetName);
    }

    /**
     * List out all members of the given data set
     *
     * @param connection  ZOSConnection object
     * @param dataSetName data set name
     * @throws Exception error processing request
     */
    public static void listMembers(zowe.client.sdk.core.ZOSConnection connection, String dataSetName) throws Exception {
        ListParams params = new ListParams.Builder().build();
        ZosDsnList zosDsnList = new ZosDsnList(connection);
        List<String> datasets = zosDsnList.listDsnMembers(dataSetName, params);
        datasets.forEach(LOG::info);
    }

}
`````  

