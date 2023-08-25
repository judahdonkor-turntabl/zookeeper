package org.apache.zookeeper.server.auth.token;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpaqueTokenParser implements AccessTokenParser {
    @Override
    public JSONObject extractClaimsFromAccessToken(String accessToken) {
        String oidcProvider = getOidcProviderFromZookeeperConfigurationFile();
        if(oidcProvider.equals("google")){
            TokenIntrospectiveParser opaqueTokenValidator = new GoogleTokenParser();
            String accessTokenJWT = opaqueTokenValidator.getAccessTokenJWT(accessToken);

            return new JSONObject(accessTokenJWT);
        }
        return null;
    }

    @Override
    public boolean validateClaims(JSONObject claims) {
        if(claims != null){
            return claims.has("exp");
        }

        return false;
    }

    @Override
    public StandardizedTokenData getStandardizedTokenStructure(JSONObject claims) {
        if(claims != null){
            String userId = claims.getString("sub");
            String groups = claims.getString("scope");

            if(groups != null){
                List<String> groupsList = new ArrayList<>();

                Arrays.asList(groups.split(" ")).forEach(group -> {
                    groupsList.add(group.replace("zookeeper:znode:", ""));
                });
                return new StandardizedTokenData(userId, groupsList);
            }

            return new StandardizedTokenData(userId, null);
        }
        return null;
    }


    public String getOidcProviderFromZookeeperConfigurationFile(){
        String oidcProvider = null;
        String configFile = "../zookeeper-runner/zoo.cfg";

        Map<String, String> configMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    configMap.put(key, value);
                }
            }

            oidcProvider = configMap.get("oidcProvider");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return oidcProvider;
    }
}
