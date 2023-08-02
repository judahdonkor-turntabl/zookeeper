package org.apache.zookeeper.server.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import com.auth0.jwt.JWT;
import org.apache.zookeeper.data.Id;

import java.util.*;
import java.util.stream.Collectors;

public class OidcAuthenticationProvider extends ServerAuthenticationProvider {
    private static final Map<String, Integer> SCOPE_PERM_MAP = Collections.unmodifiableMap(new HashMap<String, Integer>() {{
        put("zookeeper:znode:create", ZooDefs.Perms.CREATE);
        put("zookeeper:znode:read", ZooDefs.Perms.READ);
        put("zookeeper:znode:write", ZooDefs.Perms.WRITE);
        put("zookeeper:znode:delete", ZooDefs.Perms.DELETE);
    }});

    private DecodedJWT jwt;
  
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
        return jwt.getClaim("aud") != null && jwt.getClaim("exp") != null;
    }

    @Override
    public KeeperException.Code handleAuthentication(ServerObjs serverObjs, byte[] authData) {
        String accessToken = new String(authData);
        this.jwt = JWT.decode(accessToken);
        String sub = this.jwt.getClaim("sub").asString();

        if(sub != null){
            serverObjs.getCnxn().addAuthInfo(new Id("oidc", sub));
            return KeeperException.Code.OK;
        }

        return KeeperException.Code.AUTHFAILED;
    }

    @Override
    public boolean matches(ServerObjs serverObjs, MatchValues matchValues) {
        if (this.jwt == null) return false;

        String sub = this.jwt.getClaim("sub").asString();
        System.out.println("Sub: " + sub);
        if (sub == null || !sub.equals(matchValues.getAclExpr())) return false;

        List<String> scopes = Arrays.asList(this.jwt.getClaim("scope").asString().split(" "));
        System.out.println("Scopes: " + scopes);
        Set<Integer> allowedPerms = getAllowedPermsFromScopes(scopes);

        return allowedPerms.contains(matchValues.getPerm());
    }

    private Set<Integer> getAllowedPermsFromScopes(List<String> scopes) {
        return scopes.stream()
                .map(SCOPE_PERM_MAP::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}