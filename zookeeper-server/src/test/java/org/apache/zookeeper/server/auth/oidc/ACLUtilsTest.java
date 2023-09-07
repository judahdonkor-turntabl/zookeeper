package org.apache.zookeeper.server.auth.oidc;

import org.apache.zookeeper.data.Id;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ACLUtilsTest {

    @Test
    void getIdsFromAccessTokenWithUserIdAndGroupsPresent() {
        AccessToken accessToken = new AccessToken();
        String scheme = "oidc";
        accessToken.setUserID("userOne");
        Set<String> groups = new HashSet<>();
        groups.add("create");
        groups.add("delete");
        accessToken.setGroups(groups);

        List<Id> idsFromAccessToken = ACLUtils.getIdsFromAccessToken(accessToken, scheme);
        List<Id> expectedIds = Arrays.asList(new Id("oidc", "gid:create"),
                new Id("oidc", "gid:delete"),
                new Id("oidc", "uid:userOne"));

        assertEquals(3, idsFromAccessToken.size());
        assertEquals(expectedIds, idsFromAccessToken);
    }

    @Test
    void getIdsFromAccessTokenWithNoGroups() {
        AccessToken accessToken = new AccessToken();
        String scheme = "oidc";
        accessToken.setUserID("userOne");

        List<Id> idsFromAccessToken = ACLUtils.getIdsFromAccessToken(accessToken, scheme);

        assertEquals(1, idsFromAccessToken.size());
        assertEquals(Collections.singletonList(new Id("oidc", "uid:userOne")), idsFromAccessToken);
    }

    @Test
    void getIdsFromAccessTokenWithNoGroupsAndUserId() {
        AccessToken accessToken = new AccessToken();
        String scheme = "oidc";
        List<Id> idsFromAccessToken = ACLUtils.getIdsFromAccessToken(accessToken, scheme);

        assertEquals(Collections.emptyList(), idsFromAccessToken);
    }
}