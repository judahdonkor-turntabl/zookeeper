package org.apache.zookeeper.server.auth.oidc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TokenIntrospectionParser implements AccessTokenParser{
    @Override
    public boolean isStructured() {
        return false;
    }

    @Override
    public boolean isParsable() {
        return false;
    }

    @Override
    public AccessToken parse(byte[] accessTokenBytes) {
        AccessToken accessToken = new AccessToken();
        String accessTokenString = new String(accessTokenBytes, StandardCharsets.UTF_8);
        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + accessTokenString);
            JsonNode jwt = getJWTFromTokenEndpoint(url);

            if(jwt != null){
                String userId = jwt.get("sub").asText();
                String groups = jwt.get("scope").asText();

                accessToken.setUserID(userId);

                if(groups != null){
                    Set<String> groupsList = new HashSet<>();

                    Arrays.asList(groups.split(" ")).forEach(group -> {
                        groupsList.add(group.replace("zookeeper:znode:", ""));
                    });
                    accessToken.setGroups(groupsList);
                    return accessToken;
                }

                return accessToken;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private JsonNode getJWTFromTokenEndpoint(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            // Parse the JSON response directly into a JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(connection.getInputStream());
        } else {
            System.out.println("Token validation failed with response code: " + statusCode);
            return null;
        }

    }
}
