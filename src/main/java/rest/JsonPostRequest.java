package rest;

import core.ZOSConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
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

public class JsonPostRequest extends ZoweRequest {

    private static final Logger LOG = LogManager.getLogger(JsonPostRequest.class);

    private HttpPost request;
    private Map<String, String> headers = new HashMap<>();

    public JsonPostRequest(ZOSConnection connection, Optional<String> url) throws Exception {
        super(connection, ZoweRequestType.RequestType.POST_JSON);
        this.request = new HttpPost(url.orElseThrow(() -> new Exception("url not specified")));
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

        LOG.debug("JsonPostRequest::httpPost - Response statusCode {}, Response {}",
                httpResponse.getStatusLine().getStatusCode(), httpResponse.toString());

        if (Util.isHttpError(statusCode)) {
            return new Response(Optional.ofNullable(httpResponse.getStatusLine().getReasonPhrase()),
                    Optional.ofNullable(statusCode));
        }

        String result = null;
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
            LOG.debug("JsonPostRequest::httpPost - result = {}", result);
        }

        JSONParser parser = new JSONParser();
        try {
            return new Response(Optional.ofNullable(parser.parse(result)), Optional.ofNullable(statusCode));
        } catch (ParseException e) {
            e.printStackTrace();
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
        this.request = new HttpPost(url.orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

}
