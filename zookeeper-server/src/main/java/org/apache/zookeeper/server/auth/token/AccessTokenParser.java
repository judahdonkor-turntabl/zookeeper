package org.apache.zookeeper.server.auth.token;

import org.json.JSONObject;

public interface AccessTokenParser {
    JSONObject extractClaimsFromAccessToken(String accessToken);

    boolean validateClaims(JSONObject claims);

    StandardizedTokenData getStandardizedTokenStructure(JSONObject claims);
}
