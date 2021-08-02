package rest;

import core.ZOSConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;

import java.util.Map;
import java.util.Optional;

public abstract class ZoweRequest {

    private ZoweRequestType.RequestType requestType;
    protected ZOSConnection connection;
    protected HttpClient client;
    protected HttpContext localContext = new BasicHttpContext();
    protected HttpResponse httpResponse;

    public ZoweRequest(ZOSConnection connection, ZoweRequestType.RequestType requestType) {
        this.connection = connection;
        this.requestType = requestType;
    }

    public abstract Response executeHttpRequest() throws Exception;

    public abstract void setStandardHeaders();

    public abstract void setHeaders(Map<String, String> headers);

    public abstract void setRequest(Optional<String> url) throws Exception;

    protected void setup() {
        setStandardHeaders();
        try {
            client = HttpClients.custom().setSSLContext(
                            new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ZoweRequestType.RequestType requestType() {
        return requestType;
    }

}
