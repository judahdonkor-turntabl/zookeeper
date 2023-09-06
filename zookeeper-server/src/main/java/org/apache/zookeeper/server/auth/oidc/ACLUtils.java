package org.apache.zookeeper.server.auth.oidc;

import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ACLUtils {
    public static List<Id> getIdsFromAccessToken(AccessToken accessToken, String scheme) {
        List<Id> ids = new ArrayList<>();

        if (accessToken.getGroups() != null) {
            accessToken.getGroups().forEach(groupID -> ids.add(new Id(scheme, "gid:" + groupID)));
        }

        if (accessToken.getUserID() != null) {
            ids.add(new Id(scheme, "uid:" + accessToken.getUserID()));
        }

        return Collections.unmodifiableList(ids);
    }
}
