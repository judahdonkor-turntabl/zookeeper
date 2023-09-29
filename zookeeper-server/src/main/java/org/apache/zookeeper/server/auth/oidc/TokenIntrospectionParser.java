package org.apache.zookeeper.server.auth.oidc;

public class TokenIntrospectionParser implements AccessTokenParser {

    private final AccessTokenParameterType accessTokenParameterType;
    private final String accessTokenParameterName;
    private final String tokenIntrospectionEndpoint;

    private final String accessToken = "{accessToken}";

    private final Method method;

    public TokenIntrospectionParser(AccessTokenParameterType accessTokenParameterType,
        String accessTokenParameterName, String tokenIntrospectionEndpoint, Method method) {
        this.accessTokenParameterType = accessTokenParameterType;
        this.accessTokenParameterName = accessTokenParameterName;
        this.tokenIntrospectionEndpoint = tokenIntrospectionEndpoint;
        this.method = method;
    }
  
    public String constructURL(byte[] accessTokenBytes) throws Exception {
        if(accessTokenParameterType.equals(AccessTokenParameterType.PATH_PARAMETER)) {
            if (accessTokenParameterName.contains(accessToken))
            {
                return String.format("%s/%s", tokenIntrospectionEndpoint, accessTokenParameterName)
                        .replace(accessToken, new String(accessTokenBytes));
            }

            throw new Exception(accessToken.concat(" is not present in the URL provided."));
        }
        else if(accessTokenParameterType.equals(AccessTokenParameterType.QUERY_PARAMETER)){
            return tokenIntrospectionEndpoint + "?" + accessTokenParameterName + "=" + new String(accessTokenBytes);
        }
        return null;
    }
    //    Authorization Server Developer
//    access token
//    endpoint
//no standard regarding endpoint

//    https://auth-server.com/tokeninfo?access_token={accessToken}

//    https://auth-server.com/tokeninfo/access_token/{accessToken}

//    https://auth-server.com/tokeninfo?accessToken={accessToken}

//    POST
//    https://auth-server.com/tokeninfo
//    {
//        accessToken: {accessToken}
//    }

//    GET
//    header.accessToken={accessToken}
//    https://auth.org/token

    @Override
    public AccessToken parse(byte[] accessTokenBytes) {
        throw new UnsupportedOperationException();
    }

    public enum Method {
        GET
    }

    public enum AccessTokenParameterType {
        QUERY_PARAMETER, PATH_PARAMETER,
    }

    @Override
    public boolean isStructured() {
        return false;
    }

    @Override
    public boolean isParsable() {
        return false;
    }
}
