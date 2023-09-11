package org.apache.zookeeper.server.auth.oidc;

public interface AccessTokenParser {
    boolean isStructured();

    boolean isParsable();

    AccessToken parse(byte[] accessToken);
}
