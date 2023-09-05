package org.apache.zookeeper.server.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class OIDCAuthenticationProviderTest {

    private final OIDCAuthenticationProvider oidcAuthenticationProvider = new OIDCAuthenticationProvider();

    @Test
    void isValidWithValidUserId() {
        String validUserId = "uid:userOne";

        assertTrue(oidcAuthenticationProvider.isValid(validUserId));
    }

    @Test
    void isValidWithInvalidUserId() {
        String invalidUserId = "uuid.userTwo";

        assertFalse(oidcAuthenticationProvider.isValid(invalidUserId));
    }

    @Test
    void isValidWithValidGroupId() {
        String validGroupId = "gid:groupOne";

        assertTrue(oidcAuthenticationProvider.isValid(validGroupId));
    }

    @Test
    void isValidWithInvalidGroupId() {
        String invalidGroupId = "gid:group:Two";

        assertFalse(oidcAuthenticationProvider.isValid(invalidGroupId));
    }

    @Test
    void matchesWithIdenticalIds() {
        String id = "uid:userOne";
        String aclExpr = "uid:userOne";

        assertTrue(oidcAuthenticationProvider.matches(id, aclExpr));
    }

    @Test
    void matchesWithUnidenticalIds() {
        String id = "uid:userOne";
        String aclExpr = "gid:groupOne";

        assertFalse(oidcAuthenticationProvider.matches(id, aclExpr));
    }
}