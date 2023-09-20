package org.apache.zookeeper.server.auth.oidc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.AccessTokenParameterType;
import org.apache.zookeeper.server.auth.oidc.TokenIntrospectionParser.Method;
import org.junit.jupiter.api.Test;

class TokenIntrospectionParserTest {

    /**
     * Rename Test Cases Appropriately
     */
    @Test
    void exampleTest() {
        TokenIntrospectionParser tokenIntrospectionParser = new TokenIntrospectionParser(
            AccessTokenParameterType.QUERY_PARAMETER,
            "access_token",
            "https://auth-server.com/tokeninfo",
            Method.GET);

        String url = tokenIntrospectionParser.constructURL("example-access-token".getBytes());

        assertEquals("https://auth-server.com/tokeninfo?access_token=example-access-token",
            url);
    }
}