package rest;

public class Response {

    private Object result;
    private int statusCode;

    public Response(Object result, int statusCode) {
        this.result = result;
        this.statusCode = statusCode;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
