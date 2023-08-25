package org.apache.zookeeper.server.auth;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.server.ServerCnxn;
import org.apache.zookeeper.server.auth.token.JWTParser;
import org.apache.zookeeper.server.auth.token.OpaqueTokenParser;
import org.apache.zookeeper.server.auth.token.StandardizedTokenData;
import org.apache.zookeeper.server.auth.token.AccessTokenParser;
import org.json.JSONObject;

import java.util.Base64;

public class OidcAuthenticationProvider implements AuthenticationProvider{
    @Override
    public String getScheme() {
        return "oidc";
    }

    @Override
    public KeeperException.Code handleAuthentication(ServerCnxn cnxn, byte[] authData) {
        String accessToken = new String(authData);
        StandardizedTokenData standardizedTokenData = getStandardizedStructure(accessToken);
        return  KeeperException.Code.AUTHFAILED;
    }

    @Override
    public boolean matches(String id, String aclExpr) {
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isValid(String id) {
        return false;
    }

    public StandardizedTokenData getStandardizedStructure(String accessToken) {
        AccessTokenParser tokenProcessor;
        if(isAccessTokenJWT(accessToken)){
            tokenProcessor = new JWTParser();

            JSONObject claims = tokenProcessor.extractClaimsFromAccessToken(accessToken);

            if( tokenProcessor.validateClaims(claims)){
                return tokenProcessor.getStandardizedTokenStructure(claims);
            }

        } else{
            tokenProcessor = new OpaqueTokenParser();
            JSONObject claims = tokenProcessor.extractClaimsFromAccessToken(accessToken);

            if(claims != null && tokenProcessor.validateClaims(claims)){
                return tokenProcessor.getStandardizedTokenStructure(claims);
            }
        }
        return null;
    }

    private boolean isAccessTokenJWT(String accessToken){
        try{
            Base64.getDecoder().decode(accessToken.split("\\.")[1]);
            return true;
        }catch(Exception exception){
            return false;
        }
    }
}
