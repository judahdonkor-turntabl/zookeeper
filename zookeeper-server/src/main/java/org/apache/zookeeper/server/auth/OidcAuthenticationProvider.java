package org.apache.zookeeper.server.auth;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Id;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;

public class OidcAuthenticationProvider extends ServerAuthenticationProvider {
    private String googleAccessToken;
    private JSONObject clientJSON;
    private static final Map<String, Integer> SCOPE_PERM_MAP = Collections.unmodifiableMap(new HashMap<String, Integer>() {{
        put("openid", ZooDefs.Perms.CREATE);
        put("email", ZooDefs.Perms.READ);
        put("profile", ZooDefs.Perms.WRITE);
    }});

    @Override
    public String getScheme() {
        return "oidc";
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public boolean isValid(String id) {
        return clientJSON.getString("aud") != null && clientJSON.getString("exp") != null;
    }

    @Override
    public KeeperException.Code handleAuthentication(ServerObjs serverObjs, byte[] authData) {
        try {
            this.googleAccessToken = new String(authData);
            String accessTokenJwt = getAccessTokenJwt(this.googleAccessToken);
            System.out.println(accessTokenJwt);

            this.clientJSON = new JSONObject(accessTokenJwt);
            String sub = clientJSON.optString("sub", "");
            serverObjs.getCnxn().addAuthInfo(new Id("oidc", sub));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return KeeperException.Code.OK;
    }

    @Override
    public boolean matches(ServerObjs serverObjs, MatchValues matchValues) {
        if (clientJSON == null) return false;

        String sub = clientJSON.optString("sub", null);
        if (sub == null || !sub.equals(matchValues.getAclExpr())) return false;

        List<String> scopes = Arrays.asList(clientJSON.optString("scope", "").split(" "));
        Set<Integer> allowedPerms = getAllowedPermsFromScopes(scopes);

        return allowedPerms.contains(matchValues.getPerm());
    }

    private Set<Integer> getAllowedPermsFromScopes(List<String> scopes) {
        return scopes.stream()
                .map(SCOPE_PERM_MAP::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
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
