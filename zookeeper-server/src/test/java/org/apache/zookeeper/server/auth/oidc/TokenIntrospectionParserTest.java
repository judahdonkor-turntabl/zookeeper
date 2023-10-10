package org.apache.zookeeper.server.auth.oidc;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.AccessTokenParameterType;
import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.Method;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenIntrospectionParserTest {

    /**
     * Rename Test Cases Appropriately
     */

    @Test
    void constructURIWithGetAndQueryParams() {
        String tokenParameterName = "access_token";
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.QUERY_PARAMETER,
                tokenParameterName,
                "https://auth-server.com/tokeninfo",
                Method.GET);

        HttpUriRequest uriRequest = tokenIntrospectionParser.buildUri( "example_access_token".getBytes());

        assertNotNull(uriRequest);
        assertTrue(uriRequest.getMethod().equalsIgnoreCase(Method.GET.toString()));
        assertTrue(uriRequest.getURI().toString().contains(tokenParameterName+"=example_access_token"));
    }

    @Test
    void constructURIWithGetAndPathParams() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.PATH_PARAMETER,
                "access_token",
                "https://auth-server.com/v2/{accesstoken}/tokeninfo",
                Method.GET);

        HttpUriRequest uriRequest = tokenIntrospectionParser.buildUri( "example_access_token".getBytes());

        assertNotNull(uriRequest);
        assertTrue(uriRequest.getMethod().equalsIgnoreCase(Method.GET.toString()));
        assertTrue(uriRequest.getURI().toString()
                .equalsIgnoreCase("https://auth-server.com/v2/example_access_token/tokeninfo"));
    }

    @Test
    void constructURIWithGetAndPathParamsThowsException() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.PATH_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo",
                Method.GET);


        assertThrows(IllegalArgumentException.class, () -> tokenIntrospectionParser.buildUri( "example_access_token".getBytes()));
    }

    @Test
    void constructURIWithGetAndHeaderParams() {
        String tokenParameterName = "access_token";
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.HEADER_PARAMETER,
                tokenParameterName,
                "https://auth-server.com/tokeninfo",
                Method.GET);

        HttpUriRequest uriRequest = tokenIntrospectionParser.buildUri( "example_access_token".getBytes());

        assertNotNull(uriRequest);
        assertTrue(uriRequest.getMethod().equalsIgnoreCase(Method.GET.toString()));
        assertEquals(uriRequest.getFirstHeader(tokenParameterName).getValue(),
                "example_access_token");
    }

    @Test
    void constructURIWithGetMethod_HeaderParamsAndTemplateUrl() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
                AccessTokenParameterType.HEADER_PARAMETER,
                "access_token",
                "https://auth-server.com/tokeninfo/{accesstoken}",
                Method.GET);

        assertThrows(IllegalArgumentException.class, () -> tokenIntrospectionParser.buildUri( "example_access_token".getBytes()));
    }
}