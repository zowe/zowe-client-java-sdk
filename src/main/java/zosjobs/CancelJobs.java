package zosjobs;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CancelJobs {

    private static final Logger LOG = LogManager.getLogger(CancelJobs.class);
    private final ZOSConnection connection;

    /**
     * CancelJobs Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Nikunj Goyal
     */
    public CancelJobs(ZOSConnection connection) {
        this.connection = connection;
    }


}
