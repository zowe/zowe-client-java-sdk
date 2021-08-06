package rest;

import core.ZOSConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonDeleteRequest extends ZoweRequest {

    private static final Logger LOG = LogManager.getLogger(JsonDeleteRequest.class);

    private HttpDelete request;
    private Map<String, String> headers = new HashMap<>();

    public JsonDeleteRequest(ZOSConnection connection, Optional<String> url) throws Exception {
        super(connection, ZoweRequestType.RequestType.DELETE_JSON);
        this.request = new HttpDelete(url.orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

    @Override
    public Response executeHttpRequest() throws Exception {
        // add any additional headers...
        headers.forEach((key, value) -> request.setHeader(key, value));
        try {
            this.httpResponse = client.execute(request, localContext);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        LOG.debug("JsonDeleteRequest::httpDelete - Response statusCode {}, Response {}",
                httpResponse.getStatusLine().getStatusCode(), httpResponse.toString());

        if (Util.isHttpError(statusCode)) {
            return new Response(Optional.ofNullable(httpResponse.getStatusLine().getReasonPhrase()),
                    Optional.ofNullable(statusCode));
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("JsonDeleteRequest::httpDelete - result = {}", result);

            JSONParser parser = new JSONParser();
            try {
                return new Response(Optional.ofNullable(parser.parse(result)), Optional.ofNullable(statusCode));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void setStandardHeaders() {
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
        request.setHeader("Content-Type", "application/json");
        request.setHeader(X_CSRF_ZOSMF_HEADER_KEY, X_CSRF_ZOSMF_HEADER_VALUE);
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void setRequest(String url) throws Exception {
        Optional<String> str = Optional.ofNullable(url);
        this.request = new HttpDelete(str.orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

}