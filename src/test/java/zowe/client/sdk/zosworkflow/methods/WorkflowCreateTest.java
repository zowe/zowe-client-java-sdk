package zowe.client.sdk.zosworkflow;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.methods.JobCancel;
import zowe.client.sdk.zosworkflow.methods.WorkflowCreate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkflowCreateTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");

    private PostJsonZosmfRequest mockPostJsonZosmfRequest;

    @Test
    public void tstWorkflowCreateSuccess() throws ZosmfRequestException {
        final WorkflowCreate workflowCreate = new WorkflowCreate(connection, mockPostJsonZosmfRequest);
        final Response response = jobCancel.cancel("JOBNAME", "JOBID", "1.0");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restjobs/jobs/JOBNAME/JOBID", mockPutJsonZosmfRequest.getUrl());
    }
}
