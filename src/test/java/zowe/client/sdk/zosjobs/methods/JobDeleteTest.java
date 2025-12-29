/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.methods;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for JobDelete.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class JobDeleteTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private DeleteJsonZosmfRequest mockJsonDeleteRequest;
    private DeleteJsonZosmfRequest mockJsonDeleteRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockJsonDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockJsonDeleteRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockJsonDeleteRequest).getUrl();

        mockJsonDeleteRequestToken = Mockito.mock(DeleteJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonDeleteRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonDeleteRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonDeleteRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonDeleteRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonDeleteRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonDeleteRequestToken).getUrl();
    }

    @Test
    public void tstJobDeleteSuccess() throws ZosmfRequestException {
        final JobDelete jobDelete = new JobDelete(connection, mockJsonDeleteRequest);
        final Response response = jobDelete.delete("JOBNAME", "JOBID", "1.0");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restjobs/jobs/JOBNAME/JOBID", mockJsonDeleteRequest.getUrl());
    }

    @Test
    public void tstJobDeleteTokenSuccess() throws ZosmfRequestException {
        final JobDelete jobDelete = new JobDelete(connection, mockJsonDeleteRequestToken);
        final Response response = jobDelete.delete("JOBNAME", "JOBID", "1.0");
        assertEquals("{X-IBM-Job-Modify-Version=1.0, X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonDeleteRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restjobs/jobs/JOBNAME/JOBID", mockJsonDeleteRequestToken.getUrl());
    }

    @Test
    public void tstJobDeleteWithVersion2TokenSuccess() throws ZosmfRequestException {
        final JobDelete jobDelete = new JobDelete(connection, mockJsonDeleteRequestToken);
        final Response response = jobDelete.delete("JOBNAME", "JOBID", "2.0");
        assertEquals("{X-IBM-Job-Modify-Version=2.0, X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonDeleteRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restjobs/jobs/JOBNAME/JOBID", mockJsonDeleteRequestToken.getUrl());
    }

    @Test
    public void tstJobDeleteSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        JobDelete jobDelete = new JobDelete(connection, request);
        assertNotNull(jobDelete);
    }

    @Test
    public void tstJobDeleteSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobDelete(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstJobDeleteSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobDelete(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstJobDeleteSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a DeleteJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JobDelete(connection, request)
        );
        assertEquals("DELETE_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstJobDeletePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        JobDelete jobDelete = new JobDelete(connection);
        assertNotNull(jobDelete);
    }

    @Test
    public void tstJobDeletePrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobDelete(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
