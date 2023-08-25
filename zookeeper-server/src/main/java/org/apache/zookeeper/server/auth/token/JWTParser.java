package org.apache.zookeeper.server.auth.token;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class JWTParser implements AccessTokenParser {
    @Override
    public JSONObject extractClaimsFromAccessToken(String accessToken) {
        String[] jwtParts = accessToken.split("\\.");
        String jwtPayload = new String(Base64.getDecoder().decode(jwtParts[1]));

        return new JSONObject(jwtPayload);
    }

    @Override
    public boolean validateClaims(JSONObject claims) {
        long expiryTimestamp = claims.getLong("exp")*1000;
        long currentTimestamp = System.currentTimeMillis();
        return expiryTimestamp > currentTimestamp;
    }

    @Override
    public StandardizedTokenData getStandardizedTokenStructure(JSONObject claims) {
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
}
