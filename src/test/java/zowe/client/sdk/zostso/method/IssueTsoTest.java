package zowe.client.sdk.zostso.method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zostso.input.StartTsoInputData;
import zowe.client.sdk.zostso.service.TsoReplyService;
import zowe.client.sdk.zostso.service.TsoSendService;
import zowe.client.sdk.zostso.service.TsoStartService;
import zowe.client.sdk.zostso.service.TsoStopService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IssueTsoTest {

    private ZosConnection connection;
    private TsoStartService tsoStartService;
    private TsoSendService tsoSendService;
    private TsoReplyService tsoReplyService;
    private TsoStopService tsoStopService;

    @BeforeEach
    void setUp() {
        connection = mock(ZosConnection.class);
        tsoStartService = mock(TsoStartService.class);
        tsoSendService = mock(TsoSendService.class);
        tsoReplyService = mock(TsoReplyService.class);
        tsoStopService = mock(TsoStopService.class);
    }

    @Test
    void testIssueCommand_withPromptReturned() throws Exception {
        // Arrange
        String account = "ACCT123";
        String sessionId = "SESSION123";
        String command = "LISTDS";

        String firstResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"JOB STARTED\"}}]}";

        String secondResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"HIDDEN\":\"READY\"}}]}";

        when(tsoStartService.startTso(any(StartTsoInputData.class))).thenReturn(sessionId);
        when(tsoSendService.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(tsoReplyService.reply(sessionId)).thenReturn(secondResponse);

        IssueTso issueTso = new IssueTso(
                connection,
                account,
                tsoStartService,
                tsoStopService,
                tsoSendService,
                tsoReplyService
        );

        // Act
        List<String> result = issueTso.issueCommand(command);

        // Assert
        assertEquals(1, result.size());
        assertEquals("JOB STARTED", result.get(0));

        verify(tsoStartService, times(1)).startTso(any(StartTsoInputData.class));
        verify(tsoSendService, times(1)).sendCommand(sessionId, command);
        verify(tsoReplyService, atLeastOnce()).reply(sessionId);
        verify(tsoStopService, times(1)).stopTso(sessionId);
    }

    @Test
    void testIssueCommand_withoutPrompt_loopBreaksOnFirstReply() throws Exception {
        // Arrange
        String account = "ACCT999";
        String sessionId = "SESSION999";
        String command = "STATUS";

        String firstResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"RUNNING\"}}]}";

        String replyResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"HIDDEN\":\"READY\"}}]}";

        when(tsoStartService.startTso(any(StartTsoInputData.class))).thenReturn(sessionId);
        when(tsoSendService.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(tsoReplyService.reply(sessionId)).thenReturn(replyResponse);

        IssueTso issueTso = new IssueTso(
                connection,
                account,
                tsoStartService,
                tsoStopService,
                tsoSendService,
                tsoReplyService
        );

        // Act
        List<String> result = issueTso.issueCommand(command);

        // Assert
        assertEquals(List.of("RUNNING"), result);
        verify(tsoStopService).stopTso(sessionId);
    }

    @Test
    void testIssueCommand_throwsWhenStartFails() throws Exception {
        when(tsoStartService.startTso(any())).thenThrow(new ZosmfRequestException("start failed"));

        IssueTso issueTso = new IssueTso(
                connection,
                "ACCTFAIL",
                tsoStartService,
                tsoStopService,
                tsoSendService,
                tsoReplyService
        );

        try {
            issueTso.issueCommand("TIME");
        } catch (ZosmfRequestException e) {
            assertEquals("start failed", e.getMessage());
        }

        verify(tsoStopService, never()).stopTso(anyString());
    }

}
