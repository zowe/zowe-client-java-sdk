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

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.UUID;

public class HttpResponseInvalidJsonMock {

    private final HttpResponse httpResponse = new HttpResponse() {

        private final String json = UUID.randomUUID().toString();

        @Override
        public StatusLine getStatusLine() {
            return new StatusLine() {
                @Override
                public ProtocolVersion getProtocolVersion() {
                    return null;
                }

                @Override
                public int getStatusCode() {
                    return 200;
                }

                @Override
                public String getReasonPhrase() {
                    return json;
                }
            };
        }

        @Override
        public void setStatusLine(StatusLine statusLine) {

        }

        @Override
        public void setStatusLine(ProtocolVersion protocolVersion, int i) {

        }

        @Override
        public void setStatusLine(ProtocolVersion protocolVersion, int i, String s) {

        }

        @Override
        public void setStatusCode(int i) throws IllegalStateException {

        }

        @Override
        public void setReasonPhrase(String s) throws IllegalStateException {

        }

        @Override
        public HttpEntity getEntity() {
            return new HttpEntity() {
                @Override
                public boolean isRepeatable() {
                    return false;
                }

                @Override
                public boolean isChunked() {
                    return false;
                }

                @Override
                public long getContentLength() {
                    return 0;
                }

                @Override
                public Header getContentType() {
                    return null;
                }

                @Override
                public Header getContentEncoding() {
                    return null;
                }

                @Override
                public InputStream getContent() throws UnsupportedOperationException {
                    InputStream stubInputStream =
                            IOUtils.toInputStream(json, "UTF-8");
                    return stubInputStream;
                }

                @Override
                public void writeTo(OutputStream outputStream) {

                }

                @Override
                public boolean isStreaming() {
                    return false;
                }

                @Override
                public void consumeContent() {

                }
            };
        }

        @Override
        public void setEntity(HttpEntity httpEntity) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public void setLocale(Locale locale) {

        }

        @Override
        public ProtocolVersion getProtocolVersion() {
            return null;
        }

        @Override
        public boolean containsHeader(String s) {
            return false;
        }

        @Override
        public Header[] getHeaders(String s) {
            return new Header[0];
        }

        @Override
        public Header getFirstHeader(String s) {
            return null;
        }

        @Override
        public Header getLastHeader(String s) {
            return null;
        }

        @Override
        public Header[] getAllHeaders() {
            return new Header[0];
        }

        @Override
        public void addHeader(Header header) {

        }

        @Override
        public void addHeader(String s, String s1) {

        }

        @Override
        public void setHeader(Header header) {

        }

        @Override
        public void setHeader(String s, String s1) {

        }

        @Override
        public void setHeaders(Header[] headers) {

        }

        @Override
        public void removeHeader(Header header) {

        }

        @Override
        public void removeHeaders(String s) {

        }

        @Override
        public HeaderIterator headerIterator() {
            return null;
        }

        @Override
        public HeaderIterator headerIterator(String s) {
            return null;
        }

        @Override
        public HttpParams getParams() {
            return null;
        }

        @Override
        public void setParams(HttpParams httpParams) {

        }
    };

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

}
