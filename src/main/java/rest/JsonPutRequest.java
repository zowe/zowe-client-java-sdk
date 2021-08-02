package rest;

import core.ZOSConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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

public class JsonPutRequest extends ZoweRequest {

    private static final Logger LOG = LogManager.getLogger(JsonPutRequest.class);

    private HttpPut request;
    private Optional<String> body;
    private Map<String, String> headers = new HashMap<>();

    public JsonPutRequest(ZOSConnection connection, Optional<String> url, Optional<String> body) throws Exception {
        super(connection, ZoweRequestType.RequestType.PUT_JSON);
        this.body = body;
        this.request = new HttpPut(url.orElseThrow(() -> new Exception("url not specified")));
    }

    @Override
    public Response executeHttpRequest() throws Exception {
        // add any additional headers...
        headers.forEach((key, value) -> request.setHeader(key, value));
        request.setEntity(new StringEntity(body.orElse("")));

        try {
            this.httpResponse = client.execute(request, localContext);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        LOG.debug("JsonPutRequest::httpPut - Response statusCode {}, Response {}",
                httpResponse.getStatusLine().getStatusCode(), httpResponse.toString());

        if (Util.isHttpError(statusCode)) {
            return new Response(Optional.ofNullable(httpResponse.getStatusLine().getReasonPhrase()),
                    Optional.ofNullable(statusCode));
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("JsonPutRequest::httpPut - result = {}", result);

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
        String key = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
        String value = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);

        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
        request.setHeader("Content-Type", "application/json");
        request.setHeader(key, value);
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void setRequest(Optional<String> url) throws Exception {
        this.request = new HttpPut(url.orElseThrow(() -> new Exception("url not specified")));
    }

}
