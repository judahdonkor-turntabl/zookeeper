package org.apache.zookeeper.server.auth.oidc;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.ServerCnxn;
import org.apache.zookeeper.server.auth.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OIDCAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OIDCAuthenticationProvider.class);
    public static final String GROUP_ID_PREFIX = "gid";
    public static final String USER_ID_PREFIX = "uid";

    @Override
    public String getScheme() {
        return "oidc";
    }

    @Override
    public KeeperException.Code handleAuthentication(ServerCnxn cnxn, byte[] authData) {
        List<Id> ids = handleAuthentication(authData);

        if (!ids.isEmpty()) {
            ids.forEach(cnxn::addAuthInfo);
            return KeeperException.Code.OK;
        }
        return KeeperException.Code.AUTHFAILED;
    }

    @Override
    public List<Id> handleAuthentication(HttpServletRequest request, byte[] authData) {
        return handleAuthentication(authData);
    }

    @Override
    public boolean matches(String id, String aclExpr) {
        return id.equals(aclExpr);
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isValid(String id) {

        if (id == null) {
            return false;
        }

        String[] parts = id.split(":");

        return parts.length == 2 &&
                (parts[0].equals(USER_ID_PREFIX)  || parts[0].equals(GROUP_ID_PREFIX));
    }

    @Override
    public String getUserName(String id) {
        return AuthenticationProvider.super.getUserName(id);
    }

    private List<Id> handleAuthentication(final byte[] authData) {

        List<Id> ids = new ArrayList<>();

        try {
            AccessToken accessToken = new AccessTokenProcessor().process(authData);
            ids = ACLUtils.getIdsFromAccessToken(accessToken, getScheme());

        } catch (Exception exception) {
            LOG.error(exception.getMessage());
        }

        return Collections.unmodifiableList(ids);
    }
}
