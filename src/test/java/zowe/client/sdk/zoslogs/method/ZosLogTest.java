/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zoslogs.method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zoslogs.input.ZosLogInputData;
import zowe.client.sdk.zoslogs.response.ZosLogResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit test for ZosLog class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosLogTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");

    private GetJsonZosmfRequest mockJsonGetRequest;

    @BeforeEach
    public void init() {
        mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
    }

    @Test
    public void tstIssueCommandParsesValidResponse() throws ZosmfRequestException {
        String json = "{\n" +
                "  \"nextTimestamp\": 1621920856259,\n" +
                "  \"source\": \"OPERLOGS\",\n" +
                "  \"totalitems\": 2,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"jobName\": \"BPXAS   \",\n" +
                "      \"system\": \"SY1     \",\n" +
                "      \"color\": \"green\",\n" +
                "      \"replyId\": \"0\",\n" +
                "      \"messageId\": \"1163454704\",\n" +
                "      \"subType\": \"NULL\",\n" +
                "      \"time\": \"Tue May 25 05:34:17 GMT 2021\",\n" +
                "      \"message\": \" $HASP373 BPXAS    STARTED\",\n" +
                "      \"type\": \"HARDCOPY\",\n" +
                "      \"cart\": \"0\",\n" +
                "      \"timestamp\": 1621920857500\n" +
                "    },\n" +
                "    {\n" +
                "      \"jobName\": \"BPXAS   \",\n" +
                "      \"system\": \"SY1     \",\n" +
                "      \"color\": \"green\",\n" +
                "      \"replyId\": \"0\",\n" +
                "      \"messageId\": \"1163454960\",\n" +
                "      \"subType\": \"NULL\",\n" +
                "      \"time\": \"Tue May 25 05:34:18 GMT 2021\",\n" +
                "      \"message\": \" BPXP024I BPXAS INITIATOR STARTED ON BEHALF OF JOB IZUSVR13 RUNNING IN ASID 0028\",\n" +
                "      \"type\": \"HARDCOPY\",\n" +
                "      \"cart\": \"0\",\n" +
                "      \"timestamp\": 1621920858120\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosLog zosLog = new ZosLog(connection, mockJsonGetRequest);

        ZosLogInputData inputData = Mockito.mock(ZosLogInputData.class);
        Mockito.when(inputData.getStartTime()).thenReturn(Optional.empty());
        Mockito.when(inputData.getTimeRange()).thenReturn(Optional.empty());
        Mockito.when(inputData.getDirection()).thenReturn(Optional.empty());
        Mockito.when(inputData.getHardCopy()).thenReturn(Optional.empty());
        Mockito.when(inputData.getQueryCount()).thenReturn(1);

        ZosLogResponse response = zosLog.issueCommand(inputData);

        assertNotNull(response);
        assertEquals(2, response.getTotalItems());
        assertEquals("OPERLOGS", response.getSource());
        assertEquals("BPXAS   ", response.getItems().get(0).getJobName());
        assertEquals("BPXAS   ", response.getItems().get(1).getJobName());
        assertEquals(" BPXP024I BPXAS INITIATOR STARTED ON BEHALF OF JOB IZUSVR13 RUNNING IN ASID 0028",
                response.getItems().get(1).getMessage());
    }

    @Test
    public void tstIssueCommandThrowsOnNullInputFailure() {
        ZosLog zosLog = new ZosLog(connection, mockJsonGetRequest);
        assertThrows(NullPointerException.class, () -> zosLog.issueCommand(null));
    }

    @Test
    public void tstIssueCommandThrowsOnInvalidJsonFailure() throws ZosmfRequestException {
        String json = "{ invalid json }";
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosLog zosLog = new ZosLog(connection, mockJsonGetRequest);
        ZosLogInputData inputData = Mockito.mock(ZosLogInputData.class);
        Mockito.when(inputData.getStartTime()).thenReturn(Optional.empty());
        Mockito.when(inputData.getTimeRange()).thenReturn(Optional.empty());
        Mockito.when(inputData.getDirection()).thenReturn(Optional.empty());
        Mockito.when(inputData.getHardCopy()).thenReturn(Optional.empty());
        Mockito.when(inputData.getQueryCount()).thenReturn(1);

        assertThrows(ZosmfRequestException.class, () -> zosLog.issueCommand(inputData));
    }

    @Test
    public void tstIssueCommandParsesValidWithOneItemResponseSuccess() throws ZosmfRequestException {
        String json = "{\n" +
                "  \"nextTimestamp\": 1621920856259,\n" +
                "  \"source\": \"OPERLOGS\",\n" +
                "  \"totalitems\": 1,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"jobName\": \"BPXAS   \",\n" +
                "      \"system\": \"SY1     \",\n" +
                "      \"color\": \"green\",\n" +
                "      \"replyId\": \"0\",\n" +
                "      \"messageId\": \"1163454704\",\n" +
                "      \"subType\": \"NULL\",\n" +
                "      \"time\": \"Tue May 25 05:34:17 GMT 2021\",\n" +
                "      \"message\": \" $HASP373 BPXAS    STARTED\",\n" +
                "      \"type\": \"HARDCOPY\",\n" +
                "      \"cart\": \"0\",\n" +
                "      \"timestamp\": 1621920857500\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosLog zosLog = new ZosLog(connection, mockJsonGetRequest);

        ZosLogInputData inputData = Mockito.mock(ZosLogInputData.class);
        Mockito.when(inputData.getStartTime()).thenReturn(Optional.empty());
        Mockito.when(inputData.getTimeRange()).thenReturn(Optional.empty());
        Mockito.when(inputData.getDirection()).thenReturn(Optional.empty());
        Mockito.when(inputData.getHardCopy()).thenReturn(Optional.empty());
        Mockito.when(inputData.getQueryCount()).thenReturn(1);

        ZosLogResponse response = zosLog.issueCommand(inputData);

        assertNotNull(response);
        assertEquals(1, response.getTotalItems());
        assertEquals("OPERLOGS", response.getSource());
        assertEquals("BPXAS   ", response.getItems().get(0).getJobName());
    }

    @Test
    public void tstIssueCommandHandlesEmptyItemsSuccess() throws ZosmfRequestException {
        String json = "{ \"nextTimestamp\": 1234567890, \"source\": \"OPERLOGS\", \"totalitems\": 0, \"items\": [] }";

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosLog zosLog = new ZosLog(connection, mockJsonGetRequest);

        ZosLogInputData inputData = Mockito.mock(ZosLogInputData.class);
        Mockito.when(inputData.getStartTime()).thenReturn(Optional.empty());
        Mockito.when(inputData.getTimeRange()).thenReturn(Optional.empty());
        Mockito.when(inputData.getDirection()).thenReturn(Optional.empty());
        Mockito.when(inputData.getHardCopy()).thenReturn(Optional.empty());
        Mockito.when(inputData.getQueryCount()).thenReturn(1);

        ZosLogResponse response = zosLog.issueCommand(inputData);

        assertNotNull(response);
        assertEquals(0, response.getTotalItems());
        assertEquals(0, response.getItems().size());
    }

    @Test
    public void tstIssueCommandHandlesEmptyOrNullMemberValuesSuccess() throws ZosmfRequestException {
        String json = "{\n" +
                "  \"nextTimestamp\": null,\n" +
                "  \"source\": \"\",\n" +
                "  \"totalitems\": 1,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"jobName\": null,\n" +
                "      \"system\": \"\",\n" +
                "      \"color\": null,\n" +
                "      \"replyId\": null,\n" +
                "      \"messageId\": \"\",\n" +
                "      \"subType\": null,\n" +
                "      \"time\": \"\",\n" +
                "      \"message\": null,\n" +
                "      \"type\": null,\n" +
                "      \"cart\": \"\",\n" +
                "      \"timestamp\": null\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosLog zosLog = new ZosLog(connection, mockJsonGetRequest);

        ZosLogInputData inputData = Mockito.mock(ZosLogInputData.class);
        Mockito.when(inputData.getStartTime()).thenReturn(Optional.empty());
        Mockito.when(inputData.getTimeRange()).thenReturn(Optional.empty());
        Mockito.when(inputData.getDirection()).thenReturn(Optional.empty());
        Mockito.when(inputData.getHardCopy()).thenReturn(Optional.empty());
        Mockito.when(inputData.getQueryCount()).thenReturn(1);

        ZosLogResponse response = zosLog.issueCommand(inputData);

        assertNotNull(response);
        assertEquals(1, response.getTotalItems());
        assertEquals("", response.getSource()); // empty string
        assertEquals(0L, response.getNextTimeStamp()); // null value
        assertEquals("", response.getItems().get(0).getJobName());
        assertEquals("", response.getItems().get(0).getSystem());
        assertEquals("", response.getItems().get(0).getColor());
        assertEquals("", response.getItems().get(0).getMessage());
    }

}
