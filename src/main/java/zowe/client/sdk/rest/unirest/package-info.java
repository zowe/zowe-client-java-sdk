/**
 * Http functionality used by the SDK via UNIREST library.
 * <p>
 * This package provides an alternative to the legacy org.apache.http library implemented and in place since
 * the beginning.
 * <p>
 * With legacy, http request failure does not retrieve the IBM z/OSMF's JSON error report document which provides
 * information on why the REST API request failed.
 * <p>
 * UNIREST library provides the ability to retrieve JSON error document.
 * <p>
 * For example, the following http GET request will result in an HTTP 500 error:
 * https://xxxxxxx.broadcom.net:xxxx/zosmf/restfiles/ds?
 * <p>
 * and the JSON error report document body response is:
 * {"rc":4,"reason":13,"category":1,"message":"query parm dslevel= or volser= must be specified"}
 */
package zowe.client.sdk.rest.unirest;