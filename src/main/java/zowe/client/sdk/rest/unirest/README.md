# unirest Package

Http functionality used by the SDK via Unirest for Java.

This package provides an alternative to the legacy org.apache.http library implemented and in place since the beginning.

With legacy, http request failure does not retrieve the IBM z/OSMF's JSON error report document which provides
information on why the REST API request failed.

Unirest library provides the ability to retrieve JSON error document.

For example, the following http GET request will result in an HTTP 500 error:

    https://xxxxxxx.xxxxx.net:xxxx/zosmf/restfiles/ds?

and the JSON error report document body response is:

    {"rc":4,"reason":13,"category":1,"message":"query parm dslevel= or volser= must be specified"} 

At the moment the legacy rest processing will stay in place while unirest is given a test drive.

You are welcome to use unirest processing by importing the API classes existing within the unirest package.  
  
