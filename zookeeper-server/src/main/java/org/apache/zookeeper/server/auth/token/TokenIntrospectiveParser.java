package org.apache.zookeeper.server.auth.token;

public interface TokenIntrospectiveParser {
    String getAccessTokenJWT(String accessToken);
}
