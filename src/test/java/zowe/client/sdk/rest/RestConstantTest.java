package zowe.client.sdk.rest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class containing unit test for RestConstant.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class RestConstantTest {


    /**
     * Test immutability
     */
    @Test
    public void tstRestConstantIsImmutableSuccess() {
        try {
            RestConstant.HTTP_STATUS.put(401, "test");
        } catch (UnsupportedOperationException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Validate all values are correct
     */
    @Test
    public void tstRestConstantValidateValuesSuccess() {
        String value = RestConstant.HTTP_STATUS.get(200);
        assertEquals("ok", value);
        value = RestConstant.HTTP_STATUS.get(201);
        assertEquals("created", value);
        value = RestConstant.HTTP_STATUS.get(202);
        assertEquals("accepted", value);
        value = RestConstant.HTTP_STATUS.get(203);
        assertEquals("non authoritative information", value);
        value = RestConstant.HTTP_STATUS.get(204);
        assertEquals("no content", value);
        value = RestConstant.HTTP_STATUS.get(205);
        assertEquals("reset content", value);
        value = RestConstant.HTTP_STATUS.get(206);
        assertEquals("partial content", value);
        value = RestConstant.HTTP_STATUS.get(207);
        assertEquals("multi status", value);
        value = RestConstant.HTTP_STATUS.get(208);
        assertEquals("already reported", value);
        value = RestConstant.HTTP_STATUS.get(226);
        assertEquals("im used", value);
        value = RestConstant.HTTP_STATUS.get(300);
        assertEquals("multiple choice", value);
        value = RestConstant.HTTP_STATUS.get(301);
        assertEquals("moved permanently", value);
        value = RestConstant.HTTP_STATUS.get(302);
        assertEquals("found", value);
        value = RestConstant.HTTP_STATUS.get(303);
        assertEquals("see other", value);
        value = RestConstant.HTTP_STATUS.get(304);
        assertEquals("not modified", value);
        value = RestConstant.HTTP_STATUS.get(305);
        assertEquals("use proxy", value);
        value = RestConstant.HTTP_STATUS.get(306);
        assertEquals("unused", value);
        value = RestConstant.HTTP_STATUS.get(307);
        assertEquals("temporary redirect", value);
        value = RestConstant.HTTP_STATUS.get(308);
        assertEquals("permanent redirect", value);
        value = RestConstant.HTTP_STATUS.get(400);
        assertEquals("bad request", value);
        value = RestConstant.HTTP_STATUS.get(401);
        assertEquals("unauthorized", value);
        value = RestConstant.HTTP_STATUS.get(402);
        assertEquals("payment required", value);
        value = RestConstant.HTTP_STATUS.get(403);
        assertEquals("forbidden", value);
        value = RestConstant.HTTP_STATUS.get(404);
        assertEquals("not found", value);
        value = RestConstant.HTTP_STATUS.get(405);
        assertEquals("method not allowed", value);
        value = RestConstant.HTTP_STATUS.get(406);
        assertEquals("not acceptable", value);
        value = RestConstant.HTTP_STATUS.get(407);
        assertEquals("proxy authentication required", value);
        value = RestConstant.HTTP_STATUS.get(408);
        assertEquals("request timeout", value);
        value = RestConstant.HTTP_STATUS.get(409);
        assertEquals("conflict", value);
        value = RestConstant.HTTP_STATUS.get(410);
        assertEquals("gone", value);
        value = RestConstant.HTTP_STATUS.get(411);
        assertEquals("length required", value);
        value = RestConstant.HTTP_STATUS.get(412);
        assertEquals("precondition failed", value);
        value = RestConstant.HTTP_STATUS.get(413);
        assertEquals("payload too long", value);
        value = RestConstant.HTTP_STATUS.get(414);
        assertEquals("uri too long", value);
        value = RestConstant.HTTP_STATUS.get(415);
        assertEquals("unsupported media type", value);
        value = RestConstant.HTTP_STATUS.get(416);
        assertEquals("range not satisfiable", value);
        value = RestConstant.HTTP_STATUS.get(417);
        assertEquals("expectation failed", value);
        value = RestConstant.HTTP_STATUS.get(418);
        assertEquals("in a teapot", value);
        value = RestConstant.HTTP_STATUS.get(421);
        assertEquals("misdirected request", value);
        value = RestConstant.HTTP_STATUS.get(422);
        assertEquals("unprocessable entity", value);
        value = RestConstant.HTTP_STATUS.get(423);
        assertEquals("locked", value);
        value = RestConstant.HTTP_STATUS.get(424);
        assertEquals("failed dependency", value);
        value = RestConstant.HTTP_STATUS.get(425);
        assertEquals("to early", value);
        value = RestConstant.HTTP_STATUS.get(426);
        assertEquals("upgrade required", value);
        value = RestConstant.HTTP_STATUS.get(428);
        assertEquals("precondition required", value);
        value = RestConstant.HTTP_STATUS.get(429);
        assertEquals("too many requests", value);
        value = RestConstant.HTTP_STATUS.get(430);
        assertEquals("request header fields too large", value);
        value = RestConstant.HTTP_STATUS.get(451);
        assertEquals("unavailable for legal reasons", value);
        value = RestConstant.HTTP_STATUS.get(500);
        assertEquals("internal server error", value);
        value = RestConstant.HTTP_STATUS.get(501);
        assertEquals("not implemented", value);
        value = RestConstant.HTTP_STATUS.get(502);
        assertEquals("bad gateway", value);
        value = RestConstant.HTTP_STATUS.get(503);
        assertEquals("service unavailable", value);
        value = RestConstant.HTTP_STATUS.get(504);
        assertEquals("gateway timeout", value);
        value = RestConstant.HTTP_STATUS.get(505);
        assertEquals("version not supported", value);
        value = RestConstant.HTTP_STATUS.get(506);
        assertEquals("variant also negotiates", value);
        value = RestConstant.HTTP_STATUS.get(507);
        assertEquals("insufficient storage", value);
        value = RestConstant.HTTP_STATUS.get(508);
        assertEquals("loop detected", value);
        value = RestConstant.HTTP_STATUS.get(510);
        assertEquals("not expended", value);
        value = RestConstant.HTTP_STATUS.get(511);
        assertEquals("network authentication required", value);
    }

}
