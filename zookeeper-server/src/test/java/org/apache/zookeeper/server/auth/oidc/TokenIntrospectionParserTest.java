package org.apache.zookeeper.server.auth.oidc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.AccessTokenParameterType;
import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.Method;
import org.junit.jupiter.api.Test;

class TokenIntrospectionParserTest {

    /**
     * Rename Test Cases Appropriately
     */
    @Test
    public void testURLConstructionUsingPathParam() throws Exception {

        String tokenIntrospectionEndpoint = "https://auth-server.com/tokeninfo";
        String accessTokenParameterName = "access_token/{accessToken}";
        byte[] accessToken = "testing".getBytes();


        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.PATH_PARAMETER,
                accessTokenParameterName, tokenIntrospectionEndpoint, Method.GET);

        String actualURL = tokenIntrospectionParser.constructURL("testing".getBytes());
        String expectedURL = tokenIntrospectionEndpoint + "/" + accessTokenParameterName.replace("{accessToken}", new String(accessToken));

        assertEquals(expectedURL, actualURL);
    }

    @Test
    public void testURLConstructionUsingPathParamWithExceptionThrown() {

        String tokenIntrospectionEndpoint = "https://auth-server.com/tokeninfo";
        String accessTokenParameterName = "access_token/";
        byte[] accessToken = "testing".getBytes();


        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.PATH_PARAMETER,
                accessTokenParameterName, tokenIntrospectionEndpoint, Method.GET);

        Exception exception = assertThrows(Exception.class, () -> {
            tokenIntrospectionParser.constructURL(accessToken);
        });

        String expectedMessage = "{accessToken} is not present in the URL provided.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testURLConstructionUsingInvalidPathParam() throws Exception {

        String tokenIntrospectionEndpoint = "https://auth-server.com/tokeninfo";
        String accessTokenParameterName = "accessToken";
        byte[] accessToken = "testing".getBytes();


        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.PATH_PARAMETER,
                accessTokenParameterName, tokenIntrospectionEndpoint, Method.GET);

        String actualURL = tokenIntrospectionParser.constructURL("testing".getBytes());
        String expectedURL = tokenIntrospectionEndpoint + "/" + "access_token/{accessToken}".replace("{accessToken}", new String(accessToken));

        assertNotEquals(expectedURL, actualURL);
    }

    @Test
    void constructURLUsingQueryParameterWithValidAccessTokenParameterName() throws Exception {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.QUERY_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo",
                Method.GET);

        String url = tokenIntrospectionParser.constructURL("example_access_token".getBytes());

        assertEquals("https://auth-server.com/tokeninfo?access_token=example_access_token",url);
    }

    @Test
    void constructURLUsingQueryParameterWithInvalidAccessTokenParameterName() throws Exception {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.QUERY_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo",
                Method.GET);

        String url = tokenIntrospectionParser.constructURL("example_access_token".getBytes());

        assertNotEquals("https://auth-server.com/tokeninfo?accessToken=example_access_token",url);
    }
}