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
    private final String accessTokenPlaceholder = "{accesstoken}";

    public TokenIntrospectionParser(AccessTokenParameterType accessTokenParameterType,
        String accessTokenParameterName, String tokenIntrospectionEndpoint, Method method) {
        this.accessTokenParameterType = accessTokenParameterType;
        this.accessTokenParameterName = accessTokenParameterName;
        this.tokenIntrospectionEndpoint = tokenIntrospectionEndpoint;
        this.method = method;
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
        HttpResponse response;

        try {
            HttpUriRequest uriRequest = buildUri(accessTokenBytes);
            response = httpClient.execute(uriRequest);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Do something with response and remove exception
        throw new UnsupportedOperationException();
    }

    public HttpUriRequest buildUri(byte[] accessTokenBytes){

        RequestBuilder requestBuilder = RequestBuilder.create(method.toString().toLowerCase());
        String accessToken = new String(accessTokenBytes);
        String urlEndpoint;

        if(AccessTokenParameterType.PATH_PARAMETER.equals(accessTokenParameterType)
                && tokenIntrospectionEndpoint.contains(accessTokenPlaceholder)){

            urlEndpoint = tokenIntrospectionEndpoint.replace(accessTokenPlaceholder, new String(accessTokenBytes));
        } else if (AccessTokenParameterType.QUERY_PARAMETER.equals(accessTokenParameterType)) {

            requestBuilder.addParameter(accessTokenParameterName, accessToken);
            urlEndpoint = tokenIntrospectionEndpoint;
        } else if (AccessTokenParameterType.HEADER_PARAMETER.equals(accessTokenParameterType)) {

            requestBuilder.setHeader(accessTokenParameterName, accessToken);
            urlEndpoint = tokenIntrospectionEndpoint;
        }else{
            throw new IllegalArgumentException();
        }

        requestBuilder.setUri(urlEndpoint);

        return requestBuilder.build();
    }

    public enum Method {
        GET,
        POST
    }

    public enum AccessTokenParameterType {
        QUERY_PARAMETER,
        PATH_PARAMETER,
        HEADER_PARAMETER
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
