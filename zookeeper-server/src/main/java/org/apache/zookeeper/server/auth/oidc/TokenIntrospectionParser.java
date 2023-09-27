package org.apache.zookeeper.server.auth.oidc;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class TokenIntrospectionParser implements AccessTokenParser {

    private final AccessTokenParameterType accessTokenParameterType;
    private final String accessTokenParameterName;
    private final String tokenIntrospectionEndpoint;
    private final Method method;

    public TokenIntrospectionParser(AccessTokenParameterType accessTokenParameterType,
        String accessTokenParameterName, String tokenIntrospectionEndpoint, Method method) {
        this.accessTokenParameterType = accessTokenParameterType;
        this.accessTokenParameterName = accessTokenParameterName;
        this.tokenIntrospectionEndpoint = tokenIntrospectionEndpoint;
        this.method = method;
    }

    public String constructURL(byte[] accessTokenBytes) {
        if(accessTokenParameterType.equals(AccessTokenParameterType.PATH_PARAMETER)){
            return tokenIntrospectionEndpoint + "/" + accessTokenParameterName + "/" + new String(accessTokenBytes);
        }else if(accessTokenParameterType.equals(AccessTokenParameterType.QUERY_PARAMETER)){
            return tokenIntrospectionEndpoint + "?" + accessTokenParameterName + "=" + new String(accessTokenBytes);
        }
        return null;
    }
    //    Authorization Server Developer
//    access token
//    endpoint
//no standard regarding endpoint

//    https://auth-server.com/tokeninfo?access_token={accessToken}

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

        HttpClient httpClient = HttpClients.createDefault();
        String url = constructURL(accessTokenBytes);
        HttpResponse response;

        try {
            HttpUriRequest uriRequest = buildUri(url);
            response = httpClient.execute(uriRequest);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Do something with response

        throw new UnsupportedOperationException();
    }

    public HttpUriRequest buildUri(String url){

        RequestBuilder requestBuilder;

        switch (method){
            case GET:
                requestBuilder = RequestBuilder.get(url)
                        .addHeader("get", "getter")
                        .addParameter("abc", "def");
                break;
            case POST:
                requestBuilder = RequestBuilder.post(url)
                        .addHeader("post", "poster")
                        .addParameter("mno", "qrt");
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return requestBuilder.build();
    }

    public enum Method {
        GET,
        POST
    }

    public enum AccessTokenParameterType {
        QUERY_PARAMETER,
        PATH_PARAMETER
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
