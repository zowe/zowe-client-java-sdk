package zosfiles.samples;


import core.ZOSConnection;
import zosfiles.ZosDsn;
import core.ZOSConnection;
import zosfiles.ZosDsnCreate;
import zosfiles.input.CreateParams;
import zosfiles.input.ListParams;

public class CreateDatasetTest {

        public static void main(String[] args) throws Exception {
            String hostName = "XXX";
            String port = "XXX";
            String userName = "XXX";
            String password = "XXX";
            String dataSetName = "XXX";

            ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

            zosfiles.samples.CreateDatasetTest.tstCreateDsn(connection, dataSetName);
        }

        private static void tstCreateDsn(ZOSConnection connection, String datasetMember) {
            CreateParams parms = new CreateParams.Builder().build();
            ZosDsnCreate.createDsn(connection, datasetMember, parms);
        }



}
