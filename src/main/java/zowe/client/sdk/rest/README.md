# Rest Package

Http functionality used by the SDK via Unirest for Java.

Unirest library provides the ability to retrieve IBM's z/OSMF JSON error document.

For example, the following http GET request will result in an HTTP 500 error:

    https://xxxxxxx.xxxxx.net:xxxx/zosmf/restfiles/ds?

and the JSON error report document body response is:

    {"rc":4,"reason":13,"category":1,"message":"query parm dslevel= or volser= must be specified"} 
    