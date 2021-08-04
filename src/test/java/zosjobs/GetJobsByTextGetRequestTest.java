package zosjobs;

import core.ZOSConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import rest.Response;
import rest.TextGetRequest;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class GetJobsByTextGetRequestTest {

    private TextGetRequest request;
    private ZOSConnection connection;
    private GetJobs getJobs;

    @Before
    public void init() {
        request = Mockito.mock(TextGetRequest.class);
        connection = new ZOSConnection("1", "1", "1", "1");
        getJobs = new GetJobs(connection);
        Whitebox.setInternalState(getJobs, "request", request);
    }

    @Test
    public void tstGetSpoolContentByIdSuccess() throws Exception {
        Response response = new Response(Optional.of("1\n2\n3\n"), Optional.of(200));
        Mockito.when(request.executeHttpRequest()).thenReturn(response);

        String results = getJobs.getSpoolContentById("jobName", "jobId", 1);
        assertTrue("https://1:1/zosmf/restjobs/jobs/jobName/jobId/files/1/records".equals(getJobs.getUrl()));
        assertTrue("1\n2\n3\n".equals(results));
    }

}