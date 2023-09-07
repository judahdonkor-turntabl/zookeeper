package org.apache.zookeeper.server.auth.oidc;

import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.zookeeper.server.auth.oidc.OIDCAuthenticationProvider.GROUP_ID_PREFIX;
import static org.apache.zookeeper.server.auth.oidc.OIDCAuthenticationProvider.USER_ID_PREFIX;

public class ACLUtils {
    public static List<Id> getIdsFromAccessToken(AccessToken accessToken, String scheme) {
        List<Id> ids = new ArrayList<>();

        if (accessToken.getGroups() != null) {
            accessToken.getGroups().forEach(groupID -> ids.add(new Id(scheme, GROUP_ID_PREFIX + ":" + groupID)));
        }

        if (accessToken.getUserID() != null) {
            ids.add(new Id(scheme, USER_ID_PREFIX + ":" + accessToken.getUserID()));
        }

        return Collections.unmodifiableList(ids);
    }
}
