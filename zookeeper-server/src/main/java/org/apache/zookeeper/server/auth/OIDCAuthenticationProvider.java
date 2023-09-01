package org.apache.zookeeper.server.auth;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.ServerCnxn;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class OIDCAuthenticationProvider implements AuthenticationProvider {

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public KeeperException.Code handleAuthentication(ServerCnxn cnxn, byte[] authData) {
        return null;
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
