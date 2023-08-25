package org.apache.zookeeper.server.auth.token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleTokenParser implements TokenIntrospectiveParser {
    @Override
    public String getAccessTokenJWT(String accessToken) {
        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + accessToken);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
