package zowe.client.sdk.zosworkflow.methods;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * Class containing unit tests for WorkflowCreate.
 *
 * @author Carlo Maria Proietti
 */
public class WorkflowCreateTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");

    private PostJsonZosmfRequest mockPostJsonZosmfRequest;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockPostJsonZosmfRequest = Mockito.mock(PostJsonZosmfRequest.class);
        Mockito.when(mockPostJsonZosmfRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 201, "created"));
        doCallRealMethod().when(mockPostJsonZosmfRequest).setUrl(any());
        doCallRealMethod().when(mockPostJsonZosmfRequest).getUrl();
    }

    /**
     * This test assures that the URI of the request is correctly set and that it is
     * possible to create a request without optional properties.
     */
    @Test
    public void testSimpleWorkflowCreateSuccess() throws ZosmfRequestException {
        final WorkflowCreate workflowCreate = new WorkflowCreate(connection, mockPostJsonZosmfRequest);
        final Response response = workflowCreate.create
                ("WFNAME", "WF_DF", "sys", "me", "2.0", null);
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(201, response.getStatusCode().orElse(-1));
        assertEquals("created", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/workflow/rest/2.0/workflows", mockPostJsonZosmfRequest.getUrl());
    }

    /**
     * This test assures that the URI of the request is correctly set and that it is
     * possible to create a request with optional properties, including the property "variables".
     */
    @Test
    public void testComplexWorkflowCreateSuccess() throws ZosmfRequestException {
        final WorkflowCreate workflowCreate = new WorkflowCreate(connection, mockPostJsonZosmfRequest);
        Map<String, Object> map = new HashMap<>();
        // reminder: "variables" property requires an Array of Objects (z/OSMF doc)
        ArrayList<Map<String, String>> variablesList = new ArrayList<>();
        // a variable inside "variables" optional request's property
        Map<String, String> var1 = new HashMap<>();
        var1.put("name", "user_name");
        var1.put("value", "IBMUSER");
        variablesList.add(var1);
        // a variable inside "variables" optional request's property
        JSONObject var2 = new JSONObject();
        var2.put("name", "file_name");
        var2.put("value", "textfile.txt");
        variablesList.add(var2);
        // a variable inside "variables" optional request's property,
        // value represents an array which is 'stringed' according to z/OSMF documentation
        Map<String, String> var3 = new HashMap<>();
        var3.put("name", "array3");
        var3.put("value", "[ \"ZOSV23T\", \"DB211T\", {\"property1\":\"tt1\",\"dsname\":\"TEST.DSNAME.TT1\"}]");
        variablesList.add(var3);
        // 3. Finalize the map
        map.put("variables", variablesList);
        final Response response = workflowCreate.create
                ("WFNAME", "WF_DF", "sys", "me", "2.0",
                        map);
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(201, response.getStatusCode().orElse(-1));
        assertEquals("created", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/workflow/rest/2.0/workflows", mockPostJsonZosmfRequest.getUrl());
    }
}
