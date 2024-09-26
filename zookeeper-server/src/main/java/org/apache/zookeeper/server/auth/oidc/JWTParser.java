package org.apache.zookeeper.server.auth.oidc;

public class JWTParser implements AccessTokenParser{
    @Override
    public boolean isStructured() {
        return false;
    }

    @Override
    public boolean isParsable() {
        return false;
    }

    @Override
    public AccessToken parse(byte[] accessToken) {
        return null;
    }
}
