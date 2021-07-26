/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package rest;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.util.Map;

public interface IZoweRequest {

    public <T> T httpGet() throws Exception;

    public <T> T httpPut() throws Exception;

    public <T> T httpPost() throws Exception;

    public <T> T httpDelete() throws IOException, Exception;

    public void setHeaders(Map<String, String> headers);

    public void setGetRequest(HttpGet getRequest);

    public void setPutRequest(HttpPut putRequest);

    public void setPostRequest(HttpPost postRequest);

    public void setDeleteRequest(HttpDelete deleteRequest);

}
