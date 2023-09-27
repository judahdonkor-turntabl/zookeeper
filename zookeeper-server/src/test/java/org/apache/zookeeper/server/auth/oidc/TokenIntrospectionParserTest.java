package org.apache.zookeeper.server.auth.oidc;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.AccessTokenParameterType;
import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.Method;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TokenIntrospectionParserTest {

    /**
     * Rename Test Cases Appropriately
     */
    @Test
    void constructURLUsingPathParameterTest() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.PATH_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo",
                Method.GET);

        String url = tokenIntrospectionParser.constructURL("example_access_token".getBytes());

        assertEquals("https://auth-server.com/tokeninfo/access_token/example_access_token",url);
    }

    @Test
    void constructURLUsingQueryParameterWithValidAccessTokenParameterName() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.QUERY_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo",
                Method.GET);

        String url = tokenIntrospectionParser.constructURL("example_access_token".getBytes());

        assertEquals("https://auth-server.com/tokeninfo?access_token=example_access_token",url);
    }

    @Test
    void constructURLUsingQueryParameterWithInvalidAccessTokenParameterName() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.QUERY_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo",
                Method.GET);

        String url = tokenIntrospectionParser.constructURL("example_access_token".getBytes());

        assertNotEquals("https://auth-server.com/tokeninfo?accessToken=example_access_token",url);
    }

    @Test
    void constructURI() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.QUERY_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo",
                Method.GET);

        HttpUriRequest uriRequest = tokenIntrospectionParser.buildUri(
                tokenIntrospectionParser.constructURL("example_access_token".getBytes())
        );

        assertNotNull(uriRequest);
        assertEquals(1, uriRequest.getAllHeaders().length);
        assertEquals("getter", uriRequest.getFirstHeader("get").getValue());
    }
}