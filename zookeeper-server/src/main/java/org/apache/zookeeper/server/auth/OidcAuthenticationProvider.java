package org.apache.zookeeper.server.auth;

import org.apache.zookeeper.KeeperException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OidcAuthenticationProvider extends ServerAuthenticationProvider{
    private String googleAccessToken;

    @Override
    public String getScheme() {
        return "oidc";
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isValid(String id) {
        return false;
    }

    @Override
    public KeeperException.Code handleAuthentication(ServerObjs serverObjs, byte[] authData) {
        try {
            this.googleAccessToken = new String(authData);
            String accessTokenJwt = getAccessTokenJwt(this.googleAccessToken);
            System.out.println(accessTokenJwt);

            JSONObject json = new JSONObject(accessTokenJwt);

            List<String> scopes = Arrays.stream(json.getString("scope").split(" ")).collect(Collectors.toList());
            System.out.println(scopes);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return KeeperException.Code.OK;
    }

    @Override
    public boolean matches(ServerObjs serverObjs, MatchValues matchValues) {
        return false;
    }

    private String getAccessTokenJwt(String accessToken) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + accessToken);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");


        int statusCode = connection.getResponseCode();

        InputStream inputStream = (statusCode == 200) ? connection.getInputStream() : connection.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        return responseBuilder.toString();
    }
}
