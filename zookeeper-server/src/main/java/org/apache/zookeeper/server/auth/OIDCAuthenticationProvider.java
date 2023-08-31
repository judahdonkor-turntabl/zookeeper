package org.apache.zookeeper.server.auth;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.ServerCnxn;
import org.apache.zookeeper.server.auth.oidc.AccessToken;
import org.apache.zookeeper.server.auth.oidc.AccessTokenProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class OIDCAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OIDCAuthenticationProvider.class);

    @Override
    public String getScheme() {
        return "oidc";
    }

    @Override
    public KeeperException.Code handleAuthentication(ServerCnxn cnxn, byte[] authData) {
        try {

            AccessToken accessToken = new AccessTokenProcessor().process(authData);
            if (!accessToken.getGroups().isEmpty()) {
                accessToken.getGroups().forEach(groupID -> cnxn.addAuthInfo(new Id(getScheme(), groupID)));
            }

            if (!accessToken.getUserID().isEmpty()) {
                cnxn.addAuthInfo(new Id(getScheme(), accessToken.getUserID()));
            }

            return KeeperException.Code.OK;
        } catch (Exception exception) {
            LOG.error(exception.getMessage());
            return KeeperException.Code.AUTHFAILED;
        }
    }

    @Override
    public List<Id> handleAuthentication(HttpServletRequest request, byte[] authData) {
        return AuthenticationProvider.super.handleAuthentication(request, authData);
    }

    @Override
    public boolean matches(String id, String aclExpr) {
        return false;
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
    public String getUserName(String id) {
        return AuthenticationProvider.super.getUserName(id);
    }
}
